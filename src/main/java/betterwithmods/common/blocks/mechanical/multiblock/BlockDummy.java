package betterwithmods.common.blocks.mechanical.multiblock;

import betterwithmods.api.tile.multiblock.TileEntityProxyBlock;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDummy extends BlockContainer {
    public BlockDummy(Material material) {
        super(material);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityProxyBlock) {
            ((TileEntityProxyBlock) world.getTileEntity(pos)).notifyControllerOnBreak();
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityProxyBlock();
    }
}
