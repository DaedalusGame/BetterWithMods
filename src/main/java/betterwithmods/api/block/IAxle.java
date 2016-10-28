package betterwithmods.api.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IAxle {
    int getAxisAlignment(IBlockAccess world, BlockPos pos);

    boolean isAxleOrientedToFacing(IBlockAccess world, BlockPos pos, EnumFacing dir);

    int getPowerLevel(IBlockAccess world, BlockPos pos);
}
