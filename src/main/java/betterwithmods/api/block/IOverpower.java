package betterwithmods.api.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by primetoxinz on 7/18/17.
 */
//TODO this will be used in the future to dictate what happens when a block is overpowered.
public interface IOverpower {
    void overpower(World world, BlockPos pos);
}
