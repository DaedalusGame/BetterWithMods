package betterwithmods.module.industry.multiblocks;

import betterwithmods.api.tile.multiblock.IMultiblock;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockSteel;
import betterwithmods.util.DirUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Lathe implements IMultiblock {
    @Override
    public String getName() {
        return "Lathe";
    }

    @Override
    public ItemStack[][][] getStructureBlocks() {
        return new ItemStack[][][]{
                new ItemStack[][]{new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, new ItemStack(BWMBlocks.STEEL_BLOCK)}},
                new ItemStack[][]{new ItemStack[]{new ItemStack(BWMBlocks.STEEL_BLOCK), new ItemStack(BWMBlocks.STEEL_BLOCK), new ItemStack(BWMBlocks.STEEL_BLOCK)}}
        };
    }

    @Override
    public boolean isController(int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isMultiblockStructure(World world, BlockPos pos, IBlockState state, EnumFacing facing) {
        if (facing.getAxis().isVertical())
            return false;
        IBlockState current;


        return IMultiblock.isState(world, pos.offset(DirUtils.rotateFacingAroundY(facing, false)), BlockSteel.getBlock(0))
                && IMultiblock.isState(world, pos.offset(DirUtils.rotateFacingAroundY(facing, true)), BWMBlocks.STEEL_GEARBOX.getDefaultState().withProperty(DirUtils.FACING, DirUtils.rotateFacingAroundY(facing, true)))
                && IMultiblock.isState(world, pos.offset(DirUtils.rotateFacingAroundY(facing, true)).up(), BlockSteel.getBlock(0));
    }

    @Override
    public void createMultiblockStructure(World world, BlockPos pos, IBlockState state, EnumFacing facing) {
        System.out.println("VALID");

    }

    @Override
    public void destroyMultiblock(World world, BlockPos pos, IBlockState state, EnumFacing facing) {

    }
}
