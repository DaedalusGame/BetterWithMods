package betterwithmods.world.gen.feature;

import betterwithmods.blocks.BlockStump;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;

import java.util.Random;

/**
 * Hardcore Stumping tree.
 *
 * @author Koward
 */
public class WorldGenTreesWithStump extends WorldGenTrees {
    private final IBlockState metaWood;

    public WorldGenTreesWithStump(boolean p_i2027_1_) {
        super(p_i2027_1_);
        this.metaWood = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
    }

    public WorldGenTreesWithStump(boolean p_i46446_1_, int p_i46446_2_, IBlockState p_i46446_3_, IBlockState p_i46446_4_, boolean p_i46446_5_) {
        super(p_i46446_1_, p_i46446_2_, p_i46446_3_, p_i46446_4_, p_i46446_5_);
        this.metaWood = p_i46446_3_;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        boolean vanillaResult = super.generate(worldIn, rand, position);
        if (vanillaResult && BlockStump.canPlaceStump(worldIn, position)) {
            IBlockState stump = BlockStump.getStump(metaWood);
            if (stump == null) return vanillaResult;
            this.setBlockAndNotifyAdequately(worldIn, position, stump);
        }
        return vanillaResult;
    }
}
