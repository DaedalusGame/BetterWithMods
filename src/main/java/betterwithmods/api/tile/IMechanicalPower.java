package betterwithmods.api.tile;

import betterwithmods.util.MechanicalUtil;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public interface IMechanicalPower {

    default void overpower(World world, BlockPos pos, EnumFacing facing) {
    }

    int getMechanicalOutput(World world, BlockPos pos, EnumFacing facing);

    int getMechanicalInput(World world, BlockPos pos, EnumFacing facing);

    default boolean isInputtingPower(World world, BlockPos pos, EnumFacing facing) {
        return getMechanicalInput(world, pos, facing) > 0;
    }

    boolean canInputPower(Mode mode, EnumFacing facing);

    default EnumFacing getPoweredSide(World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (isInputtingPower(world, pos, facing))
                return facing;
        }
        return null;
    }

    default boolean isPowered(World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (isInputtingPower(world, pos, facing))
                return true;
        }
        return false;
    }

    default Set<Mode> getModes() {
        return ImmutableSet.of(Mode.AXLE);
    }

    enum Mode {
        AXLE {
            @Override
            public boolean isActive(World world, BlockPos pos, EnumFacing facing) {
                return MechanicalUtil.isBlockPoweredByAxleOnSide(world, pos, facing);
            }
        },
        CRANK {
            @Override
            public boolean isActive(World world, BlockPos pos, EnumFacing facing) {
                return MechanicalUtil.isPoweredByCrankOnSide(world, pos, facing);
            }
        };

        public boolean isActive(World world, BlockPos pos, EnumFacing facing) {
            return false;
        }
    }
}
