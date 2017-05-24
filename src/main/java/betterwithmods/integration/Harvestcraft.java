package betterwithmods.integration;

import betterwithmods.common.BWMItems;
import betterwithmods.module.gameplay.CauldronRecipes;
import betterwithmods.module.gameplay.MillRecipes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@SuppressWarnings("unused")
public class Harvestcraft extends CompatFeature {
    public Harvestcraft() {
        super("harvestcraft");
    }


    @Override
    public void init(FMLInitializationEvent event) {

        Item corn = getItem(new ResourceLocation(modid, "cornitem"));
        Item cornmeal = getItem(new ResourceLocation(modid, "cornmealitem"));
        Item pepper = getItem(new ResourceLocation(modid, "blackpepperitem"));
        Item peppercorn = getItem(new ResourceLocation(modid, "peppercornitem"));
        Item cocoa = getItem(new ResourceLocation(modid, "cocoapowderitem"));
        Item cinnamon = getItem(new ResourceLocation(modid, "cinnamonitem"));
        Item cinnamonPowder = getItem(new ResourceLocation(modid, "groundcinnamonitem"));
        Item nutmeg = getItem(new ResourceLocation(modid, "nutmegitem"));
        Item nutmegPowder = getItem(new ResourceLocation(modid, "groundnutmegitem"));
        Item curry = getItem(new ResourceLocation(modid, "curryleafitem"));
        Item curryPowder = getItem(new ResourceLocation(modid, "currypowderitem"));
        Item boiledEgg = getItem(new ResourceLocation(modid, "boiledeggitem"));
        Item dough = getItem(new ResourceLocation(modid, "doughitem"));

        MillRecipes.addMillRecipe(new ItemStack(cornmeal), new ItemStack(corn));
        MillRecipes.addMillRecipe(new ItemStack(pepper), new ItemStack(peppercorn));
        MillRecipes.addMillRecipe(new ItemStack(cocoa), new ItemStack(Items.DYE, 1, 3));
        MillRecipes.addMillRecipe(new ItemStack(cinnamonPowder), new ItemStack(cinnamon));
        MillRecipes.addMillRecipe(new ItemStack(nutmegPowder), new ItemStack(nutmeg));
        MillRecipes.addMillRecipe(new ItemStack(curryPowder), new ItemStack(curry));
        CauldronRecipes.addCauldronRecipe(new ItemStack(boiledEgg), new Object[]{new ItemStack(Items.EGG)});
        CauldronRecipes.addCauldronRecipe(new ItemStack(Items.BREAD), new Object[]{new ItemStack(dough)});
        OreDictionary.registerOre("foodDonut", new ItemStack(BWMItems.DONUT));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMItems.CHOCOLATE, 2), "foodCocoapowder", "listAllmilk", Items.SUGAR, "toolSaucepan"));
    }

}
