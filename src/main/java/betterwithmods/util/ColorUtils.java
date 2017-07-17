package betterwithmods.util;

import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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


    public static float[] average(float[]... arrays) {
        int divisor = arrays.length;
        float[] output = new float[arrays[0].length];
        for (int i = 0; i < divisor; i++) {
            for (int j = 0; j < arrays[i].length; j++) {
                output[j] += arrays[i][j];
            }
        }
        for (int i = 0; i < output.length; i++) {
            output[i] = output[i] / divisor;
        }
        return output;
    }

    public static float[] getColorFromBlock(World world, BlockPos pos, BlockPos beacon) {
        if (world.isAirBlock(pos))
            return new float[]{1, 1, 1};
        IBlockState state = world.getBlockState(pos);
        float[] color = state.getBlock().getBeaconColorMultiplier(state, world, pos, beacon);
        if (state.getBlock() == Blocks.STAINED_GLASS) {
            color = state.getValue(BlockStainedGlass.COLOR).getColorComponentValues();
        } else if (state.getBlock() == Blocks.STAINED_GLASS_PANE) {
            color = state.getValue(BlockStainedGlassPane.COLOR).getColorComponentValues();
        }
        return color != null ? color : new float[]{1, 1, 1};
    }
}
