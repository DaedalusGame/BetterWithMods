package betterwithmods.event;

import betterwithmods.blocks.BlockStump;
import betterwithmods.config.BWConfig;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Koward
 */
public class StumpingEventHandler {
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
        if (!BWConfig.hardcoreStumping) return;
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
}
