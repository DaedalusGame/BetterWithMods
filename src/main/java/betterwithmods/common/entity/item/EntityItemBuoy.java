package betterwithmods.common.entity.item;

import betterwithmods.util.item.ItemExt;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Koward
 */
public class EntityItemBuoy extends EntityItem {
    private final static byte BUOYANCY_MAX_ITERATIONS = 10;

    /**
     * Wrapper around EntityItem.
     */
    public EntityItemBuoy(EntityItem orig) {
        super(orig.getEntityWorld(), orig.posX, orig.posY, orig.posZ, orig.getEntityItem());
        NBTTagCompound originalData = new NBTTagCompound();
        orig.writeEntityToNBT(originalData);
        this.readEntityFromNBT(originalData);

        String thrower = orig.getThrower();
        Entity entity = thrower == null ? null : orig.getEntityWorld().getPlayerEntityByName(thrower);
        double tossSpeed = entity != null && entity.isSprinting() ? 2D : 1D;

        this.motionX = orig.motionX * tossSpeed;
        this.motionY = orig.motionY * tossSpeed;
        this.motionZ = orig.motionZ * tossSpeed;
    }

    /**
     * Required for entities. Not actually used anywhere.
     */
    @SuppressWarnings("unused")
    public EntityItemBuoy(World world) {
        super(world);
    }

