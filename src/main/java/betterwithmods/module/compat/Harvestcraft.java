package betterwithmods.module.compat;

import betterwithmods.common.BWMItems;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.items.ItemBark;
import betterwithmods.module.CompatFeature;
import betterwithmods.module.gameplay.CauldronRecipes;
import betterwithmods.module.gameplay.MillRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("unused")
public class Harvestcraft extends CompatFeature {
    public Harvestcraft() {
        super("harvestcraft");
    }

    @GameRegistry.ObjectHolder("harvestcraft:pamcinnamon")
    public static final Block logCinnamon = null;
    @GameRegistry.ObjectHolder("harvestcraft:pammaple")
    public static final Block logMaple = null;
    @GameRegistry.ObjectHolder("harvestcraft:pampaperbark")
    public static final Block logPaperbark = null;

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
//        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMItems.CHOCOLATE, 2), "foodCocoapowder", "listAllmilk", Items.SUGAR, "toolSaucepan");

        BWOreDictionary.woods.add(new BWOreDictionary.Wood(new ItemStack(logMaple),new ItemStack(Blocks.PLANKS,1, BlockPlanks.EnumType.SPRUCE.getMetadata()), ItemBark.getStack("spruce",1)));
        BWOreDictionary.woods.add(new BWOreDictionary.Wood(new ItemStack(logCinnamon),new ItemStack(Blocks.PLANKS,1, BlockPlanks.EnumType.JUNGLE.getMetadata()), ItemBark.getStack("jungle",1)));
        BWOreDictionary.woods.add(new BWOreDictionary.Wood(new ItemStack(logPaperbark),new ItemStack(Blocks.PLANKS,1, BlockPlanks.EnumType.JUNGLE.getMetadata()), ItemBark.getStack("jungle",1)));
    }

}
