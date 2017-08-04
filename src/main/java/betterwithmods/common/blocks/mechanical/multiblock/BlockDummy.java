package betterwithmods.common.blocks.mechanical.multiblock;

import betterwithmods.api.tile.multiblock.TileEntityProxyBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDummy extends Block {
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
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityProxyBlock();
    }
}
