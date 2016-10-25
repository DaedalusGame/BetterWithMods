package betterwithmods.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BWMRecipeHelper {
    public static void registerHardcoreLogs(Block log, int meta, ItemStack planks) {
        InterModCommsHelper.registerLogInteractionRecipe("addLogHarvest", log, meta, planks);
    }

    public static void addNetherWhitelistBlock(Block block, int meta) {
        InterModCommsHelper.registerWhitelistRecipe("addNetherSpawnBlock", block, meta);
    }

    public static void addHeatRegistry(Block block, int meta, int heatValue) {
        InterModCommsHelper.registerHeatInteractionRecipe("addHeatRegistry", block, meta, heatValue);
    }

    public static void addTurntableRecipe(Block block, int meta, Block output, int outMeta, ItemStack... scraps) {
        InterModCommsHelper.registerTurntableRecipe(block, meta, output, outMeta, scraps);
    }

    public static void addKilnRecipe(Block block, int meta, ItemStack output) {
        InterModCommsHelper.registerInteractionRecipe("addKilnRecipe", block, meta, output);
    }

    public static void addMillRecipe(ItemStack output, Object[] input) {
        addMillRecipe(output, null, input);
    }

    public static void addMillRecipe(ItemStack output, ItemStack secondary, Object[] input) {
        InterModCommsHelper.registerBulkRecipe("addMillRecipe", output, secondary, input);
    }

    public static void addStokedCrucibleRecipe(ItemStack output, Object[] input) {
        addStokedCrucibleRecipe(output, null, input);
    }

    public static void addStokedCrucibleRecipe(ItemStack output, ItemStack secondary, Object[] input) {
        InterModCommsHelper.registerBulkRecipe("addStokedCrucibleRecipe", output, secondary, input);
    }

    public static void addCrucibleRecipe(ItemStack output, Object[] input) {
        addCrucibleRecipe(output, null, input);
    }

    public static void addCrucibleRecipe(ItemStack output, ItemStack secondary, Object[] input) {
        InterModCommsHelper.registerBulkRecipe("addCrucibleRecipe", output, secondary, input);
    }

    public static void addStokedCauldronRecipe(ItemStack output, Object[] input) {
        addStokedCauldronRecipe(output, null, input);
    }

    public static void addStokedCauldronRecipe(ItemStack output, ItemStack secondary, Object[] input) {
        InterModCommsHelper.registerBulkRecipe("addStokedCauldronRecipe", output, secondary, input);
    }

    public static void addCauldronRecipe(ItemStack output, Object[] input) {
        addCauldronRecipe(output, null, input);
    }

    public static void addCauldronRecipe(ItemStack output, ItemStack secondary, Object[] input) {
        InterModCommsHelper.registerBulkRecipe("addCauldronRecipe", output, secondary, input);
    }
}
