package betterwithmods.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IMechanicalBlock {
    public boolean canOutputMechanicalPower();

    public boolean canInputMechanicalPower();

    public boolean isInputtingMechPower(World world, BlockPos pos);

    public boolean isOutputtingMechPower(World world, BlockPos pos);

    public boolean canInputPowerToSide(IBlockAccess world, BlockPos pos, EnumFacing dir);

    public void overpower(World world, BlockPos pos);

    public boolean isMechanicalOn(IBlockAccess world, BlockPos pos);

    public void setMechanicalOn(World world, BlockPos pos, boolean isOn);

    public boolean isMechanicalOnFromState(IBlockState state);
}
