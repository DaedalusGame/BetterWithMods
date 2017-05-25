package betterwithmods.module.tweaks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockMechMachines;
import betterwithmods.common.blocks.mini.BlockMini;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.HCSaw;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Created by tyler on 5/16/17.
 */
public class HighEfficiencyRecipes extends Feature {
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, BlockMechMachines.EnumType.PULLEY.getMeta() << 1), "PIP", "GLG", "PIP", 'P', new ItemStack(BWMBlocks.WOOD_SIDING, 1, 32767), 'I', "ingotIron", 'G', "gearWood", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PLATFORM), "MWM", " M ", "MWM", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, 32767), 'W', new ItemStack(BWMBlocks.PANE, 1, 2)));
        GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.AXLE), "M", "R", "M", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE), 'R', BWMBlocks.ROPE);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GEARBOX), "SGS", "GLG", "SGS", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH), 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'G', "gearWood"));
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            GameRegistry.addShapelessRecipe(new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata()), new ItemStack(BWMBlocks.WOOD_CORNER, 1, type.getMetadata()), new ItemStack(BWMBlocks.WOOD_CORNER, 1, type.getMetadata()));
            GameRegistry.addShapelessRecipe(new ItemStack(BWMBlocks.WOOD_SIDING, 1, type.getMetadata()), new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata()), new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata()));
            GameRegistry.addShapelessRecipe(new ItemStack(Blocks.PLANKS, 1, type.getMetadata()), new ItemStack(BWMBlocks.WOOD_SIDING, 1, type.getMetadata()), new ItemStack(BWMBlocks.WOOD_SIDING, 1, type.getMetadata()));
            GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.WOOD_TABLE, 4, type.getMetadata()), "SSS", " M ", " M ", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, type.getMetadata()), 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata()));
            GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.WOOD_BENCH, 4, type.getMetadata()), "SSS", " M ", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, type.getMetadata()), 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata()));
        }
        for (BlockMini.EnumType type : BlockMini.EnumType.values()) {
            GameRegistry.addShapelessRecipe(new ItemStack(BWMBlocks.STONE_MOULDING, 1, type.getMetadata()), new ItemStack(BWMBlocks.STONE_CORNER, 1, type.getMetadata()), new ItemStack(BWMBlocks.STONE_CORNER, 1, type.getMetadata()));
            GameRegistry.addShapelessRecipe(new ItemStack(BWMBlocks.STONE_SIDING, 1, type.getMetadata()), new ItemStack(BWMBlocks.STONE_MOULDING, 1, type.getMetadata()), new ItemStack(BWMBlocks.STONE_MOULDING, 1, type.getMetadata()));
            GameRegistry.addShapelessRecipe(type.getBlock(), new ItemStack(BWMBlocks.STONE_SIDING, 1, type.getMetadata()), new ItemStack(BWMBlocks.STONE_SIDING, 1, type.getMetadata()));
        }
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.STONE_BRICK_STAIRS, "M ", "MM", 'M', new ItemStack(BWMBlocks.STONE_MOULDING, 1, BlockMini.EnumType.STONEBRICK.getMetadata())).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.NETHER_BRICK_STAIRS, "M ", "MM", 'M', new ItemStack(BWMBlocks.STONE_MOULDING, 1, BlockMini.EnumType.NETHERBRICK.getMetadata())).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.BRICK_STAIRS, "M ", "MM", 'M', new ItemStack(BWMBlocks.STONE_MOULDING, 1, BlockMini.EnumType.BRICK.getMetadata())).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.SANDSTONE_STAIRS, "M ", "MM", 'M', new ItemStack(BWMBlocks.STONE_MOULDING, 1, BlockMini.EnumType.SANDSTONE.getMetadata())).setMirrored(true));
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.BOOKSHELF), "SSS", "BBB", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'B', Items.BOOK);
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.CHEST), "SSS", "S S", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.NOTEBLOCK), "SSS", "SRS", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.JUKEBOX), "SSS", "SDS", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'D', "gemDiamond"));
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.LADDER, 4), "M M", "MMM", "M M", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.TRAPDOOR, 2), "SSS", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapedRecipe(new ItemStack(Items.SIGN, 3), "S", "M", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.STICK), new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.BOOK), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_CUT), "paper", "paper", "paper"));
        GameRegistry.addShapedRecipe(new ItemStack(Items.ITEM_FRAME), "MMM", "MLM", "MMM", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE), 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_CUT));
        GameRegistry.addShapedRecipe(new ItemStack(Items.BOWL, 6), "S S", " S ", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SAW), "III", "GBG", "WGW", 'I', "ingotIron", 'G', "gearWood", 'W', BWMBlocks.WOOD_SIDING, 'B', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT)));
        int count = ModuleLoader.isFeatureEnabled(HCSaw.class) ? 1 : 3;
        ItemStack[] doors = {new ItemStack(Items.OAK_DOOR, count), new ItemStack(Items.SPRUCE_DOOR, count), new ItemStack(Items.BIRCH_DOOR, count), new ItemStack(Items.JUNGLE_DOOR, count), new ItemStack(Items.ACACIA_DOOR, count), new ItemStack(Items.DARK_OAK_DOOR, count)};
        ItemStack[] boats = {new ItemStack(Items.BOAT), new ItemStack(Items.SPRUCE_BOAT), new ItemStack(Items.BIRCH_BOAT), new ItemStack(Items.JUNGLE_BOAT), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Items.DARK_OAK_BOAT)};
        ItemStack[] fences = {new ItemStack(Blocks.OAK_FENCE, 2), new ItemStack(Blocks.SPRUCE_FENCE, 2), new ItemStack(Blocks.BIRCH_FENCE, 2), new ItemStack(Blocks.JUNGLE_FENCE, 2), new ItemStack(Blocks.ACACIA_FENCE, 2), new ItemStack(Blocks.DARK_OAK_FENCE, 2)};
        ItemStack[] gates = {new ItemStack(Blocks.OAK_FENCE_GATE), new ItemStack(Blocks.SPRUCE_FENCE_GATE), new ItemStack(Blocks.BIRCH_FENCE_GATE), new ItemStack(Blocks.JUNGLE_FENCE_GATE), new ItemStack(Blocks.ACACIA_FENCE_GATE), new ItemStack(Blocks.DARK_OAK_FENCE_GATE)};
        ItemStack[] stairs = {new ItemStack(Blocks.OAK_STAIRS), new ItemStack(Blocks.SPRUCE_STAIRS), new ItemStack(Blocks.BIRCH_STAIRS), new ItemStack(Blocks.JUNGLE_STAIRS), new ItemStack(Blocks.ACACIA_STAIRS), new ItemStack(Blocks.DARK_OAK_STAIRS)};
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            int meta = type.getMetadata();
            GameRegistry.addShapedRecipe(doors[meta], "SS", "SS", "SS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, meta));
            GameRegistry.addShapedRecipe(boats[meta], "S S", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, meta));
            GameRegistry.addShapedRecipe(fences[meta], "MMM", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta));
            GameRegistry.addShapedRecipe(gates[meta], "MSM", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta), 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, meta));
            GameRegistry.addRecipe(new ShapedOreRecipe(stairs[meta], "M ", "MM", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta)).setMirrored(true));
        }
//        GameRegistry.addShapedRecipe(doors[0], "SS", "SS", "SS", 'S', BWMBlocks.WOOD_SIDING);
//        GameRegistry.addShapedRecipe(boats[0], "S S", "SSS", 'S', BWMBlocks.WOOD_SIDING);
//        GameRegistry.addShapedRecipe(fences[0], "MMM", 'M', BWMBlocks.WOOD_MOULDING);
//        GameRegistry.addShapedRecipe(gates[0], "MSM", 'M', BWMBlocks.WOOD_MOULDING, 'S', BWMBlocks.WOOD_SIDING);
//        GameRegistry.addRecipe(new ShapedOreRecipe(stairs[0], "M ", "MM", 'M', BWMBlocks.WOOD_MOULDING).setMirrored(true));
    }

    @Override
    public String getFeatureDescription() {
        return "Add High Efficiency Recipes by using more advanced materials";
    }
}
