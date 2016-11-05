package betterwithmods.blocks;

import betterwithmods.BWMItems;
import betterwithmods.api.block.IDebarkable;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.api.block.ITurnable;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDebarkedOld extends BlockOldLog implements IDebarkable, ITurnable, IMultiVariants {
    public BlockDebarkedOld() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public String[] getVariants() {
        return new String[]{"axis=y,variant=oak", "axis=y,variant=spruce", "axis=y,variant=birch", "axis=y,variant=jungle"};
    }

    @Override
    public ItemStack getBark(IBlockState state) {
        return new ItemStack(BWMItems.BARK, 1, this.damageDropped(state));
    }

    @Override
    public EnumFacing getFacingFromBlockState(IBlockState state) {
        return null;
    }

    @Override
    public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing) {
        return null;
    }

    @Override
    public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canRotateHorizontally(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canRotateVertically(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public void rotateAroundYAxis(World world, BlockPos pos, boolean reverse) {

    }
}
