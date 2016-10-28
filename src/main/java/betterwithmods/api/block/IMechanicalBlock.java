package betterwithmods.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IMechanicalBlock {
    boolean canOutputMechanicalPower();

    boolean canInputMechanicalPower();

    boolean isInputtingMechPower(World world, BlockPos pos);

    boolean isOutputtingMechPower(World world, BlockPos pos);

    boolean canInputPowerToSide(IBlockAccess world, BlockPos pos, EnumFacing dir);

    void overpower(World world, BlockPos pos);

    boolean isMechanicalOn(IBlockAccess world, BlockPos pos);

    void setMechanicalOn(World world, BlockPos pos, boolean isOn);

    boolean isMechanicalOnFromState(IBlockState state);
}
