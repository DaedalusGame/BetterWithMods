package betterwithmods.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityFallingGourd extends EntityFallingBlock {
    private static final DataParameter<Integer> FALLBLOCK = EntityDataManager.createKey(EntityFallingGourd.class, DataSerializers.VARINT);
    private ItemStack seedStack;

    public EntityFallingGourd(World worldIn) {
        super(worldIn);
    }

    public EntityFallingGourd(World worldIn, double x, double y, double z, IBlockState fallingBlockState) {
        super(worldIn, x, y, z, fallingBlockState);
        setBlock(fallingBlockState);
    }

    @Override
    public void onUpdate() {
        IBlockState fallblock = getBlock();
        Block block = fallblock.getBlock();
        if (fallblock.getMaterial() == Material.AIR) {
            this.setDead();
        } else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            BlockPos blockpos1;
            if (this.fallTime++ == 0) {
                blockpos1 = new BlockPos(this);
                if (this.getEntityWorld().getBlockState(blockpos1).getBlock() == block) {
                    this.getEntityWorld().setBlockToAir(blockpos1);
                } else if (!this.getEntityWorld().isRemote) {
                    this.setDead();
                    return;
                }
            }

            if (!this.hasNoGravity()) {
                this.motionY -= 0.03999999910593033D;
            }

            this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;
            if (!this.getEntityWorld().isRemote) {
                blockpos1 = new BlockPos(this);
                if (this.onGround) {
                    IBlockState iblockstate = this.getEntityWorld().getBlockState(blockpos1);
                    if (this.getEntityWorld().isAirBlock(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ)) && BlockFalling.canFallThrough(this.getEntityWorld().getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ)))) {
                        this.onGround = false;
                        return;
                    }

                    this.motionX *= 0.699999988079071D;
                    this.motionZ *= 0.699999988079071D;
                    this.motionY *= -0.5D;
                    if (iblockstate.getBlock() != Blocks.PISTON_EXTENSION) {
                        if (this.getEntityWorld().canBlockBePlaced(block, blockpos1, true, EnumFacing.UP, (Entity) null, (ItemStack) null) && !BlockFalling.canFallThrough(this.getEntityWorld().getBlockState(blockpos1.down())) && (10 + rand.nextInt(7)) > this.fallTime && this.getEntityWorld().setBlockState(blockpos1, fallblock, 3)) {
                            this.setDead();
                            if (block instanceof BlockFalling) {
                                ((BlockFalling) block).onEndFalling(this.getEntityWorld(), blockpos1);
                            }
                        } else {
                            this.shatter();
                        }
                    }
                } else if (this.fallTime > 100 && !this.getEntityWorld().isRemote && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600) {
                    this.shatter();
                }
            }
        }
    }

    @Override
    public void fall(float p_fall_1_, float p_fall_2_) {
        //TODO: I guess this should deal like half a heart of damage maybe
    }

    public void shatter() {
        //TODO: This needs to make a splash sound
        if (!getEntityWorld().isRemote) {
            IBlockState fallblock = getBlock();
            if (fallblock != null)
                getEntityWorld().playEvent(2001, new BlockPos(this), Block.getStateId(fallblock));

            if (seedStack != null) {
                ItemStack seeds = seedStack.copy();
                seeds.func_190920_e(rand.nextInt(3) + 1);
                if (this.shouldDropItem && this.getEntityWorld().getGameRules().getBoolean("doEntityDrops")) {
                    this.entityDropItem(seeds, 0.0F);
                }
            }
        }

        this.setDead();
    }

    @Nullable
    @Override
    public IBlockState getBlock() {
        return Block.getStateById(dataManager.get(FALLBLOCK));
    }

    public void setBlock(IBlockState blockstate) {
        dataManager.set(FALLBLOCK, Block.getStateId(blockstate));
    }

    public ItemStack getSeedStack() {
        return seedStack;
    }

    public void setSeedStack(ItemStack stack) {
        seedStack = stack;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(FALLBLOCK, 0);
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound p_readEntityFromNBT_1_) {
        super.readEntityFromNBT(p_readEntityFromNBT_1_);
        IBlockState fallblock;
        int i = p_readEntityFromNBT_1_.getByte("Data") & 255;
        if (p_readEntityFromNBT_1_.hasKey("Block", 8)) {
            fallblock = Block.getBlockFromName(p_readEntityFromNBT_1_.getString("Block")).getStateFromMeta(i);
        } else if (p_readEntityFromNBT_1_.hasKey("TileID", 99)) {
            fallblock = Block.getBlockById(p_readEntityFromNBT_1_.getInteger("TileID")).getStateFromMeta(i);
        } else {
            fallblock = Block.getBlockById(p_readEntityFromNBT_1_.getByte("Tile") & 255).getStateFromMeta(i);
        }
        setBlock(fallblock);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound p_writeEntityToNBT_1_) {
        super.writeEntityToNBT(p_writeEntityToNBT_1_);
        IBlockState fallblock = getBlock();
        Block block = fallblock != null ? fallblock.getBlock() : Blocks.AIR;
        ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(block);
        p_writeEntityToNBT_1_.setString("Block", resourcelocation == null ? "" : resourcelocation.toString());
        p_writeEntityToNBT_1_.setByte("Data", (byte) block.getMetaFromState(fallblock));
    }
}
