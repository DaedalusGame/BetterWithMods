package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.manager.MillManager;
import betterwithmods.module.Feature;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by tyler on 5/16/17.
 */
public class MillRecipes extends Feature {
    public MillRecipes() {
        canDisable = false;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        addMillRecipe(new ItemStack(Items.STRING, 10), new ItemStack(Items.DYE, 3, 1),new Object[]{new ItemStack(BWMBlocks.WOLF)});
        addMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new ItemStack(Items.WHEAT));

        addMillRecipe(new ItemStack(Items.SUGAR, 2, 0), new ItemStack(Items.REEDS));
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_FIBERS, 3), "cropHemp");
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.COAL_DUST), new ItemStack(Items.COAL, 1, 0));
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHARCOAL_DUST), new ItemStack(Items.COAL, 1, 1));
        addMillRecipe(new ItemStack(Items.DYE, 6, 15), new ItemStack(Items.BONE));
        addMillRecipe(new ItemStack(Items.DYE, 10, 15), new ItemStack(Items.SKULL, 1, 0));
        addMillRecipe(new ItemStack(Items.DYE, 10, 15), new ItemStack(Items.SKULL, 1, 1));
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GROUND_NETHERRACK), new ItemStack(Blocks.NETHERRACK));
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER), new ItemStack(Items.LEATHER));
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT), new ItemStack(Items.RABBIT_HIDE));
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_CUT));
        addMillRecipe(new ItemStack(Items.BLAZE_POWDER, 3, 0), new ItemStack(Items.BLAZE_ROD));

        //Dyes
        addMillRecipe(new ItemStack(Items.DYE, 2, 1), new ItemStack(Items.BEETROOT));
        addMillRecipe(new ItemStack(Items.DYE, 4, 1), new ItemStack(Blocks.RED_FLOWER, 1, 0));
        addMillRecipe(new ItemStack(Items.DYE, 4, 11), new ItemStack(Blocks.YELLOW_FLOWER, 1, 0));
        addMillRecipe(new ItemStack(Items.DYE, 4, 12), new ItemStack(Blocks.RED_FLOWER, 1, 1));
        addMillRecipe(new ItemStack(Items.DYE, 4, 13), new ItemStack(Blocks.RED_FLOWER, 1, 2));
        addMillRecipe(new ItemStack(Items.DYE, 4, 7), new ItemStack(Blocks.RED_FLOWER, 1, 3));
        addMillRecipe(new ItemStack(Items.DYE, 4, 1), new ItemStack(Blocks.RED_FLOWER, 1, 4));
        addMillRecipe(new ItemStack(Items.DYE, 4, 14), new ItemStack(Blocks.RED_FLOWER, 1, 5));
        addMillRecipe(new ItemStack(Items.DYE, 4, 7), new ItemStack(Blocks.RED_FLOWER, 1, 6));
        addMillRecipe(new ItemStack(Items.DYE, 4, 9), new ItemStack(Blocks.RED_FLOWER, 1, 7));
        addMillRecipe(new ItemStack(Items.DYE, 4, 7), new ItemStack(Blocks.RED_FLOWER, 1, 8));
        addMillRecipe(new ItemStack(Items.DYE, 6, 11), new ItemStack(Blocks.DOUBLE_PLANT, 1, 0));
        addMillRecipe(new ItemStack(Items.DYE, 6, 13), new ItemStack(Blocks.DOUBLE_PLANT, 1, 1));
        addMillRecipe(new ItemStack(Items.DYE, 6, 1), new ItemStack(Blocks.DOUBLE_PLANT, 1, 4));
        addMillRecipe(new ItemStack(Items.DYE, 6, 9), new ItemStack(Blocks.DOUBLE_PLANT, 1, 5));
        addMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), "cropBarley");
        addMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), "cropOats");
        addMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), "cropRye");
        addMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), "cropRice");
        addMillRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.COCOA_POWDER), new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()));
    }

    public static void addMillRecipe(ItemStack output, Object... inputs) {
        MillManager.getInstance().addRecipe(output, ItemStack.EMPTY, inputs);
    }

    public static void addMillRecipe(ItemStack output, ItemStack inputs) {
        MillManager.getInstance().addRecipe(output, ItemStack.EMPTY, inputs);
    }

    public static void addMillRecipe(ItemStack output, ItemStack secondary, Object... inputs) {
        MillManager.getInstance().addRecipe(output, secondary, inputs);
    }
}
