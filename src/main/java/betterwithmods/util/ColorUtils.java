package betterwithmods.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Hashtable;

public class ColorUtils {
    public static final String[] dyes = {"dyeWhite", "dyeOrange", "dyeMagenta", "dyeLightBlue", "dyeYellow", "dyeLime", "dyePink", "dyeGray", "dyeLightGray", "dyeCyan", "dyePurple", "dyeBlue", "dyeBrown", "dyeGreen", "dyeRed", "dyeBlack"};
    private static final Hashtable<String, Integer> colors = new Hashtable<>();
    public static int[] reverseMeta = {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};

    public static void addColor(ItemStack stack, int color) {
        colors.put(stack.getItem() + ":" + stack.getItemDamage(), color);
    }

    public static void initColors() {
        for (int i = 0; i < 16; i++) {
            for (ItemStack dye : OreDictionary.getOres(dyes[i]))
                addColor(dye, i);
        }
    }

    public static boolean contains(Item item, int meta) {
        return colors.containsKey(item + ":" + meta);
    }

    public static int get(Item item, int meta) {
        return colors.get(item + ":" + meta);
    }
}
