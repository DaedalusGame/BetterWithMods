package betterwithmods.integration;

import betterwithmods.BWCrafting;
import betterwithmods.BWMItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class Harvestcraft
{
    public static void init()
    {
        Item corn = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "cornItem"));
        Item cornmeal = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "cornmealItem"));
        BWCrafting.addMillRecipe(new ItemStack(cornmeal), new ItemStack(corn));
        Item pepper = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "blackpepperItem"));
        Item peppercorn = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "peppercornItem"));
        BWCrafting.addMillRecipe(new ItemStack(pepper), new ItemStack(peppercorn));
        Item cocoa = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "cocoapowderItem"));
        BWCrafting.addMillRecipe(new ItemStack(cocoa), new ItemStack(Items.DYE, 1, 3));
        Item cinnamon = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "cinnamonItem"));
        Item cinnamonPowder = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "groundcinnamonItem"));
        BWCrafting.addMillRecipe(new ItemStack(cinnamonPowder), new ItemStack(cinnamon));
        Item nutmeg = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "nutmegItem"));
        Item nutmegPowder = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "groundnutmegItem"));
        BWCrafting.addMillRecipe(new ItemStack(nutmegPowder), new ItemStack(nutmeg));
        Item curry = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "curryleafItem"));
        Item curryPowder = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "currypowderItem"));
        BWCrafting.addMillRecipe(new ItemStack(curryPowder), new ItemStack(curry));
        Item boiledEgg = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "boiledeggItem"));
        BWCrafting.addCauldronRecipe(new ItemStack(boiledEgg), new ItemStack[] {new ItemStack(Items.EGG)});
        Item dough = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "doughItem"));
        BWCrafting.addCauldronRecipe(new ItemStack(Items.BREAD), new ItemStack[] {new ItemStack(dough)});
        OreDictionary.registerOre("foodDonut", new ItemStack(BWMItems.donut));
    }
}
