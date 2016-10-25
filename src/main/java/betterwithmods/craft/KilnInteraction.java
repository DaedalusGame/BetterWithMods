package betterwithmods.craft;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class KilnInteraction {
    private static Hashtable<String, List<ItemStack>> cookables = new Hashtable<>();

    public static void addBlockRecipe(Block block, ItemStack... output) {
        addBlockRecipe(block, Arrays.asList(output));
    }

    public static void addBlockRecipe(Block block, List<ItemStack> output) {
        for (IBlockState state : block.getBlockState().getValidStates()) {
            if (!contains(block, block.damageDropped(state)))
                cookables.put(block + ":" + block.damageDropped(state), output);
        }
    }

    //TODO IBlockState version instead of block + meta
    public static void addBlockRecipe(Block block, int meta, ItemStack... output) {
        addBlockRecipe(block, meta, Arrays.asList(output));
    }

    public static void addBlockRecipe(Block block, int meta, List<ItemStack> output) {
        cookables.put(block + ":" + meta, output);
    }

    public static boolean contains(Block block, int meta) {
        return cookables.containsKey(block + ":" + meta);
    }

    public static List<ItemStack> getProduct(Block block, int meta) {
        return cookables.get(block + ":" + meta);
    }

    public static Hashtable<String, List<ItemStack>> getCookables() {
        return cookables;
    }
}
