package betterwithmods.blocks;

import betterwithmods.blocks.tile.TileEntityGearbox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAdvGearbox extends BlockGearbox
{
    public BlockAdvGearbox() {
        super();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityGearbox();
    }
}
