package betterwithmods.common.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by primetoxinz on 7/19/17.
 */
public interface IRenderUpdate {

    void update(World world, BlockPos pos);
}
