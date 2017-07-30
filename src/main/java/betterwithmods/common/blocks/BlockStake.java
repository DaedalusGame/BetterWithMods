package betterwithmods.common.blocks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by tyler on 5/17/17.
 */
public class BlockStake extends BWMBlock {


    public BlockStake() {
        super(Material.WOOD);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(DirUtils.FACING, facing.getOpposite());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.FACING, DirUtils.UP, DirUtils.DOWN, DirUtils.NORTH, DirUtils.SOUTH, DirUtils.WEST, DirUtils.EAST);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DirUtils.FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(DirUtils.FACING, EnumFacing.getFront(meta));
    }


    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItemMainhand();
        if (side != state.getValue(DirUtils.FACING) && stack.isItemEqual(new ItemStack(Items.STRING)))
            return placeString(worldIn, pos, side.getOpposite(), stack);
        return false;
    }

    public boolean placeString(World world, BlockPos pos, EnumFacing facing, ItemStack stack) {
        int count = stack.getCount();
        int build = -1;
        for (int i = 1; i <= 64; i++) {
            if (world.getBlockState(pos.offset(facing, i)).getBlock() instanceof BlockStake) {
                build = i;
                break;
            }
        }
        if (build > -1 && count >= (build - 1)) {
            stack.shrink(build);
            for (int i = 1; i < build; i++) {
                world.setBlockState(pos.offset(facing, i), BWMBlocks.STAKE_STRING.getDefaultState());
            }
            return true;
        }
        return false;
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(DirUtils.FACING)) {

            default:
                return new AxisAlignedBB(6 / 16f, 0, 6 / 16f, 10f / 16, 12f / 16f, 10f / 16f);
            case UP:
                return new AxisAlignedBB(6 / 16f, 4 / 16f, 6 / 16f, 10f / 16, 1, 10f / 16f);
            case NORTH:
                return new AxisAlignedBB(6 / 16f, 6 / 16f, 12f / 16f, 10f / 16f, 10f / 16, 0);
            case SOUTH:
                return new AxisAlignedBB(6 / 16f, 6 / 16f, 4f / 16f, 10f / 16f, 10f / 16, 1);
            case WEST:
                return new AxisAlignedBB(12f / 16f, 6 / 16f, 6 / 16f, 0, 10f / 16, 10f / 16f);
            case EAST:
                return new AxisAlignedBB(4f / 16f, 6 / 16f, 6 / 16f, 1, 10f / 16, 10f / 16f);

        }
    }


    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState newState = state;
        for (int i = 0; i < EnumFacing.VALUES.length; i++) {
            newState = newState.withProperty(DirUtils.DIR_PROP[i], getDirection(worldIn, pos, EnumFacing.getFront(i).getOpposite()));
        }
        return newState;
    }

    public static boolean getDirection(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        Block block = world.getBlockState(pos.offset(facing)).getBlock();
        return block instanceof BlockStakeString || block instanceof BlockStake;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.notifyNeighborsOfStateChange(pos,this,true);
        for(EnumFacing facing: EnumFacing.VALUES) {
            if(getDirection(worldIn,pos,facing)) {
                InvUtils.ejectStackWithOffset(worldIn, pos.offset(facing), getItem(worldIn, pos, state));
                worldIn.setBlockToAir(pos.offset(facing));
            }
        }
        super.breakBlock(worldIn, pos, state);
    }
}
