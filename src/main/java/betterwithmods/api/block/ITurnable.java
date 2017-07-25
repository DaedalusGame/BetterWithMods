package betterwithmods.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface ITurnable {
    EnumFacing getFacing(IBlockState state);

    IBlockState setFacingInBlock(IBlockState state, EnumFacing facing);

    boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos);

    boolean canRotateHorizontally(IBlockAccess world, BlockPos pos);

    boolean canRotateVertically(IBlockAccess world, BlockPos pos);

    void rotateAroundYAxis(World world, BlockPos pos, boolean reverse);
}
