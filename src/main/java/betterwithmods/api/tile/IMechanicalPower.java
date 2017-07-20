package betterwithmods.api.tile;

import net.minecraft.util.EnumFacing;

public interface IMechanicalPower {
    int getMechanicalOutput(EnumFacing facing);

    int getMechanicalInput(EnumFacing facing);

    int getMaximumInput(EnumFacing facing);

    int getMinimumInput(EnumFacing facing);

}
