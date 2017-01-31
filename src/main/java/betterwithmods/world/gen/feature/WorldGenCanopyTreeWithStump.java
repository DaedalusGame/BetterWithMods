package betterwithmods.world.gen.feature;

import betterwithmods.blocks.BlockStump;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;

import java.util.Random;

/**
 * Hardcore Stumping tree.
 *
 * @author Koward
 */
public class WorldGenCanopyTreeWithStump extends WorldGenCanopyTree {
    private static final IBlockState LOG = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);

    public WorldGenCanopyTreeWithStump(boolean notify) {
        super(notify);
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
            IBlockState stump = BlockStump.getStump(LOG);
            if (stump == null) return;
            this.setBlockAndNotifyAdequately(worldIn, position, stump);
        }
    }
}
