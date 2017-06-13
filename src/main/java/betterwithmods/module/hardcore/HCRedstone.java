package betterwithmods.module.hardcore;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockUrn;
import betterwithmods.common.blocks.mini.BlockMini;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.module.gameplay.CrucibleRecipes;
import betterwithmods.util.RecipeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by tyler on 4/20/17.
 */
public class HCRedstone extends Feature {


    public static boolean stoneDeviceRecipesAnvil;

    @Override
    public void setupConfig() {
        stoneDeviceRecipesAnvil = loadPropBool("Stone Device Recipes Require Anvil", "Makes it so stone buttons and pressure plates require cut stone, which must be done in the anvil", true);
    }

    @Override
    public String getFeatureDescription() {
        return "Changes the recipes for Redstone devices to be more complex";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        ItemStack LATCH = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH);
        RecipeUtils.removeRecipes(Blocks.STONE_BUTTON);
        RecipeUtils.removeRecipes(Blocks.STONE_PRESSURE_PLATE);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.STONE_BUTTON, "C", "R", 'C', new ItemStack(BWMBlocks.STONE_CORNER, 1, BlockMini.EnumType.STONE.getMetadata()), 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.STONE_PRESSURE_PLATE, "S", "R", 'S', new ItemStack(BWMBlocks.STONE_SIDING, 1, BlockMini.EnumType.STONE.getMetadata()), 'R', "dustRedstone"));
        if (!stoneDeviceRecipesAnvil) {
            GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.STONE_BUTTON, "C", "R", 'C', new ItemStack(Blocks.STONE), 'R', LATCH));
            GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.STONE_PRESSURE_PLATE, "S", "R", 'S', new ItemStack(Blocks.STONE), 'R', LATCH));
        }
        RecipeUtils.removeRecipes(Blocks.DISPENSER);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.DISPENSER, "CCC", "CBC", "CRC", 'C', "cobblestone", 'B', Items.BOW, 'R', LATCH));
        RecipeUtils.removeRecipes(Blocks.DROPPER);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.DROPPER, "CCC", "C C", "CRC", 'C', "cobblestone", 'R', LATCH));
        //Reduce Iron Door & Iron Trapdoor Output?
        RecipeUtils.removeRecipes(Items.IRON_DOOR, 0);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.IRON_DOOR, 3), "RII", " II", "RII", 'I', "ingotIron", 'R', LATCH));
        RecipeUtils.removeRecipes(Blocks.IRON_TRAPDOOR);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.IRON_TRAPDOOR, 2), "RII", "RII", 'I', "ingotIron", 'R', LATCH));
        RecipeUtils.removeRecipes(Blocks.LEVER);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.LEVER, "S", "C", "R", 'S', "stickWood", 'C', "cobblestone", 'R', "dustRedstone"));
        RecipeUtils.removeRecipes(Blocks.PISTON);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.PISTON, "WIW", "CSC", "CRC", 'W', new ItemStack(BWMBlocks.WOOD_SIDING, 1, 32767), 'I', "ingotIron", 'C', "cobblestone", 'S', new ItemStack(BWMBlocks.URN, 1, BlockUrn.EnumUrnType.FULL.getMeta()), 'R', LATCH));

        RecipeUtils.removeRecipes(Blocks.TRIPWIRE_HOOK);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.TRIPWIRE_HOOK, "I", "M", "R", 'I', "nuggetIron", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, 32767), 'R', "dustRedstone"));
        RecipeUtils.removeRecipes(Blocks.WOODEN_BUTTON);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.WOODEN_BUTTON, "C", "R", 'C', new ItemStack(BWMBlocks.WOOD_CORNER, 1, 32767), 'R', "dustRedstone"));
        RecipeUtils.removeRecipes(Blocks.WOODEN_PRESSURE_PLATE);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.WOODEN_PRESSURE_PLATE, "S", "R", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, 32767), 'R', "dustRedstone"));
        RecipeUtils.removeRecipes(Items.REPEATER, 0);
        GameRegistry.addRecipe(new ShapedOreRecipe(Items.REPEATER, "RCR", "SSS", 'R', Blocks.REDSTONE_TORCH, 'C', Items.CLOCK, 'S', "stone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(Items.REPEATER, "RCR", "SSS", 'R', Blocks.REDSTONE_TORCH, 'C', Items.CLOCK, 'S', new ItemStack(BWMBlocks.STONE_SIDING, 1, BlockMini.EnumType.STONE.getMetadata())));

        RecipeUtils.removeRecipes(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, "II", "RR", 'I', "ingotIron", 'R', "dustRedstone"));
        RecipeUtils.removeRecipes(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, "GG", "RR", 'G', "ingotGold", 'R', "dustRedstone"));

        RecipeUtils.removeRecipes(Blocks.HOPPER);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.HOPPER, "ICI", "IRI", " I ", 'I', "ingotIron", 'C', "chestWood", 'R', LATCH));
        GameRegistry.addRecipe(new ShapedOreRecipe(Items.COMPARATOR, " R ", "RQR", "SSS", 'R', Blocks.REDSTONE_TORCH, 'Q', "gemQuartz", 'S', new ItemStack(BWMBlocks.STONE_SIDING, 1, BlockMini.EnumType.STONE.getMetadata())));

        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 4, 0), new Object[]{new ItemStack(Blocks.IRON_TRAPDOOR, 2)});

        RecipeUtils.removeRecipes(Blocks.OBSERVER);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.OBSERVER,"CCC","LLU","CCC", 'C',"cobblestone",'L',LATCH,'U',BlockUrn.getStack(BlockUrn.EnumUrnType.FULL)));
    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 6, 0), new Object[]{new ItemStack(Blocks.IRON_TRAPDOOR, 2)});
    }
}
