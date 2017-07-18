package betterwithmods.common.blocks.mechanical;

import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.tile.gen.TileEntityWaterwheel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockWaterwheel extends BlockMillGenerator {

    public BlockWaterwheel() {
        super(Material.WOOD);
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(BWMItems.WINDMILL, 1, 1);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityWaterwheel) {
            boolean active = ((TileEntityWaterwheel) world.getTileEntity(pos)).isValid();
            world.setBlockState(pos, state.withProperty(ISACTIVE, active));
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return BWMItems.WINDMILL;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 1;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityWaterwheel();
    }

    @Override
    public ItemStack getGenStack(IBlockState state) {
        return new ItemStack(BWMItems.WINDMILL, 1, 1);
    }
}
