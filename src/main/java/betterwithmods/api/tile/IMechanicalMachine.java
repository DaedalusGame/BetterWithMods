package betterwithmods.api.tile;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

/**
 * Created by primetoxinz on 6/20/17.
 */
public interface IMechanicalMachine extends IMechanicalPower {
    @Override
    default int getMechanicalOutput(World world, BlockPos pos, EnumFacing facing) {
        return 0;
    }

    @Override
    default int getMechanicalInput(World world, BlockPos pos, EnumFacing facing) {
        Set<Mode> modes = getModes();

        for(Mode mode: modes) {
            if(canInputPower(mode, facing)) {
                return mode.isActive(world,pos,facing) ? 1 : 0;
            }
        }
        return 0;
    }


    default void validate(World world, BlockPos pos) {
        boolean isPowered = isPowered(world,pos);
        boolean active = isActive();
        if(isPowered != active) {
            setActive(isPowered);
            world.notifyBlockUpdate(pos,world.getBlockState(pos),world.getBlockState(pos),3);
        }
    }

    default void overload(World world, BlockPos pos) {

    }
    boolean isActive();

    void setActive(boolean active);
}