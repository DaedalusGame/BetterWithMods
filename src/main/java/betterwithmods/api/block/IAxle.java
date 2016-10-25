package betterwithmods.api.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IAxle {
    public int getAxisAlignment(IBlockAccess world, BlockPos pos);

    public boolean isAxleOrientedToFacing(IBlockAccess world, BlockPos pos, EnumFacing dir);

    public int getPowerLevel(IBlockAccess world, BlockPos pos);
}
