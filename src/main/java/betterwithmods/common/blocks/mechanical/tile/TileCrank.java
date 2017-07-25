package betterwithmods.common.blocks.mechanical.tile;

import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IAxle;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.blocks.mechanical.BlockCrank;
import betterwithmods.common.blocks.tile.TileBasic;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;

/**
 * Created by primetoxinz on 7/24/17.
 */
public class TileCrank extends TileBasic implements IMechanicalPower, IAxle {
    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        if (world.getBlockState(pos).getValue(BlockCrank.STAGE) > 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 1;
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityMechanicalPower.MECHANICAL_POWER)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nonnull
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityMechanicalPower.MECHANICAL_POWER)
            return CapabilityMechanicalPower.MECHANICAL_POWER.cast(this);
        return super.getCapability(capability, facing);
    }

    @Override
    public byte getSignal() {
        return 0;
    }

    @Override
    public byte getMaximumSignal() {
        return 0;
    }

    @Override
    public int getMaximumInput() {
        return 0;
    }

    @Override
    public int getMinimumInput() {
        return 0;
    }

    @Override
    public EnumFacing[] getDirections() {
        return new EnumFacing[0];
    }

    @Override
    public EnumFacing.Axis getAxis() {
        return null;
    }
}
