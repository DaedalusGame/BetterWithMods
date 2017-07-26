package betterwithmods.module.hardcore;

import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.CrucibleRecipes;
import betterwithmods.module.gameplay.MetalReclaming;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.DIAMOND_AXE), "DD", "DS", " S", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND), 'S', "stickWood").setMirrored(true).setRegistryName(new ResourceLocation("minecraft", "diamond_axe")));
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.DIAMOND_HOE), "DD", " S", " S", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND), 'S', "stickWood").setMirrored(true).setRegistryName(new ResourceLocation("minecraft", "diamond_hoe")));
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.DIAMOND_PICKAXE), "DDD", " S ", " S ", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND), 'S', "stickWood").setRegistryName(new ResourceLocation("minecraft", "diamond_pickaxe")));
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.DIAMOND_SHOVEL), "D", "S", "S", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND), 'S', "stickWood").setRegistryName(new ResourceLocation("minecraft", "diamond_shovel")));
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.DIAMOND_SWORD), "D", "D", "S", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND), 'S', "stickWood").setRegistryName(new ResourceLocation("minecraft", "diamond_sword")));
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.DIAMOND_HELMET), "DDD", "D D", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND)).setRegistryName(new ResourceLocation("minecraft", "diamond_helmet")));
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.DIAMOND_CHESTPLATE), "D D", "DDD", "DDD", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND)).setRegistryName(new ResourceLocation("minecraft", "diamond_chestplate")));
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.DIAMOND_LEGGINGS), "DDD", "D D", "D D", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND)).setRegistryName(new ResourceLocation("minecraft", "diamond_leggings")));
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.DIAMOND_BOOTS), "D D", "D D", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND)).setRegistryName(new ResourceLocation("minecraft", "diamond_boots")));

        if (ModuleLoader.isFeatureEnabled(MetalReclaming.class) && MetalReclaming.reclaimCount > 0) {
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND, 3),new Object[]{new ItemStack(Items.DIAMOND_AXE, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND, 2),new Object[]{new ItemStack(Items.DIAMOND_HOE, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND, 3),new Object[]{new ItemStack(Items.DIAMOND_PICKAXE, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND, 1),new Object[]{new ItemStack(Items.DIAMOND_SHOVEL, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND, 2),new Object[]{new ItemStack(Items.DIAMOND_SWORD, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND, 5),new Object[]{new ItemStack(Items.DIAMOND_HELMET, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND, 8),new Object[]{new ItemStack(Items.DIAMOND_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND, 7),new Object[]{new ItemStack(Items.DIAMOND_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_DIAMOND, 4),new Object[]{new ItemStack(Items.DIAMOND_BOOTS, 1, OreDictionary.WILDCARD_VALUE)});
        }
    }


    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }
}
