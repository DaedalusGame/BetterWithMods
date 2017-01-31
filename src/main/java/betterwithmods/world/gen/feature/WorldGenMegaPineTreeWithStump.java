package betterwithmods.world.gen.feature;

import betterwithmods.blocks.BlockStump;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;

import java.util.Random;

/**
 * Hardcore Stumping tree.
 *
 * @author Koward
 */
public class WorldGenMegaPineTreeWithStump extends WorldGenMegaPineTree {

    public WorldGenMegaPineTreeWithStump(boolean notify, boolean p_i45457_2_) {
        super(notify, p_i45457_2_);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        boolean vanillaResult = super.generate(worldIn, rand, position);
        if (vanillaResult) {
            attemptToPlaceStump(worldIn, position);
            attemptToPlaceStump(worldIn, position.add(0, 0, 1));
            attemptToPlaceStump(worldIn, position.add(1, 0, 0));
            attemptToPlaceStump(worldIn, position.add(1, 0, 1));
        }
        return vanillaResult;
    }

    private void attemptToPlaceStump(World worldIn, BlockPos position) {
        if (BlockStump.canPlaceStump(worldIn, position)) {
            IBlockState stump = BlockStump.getStump(woodMetadata);
            if (stump == null) return;
            this.setBlockAndNotifyAdequately(worldIn, position, stump);
        }
    }
}
