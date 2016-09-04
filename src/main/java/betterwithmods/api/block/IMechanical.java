package betterwithmods.api.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

public interface IMechanical 
{
	public int getMechPowerLevelToFacing(World world, BlockPos pos, EnumFacing dir);
}
