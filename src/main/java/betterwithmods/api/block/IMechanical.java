package betterwithmods.api.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMechanical {
    public int getMechPowerLevelToFacing(World world, BlockPos pos, EnumFacing dir);

    boolean isMechanicalJunction();
}
