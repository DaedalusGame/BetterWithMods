package betterwithmods.world.gen.feature;

import betterwithmods.blocks.BlockStump;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

import java.util.Random;

/**
 * Hardcore Stumping tree.
 *
 * @author Koward
 */
public class WorldGenSavannaTreeWithStump extends WorldGenSavannaTree {
    private static final IBlockState LOG = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);

    public WorldGenSavannaTreeWithStump(boolean doBlockNotify) {
        super(doBlockNotify);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        boolean vanillaResult = super.generate(worldIn, rand, position);
        if (vanillaResult && BlockStump.canPlaceStump(worldIn, position)) {
            IBlockState stump = BlockStump.getStump(LOG);
            if (stump == null) return vanillaResult;
            this.setBlockAndNotifyAdequately(worldIn, position, stump);
        }
        return vanillaResult;
    }
}
