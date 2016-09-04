package betterwithmods.blocks.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPulley extends TileEntity
{
	private boolean isRedstonePowered()
	{
		return false;
	}
	
	private boolean isMechanicallyPowered()
	{
		return false;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}
	
	public boolean isRaising()
	{
		return !isRedstonePowered() && isMechanicallyPowered();
	}
	
	public boolean isLowering()
	{
		return isRedstonePowered() || !isMechanicallyPowered();
	}
}
