package betterwithmods.api.tile;

import net.minecraft.util.EnumFacing;

public interface IAxle {
    byte getSignal();

    byte getMaximumSignal();

    int getMaximumInput();

    EnumFacing[] getDirections();
}
