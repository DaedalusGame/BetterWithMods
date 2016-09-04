package betterwithmods.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface ISoulSensitive 
{
	//In case only certain blocks can consume souls. Return true if all of them can.
	public boolean isSoulSensitive(IBlockAccess world, BlockPos pos);
	
	//How many souls your block can consume simultaneously.
	public int getMaximumSoulIntake(IBlockAccess world, BlockPos pos);

	public int getMaximumSoulIntake(IBlockState state);
	
	/*
	 * Similar to RF transfer, you will want to return the lowest number between
	 * maximum transfer and the number of souls available.
	 */
	public int processSouls(World world, BlockPos pos, int souls);

	//Tells the filtered hopper if it can consume the souls.
	public boolean consumeSouls(World world, BlockPos pos, int souls);
}
