package betterwithmods.blocks;

import betterwithmods.api.block.ITurnable;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BWMBlock extends Block implements ITurnable {
    public BWMBlock(Material material) {
        super(material);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public EnumFacing getFacingFromBlockState(IBlockState state) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canRotateHorizontally(IBlockAccess world, BlockPos pos) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canRotateVertically(IBlockAccess world, BlockPos pos) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void rotateAroundYAxis(World world, BlockPos pos, boolean reverse) {
        // TODO Auto-generated method stub
    }
}
