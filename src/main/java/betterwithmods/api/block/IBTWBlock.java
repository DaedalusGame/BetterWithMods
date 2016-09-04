package betterwithmods.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IBTWBlock 
{
	public EnumFacing getFacing(IBlockAccess world, BlockPos pos);
	
	public void setFacing(World world, BlockPos pos, EnumFacing facing);
	
	public EnumFacing getFacingFromBlockState(IBlockState state);
	
	public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing);
	
	public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos);
	
	public boolean canRotateHorizontally(IBlockAccess world, BlockPos pos);
	
	public boolean canRotateVertically(IBlockAccess world, BlockPos pos);
	
	public void rotateAroundYAxis(World world, BlockPos pos, boolean reverse);
}
