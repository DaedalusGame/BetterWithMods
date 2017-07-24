package betterwithmods.common.registry;

import betterwithmods.common.BWMBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by primetoxinz on 6/6/17.
 */
public class PulleyStructureManager {

    public static Set<IBlockState> PULLEY_BLOCKS = new HashSet<>();

    static {
        registerPulleyBlock(BWMBlocks.PLATFORM.getDefaultState());
        registerPulleyBlock(BWMBlocks.IRON_WALL.getDefaultState());
        registerPulleyBlock(Blocks.DIAMOND_BLOCK.getDefaultState());
    }
    public static void registerPulleyBlock(IBlockState state) {
        PULLEY_BLOCKS.add(state);
    }

    public static boolean isPulleyBlock(IBlockState state) {
        return PULLEY_BLOCKS.contains(state);
    }

    public static void removePulleyBlock(IBlockState state) {
        PULLEY_BLOCKS.remove(state);
    }
}
