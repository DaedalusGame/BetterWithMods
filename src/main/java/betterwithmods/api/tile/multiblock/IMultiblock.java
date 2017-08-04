package betterwithmods.api.tile.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMultiblock<T extends TileEntityMultiblock> {
    String getName();

    ItemStack[][][] getStructureBlocks();

    boolean isController(int x, int y, int z);

    boolean isMultiblockStructure(World world, BlockPos pos, IBlockState state, EnumFacing facing);

    void createMultiblockStructure(World world, BlockPos pos, IBlockState state, EnumFacing facing);

    void destroyMultiblock(World world, BlockPos pos, IBlockState state, EnumFacing facing);
}
