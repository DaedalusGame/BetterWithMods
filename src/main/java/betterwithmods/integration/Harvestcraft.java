package betterwithmods.integration;

import betterwithmods.BWCrafting;
import betterwithmods.BWMItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("unused")
public class Harvestcraft implements ICompatModule {
    public static final String MODID = "harvestcraft";

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        Item corn = Item.REGISTRY.getObject(new ResourceLocation(MODID, "cornItem"));
        Item cornmeal = Item.REGISTRY.getObject(new ResourceLocation(MODID, "cornmealItem"));
        BWCrafting.addMillRecipe(new ItemStack(cornmeal), new ItemStack(corn));
        Item pepper = Item.REGISTRY.getObject(new ResourceLocation(MODID, "blackpepperItem"));
        Item peppercorn = Item.REGISTRY.getObject(new ResourceLocation(MODID, "peppercornItem"));
        BWCrafting.addMillRecipe(new ItemStack(pepper), new ItemStack(peppercorn));
        Item cocoa = Item.REGISTRY.getObject(new ResourceLocation(MODID, "cocoapowderItem"));
        BWCrafting.addMillRecipe(new ItemStack(cocoa), new ItemStack(Items.DYE, 1, 3));
        Item cinnamon = Item.REGISTRY.getObject(new ResourceLocation(MODID, "cinnamonItem"));
        Item cinnamonPowder = Item.REGISTRY.getObject(new ResourceLocation(MODID, "groundcinnamonItem"));
        BWCrafting.addMillRecipe(new ItemStack(cinnamonPowder), new ItemStack(cinnamon));
        Item nutmeg = Item.REGISTRY.getObject(new ResourceLocation(MODID, "nutmegItem"));
        Item nutmegPowder = Item.REGISTRY.getObject(new ResourceLocation(MODID, "groundnutmegItem"));
        BWCrafting.addMillRecipe(new ItemStack(nutmegPowder), new ItemStack(nutmeg));
        Item curry = Item.REGISTRY.getObject(new ResourceLocation(MODID, "curryleafItem"));
        Item curryPowder = Item.REGISTRY.getObject(new ResourceLocation(MODID, "currypowderItem"));
        BWCrafting.addMillRecipe(new ItemStack(curryPowder), new ItemStack(curry));
        Item boiledEgg = Item.REGISTRY.getObject(new ResourceLocation(MODID, "boiledeggItem"));
        BWCrafting.addCauldronRecipe(new ItemStack(boiledEgg), new ItemStack[]{new ItemStack(Items.EGG)});
        Item dough = Item.REGISTRY.getObject(new ResourceLocation(MODID, "doughItem"));
        BWCrafting.addCauldronRecipe(new ItemStack(Items.BREAD), new ItemStack[]{new ItemStack(dough)});
        OreDictionary.registerOre("foodDonut", new ItemStack(BWMItems.DONUT));

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
