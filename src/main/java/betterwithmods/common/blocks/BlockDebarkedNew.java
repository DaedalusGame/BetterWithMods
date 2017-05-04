package betterwithmods.common.blocks;

import betterwithmods.api.block.IDebarkable;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.api.block.ITurnable;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.BWMItems;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDebarkedNew extends BlockNewLog implements IDebarkable, ITurnable, IMultiVariants {
    public BlockDebarkedNew() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public ItemStack getBark(IBlockState state) {
        return new ItemStack(BWMItems.BARK, 1, 4 + this.damageDropped(state));
    }

    @Override
    public String[] getVariants() {
        return new String[]{"axis=y,variant=acacia", "axis=y,variant=dark_oak"};
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
