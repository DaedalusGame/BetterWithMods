package betterwithmods.common.blocks;

import betterwithmods.common.entity.EntityMiningCharge;
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

/**
 * Created by tyler on 9/5/16.
 */
public class BlockMiningCharge extends BWMBlock {
    public static final PropertyBool EXPLODE = PropertyBool.create("explode");
    private static final AxisAlignedBB D_AABB = new AxisAlignedBB(0, .5, 0, 1, 1, 1);
    private static final AxisAlignedBB U_AABB = new AxisAlignedBB(0, 0, 0, 1, .5, 1);
    private static final AxisAlignedBB N_AABB = new AxisAlignedBB(0, 0, .5, 1, 1, 1);
    private static final AxisAlignedBB S_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, .5);
    private static final AxisAlignedBB W_AABB = new AxisAlignedBB(.5, 0, 0, 1, 1, 1);
    private static final AxisAlignedBB E_AABB = new AxisAlignedBB(0, 0, 0, .5, 1, 1);

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
        switch (facing) {
            case DOWN://DOWN
                return D_AABB;
            case UP://UP
                return U_AABB;
            case NORTH:
                return N_AABB;
            case SOUTH:
                return S_AABB;
            case WEST:
                return W_AABB;
            case EAST:
                return E_AABB;
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
            worldIn.spawnEntity(miningCharge);
            worldIn.playSound(null, miningCharge.posX, miningCharge.posY, miningCharge.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public EnumFacing getFacingFromBlockState(IBlockState state) {
        return state.getValue(DirUtils.FACING);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(DirUtils.FACING, facing);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(EXPLODE, Boolean.TRUE));
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos other) {
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(EXPLODE, Boolean.TRUE));
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        onBlockDestroyedByExplosion(world, pos, explosion);
        world.setBlockToAir(pos);
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            EntityMiningCharge miningCharge = new EntityMiningCharge(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy(), getFacingFromBlockState(worldIn.getBlockState(pos)));
            miningCharge.setFuse((short) (worldIn.rand.nextInt(miningCharge.getFuse() / 4) + miningCharge.getFuse() / 8));
            worldIn.spawnEntity(miningCharge);
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        this.explode(worldIn, pos, state, null);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        if (!heldItem.isEmpty() && (heldItem.getItem() == Items.FLINT_AND_STEEL || heldItem.getItem() == Items.FIRE_CHARGE)) {
            this.explode(worldIn, pos, state.withProperty(EXPLODE, Boolean.TRUE), playerIn);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

            if (heldItem.getItem() == Items.FLINT_AND_STEEL) {
                heldItem.damageItem(1, playerIn);
            } else if (!playerIn.capabilities.isCreativeMode) {
                heldItem.shrink(1);
            }

            return true;
        } else {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
            EntityArrow entityarrow = (EntityArrow) entityIn;
            if (entityarrow.isBurning()) {
                this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty(EXPLODE, Boolean.TRUE), entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase) entityarrow.shootingEntity : null);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean explode = (meta & 1) > 0;
        EnumFacing facing = EnumFacing.getFront(meta >> 1);
        return this.getDefaultState().withProperty(EXPLODE, explode).withProperty(DirUtils.FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facing = state.getValue(DirUtils.FACING).getIndex() << 1;
        int explode = state.getValue(EXPLODE) ? 1 : 0;
        return explode | facing;
    }
}