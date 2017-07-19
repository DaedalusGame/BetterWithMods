package betterwithmods.common.blocks.tile.gen;

import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IMechanicalPower;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by tyler on 8/5/16.
 */
public class TileEntityCreativeGen extends TileEntity implements IMechanicalPower {
    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityMechanicalPower.MECHANICAL_POWER || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityMechanicalPower.MECHANICAL_POWER) {
            return CapabilityMechanicalPower.MECHANICAL_POWER.cast(this);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        return 4;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 0;
    }

}
