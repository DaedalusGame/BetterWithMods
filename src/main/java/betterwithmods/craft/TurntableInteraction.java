package betterwithmods.craft;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.Hashtable;

public class TurntableInteraction {
    private static Hashtable<IBlockState, TurntableCraft> spinnables = new Hashtable<>();

    public static void addBlockRecipe(Block block, IBlockState result, ItemStack... scraps) {
        TurntableCraft craft = new TurntableCraft(result, scraps);
        for (IBlockState state : block.getBlockState().getValidStates())
            spinnables.put(state, craft);
    }

    public static void addBlockRecipe(IBlockState state, IBlockState result, ItemStack... scraps) {
        spinnables.put(state, new TurntableCraft(result, scraps));
    }

    public static boolean contains(IBlockState state) {
        return spinnables.containsKey(state);
    }

    public static TurntableCraft getProduct(IBlockState state) {
        return spinnables.get(state);
    }

    public static Hashtable<IBlockState, TurntableCraft> getSpinnables() {
        return spinnables;
    }
}
