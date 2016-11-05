package betterwithmods.blocks;

import betterwithmods.api.block.IMechanical;
import betterwithmods.blocks.tile.gen.TileEntityCreativeGen;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by tyler on 8/5/16.
 */
public class BlockCreativeGenerator extends BWMBlock implements IMechanical {
    public BlockCreativeGenerator() {
        super(Material.WOOD);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCreativeGen();
    }

    @Override
    public int getMechPowerLevelToFacing(World var1, BlockPos var2, EnumFacing var3) {
        return 4;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public boolean isMechanicalJunction() {
        return true;
    }
}
