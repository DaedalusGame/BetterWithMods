package betterwithmods.api.tile;

import betterwithmods.util.DirUtils;
import net.minecraft.util.EnumFacing;

public interface IMechanicalPower {
    int getMechanicalOutput(EnumFacing facing);

    int getMechanicalInput(EnumFacing facing);

    int getMaximumInput(EnumFacing facing);

    int getMinimumInput(EnumFacing facing);

    default int calculateInput() {
        int findPower = 0;
        for (EnumFacing facing : DirUtils.Y_AXIS) {
            int power = getMechanicalInput(facing);
            if (power > findPower) {
                findPower = power;
            }
        }

        return findPower;
    }
}
