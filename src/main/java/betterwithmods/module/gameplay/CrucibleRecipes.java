package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.OreStack;
import betterwithmods.common.registry.bulk.manager.CrucibleManager;
import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;
import betterwithmods.module.Feature;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by tyler on 5/16/17.
 */
public class CrucibleRecipes extends Feature {
    public CrucibleRecipes() {
        canDisable = false;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), new Object[]{new OreStack("nuggetDiamond", 9)});
        addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL), new Object[]{new OreStack("nuggetSoulforgedSteel", 9)});

        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 2, 0), new Object[]{new ItemStack(Items.IRON_DOOR)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 8, 0), new Object[]{new ItemStack(Items.IRON_HORSE_ARMOR, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 8, 0), new Object[]{new ItemStack(Items.GOLDEN_HORSE_ARMOR, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 5), new Object[]{new ItemStack(Items.MINECART)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 5), new Object[]{new ItemStack(Items.CHEST_MINECART)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 5), new Object[]{new ItemStack(Items.FURNACE_MINECART)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 5), new Object[]{new ItemStack(Items.HOPPER_MINECART)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 5), new Object[]{new ItemStack(Items.TNT_MINECART)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 3), new Object[]{new ItemStack(Blocks.RAIL, 8)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 6), new Object[]{new ItemStack(Blocks.GOLDEN_RAIL, 6)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 6), new Object[]{new ItemStack(Blocks.ACTIVATOR_RAIL, 6)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 3), new Object[]{new ItemStack(Blocks.IRON_BARS, 8)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 7), new Object[]{new ItemStack(Items.CAULDRON)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 31), new Object[]{new ItemStack(Blocks.ANVIL, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT), new Object[]{new ItemStack(Blocks.TRIPWIRE_HOOK, 2, 0)});
        addStokedCrucibleRecipe(new ItemStack(Blocks.GLASS), new Object[]{new ItemStack(Blocks.SAND, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Blocks.GLASS, 3), new Object[]{new ItemStack(Blocks.GLASS_PANE, 8)});
        addStokedCrucibleRecipe(new ItemStack(Blocks.STONE), new Object[]{new ItemStack(Blocks.COBBLESTONE)});
        addStokedCrucibleRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 6), new Object[]{new ItemStack(BWMBlocks.AESTHETIC, 1, 7)});
        addCrucibleRecipe(new ItemStack(Blocks.SPONGE, 1, 0), new Object[]{new ItemStack(Blocks.SPONGE, 1, 1)});
        addCrucibleRecipe(new ItemStack(Blocks.SPONGE, 1, 0), new ItemStack(Items.WATER_BUCKET), new Object[]{new ItemStack(Blocks.SPONGE, 1, 1), new ItemStack(Items.BUCKET)});
    }

    public static void addCrucibleRecipe(ItemStack output, Object[] inputs) {
        CrucibleManager.getInstance().addRecipe(output, ItemStack.EMPTY, inputs);
    }

    public static void addCrucibleRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        CrucibleManager.getInstance().addRecipe(output, secondary, inputs);
    }

    public static void addStokedCrucibleRecipe(ItemStack output, Object[] inputs) {
        StokedCrucibleManager.getInstance().addRecipe(output, inputs);
    }

    public static void addStokedCrucibleRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        StokedCrucibleManager.getInstance().addRecipe(output, secondary, inputs);
    }
}
