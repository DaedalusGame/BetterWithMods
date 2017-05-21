package betterwithmods.module.hardcore;

import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.CrucibleRecipes;
import betterwithmods.module.gameplay.MetalReclaming;
import betterwithmods.util.RecipeUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by tyler on 4/20/17.
 */
public class HCDiamond extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Makes it so diamonds have to me made into an ingot alloy to be used in certain recipes";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        RecipeUtils.removeRecipes(Items.DIAMOND_AXE, 0);
        RecipeUtils.removeRecipes(Items.DIAMOND_HOE, 0);
        RecipeUtils.removeRecipes(Items.DIAMOND_PICKAXE, 0);
        RecipeUtils.removeRecipes(Items.DIAMOND_SHOVEL, 0);
        RecipeUtils.removeRecipes(Items.DIAMOND_SWORD, 0);
        RecipeUtils.removeRecipes(Items.DIAMOND_HELMET, 0);
        RecipeUtils.removeRecipes(Items.DIAMOND_CHESTPLATE, 0);
        RecipeUtils.removeRecipes(Items.DIAMOND_LEGGINGS, 0);
        RecipeUtils.removeRecipes(Items.DIAMOND_BOOTS, 0);
        GameRegistry.addRecipe(new ShapedOreRecipe(Items.DIAMOND_AXE, "DD", "DS", " S", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), 'S', "stickWood").setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(Items.DIAMOND_HOE, "DD", " S", " S", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), 'S', "stickWood").setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(Items.DIAMOND_PICKAXE, "DDD", " S ", " S ", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(Items.DIAMOND_SHOVEL, "D", "S", "S", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(Items.DIAMOND_SWORD, "D", "D", "S", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(Items.DIAMOND_HELMET, "DDD", "D D", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT)));
        GameRegistry.addRecipe(new ShapedOreRecipe(Items.DIAMOND_CHESTPLATE, "D D", "DDD", "DDD", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT)));
        GameRegistry.addRecipe(new ShapedOreRecipe(Items.DIAMOND_LEGGINGS, "DDD", "D D", "D D", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT)));
        GameRegistry.addRecipe(new ShapedOreRecipe(Items.DIAMOND_BOOTS, "D D", "D D", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT)));

        if (ModuleLoader.isFeatureEnabled(MetalReclaming.class) && MetalReclaming.reclaimCount > 0) {
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 3),new Object[]{new ItemStack(Items.DIAMOND_AXE, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2),new Object[]{new ItemStack(Items.DIAMOND_HOE, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 3),new Object[]{new ItemStack(Items.DIAMOND_PICKAXE, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 1),new Object[]{new ItemStack(Items.DIAMOND_SHOVEL, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2),new Object[]{new ItemStack(Items.DIAMOND_SWORD, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 5),new Object[]{new ItemStack(Items.DIAMOND_HELMET, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 8),new Object[]{new ItemStack(Items.DIAMOND_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 7),new Object[]{new ItemStack(Items.DIAMOND_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 4),new Object[]{new ItemStack(Items.DIAMOND_BOOTS, 1, OreDictionary.WILDCARD_VALUE)});
        }
    }
    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }
}
