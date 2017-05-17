package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;
import betterwithmods.module.Feature;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

import static betterwithmods.module.gameplay.CrucibleRecipes.addStokedCrucibleRecipe;

/**
 * Created by tyler on 4/20/17.
 */
public class MetalReclaming extends Feature {
    public static int reclaimCount;

    @Override
    public String getFeatureDescription() {
        return "Adds recipes to the Crucible to melt metal items back into their component metals";
    }

    @Override
    public void setupConfig() {
        reclaimCount = loadPropInt("Reclaming Count","Amount (in nuggets per ingot) tools and armor in the crucible reclaim. Does not affect diamond or soulforged steel ingot reclamation. (Set to 0 to disable reclamation entirely.)", "",6, 0, 9);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        if (reclaimCount > 0) {
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL, 2), new ItemStack[]{new ItemStack(BWMItems.STEEL_HOE, 1, OreDictionary.WILDCARD_VALUE)});
            //addStokedCrucibleRecipe(ItemMaterial.getMaterial(EnumMaterial.INGOT_STEEL, 2), new ItemStack[]{new ItemStack(BWMItems.STEEL_SWORD, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL, 3), new ItemStack[]{new ItemStack(BWMItems.STEEL_SWORD, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL, 3), new ItemStack[]{new ItemStack(BWMItems.STEEL_PICKAXE, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL, 3), new ItemStack[]{new ItemStack(BWMItems.STEEL_AXE, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL), new ItemStack[]{new ItemStack(BWMItems.STEEL_SHOVEL, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL, 4), new ItemStack[]{new ItemStack(BWMItems.STEEL_MATTOCK, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL, 5), new ItemStack[]{new ItemStack(BWMItems.STEEL_BATTLEAXE, 1, OreDictionary.WILDCARD_VALUE)});
            addReclaimRecipe(new ItemStack(Items.IRON_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE), "Iron", 8);
            addReclaimRecipe(new ItemStack(Items.IRON_AXE, 1, OreDictionary.WILDCARD_VALUE), "Iron", 3);
            addReclaimRecipe(new ItemStack(Items.IRON_BOOTS, 1, OreDictionary.WILDCARD_VALUE), "Iron", 4);
            addReclaimRecipe(new ItemStack(Items.IRON_HELMET, 1, OreDictionary.WILDCARD_VALUE), "Iron", 5);
            addReclaimRecipe(new ItemStack(Items.IRON_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE), "Iron", 7);
            addReclaimRecipe(new ItemStack(Items.IRON_HOE, 1, OreDictionary.WILDCARD_VALUE), "Iron", 2);
            addReclaimRecipe(new ItemStack(Items.IRON_PICKAXE, 1, OreDictionary.WILDCARD_VALUE), "Iron", 3);
            addReclaimRecipe(new ItemStack(Items.IRON_SHOVEL, 1, OreDictionary.WILDCARD_VALUE), "Iron", 1);
            addReclaimRecipe(new ItemStack(Items.IRON_SWORD, 1, OreDictionary.WILDCARD_VALUE), "Iron", 2);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE), "Gold", 8);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_AXE, 1, OreDictionary.WILDCARD_VALUE), "Gold", 3);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_BOOTS, 1, OreDictionary.WILDCARD_VALUE), "Gold", 4);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_HELMET, 1, OreDictionary.WILDCARD_VALUE), "Gold", 5);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE), "Gold", 7);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_HOE, 1, OreDictionary.WILDCARD_VALUE), "Gold", 2);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_PICKAXE, 1, OreDictionary.WILDCARD_VALUE), "Gold", 3);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_SHOVEL, 1, OreDictionary.WILDCARD_VALUE), "Gold", 1);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_SWORD, 1, OreDictionary.WILDCARD_VALUE), "Gold", 2);
            addReclaimRecipe(new ItemStack(Items.SHEARS, 1, OreDictionary.WILDCARD_VALUE), "Iron", 2);

            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL, 10), new ItemStack[]{new ItemStack(BWMItems.STEEL_HELMET, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL, 14), new ItemStack[]{new ItemStack(BWMItems.STEEL_CHEST, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL, 12), new ItemStack[]{new ItemStack(BWMItems.STEEL_PANTS, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL, 8), new ItemStack[]{new ItemStack(BWMItems.STEEL_BOOTS, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(new ItemStack(Items.field_191525_da, 4), new ItemStack[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL)});
            addStokedCrucibleRecipe(new ItemStack(Items.field_191525_da, 20), new ItemStack[]{new ItemStack(Items.CHAINMAIL_HELMET, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(new ItemStack(Items.field_191525_da, 32), new ItemStack[]{new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(new ItemStack(Items.field_191525_da, 28), new ItemStack[]{new ItemStack(Items.CHAINMAIL_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(new ItemStack(Items.field_191525_da, 16), new ItemStack[]{new ItemStack(Items.CHAINMAIL_BOOTS, 1, OreDictionary.WILDCARD_VALUE)});
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL, 7), new ItemStack[]{new ItemStack(BWMBlocks.STEEL_ANVIL)});
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL), new ItemStack[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ARMOR_PLATE)});
            addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL, 16), new ItemStack[]{new ItemStack(BWMBlocks.AESTHETIC, 1, 2)});
        }


    }
    public static void addReclaimRecipe(ItemStack input, String oreSuffix, int ingotCount) {
        int totalNuggets = ingotCount * reclaimCount;
        int ingots = totalNuggets / 9;
        int nuggets = totalNuggets % 9;
        ItemStack ingotStack = null;
        ItemStack nuggetStack = null;
        if (ingots > 0 && !OreDictionary.getOres("ingot" + oreSuffix).isEmpty())
            ingotStack = OreDictionary.getOres("ingot" + oreSuffix).get(0);
        if (nuggets > 0 && !OreDictionary.getOres("nugget" + oreSuffix).isEmpty())
            nuggetStack = OreDictionary.getOres("nugget" + oreSuffix).get(0);
        if (ingotStack == null) {
            if (nuggetStack != null) {
                StokedCrucibleManager.getInstance().addRecipe(new ItemStack(nuggetStack.getItem(), totalNuggets > nuggets ? totalNuggets : nuggets, nuggetStack.getMetadata()), input.copy());
            }
        } else {
            StokedCrucibleManager.getInstance().addRecipe(new ItemStack(ingotStack.getItem(), ingots, ingotStack.getMetadata()), nuggetStack != null ? new ItemStack(nuggetStack.getItem(), nuggets, nuggetStack.getMetadata()) : null, input.copy());
        }
    }
}
