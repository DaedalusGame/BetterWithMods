package betterwithmods.api.tile;

import net.minecraft.util.EnumFacing;

public interface IAxle {
    byte getSignal();

    byte getMaximumSignal();

    int getMaximumInput();

    EnumFacing[] getDirections();

    EnumFacing.Axis getAxis();

    default boolean isFacing(IAxle axle) {
        return axle.getAxis() == this.getAxis();
    }
}