    /**
     * Used to access {@link EntityItem#searchForOtherItemsNearby} as it's
     * private.
     */
    private void superSearchForOtherItemsNearby() {
        try {
            ReflectionHelper.findMethod(EntityItem.class, this,
                    new String[]{"func_85054_d", "searchForOtherItemsNearby"}, new Class<?>[]{})
                    .invoke(this, (Object[]) null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void setAge(int age) {
        ReflectionHelper.setPrivateValue(EntityItem.class, this, age, "field_70292_b", "age");
    }

    private int getAge0() {
        return ReflectionHelper.getPrivateValue(EntityItem.class, this, new String[]{"field_70292_b", "age"});
    }

    private DataParameter<ItemStack> getITEM() {
        return ReflectionHelper.getPrivateValue(EntityItem.class, this, new String[]{"field_184533_c", "ITEM"});
    }

    private int getPickupDelay() {
        return ReflectionHelper.getPrivateValue(EntityItem.class, this,
                new String[]{"field_145804_b", "delayBeforeCanPickup"});
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        ItemStack stack = this.getDataManager().get(getITEM());
        if (stack != ItemStack.EMPTY && stack.getItem() != null && stack.getItem().onEntityItemUpdate(this))
            return;
        if (this.getEntityItem() == ItemStack.EMPTY) {
            this.setDead();
        } else {
            // super.super.onUpdate() START
            if (!this.getEntityWorld().isRemote) {
                this.setFlag(6, this.isGlowing());
            }
            this.onEntityUpdate();
            // super.super.onUpdate() END

            if (getPickupDelay() > 0 && getPickupDelay() != 32767) {
                setPickupDelay(getPickupDelay() - 1);
            }

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            if (!this.hasNoGravity()) {
                this.motionY -= 0.03999999910593033D;
            }
            updateBuoy();

            this.noClip = this.pushOutOfBlocks(this.posX,
                    (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            boolean flag = (int) this.prevPosX != (int) this.posX || (int) this.prevPosY != (int) this.posY
                    || (int) this.prevPosZ != (int) this.posZ;

            if (flag || this.ticksExisted % 25 == 0) {
                if (this.getEntityWorld().getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA) {
                    this.motionY = 0.20000000298023224D;
                    this.motionX = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    this.motionZ = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
                }

                if (!this.getEntityWorld().isRemote) {
                    superSearchForOtherItemsNearby();
                }
            }

            float f = 0.98F;

            if (this.onGround) {
                f = this.getEntityWorld().getBlockState(new BlockPos(MathHelper.floor(this.posX),
                        MathHelper.floor(this.getEntityBoundingBox().minY) - 1,
                        MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.98F;
            }

            this.motionX *= (double) f;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= (double) f;

            if (this.onGround) {
                this.motionY *= -0.5D;
            }

            if (this.getAge0() != -32768) {
                setAge(getAge0() + 1);
            }

            // this.handleWaterMovement();

            ItemStack item = this.getDataManager().get(getITEM());

            if (!this.getEntityWorld().isRemote && this.getAge0() >= lifespan) {
                int hook = net.minecraftforge.event.ForgeEventFactory.onItemExpire(this, item);
                if (hook < 0)
                    this.setDead();
                else
                    this.lifespan += hook;
            }
            if (item != ItemStack.EMPTY && item.getCount() <= 0) {
                this.setDead();
            }
        }
    }

    private void updateBuoy() {
        double waterAccumulator = 0.0D;
        final double offset = 0.1D;

        for (int i = 0; i < BUOYANCY_MAX_ITERATIONS; ++i) {
            double low = getEntityBoundingBox().minY
                    + (getEntityBoundingBox().maxY - getEntityBoundingBox().minY) * (double) (i) * 0.375D + offset;
            double high = getEntityBoundingBox().minY
                    + (getEntityBoundingBox().maxY - getEntityBoundingBox().minY) * (double) (i + 1) * 0.375D + offset;
            AxisAlignedBB boundingBox = new AxisAlignedBB(getEntityBoundingBox().minX, low, getEntityBoundingBox().minZ,
                    getEntityBoundingBox().maxX, high, getEntityBoundingBox().maxZ);

            if (!isAABBInMaterial(getEntityWorld(), boundingBox, Material.WATER)) {
                break;
            }

            waterAccumulator += 1.0D / (double) BUOYANCY_MAX_ITERATIONS;
        }

        if (waterAccumulator > 0.001D) {
            if (!isDrifted()) {
                float buoyancy = ItemExt.getBuoyancy(getEntityItem()) + 1.0F;
                motionY += 0.04D * (double) buoyancy * waterAccumulator;
            }

            motionX *= 0.9;
            motionY *= 0.9;
            motionZ *= 0.9;
        }
    }

    /**
     * Check the non visible current between two water blocks for all blocks
     * nearby entity.
     */
    private boolean isDrifted() {
        int minX = MathHelper.floor(getEntityBoundingBox().minX);
        int maxX = MathHelper.floor(getEntityBoundingBox().maxX + 1.0D);
        int minY = MathHelper.floor(getEntityBoundingBox().minY);
        int maxY = MathHelper.floor(getEntityBoundingBox().maxY + 1.0D);
        int minZ = MathHelper.floor(getEntityBoundingBox().minZ);
        int maxZ = MathHelper.floor(getEntityBoundingBox().maxZ + 1.0D);

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    if (checkBlockDrifting(x, y, z))
                        return true;
                }
            }
        }

        return false;
    }

    /**
     * Check the non visible current between two water blocks.
     */
    private boolean checkBlockDrifting(int x, int y, int z) {
        for (int height = y - 1; height <= y + 1; height++) {
            IBlockState blockState = getEntityWorld().getBlockState(new BlockPos(x, height, z));
            if (blockState.getBlock() == Blocks.FLOWING_WATER || blockState.getBlock() == Blocks.WATER) {
                int meta = blockState.getBlock().getMetaFromState(blockState);
                if (meta >= 8)
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given AABB is in the material given.
     * Was in {@link World} before 1.11 and then was deleted. No idea where it went so this is a copy.
     */
    private boolean isAABBInMaterial(World world, AxisAlignedBB bb, Material materialIn) {
        int i = MathHelper.floor(bb.minX);
        int j = MathHelper.ceil(bb.maxX);
        int k = MathHelper.floor(bb.minY);
        int l = MathHelper.ceil(bb.maxY);
        int i1 = MathHelper.floor(bb.minZ);
        int j1 = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    IBlockState iblockstate = world.getBlockState(blockpos$pooledmutableblockpos.setPos(k1, l1, i2));

                    Boolean result = iblockstate.getBlock().isAABBInsideMaterial(world, blockpos$pooledmutableblockpos, bb, materialIn);
                    if (result != null) return result;

                    if (iblockstate.getMaterial() == materialIn) {
                        int j2 = iblockstate.getValue(BlockLiquid.LEVEL);
                        double d0 = (double) (l1 + 1);

                        if (j2 < 8) {
                            d0 = (double) (l1 + 1) - (double) j2 / 8.0D;
                        }

                        if (d0 >= bb.minY) {
                            blockpos$pooledmutableblockpos.release();
                            return true;
                        }
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        return false;
    }
}