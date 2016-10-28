package betterwithmods.craft;

import betterwithmods.BWMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class SawInteraction {
    private static Hashtable<String, List<ItemStack>> woodProduct = new Hashtable<String, List<ItemStack>>();

    public static void addBlock(ItemStack input, ItemStack... products) {
        if (input.getItem() != null && input.getItem() instanceof ItemBlock) {
            addBlock(((ItemBlock) input.getItem()).getBlock(), input.getMetadata(), products);
        } else {
            BWMod.logger.info("Saw input: %s must be a block", input);
        }
    }

    public static void addBlock(Block block, int meta, ItemStack... products) {
        woodProduct.put(block + ":" + meta, Arrays.asList(products));
    }

    public static boolean contains(Block block, int meta) {
        return woodProduct.containsKey(block + ":" + meta);
    }

    public static List<ItemStack> getProducts(Block block, int meta) {
        return woodProduct.get(block + ":" + meta);
    }

    public static Hashtable<String, List<ItemStack>> getWoodProducts() {
        return woodProduct;
    }
}
