package betterwithmods.blocks.tile.gen;

import betterwithmods.api.block.IMechanical;
import betterwithmods.api.capabilities.MechanicalCapability;
import betterwithmods.api.tile.IMechanicalPower;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by tyler on 8/5/16.
 */
public class TileEntityCreativeGen extends TileEntity implements IMechanicalPower {
    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    //Unless you increase this, expect to see the TESR to pop in as you approach.
    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return super.getMaxRenderDistanceSquared() * 3.0D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == MechanicalCapability.MECHANICAL_POWER)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == MechanicalCapability.MECHANICAL_POWER) {
            return MechanicalCapability.MECHANICAL_POWER.cast(this);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        if (getBlockType() instanceof IMechanical) {
            if (((IMechanical) getBlockType()).getMechPowerLevelToFacing(getWorld(), pos, facing) > 0)
                return 20;
        }
        return 0;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public void readFromTag(NBTTagCompound tag) {
    }

    @Override
    public NBTTagCompound writeToTag(NBTTagCompound tag) {
        return new NBTTagCompound();
    }
}
