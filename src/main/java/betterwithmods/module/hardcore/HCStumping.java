package betterwithmods.module.hardcore;

import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockStump;
import betterwithmods.common.world.gen.feature.*;
import betterwithmods.module.Feature;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by tyler on 4/20/17.
 */
public class HCStumping extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Makes the bottom block of trees into stumps which cannot be removed by hand, making your mark on the world more obvious";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.STUMP_REMOVER, 2), new ItemStack(BWMItems.CREEPER_OYSTER), new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(Items.ROTTEN_FLESH));
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    /**
     * Leaves and logs are skipped to reach the raw terrain surface
     *
     * @param state State to check.
     */
    private static boolean shouldBeSkipped(IBlockState state) {
        return state.getBlock() == Blocks.LEAVES ||
                state.getBlock() == Blocks.LEAVES2 ||
                state.getBlock() == Blocks.LOG ||
                state.getBlock() == Blocks.LOG2 ||
                state.getBlock() == Blocks.AIR;
    }

    /**
     * Whether a tree could be on top of the block.
     * Copy of {@link BlockBush#canSustainBush(IBlockState)} build 2185
     *
     * @param state State to check.
     */
    private static boolean canSustainTree(IBlockState state) {
        return state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.FARMLAND;
    }

    /**
     * Add stumps to trees added during world generation.
     *
     * @param event Triggered for each chunk cross-shaped intersection (hence the +8 offsets)
     */
    @SubscribeEvent
    public void addStumpsToGeneratedTrees(DecorateBiomeEvent.Post event) {
        if (!event.getWorld().provider.isSurfaceWorld()) return;
        for (int dx = 0; dx < 16; dx++) {
            int x = event.getPos().getX() + dx;
            x += 8;
            for (int dz = 0; dz < 16; dz++) {
                int z = event.getPos().getZ() + dz;
                z += 8;
                Chunk chunk = event.getWorld().getChunkFromBlockCoords(new BlockPos(x, 0, z));
                int y = chunk.getHeightValue(x & 15, z & 15) - 1;
                BlockPos pos = new BlockPos(x, y, z);
                IBlockState state = event.getWorld().getBlockState(pos);
                while (shouldBeSkipped(state)) {
                    y--;
                    pos = new BlockPos(x, y, z);
                    state = event.getWorld().getBlockState(pos);
                }
                if (BlockStump.canPlaceStump(event.getWorld(), pos.up()) && canSustainTree(state)) {
                    IBlockState stump = BlockStump.getStump(event.getWorld().getBlockState(pos.up()));
                    if (stump != null) {
                        event.getWorld().setBlockState(pos.up(), stump);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void addStumpToTree(SaplingGrowTreeEvent event) {
        IBlockState state = event.getWorld().getBlockState(event.getPos());
        if (state.getBlock() != Blocks.SAPLING)/*(!(state.getBlock() instanceof BlockSapling))*/ return;
        event.setResult(Event.Result.DENY);
        generateTreeWithStump(event.getWorld(), event.getPos(), state, event.getRand());
    }


    /**
     * Hardcore Stumping
     * Checks various properties to find a {@link BlockPlanks.EnumType} for the state.
     *
     * @param state State of the wooden block to get wood type from.
     * @return The type if it has any, null otherwise.
     */
    @Nullable
    public static BlockPlanks.EnumType getWoodType(IBlockState state) {
        if (state.getProperties().containsKey(BlockPlanks.VARIANT)) {
            return state.getValue(BlockPlanks.VARIANT);
        } else if (state.getProperties().containsKey(BlockOldLog.VARIANT)) {
            return state.getValue(BlockOldLog.VARIANT);
        } else if (state.getProperties().containsKey(BlockNewLog.VARIANT)) {
            return state.getValue(BlockNewLog.VARIANT);
        } else return null;
    }

    /**
     * Hardcore Stumping
     * <p>
     * Edited version of {@link BlockSapling#isTwoByTwoOfType(World, BlockPos, int, int, BlockPlanks.EnumType)} build 2185
     */
    private static boolean isTwoByTwoOfType(BlockSapling sapling, World worldIn, BlockPos pos, int p_181624_3_, int p_181624_4_, BlockPlanks.EnumType type) {
        return sapling.isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_), type) && sapling.isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_), type) && sapling.isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_ + 1), type) && sapling.isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_ + 1), type);
    }

    /**
     * Hardcore Stumping
     * <p>
     * Edited version of {@link BlockSapling#generateTree(World, BlockPos, IBlockState, Random)} build 2185
     */
    public static void generateTreeWithStump(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!(state.getBlock() instanceof BlockSapling)) return;
        BlockSapling sapling = (BlockSapling) state.getBlock();
        WorldGenerator worldgenerator = rand.nextInt(10) == 0 ? new WorldGenBigTreeWithStump(true) : new WorldGenTreesWithStump(true);
        int i = 0;
        int j = 0;
        boolean flag = false;

        switch (state.getValue(BlockSapling.TYPE)) {
            case SPRUCE:
                label114:

                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (isTwoByTwoOfType(sapling, worldIn, pos, i, j, BlockPlanks.EnumType.SPRUCE)) {
                            worldgenerator = new WorldGenMegaPineTreeWithStump(false, rand.nextBoolean());
                            flag = true;
                            break label114;
                        }
                    }
                }

                if (!flag) {
                    i = 0;
                    j = 0;
                    worldgenerator = new WorldGenTaiga2WithStump(true);
                }

                break;
            case BIRCH:
                worldgenerator = new WorldGenBirchTreeWithStump(true, false);
                break;
            case JUNGLE:
                IBlockState iblockstate = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
                IBlockState iblockstate1 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
                label269:

                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (isTwoByTwoOfType(sapling, worldIn, pos, i, j, BlockPlanks.EnumType.JUNGLE)) {
                            worldgenerator = new WorldGenMegaJungleWithStump(true, 10, 20, iblockstate, iblockstate1);
                            flag = true;
                            break label269;
                        }
                    }
                }

                if (!flag) {
                    i = 0;
                    j = 0;
                    worldgenerator = new WorldGenTreesWithStump(true, 4 + rand.nextInt(7), iblockstate, iblockstate1, false);
                }

                break;
            case ACACIA:
                worldgenerator = new WorldGenSavannaTreeWithStump(true);
                break;
            case DARK_OAK:
                label390:

                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (isTwoByTwoOfType(sapling, worldIn, pos, i, j, BlockPlanks.EnumType.DARK_OAK)) {
                            worldgenerator = new WorldGenCanopyTreeWithStump(true);
                            flag = true;
                            break label390;
                        }
                    }
                }

                if (!flag) {
                    return;
                }

            case OAK:
        }

        IBlockState iblockstate2 = Blocks.AIR.getDefaultState();

        if (flag) {
            worldIn.setBlockState(pos.add(i, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate2, 4);
        } else {
            worldIn.setBlockState(pos, iblockstate2, 4);
        }

        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j))) {
            if (flag) {
                worldIn.setBlockState(pos.add(i, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
            } else {
                worldIn.setBlockState(pos, state, 4);
            }
        }
    }
    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
