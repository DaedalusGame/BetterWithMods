package betterwithmods.api.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public interface IMechanicalPower {
    int getMechanicalOutput(EnumFacing facing);

    int getMechanicalInput(EnumFacing facing);

    int getMaximumInput(EnumFacing facing);

    int getMinimumInput(EnumFacing facing);

    void readFromTag(NBTTagCompound tag);

    NBTTagCompound writeToTag(NBTTagCompound tag);
}
