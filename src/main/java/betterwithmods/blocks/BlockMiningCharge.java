package betterwithmods.blocks;

import betterwithmods.entity.EntityMiningCharge;
import betterwithmods.util.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by tyler on 9/5/16.
 */
public class BlockMiningCharge extends BWMBlock {
    public static final PropertyBool EXPLODE = PropertyBool.create("explode");


    public BlockMiningCharge() {
        super(Material.TNT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(EXPLODE, Boolean.FALSE));
        setSoundType(SoundType.PLANT);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing facing = state.getValue(DirUtils.FACING);
        switch (facing.getIndex()) {
            case 0://DOWN
                return new AxisAlignedBB(0, .5, 0, 1, 1, 1);
            case 1://UP
                return new AxisAlignedBB(0, 0, 0, 1, .5, 1);
            case 2:
                return new AxisAlignedBB(0, 0, .5, 1, 1, 1);
            case 3:
                return new AxisAlignedBB(0, 0, 0, 1, 1, .5);
            case 4:
                return new AxisAlignedBB(.5, 0, 0, 1, 1, 1);
            case 5:
                return new AxisAlignedBB(0, 0, 0, .5, 1, 1);
            default:
                return FULL_BLOCK_AABB;
        }

    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, EXPLODE, DirUtils.FACING);
    }

    public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {

        if (!worldIn.isRemote && state.getValue(EXPLODE)) {
            EntityMiningCharge miningCharge = new EntityMiningCharge(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter, getFacingFromBlockState(state));
            worldIn.spawnEntityInWorld(miningCharge);
            worldIn.playSound(null, miningCharge.posX, miningCharge.posY, miningCharge.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public EnumFacing getFacingFromBlockState(IBlockState state) {
        return state.getValue(DirUtils.FACING);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(DirUtils.FACING, facing);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(EXPLODE, Boolean.TRUE));
            worldIn.setBlockToAir(pos);
        }
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(EXPLODE, Boolean.TRUE));
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        IBlockState state = world.getBlockState(pos);
        world.setBlockToAir(pos);
        onBlockDestroyedByExplosion(world, pos, state, explosion);
    }

    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, IBlockState state, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            EntityMiningCharge miningCharge = new EntityMiningCharge(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy(), getFacingFromBlockState(state));
            miningCharge.setFuse((short) (worldIn.rand.nextInt(miningCharge.getFuse() / 4) + miningCharge.getFuse() / 8));
            worldIn.spawnEntityInWorld(miningCharge);
        }
    }

    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        this.explode(worldIn, pos, state, null);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (heldItem != null && (heldItem.getItem() == Items.FLINT_AND_STEEL || heldItem.getItem() == Items.FIRE_CHARGE)) {
            this.explode(worldIn, pos, state.withProperty(EXPLODE, Boolean.TRUE), playerIn);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

            if (heldItem.getItem() == Items.FLINT_AND_STEEL) {
                heldItem.damageItem(1, playerIn);
            } else if (!playerIn.capabilities.isCreativeMode) {
                --heldItem.stackSize;
            }

            return true;
        } else {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
        }
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
            EntityArrow entityarrow = (EntityArrow) entityIn;
            if (entityarrow.isBurning()) {
                this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty(EXPLODE, Boolean.TRUE), entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase) entityarrow.shootingEntity : null);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    public IBlockState getStateFromMeta(int meta) {
        boolean explode = (meta & 1) > 0;
        EnumFacing facing = EnumFacing.getFront(meta >> 1);
        return this.getDefaultState().withProperty(EXPLODE, explode).withProperty(DirUtils.FACING, facing);
    }

    public int getMetaFromState(IBlockState state) {
        int facing = state.getValue(DirUtils.FACING).getIndex() << 1;
        int explode = state.getValue(EXPLODE) ? 1 : 0;
        return explode | facing;
    }
}