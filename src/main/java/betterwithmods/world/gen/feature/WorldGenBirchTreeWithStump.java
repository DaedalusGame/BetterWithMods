package betterwithmods.world.gen.feature;

import betterwithmods.blocks.BlockStump;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBirchTree;

import java.util.Random;

/**
 * Hardcore Stumping tree.
 *
 * @author Koward
 */
public class WorldGenBirchTreeWithStump extends WorldGenBirchTree {
    private static final IBlockState LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);

    public WorldGenBirchTreeWithStump(boolean notify, boolean useExtraRandomHeightIn) {
        super(notify, useExtraRandomHeightIn);
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
