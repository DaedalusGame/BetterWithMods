package betterwithmods.module.compat;

import betterwithmods.common.BWMItems;
import betterwithmods.common.BWMRecipes;
import betterwithmods.module.CompatFeature;
import betterwithmods.module.gameplay.CauldronRecipes;
import betterwithmods.module.gameplay.MillRecipes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

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

        MillRecipes.addMillRecipe(new ItemStack(cornmeal), new ItemStack[]{new ItemStack(corn)});
        MillRecipes.addMillRecipe(new ItemStack(pepper), new ItemStack[]{new ItemStack(peppercorn)});
        MillRecipes.addMillRecipe(new ItemStack(cocoa), new Object[]{new ItemStack(Items.DYE, 1, 3)});
        MillRecipes.addMillRecipe(new ItemStack(cinnamonPowder), new Object[]{new ItemStack(cinnamon)});
        MillRecipes.addMillRecipe(new ItemStack(nutmegPowder), new ItemStack[]{new ItemStack(nutmeg)});
        MillRecipes.addMillRecipe(new ItemStack(curryPowder), new ItemStack[]{new ItemStack(curry)});
        CauldronRecipes.addCauldronRecipe(new ItemStack(boiledEgg), new Object[]{new ItemStack(Items.EGG)});
        CauldronRecipes.addCauldronRecipe(new ItemStack(Items.BREAD), new Object[]{new ItemStack(dough)});
        OreDictionary.registerOre("foodDonut", new ItemStack(BWMItems.DONUT));
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMItems.CHOCOLATE, 2), "foodCocoapowder", "listAllmilk", Items.SUGAR, "toolSaucepan");
    }

}
