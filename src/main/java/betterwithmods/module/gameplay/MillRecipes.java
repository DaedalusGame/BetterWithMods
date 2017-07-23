package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.manager.MillManager;
import betterwithmods.module.Feature;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by tyler on 5/16/17.
 */
public class MillRecipes extends Feature {
    public MillRecipes() {
        canDisable = false;
    }


    private boolean grindingOnly;

    @Override
    public void setupConfig() {
        grindingOnly = loadPropBool("Grinding Only", "Remove normal recipes for certain grindable items", true);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        if(grindingOnly) {
            BWMRecipes.removeRecipe(new ItemStack(Blocks.REEDS));
            BWMRecipes.removeRecipe(new ItemStack(Items.BLAZE_ROD));
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {


        addMillRecipe(1, new ItemStack(Items.STRING, 10), getDye(EnumDyeColor.RED, 3), new Object[]{new ItemStack(BWMBlocks.WOLF)});
        addMillRecipe(2, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GROUND_NETHERRACK), new ItemStack[]{new ItemStack(Blocks.NETHERRACK)});
        addMillRecipe(2, new ItemStack(Items.BLAZE_POWDER, 3, 0), new ItemStack[]{new ItemStack(Items.BLAZE_ROD)});
        addMillRecipe(new ItemStack(Items.SUGAR, 2, 0), new ItemStack[]{new ItemStack(Items.REEDS)});
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_FIBERS, 3), new String[]{"cropHemp"});
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.COAL_DUST), new ItemStack[]{new ItemStack(Items.COAL, 1, 0)});
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHARCOAL_DUST), new ItemStack[]{new ItemStack(Items.COAL, 1, 1)});
        addMillRecipe(getDye(EnumDyeColor.WHITE, 6), new ItemStack[]{new ItemStack(Items.BONE)});
        addMillRecipe(getDye(EnumDyeColor.WHITE, 10), new ItemStack[]{new ItemStack(Items.SKULL, 1, 0)});
        addMillRecipe(getDye(EnumDyeColor.WHITE, 10), new ItemStack[]{new ItemStack(Items.SKULL, 1, 1)});
        addMillRecipe(getDye(EnumDyeColor.WHITE, 9), new ItemStack[]{new ItemStack(Blocks.BONE_BLOCK)});
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER), new ItemStack[]{new ItemStack(Items.LEATHER)});
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT), new ItemStack[]{new ItemStack(Items.RABBIT_HIDE)});
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT), new ItemStack[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_CUT)});
        //Dyes
        addMillRecipe(getDye(EnumDyeColor.RED, 2), new ItemStack[]{new ItemStack(Items.BEETROOT)});
        addMillRecipe(getDye(EnumDyeColor.RED, 4), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER, 1, 0)});
        addMillRecipe(getDye(EnumDyeColor.RED, 4), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER, 1, 4)});
        addMillRecipe(getDye(EnumDyeColor.YELLOW, 4), new ItemStack[]{new ItemStack(Blocks.YELLOW_FLOWER, 1, 0)});
        addMillRecipe(getDye(EnumDyeColor.LIGHT_BLUE, 4), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER, 1, 1)});
        addMillRecipe(getDye(EnumDyeColor.MAGENTA, 4), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER, 1, 2)});
        addMillRecipe(getDye(EnumDyeColor.SILVER, 4), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER, 1, 3)});
        addMillRecipe(getDye(EnumDyeColor.ORANGE, 4), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER, 1, 5)});
        addMillRecipe(getDye(EnumDyeColor.SILVER, 4), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER, 1, 6)});
        addMillRecipe(getDye(EnumDyeColor.PINK, 6), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER, 1, 7)});
        addMillRecipe(getDye(EnumDyeColor.SILVER, 4), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER, 1, 8)});
        addMillRecipe(getDye(EnumDyeColor.YELLOW, 6), new ItemStack[]{new ItemStack(Blocks.DOUBLE_PLANT, 1, 0)});
        addMillRecipe(getDye(EnumDyeColor.MAGENTA, 6), new ItemStack[]{new ItemStack(Blocks.DOUBLE_PLANT, 1, 1)});
        addMillRecipe(getDye(EnumDyeColor.RED, 6), new ItemStack[]{new ItemStack(Blocks.DOUBLE_PLANT, 1, 4)});
        addMillRecipe(getDye(EnumDyeColor.PINK, 6), new ItemStack[]{new ItemStack(Blocks.DOUBLE_PLANT, 1, 5)});
        addMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new String[]{"cropWheat"});
        addMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new String[]{"cropBarley"});
        addMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new String[]{"cropOats"});
        addMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new String[]{"cropRye"});
        addMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new String[]{"cropRice"});
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.COCOA_POWDER), new ItemStack[]{new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage())});

    }

    public static void addMillRecipe(ItemStack output, Object[] inputs) {
        MillManager.getInstance().addRecipe(0, output, ItemStack.EMPTY, inputs);
    }

    public static void addMillRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        MillManager.getInstance().addRecipe(0, output, secondary, inputs);
    }

    public static void addMillRecipe(int grindType, ItemStack output, Object[] inputs) {
        MillManager.getInstance().addRecipe(grindType, output, ItemStack.EMPTY, inputs);
    }

    public static void addMillRecipe(int grindType, ItemStack output, ItemStack secondary, Object[] inputs) {
        MillManager.getInstance().addRecipe(grindType, output, secondary, inputs);
    }

    public static ItemStack getDye(EnumDyeColor color, int count) {
        return new ItemStack(Items.DYE, count, color.getDyeDamage());
    }
}


