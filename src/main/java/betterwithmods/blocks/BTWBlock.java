package betterwithmods.blocks;

import betterwithmods.api.block.IBTWBlock;
import betterwithmods.blocks.tile.TileEntityDirectional;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BTWBlock extends Block implements IBTWBlock
{
	public BTWBlock(Material material, String name)
	{
		super(material);
		this.setUnlocalizedName("bwm:" + name);
	}

	@Override
	public EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFacing(World world, BlockPos pos, EnumFacing facing) {
		// TODO Auto-generated method stub
		
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
