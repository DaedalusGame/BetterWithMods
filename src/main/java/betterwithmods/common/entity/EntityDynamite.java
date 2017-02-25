package betterwithmods.common.entity;

import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fluids.IFluidBlock;

public class EntityDynamite extends Entity implements IProjectile {
    private static final float pi = 3.141593F;
    public ItemStack stack;
    private int fuse;

    public EntityDynamite(World world) {
        super(world);
        this.setSize(0.25F, 0.4F);
        this.fuse = -1;
        this.preventEntitySpawning = true;
        this.stack = null;
        this.isImmuneToFire = true;
    }

    public EntityDynamite(World world, double xPos, double yPos, double zPos, ItemStack stack) {
        super(world);
        this.setPosition(xPos, yPos, zPos);
        this.stack = stack;
        this.fuse = 100;
    }

    public EntityDynamite(World world, EntityLivingBase owner, ItemStack stack, boolean isLit) {
        this(world);
        this.setLocationAndAngles(owner.posX, owner.posY + owner.getEyeHeight(), owner.posZ, owner.rotationYaw, owner.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * pi) * 0.16F;
        this.posY -= 0.1D;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * pi) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * pi) * MathHelper.cos(this.rotationPitch / 180.0F * pi) * 0.4F);
        this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * pi) * MathHelper.cos(this.rotationPitch / 180.0F * pi) * 0.4F);
        this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * pi) * 0.4F);
        this.stack = stack;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 0.75F, 1.0F);
        if (isLit)
            fuse = 100;
    }

    @Override
    public void onUpdate() {
        int x = MathHelper.floor(this.posX);
        int y = MathHelper.floor(this.posY);
        int z = MathHelper.floor(this.posZ);

        Block block = this.getEntityWorld().getBlockState(new BlockPos(x, y, z)).getBlock();

        if (block == Blocks.FLOWING_LAVA || block == Blocks.LAVA)
            this.fuse = 1;
        else if (block instanceof IFluidBlock) {
            IFluidBlock fluid = (IFluidBlock) block;
            if (fluid.getFluid().getTemperature() > 1299)
                this.fuse = 1;
        }

        if (this.fuse > 0) {
            if (!this.getEntityWorld().isRemote && this.fuse % 20 == 0)
                this.getEntityWorld().playSound(null, new BlockPos(this.posX, this.posY, this.posZ), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            this.fuse--;
            if (this.fuse == 0) {
                this.setDead();
                if (!this.getEntityWorld().isRemote) {
                    explode();
                }
            }
            if (this.getEntityWorld().isRemote) {
                float smokeOffset = 0.25F;
                if ((block instanceof BlockLiquid && this.getEntityWorld().getBlockState(new BlockPos(x, y, z)).getMaterial() == Material.WATER) ||
                        (block instanceof IFluidBlock && this.getEntityWorld().getBlockState(new BlockPos(x, y, z)).getMaterial() == Material.WATER)) {
                    this.getEntityWorld().spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * smokeOffset, this.posY - this.motionY * smokeOffset, this.posZ - this.motionZ * smokeOffset, this.motionX, this.motionY, this.motionZ);
                } else
                    this.getEntityWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX - this.motionX * smokeOffset, this.posY - this.motionY * smokeOffset, this.posZ - this.motionZ * smokeOffset, this.motionX, this.motionY, this.motionZ);
            }
        } else if (block instanceof BlockFire) {
            this.fuse = 100;
            this.getEntityWorld().playSound(null, new BlockPos(this.posX, this.posY, this.posZ), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        } else if (this.onGround) {
            if (Math.abs(this.motionX) < 0.01D && Math.abs(this.motionY) < 0.01D && Math.abs(this.motionZ) < 0.01D) {
                if (!this.getEntityWorld().isRemote) {
                    convertToItem();
                    return;
                }
            }
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.04D;
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.98D;
        this.motionY *= 0.98D;
        this.motionZ *= 0.98D;

        if (this.onGround) {
            this.motionX *= 0.7D;
            this.motionZ *= 0.7D;
            this.motionY *= -0.5D;
        }

        this.extinguish();
    }

    @Override
    public void setThrowableHeading(double dX, double dY,
                                    double dZ, float angle, float f) {
        float sqrt = MathHelper.sqrt(dX * dX + dY * dY + dZ * dZ);
        dX /= sqrt;
        dY /= sqrt;
        dZ /= sqrt;
        dX += this.rand.nextGaussian() * 0.0075D * f;
        dY += this.rand.nextGaussian() * 0.0075D * f;
        dZ += this.rand.nextGaussian() * 0.0075D * f;
        dX *= angle;
        dY *= angle;
        dZ *= angle;
        this.motionX = dX;
        this.motionY = dY;
        this.motionZ = dZ;
        float pitch = MathHelper.sqrt(dX * dX + dZ * dZ);
        this.prevRotationYaw = (this.rotationYaw = (float) (Math.atan2(dX, dZ) * 180.0D / pi));
        this.prevRotationPitch = (this.rotationPitch = (float) (Math.atan2(dY, pitch) * 180.0D / pi));
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("Fuse"))
            fuse = tag.getInteger("Fuse");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        if (fuse > 0)
            tag.setInteger("Fuse", fuse);
    }

    public void explode() {
        float intensity = 1.5F;
        this.getEntityWorld().createExplosion(null, this.posX, this.posY, this.posZ, intensity, true);
        int x = MathHelper.floor(this.posX);
        int y = MathHelper.floor(this.posY);
        int z = MathHelper.floor(this.posZ);
        Block block = this.getEntityWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
        if (block instanceof BlockLiquid && this.getEntityWorld().getBlockState(new BlockPos(x, y, z)).getMaterial() == Material.WATER)
            redneckFishing(x, y, z);
    }

    private void redneckFishing(int x, int y, int z) {
        for (int i = x - 3; i < x + 4; i++) {
            for (int j = y - 2; j < y + 4; j++) {
                for (int k = z - 3; k < z + 4; k++) {
                    if (isWaterBlock(i, j, k)) {
                        if (this.rand.nextInt(20) == 0)
                            spawnDeadFish(i, j, k);
                    }
                }
            }
        }
    }

    private boolean isWaterBlock(int x, int y, int z) {
        Block block = this.getEntityWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
        return block instanceof BlockLiquid && this.getEntityWorld().getBlockState(new BlockPos(x, y, z)).getMaterial() == Material.WATER;
    }

    private void spawnDeadFish(int x, int y, int z) {
        LootContext.Builder build = new LootContext.Builder((WorldServer) this.getEntityWorld());
        for (ItemStack stack : this.getEntityWorld().getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).generateLootForPools(this.getEntityWorld().rand, build.build())) {
            EntityItem item = new EntityItem(this.getEntityWorld(), x, y, z, stack.copy());
            this.getEntityWorld().spawnEntity(item);
        }
    }

    private void convertToItem() {
        if (!this.getEntityWorld().isRemote)
            InvUtils.ejectStack(this.getEntityWorld(), this.posX, this.posY, this.posZ, this.stack);
        this.setDead();
    }
}
