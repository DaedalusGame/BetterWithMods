package betterwithmods.integration;

import betterwithmods.common.BWMItems;
import betterwithmods.module.gameplay.CauldronRecipes;
import betterwithmods.module.gameplay.MillRecipes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@SuppressWarnings("unused")
public class Harvestcraft implements ICompatModule {
    public static final String MODID = "harvestcraft";

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        Item corn = Item.REGISTRY.getObject(new ResourceLocation(MODID, "cornitem"));
        Item cornmeal = Item.REGISTRY.getObject(new ResourceLocation(MODID, "cornmealitem"));
        Item pepper = Item.REGISTRY.getObject(new ResourceLocation(MODID, "blackpepperitem"));
        Item peppercorn = Item.REGISTRY.getObject(new ResourceLocation(MODID, "peppercornitem"));
        Item cocoa = Item.REGISTRY.getObject(new ResourceLocation(MODID, "cocoapowderitem"));
        Item cinnamon = Item.REGISTRY.getObject(new ResourceLocation(MODID, "cinnamonitem"));
        Item cinnamonPowder = Item.REGISTRY.getObject(new ResourceLocation(MODID, "groundcinnamonitem"));
        Item nutmeg = Item.REGISTRY.getObject(new ResourceLocation(MODID, "nutmegitem"));
        Item nutmegPowder = Item.REGISTRY.getObject(new ResourceLocation(MODID, "groundnutmegitem"));
        Item curry = Item.REGISTRY.getObject(new ResourceLocation(MODID, "curryleafitem"));
        Item curryPowder = Item.REGISTRY.getObject(new ResourceLocation(MODID, "currypowderitem"));
        Item boiledEgg = Item.REGISTRY.getObject(new ResourceLocation(MODID, "boiledeggitem"));
        Item dough = Item.REGISTRY.getObject(new ResourceLocation(MODID, "doughitem"));

        MillRecipes.addMillRecipe(new ItemStack(cornmeal), new ItemStack(corn));
        MillRecipes.addMillRecipe(new ItemStack(pepper), new ItemStack(peppercorn));
        MillRecipes.addMillRecipe(new ItemStack(cocoa), new ItemStack(Items.DYE, 1, 3));
        MillRecipes.addMillRecipe(new ItemStack(cinnamonPowder), new ItemStack(cinnamon));
        MillRecipes.addMillRecipe(new ItemStack(nutmegPowder), new ItemStack(nutmeg));
        MillRecipes.addMillRecipe(new ItemStack(curryPowder), new ItemStack(curry));
        CauldronRecipes.addCauldronRecipe(new ItemStack(boiledEgg), new ItemStack[]{new ItemStack(Items.EGG)});
        CauldronRecipes.addCauldronRecipe(new ItemStack(Items.BREAD), new ItemStack[]{new ItemStack(dough)});
        OreDictionary.registerOre("foodDonut", new ItemStack(BWMItems.DONUT));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMItems.CHOCOLATE, 2), "foodCocoapowder", "listAllmilk", Items.SUGAR, "toolSaucepan"));
    }

    @Override
    public void postInit() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void postInitClient() {

    }

}
