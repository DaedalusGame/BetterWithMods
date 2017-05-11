package betterwithmods.common;

import betterwithmods.common.blocks.BlockCookingPot;
import betterwithmods.common.blocks.BlockMechMachines;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.blocks.BlockUnfiredPottery.EnumPotteryType;
import betterwithmods.common.blocks.mini.BlockMini;
import betterwithmods.common.items.ItemBark;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.*;
import betterwithmods.common.registry.bulk.*;
import betterwithmods.common.registry.steelanvil.SteelCraftingManager;
import betterwithmods.common.registry.steelanvil.SteelShapedOreRecipe;
import betterwithmods.common.registry.steelanvil.SteelShapedRecipe;
import betterwithmods.common.registry.steelanvil.SteelShapelessRecipe;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;
import java.util.Random;

import static betterwithmods.common.items.ItemMaterial.EnumMaterial;

public class BWCrafting {
    public static void init() {
        RecipeSorter.register("bwm:chopping", ChoppingRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        RecipeSorter.register("bwm:cutting", CuttingRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        RecipeSorter.register("bwm:dyetag", DyeWithTagRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        RecipeSorter.register("bwm:steel_shapeless", SteelShapelessRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        RecipeSorter.register("bwm:steel_shaped", SteelShapedRecipe.class, RecipeSorter.Category.SHAPED, "after:forge:shapedore");
        RecipeSorter.register("bwm:steel_shaped_ore", SteelShapedOreRecipe.class, RecipeSorter.Category.SHAPED, "after:forge:shapedore");
        addVanillaRecipes();
        addCauldronRecipes();
        addCrucibleRecipes();
        addMillRecipes();
        addKilnRecipes();
        addTurntableRecipes();
        addSawRecipes();
        addHERecipes();
        addSteelAnvilRecipes();
        addPileRecipes();
    }


    private static void addHERecipes() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, BlockMechMachines.EnumType.PULLEY.getMeta()), "PIP", "GLG", "PIP", 'P', new ItemStack(BWMBlocks.WOOD_SIDING, 1, 32767), 'I', "ingotIron", 'G', "gearWood", 'L', ItemMaterial.getMaterial(EnumMaterial.REDSTONE_LATCH)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PLATFORM), "MWM", " M ", "MWM", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, 32767), 'W', new ItemStack(BWMBlocks.PANE, 1, 2)));
        GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.AXLE), "M", "R", "M", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE), 'R', BWMBlocks.ROPE);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GEARBOX), "SGS", "GLG", "SGS", 'L', ItemMaterial.getMaterial(EnumMaterial.REDSTONE_LATCH), 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'G', "gearWood"));
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
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.BOOK), ItemMaterial.getMaterial(EnumMaterial.LEATHER_CUT), "paper", "paper", "paper"));
        GameRegistry.addShapedRecipe(new ItemStack(Items.ITEM_FRAME), "MMM", "MLM", "MMM", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE), 'L', ItemMaterial.getMaterial(EnumMaterial.LEATHER_CUT));
        GameRegistry.addShapedRecipe(new ItemStack(Items.BOWL, 6), "S S", " S ", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SAW), "III", "GBG", "WGW", 'I', "ingotIron", 'G', "gearWood", 'W', BWMBlocks.WOOD_SIDING, 'B', ItemMaterial.getMaterial(EnumMaterial.LEATHER_BELT)));

        ItemStack[] doors = {new ItemStack(Items.OAK_DOOR, 3), new ItemStack(Items.SPRUCE_DOOR, 3), new ItemStack(Items.BIRCH_DOOR, 3), new ItemStack(Items.JUNGLE_DOOR, 3), new ItemStack(Items.ACACIA_DOOR, 3), new ItemStack(Items.DARK_OAK_DOOR, 3)};
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
        GameRegistry.addShapedRecipe(doors[0], "SS", "SS", "SS", 'S', BWMBlocks.WOOD_SIDING);
        GameRegistry.addShapedRecipe(boats[0], "S S", "SSS", 'S', BWMBlocks.WOOD_SIDING);
        GameRegistry.addShapedRecipe(fences[0], "MMM", 'M', BWMBlocks.WOOD_MOULDING);
        GameRegistry.addShapedRecipe(gates[0], "MSM", 'M', BWMBlocks.WOOD_MOULDING, 'S', BWMBlocks.WOOD_SIDING);
        GameRegistry.addRecipe(new ShapedOreRecipe(stairs[0], "M ", "MM", 'M', BWMBlocks.WOOD_MOULDING).setMirrored(true));

    }

    private static void addVanillaRecipes() {

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.GEARBOX), new ItemStack(BWMBlocks.BROKEN_GEARBOX), "gearWood", "gearWood"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.STRING), "fiberHemp", "fiberHemp", "fiberHemp"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.LIGHT), " P ", "PFP", " R ", 'P', "paneGlass", 'R', "dustRedstone", 'F', ItemMaterial.getMaterial(EnumMaterial.FILAMENT)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.BOOSTER), Blocks.RAIL, BWMBlocks.ROPE, "gearWood"));
        GameRegistry.addShapelessRecipe(ItemMaterial.getMaterial(EnumMaterial.HEMP_FIBERS, 6), BWMBlocks.ROPE);
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.field_191525_da, 9), "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.IRON_INGOT), "III", "III", "III", 'I', "nuggetIron"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial(EnumMaterial.NUGGET_STEEL, 9), "ingotSoulforgedSteel"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.INGOT_STEEL), "III", "III", "III", 'I', "nuggetSoulforgedSteel"));

        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.BAMBOO_CHIME, 1, type.getMetadata()), " S ", "SPS", "BMB", 'S', "string", 'P', Blocks.WOODEN_PRESSURE_PLATE, 'B', "sugarcane", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata())));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.METAL_CHIME, 1, type.getMetadata()), " S ", "SPS", "IMI", 'S', "string", 'P', Blocks.WOODEN_PRESSURE_PLATE, 'I', "ingotIron", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata())));
        }

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.URN, 1, 9), new ItemStack(BWMBlocks.URN, 1, 8), new ItemStack(Items.ENDER_EYE), "obsidian"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SAW), "III", "GBG", "WGW", 'I', "ingotIron", 'G', "gearWood", 'W', "plankWood", 'B', ItemMaterial.getMaterial(EnumMaterial.LEATHER_BELT)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.FLINT, 9, 0), new ItemStack(BWMBlocks.AESTHETIC, 1, 5)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 5), "XXX", "XXX", "XXX", 'X', new ItemStack(Items.FLINT)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.ROPE, 9, 0), new ItemStack(BWMBlocks.AESTHETIC, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 4), "XXX", "XXX", "XXX", 'X', new ItemStack(BWMBlocks.ROPE)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial(EnumMaterial.CONCENTRATED_HELLFIRE, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 3), "XXX", "XXX", "XXX", 'X', ItemMaterial.getMaterial(EnumMaterial.CONCENTRATED_HELLFIRE)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMItems.DYNAMITE), "paper", "paper", "paper", ItemMaterial.getMaterial(EnumMaterial.FUSE), ItemMaterial.getMaterial(EnumMaterial.BLASTING_OIL), "dustWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.LENS), "GDG", "G G", "GLG", 'G', "ingotGold", 'D', "gemDiamond", 'L', "blockGlass"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 2), "XXX", "X X", "XXX", 'X', ItemMaterial.getMaterial(EnumMaterial.WINDMILL_BLADE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 1), "XXX", "X X", "XXX", 'X', ItemMaterial.getMaterial(EnumMaterial.WOOD_BLADE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 0), " X ", "X X", " X ", 'X', ItemMaterial.getMaterial(EnumMaterial.WINDMILL_BLADE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.WOOD_BLADE), "W  ", "WGW", "W  ", 'G', "slimeball", 'W', BWMBlocks.WOOD_SIDING).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.HEMP_CLOTH), "XXX", "XXX", "XXX", 'X', "fiberHemp"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.WINDMILL_BLADE), "CCC", "CCC", "WWW", 'W', "slabWood", 'C', "fabricHemp"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.ANCHOR), " I ", "SSS", 'S', "stone", 'I', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.HIBACHI), "HHH", "SES", "SRS", 'S', "stone", 'R', "dustRedstone", 'H', ItemMaterial.getMaterial(EnumMaterial.CONCENTRATED_HELLFIRE), 'E', ItemMaterial.getMaterial(EnumMaterial.ELEMENT)));
        GameRegistry.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(EnumMaterial.LEATHER_STRAP, 4), ItemMaterial.getMaterial(EnumMaterial.TANNED_LEATHER_CUT)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.LEATHER_BELT), " L ", "L L", " L ", 'L', ItemMaterial.getMaterial(EnumMaterial.LEATHER_STRAP)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.BELLOWS), "WWW", "LLL", "BGB", 'W', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'L', ItemMaterial.getMaterial(EnumMaterial.TANNED_LEATHER_CUT), 'B', ItemMaterial.getMaterial(EnumMaterial.LEATHER_BELT), 'G', "gearWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GEARBOX), "PGP", "GLG", "PGP", 'P', "plankWood", 'G', "gearWood", 'L', ItemMaterial.getMaterial(EnumMaterial.REDSTONE_LATCH)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, BlockMechMachines.EnumType.TURNTABLE.getMeta()), "LLL", "SCS", "SWS", 'L', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'S', "stone", 'W', "gearWood", 'C', Items.CLOCK));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, BlockMechMachines.EnumType.HOPPER.getMeta()), "S S", "GPG", " C ", 'C', BWMBlocks.WOOD_CORNER, 'S', BWMBlocks.WOOD_SIDING, 'G', "gearWood", 'P', Blocks.WOODEN_PRESSURE_PLATE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AXLE), "X", "R", "X", 'X', "plankWood", 'R', new ItemStack(BWMBlocks.ROPE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.HAND_CRANK), "  X", " X ", "SWS", 'X', "stickWood", 'S', "cobblestone", 'W', "gearWood").setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, BlockMechMachines.EnumType.MILL.getMeta()), "XWX", "XXX", "XXX", 'X', "stone", 'W', "gearWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.COOKING_POTS, 1, BlockCookingPot.EnumType.CAULDRON.getMeta()), "XBX", "XWX", "XXX", 'X', "ingotCopper", 'B', Items.BONE, 'W', Items.WATER_BUCKET));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.COOKING_POTS, 1, BlockCookingPot.EnumType.CAULDRON.getMeta()), "XBX", "XWX", "XXX", 'X', "ingotIron", 'B', Items.BONE, 'W', Items.WATER_BUCKET));
        GameRegistry.addSmelting(ItemMaterial.getMaterial(EnumMaterial.NETHER_SLUDGE), new ItemStack(Items.NETHERBRICK), 0.2F);
        GameRegistry.addSmelting(BWMBlocks.DEBARKED_OLD, new ItemStack(Items.COAL, 1, 1), 0.1F);
        GameRegistry.addSmelting(BWMBlocks.DEBARKED_NEW, new ItemStack(Items.COAL, 1, 1), 0.1F);
        GameRegistry.addSmelting(new ItemStack(BWMBlocks.AESTHETIC, 1, 7), new ItemStack(BWMBlocks.AESTHETIC, 1, 6), 0.1F);
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.HAFT), "S", "G", "X", 'S', ItemMaterial.getMaterial(EnumMaterial.LEATHER_STRAP), 'G', "slimeball", 'X', BWMBlocks.WOOD_MOULDING));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.GEAR, 2), "SWS", "W W", "SWS", 'S', "stickWood", 'W', "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SLATS, 4, 0), "SS", "SS", 'S', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE)));
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            int meta = type.getMetadata();
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GRATE, 4, meta), "WSW", "WSW", 'S', "stickWood", 'W', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta)));
            ItemStack moulding = new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta);
            GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.SLATS, 4, meta), "SS", "SS", 'S', moulding);
        }
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GRATE, 4, 0), "WSW", "WSW", 'S', "stickWood", 'W', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PANE, 4, 2), "RRR", "RRR", 'R', Items.REEDS));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.REDSTONE_LATCH), "GGG", " R ", 'G', "nuggetGold", 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.ROPE), "XX", "XX", "XX", 'X', "fiberHemp"));
        GameRegistry.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(EnumMaterial.LEATHER_CUT, 2), new ItemStack(Items.LEATHER)));
        GameRegistry.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(EnumMaterial.TANNED_LEATHER_CUT, 2), ItemMaterial.getMaterial(EnumMaterial.TANNED_LEATHER)));
        GameRegistry.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(EnumMaterial.SCOURED_LEATHER_CUT, 2), ItemMaterial.getMaterial(EnumMaterial.SCOURED_LEATHER)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.SHARPENING_STONE), "X ", " X", 'X', Items.FLINT).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.KNIFE_BLADE), "I ", " X", 'X', ItemMaterial.getMaterial(EnumMaterial.SHARPENING_STONE), 'I', "ingotIron").setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.KNIFE), "I ", " X", 'X', "stickWood", 'I', ItemMaterial.getMaterial(EnumMaterial.KNIFE_BLADE)).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, BlockMechMachines.EnumType.PULLEY.getMeta()), "PIP", "GLG", "PIP", 'P', "plankWood", 'I', "ingotIron", 'G', "gearWood", 'L', ItemMaterial.getMaterial(EnumMaterial.REDSTONE_LATCH)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PLATFORM), "MWM", " M ", "MWM", 'M', "plankWood", 'W', new ItemStack(BWMBlocks.PANE, 1, 2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.MINING_CHARGE), "RSR", "DDD", "DDD", 'R', BWMBlocks.ROPE, 'S', "slimeball", 'D', BWMItems.DYNAMITE));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 8), "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl"));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.ENDER_PEARL, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, 8));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.SCREW), "II ", " II", "II ", 'I', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PUMP), "xGx", "SsS", "SgS", 'x', ItemMaterial.getMaterial(EnumMaterial.GLUE), 'G', new ItemStack(BWMBlocks.GRATE, 1, 32767), 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, 32767), 's', ItemMaterial.getMaterial(EnumMaterial.SCREW), 'g', "gearWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.ENDER_OCULAR), "GGG", "GEG", "GGG", 'G', "nuggetGold", 'E', "enderpearl"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.ENDER_SPECTACLES), "OSO", 'O', ItemMaterial.getMaterial(EnumMaterial.ENDER_OCULAR), 'S', ItemMaterial.getMaterial(EnumMaterial.LEATHER_STRAP)));
        String[] dyes = {"White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};

        for (int i = 0; i < 16; i++) {
            GameRegistry.addRecipe(new DyeWithTagRecipe(new ItemStack(BWMBlocks.VASE, 1, i), new ItemStack(BWMBlocks.VASE, 1, OreDictionary.WILDCARD_VALUE), "dye" + dyes[i]));
        }
        GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.VINE_TRAP, 1), "VVV", 'V', new ItemStack(Blocks.VINE));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial(EnumMaterial.PADDING), new ItemStack(Items.FEATHER), "fabricHemp"));
        GameRegistry.addRecipe(new ShapedOreRecipe(BWMItems.BREEDING_HARNESS, "SLS", "LLL", "SLS", 'S', ItemMaterial.getMaterial(EnumMaterial.LEATHER_STRAP), 'L', ItemMaterial.getMaterial(EnumMaterial.TANNED_LEATHER)));
        GameRegistry.addSmelting(BWMItems.RAW_EGG, new ItemStack(BWMItems.COOKED_EGG), 0.1F);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.RAW_SCRAMBLED_EGG, 2), BWMItems.RAW_EGG, Items.MILK_BUCKET);
        GameRegistry.addSmelting(BWMItems.RAW_SCRAMBLED_EGG, new ItemStack(BWMItems.COOKED_SCRAMBLED_EGG), 0.1F);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.RAW_OMELET, 2), BWMItems.RAW_EGG, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM);
        GameRegistry.addSmelting(BWMItems.RAW_OMELET, new ItemStack(BWMItems.COOKED_OMELET), 0.1F);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.HAM_AND_EGGS, 2), BWMItems.COOKED_EGG, Items.COOKED_PORKCHOP);
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMItems.TASTY_SANDWICH, 2), Items.BREAD, "listAllmeatcooked"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.COMPOSITE_BOW), "GMB", "MBS", "GMB", 'G', "slimeball", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE), 'B', "bone", 'S', "string"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.BROADHEAD_ARROW), "B", "S", "F", 'B', ItemMaterial.getMaterial(EnumMaterial.BROADHEAD), 'S', "stickWood", 'F', "feather"));
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.BEEF_DINNER, 3), Items.COOKED_BEEF, Items.CARROT, Items.BAKED_POTATO);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.BEEF_POTATOES, 2), Items.COOKED_BEEF, Items.BAKED_POTATO);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.BEEF_POTATOES, 2), Items.COOKED_BEEF, Items.BAKED_POTATO);
        GameRegistry.addSmelting(BWMItems.RAW_KEBAB, new ItemStack(BWMItems.COOKED_KEBAB), 0.1F);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.PORK_DINNER, 3), Items.COOKED_PORKCHOP, Items.CARROT, Items.BAKED_POTATO);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.RAW_KEBAB, 3), Blocks.BROWN_MUSHROOM, Items.CARROT, Items.MUTTON, Items.STICK);

        GameRegistry.addRecipe(new ShapedOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), "SSS", "MEM", "FFF", 'S', Items.SUGAR, 'M', Items.MILK_BUCKET, 'E', BWMItems.RAW_EGG, 'F', "foodFlour"));
        GameRegistry.addRecipe(new ShapedOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), "FCF", 'F', "foodFlour", 'C', "foodChocolatebar"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), Blocks.PUMPKIN, Items.SUGAR, BWMItems.RAW_EGG, "foodFlour"));
        GameRegistry.addShapelessRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), ItemMaterial.getMaterial(EnumMaterial.FLOUR));

        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), new ItemStack(Items.CAKE), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new ItemStack(Items.BREAD), 0.1F);
        addKilnRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), new ItemStack(Items.CAKE));
        addKilnRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new ItemStack(Items.BREAD));

        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial(EnumMaterial.DIAMOND_INGOT), "gemDiamond", "ingotIron", BWMItems.CREEPER_OYSTER));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial(EnumMaterial.DIAMOND_NUGGET,9), ItemMaterial.getMaterial(EnumMaterial.DIAMOND_INGOT)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.DIAMOND_INGOT), "NNN","NNN","NNN", 'N',ItemMaterial.getMaterial(EnumMaterial.DIAMOND_NUGGET)));
        GameRegistry.addRecipe(new ItemStack(Items.CHAINMAIL_HELMET), "CCC", "C C", 'C', ItemMaterial.getMaterial(EnumMaterial.CHAIN_MAIL));
        GameRegistry.addRecipe(new ItemStack(Items.CHAINMAIL_CHESTPLATE), "C C", "CCC", "CCC", 'C', ItemMaterial.getMaterial(EnumMaterial.CHAIN_MAIL));
        GameRegistry.addRecipe(new ItemStack(Items.CHAINMAIL_LEGGINGS), "CCC", "C C", "C C", 'C', ItemMaterial.getMaterial(EnumMaterial.CHAIN_MAIL));
        GameRegistry.addRecipe(new ItemStack(Items.CHAINMAIL_BOOTS), "C C", "C C", 'C', ItemMaterial.getMaterial(EnumMaterial.CHAIN_MAIL));
        GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.STEEL_ANVIL), "SSS", " S ", "SSS", 'S', ItemMaterial.getMaterial(EnumMaterial.INGOT_STEEL));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.ADVANCED_BELLOWS), " C " , "SBS", " G ",  'B', BWMBlocks.BELLOWS, 'C', Items.CLOCK,'S', ItemMaterial.getMaterial(EnumMaterial.STEEL_SPRING), 'G', ItemMaterial.getMaterial(EnumMaterial.STEEL_GEAR)));
    }

    private static void addSawRecipes() {
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            addSawRecipe(BWMBlocks.WOOD_CORNER, type.getMetadata(), ItemMaterial.getMaterial(EnumMaterial.GEAR, 2));
            addSawRecipe(BWMBlocks.WOOD_MOULDING, type.getMetadata(), new ItemStack(BWMBlocks.WOOD_CORNER, 2, type.getMetadata()));
            addSawRecipe(BWMBlocks.WOOD_SIDING, type.getMetadata(), new ItemStack(BWMBlocks.WOOD_MOULDING, 2, type.getMetadata()));
        }
        addSawRecipe(Blocks.VINE, 0, new ItemStack(Blocks.VINE));
        addSawRecipe(Blocks.PUMPKIN, 0, new ItemStack(Blocks.PUMPKIN));
        SawInteraction.INSTANCE.addRecipe(new BlockMetaRecipe("saw", Blocks.MELON_BLOCK, 0, null) {
            @Override
            public List<ItemStack> getOutputs() {
                Random random = new Random();
                return Lists.newArrayList(new ItemStack(Items.MELON, 3 + random.nextInt(5)));
            }
        });
    }

    private static void addMillRecipes() {
        addMillRecipe(new ItemStack(Items.STRING, 10), new ItemStack(Items.DYE, 3, 1), new ItemStack[]{new ItemStack(BWMBlocks.WOLF)});
        addMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new ItemStack(Items.WHEAT));
        addMillRecipe(new ItemStack(Items.SUGAR, 2, 0), new ItemStack(Items.REEDS));
        addOreMillRecipe(ItemMaterial.getMaterial(EnumMaterial.HEMP_FIBERS, 3), new Object[]{"cropHemp"});
        addMillRecipe(ItemMaterial.getMaterial(EnumMaterial.COAL_DUST), new ItemStack(Items.COAL, 1, 0));
        addMillRecipe(ItemMaterial.getMaterial(EnumMaterial.CHARCOAL_DUST), new ItemStack(Items.COAL, 1, 1));
        addMillRecipe(new ItemStack(Items.DYE, 6, 15), new ItemStack(Items.BONE));
        addMillRecipe(new ItemStack(Items.DYE, 10, 15), new ItemStack(Items.SKULL, 1, 0));
        addMillRecipe(new ItemStack(Items.DYE, 10, 15), new ItemStack(Items.SKULL, 1, 1));
        addMillRecipe(ItemMaterial.getMaterial(EnumMaterial.GROUND_NETHERRACK), new ItemStack(Blocks.NETHERRACK));
        addMillRecipe(ItemMaterial.getMaterial(EnumMaterial.SCOURED_LEATHER), new ItemStack(Items.LEATHER));
        addMillRecipe(ItemMaterial.getMaterial(EnumMaterial.SCOURED_LEATHER_CUT), new ItemStack(Items.RABBIT_HIDE));
        addMillRecipe(ItemMaterial.getMaterial(EnumMaterial.SCOURED_LEATHER_CUT), ItemMaterial.getMaterial(EnumMaterial.LEATHER_CUT));
        addMillRecipe(ItemMaterial.getMaterial(EnumMaterial.SCOURED_LEATHER_CUT), ItemMaterial.getMaterial(EnumMaterial.LEATHER_STRAP));
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
        addOreMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), "cropBarley");
        addOreMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), "cropOats");
        addOreMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), "cropRye");
        addOreMillRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), "cropRice");
        addMillRecipe(ItemMaterial.getMaterial(EnumMaterial.COCOA_POWDER), new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()));
    }

    private static void addCauldronRecipes() {
        addOreCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.NETHER_SLUDGE, 8), new Object[]{"dustPotash", new OreStack("dustHellfire", 4)});
        addOreCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.NETHERCOAL, 4), new Object[]{"dustHellfire", "dustCoal"});
        //Flour OreDict is foodFlour, Donuts need sugar
        addOreCauldronRecipe(new ItemStack(BWMItems.DONUT, 4, 0), new Object[]{"foodFlour", Items.SUGAR});
        addOreCauldronRecipe(new ItemStack(Items.BREAD), new Object[]{"foodFlour"});
        addOreCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.CONCENTRATED_HELLFIRE), new Object[]{new OreStack("dustHellfire", 8)});
        addOreCauldronRecipe(new ItemStack(Items.DYE, 1, 2), new Object[]{"blockCactus"});
        addOreCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.FILAMENT), new Object[]{"string", "dustGlowstone", "dustRedstone"});
        addOreCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.ELEMENT), new Object[]{Items.BLAZE_POWDER, "dustRedstone", "string"});
        String[] barkNames = {"barkOak", "barkSpruce", "barkBirch", "barkJungle", "barkAcacia", "barkDarkOak"};
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            int meta = type.getMetadata();
            addOreCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.TANNED_LEATHER), new Object[]{ItemMaterial.getMaterial(EnumMaterial.SCOURED_LEATHER), new OreStack(barkNames[meta], ItemBark.getTanningStackSize(meta))});
            addOreCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.TANNED_LEATHER_CUT, 2), new Object[]{ItemMaterial.getMaterial(EnumMaterial.SCOURED_LEATHER_CUT, 2), new OreStack(barkNames[meta], ItemBark.getTanningStackSize(meta))});
        }
        addOreCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.TANNED_LEATHER), new Object[]{ItemMaterial.getMaterial(EnumMaterial.SCOURED_LEATHER), "dung"});
        addOreCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.TANNED_LEATHER_CUT, 2), new Object[]{ItemMaterial.getMaterial(EnumMaterial.SCOURED_LEATHER_CUT, 2), "dung"});
        addOreCauldronRecipe(new ItemStack(Items.GUNPOWDER, 2, 0), new Object[]{"dustSulfur", "dustSaltpeter", "dustCharcoal"});
        addOreCauldronRecipe(new ItemStack(Items.GUNPOWDER, 2, 0), new Object[]{"dustSulfur", "dustSaltpeter", "dustCoal"});
        addOreCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.BLASTING_OIL, 2), new Object[]{"dustHellfire", ItemMaterial.getMaterial(EnumMaterial.TALLOW)});
        addOreCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.FUSE), new Object[]{Items.GUNPOWDER, "string"});

        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.GLUE), new ItemStack[]{new ItemStack(Items.LEATHER)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.GLUE), new ItemStack[]{ItemMaterial.getMaterial(EnumMaterial.TANNED_LEATHER)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.GLUE), new ItemStack[]{ItemMaterial.getMaterial(EnumMaterial.SCOURED_LEATHER)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.GLUE), new ItemStack[]{ItemMaterial.getMaterial(EnumMaterial.LEATHER_STRAP, 8)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.GLUE), new ItemStack[]{ItemMaterial.getMaterial(EnumMaterial.LEATHER_CUT, 2)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.GLUE), new ItemStack[]{ItemMaterial.getMaterial(EnumMaterial.TANNED_LEATHER_CUT, 2)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.GLUE), new ItemStack[]{ItemMaterial.getMaterial(EnumMaterial.SCOURED_LEATHER_CUT, 2)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.GLUE, 2), new ItemStack[]{new ItemStack(Items.LEATHER_HELMET, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.GLUE, 2), new ItemStack[]{new ItemStack(Items.LEATHER_BOOTS, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.GLUE, 3), new ItemStack[]{new ItemStack(Items.LEATHER_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.GLUE, 4), new ItemStack[]{new ItemStack(Items.LEATHER_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.TALLOW), new ItemStack[]{new ItemStack(Items.COOKED_PORKCHOP)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.TALLOW), new ItemStack[]{new ItemStack(Items.PORKCHOP)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.TALLOW), new ItemStack[]{new ItemStack(Items.COOKED_BEEF, 4)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.TALLOW), new ItemStack[]{new ItemStack(Items.BEEF, 4)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.TALLOW), new ItemStack[]{new ItemStack(Items.MUTTON, 6)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.TALLOW), new ItemStack[]{new ItemStack(Items.COOKED_MUTTON, 6)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.TALLOW), new ItemStack[]{new ItemStack(Items.ROTTEN_FLESH, 10)});

        addOreStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.POTASH), new Object[]{"logWood"});
        addOreStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.POTASH), new Object[]{new OreStack("plankWood", 6)});
        addOreStokedCauldronRecipe(ItemMaterial.getMaterial(EnumMaterial.POTASH), new Object[]{new OreStack("dustWood", 16)});
        addCauldronRecipe(new ItemStack(BWMItems.CHICKEN_SOUP, 3), new ItemStack[]{new ItemStack(Items.COOKED_CHICKEN), new ItemStack(Items.CARROT), new ItemStack(Items.BAKED_POTATO), new ItemStack(Items.BOWL, 3)});
        addOreCauldronRecipe(new ItemStack(BWMItems.CHOCOLATE, 2), new ItemStack(Items.BUCKET), new Object[]{"foodCocoapowder", new ItemStack(Items.SUGAR), new ItemStack(Items.MILK_BUCKET)});
        addCauldronRecipe(new ItemStack(BWMItems.CHOWDER, 2), new ItemStack(Items.BUCKET), new ItemStack[]{new ItemStack(Items.COOKED_FISH), new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.BOWL, 2)});
        addCauldronRecipe(new ItemStack(BWMItems.CHOWDER, 2), new ItemStack(Items.BUCKET), new ItemStack[]{new ItemStack(Items.COOKED_FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()), new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.BOWL, 2)});
        addOreCauldronRecipe(new ItemStack(BWMItems.HEARTY_STEW, 5), new Object[]{"listAllmeatcooked", Items.CARROT, Items.BAKED_POTATO, new ItemStack(Items.BOWL, 5), new ItemStack(Blocks.BROWN_MUSHROOM, 3), "foodFlour"});
    }


    private static void addTurntableRecipes() {

        addTurntableRecipe(Blocks.CLAY, 0, BWMBlocks.UNFIRED_POTTERY, EnumPotteryType.CRUCIBLE.getMeta(), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY, EnumPotteryType.CRUCIBLE.getMeta(), BWMBlocks.UNFIRED_POTTERY, EnumPotteryType.PLANTER.getMeta(), ItemStack.EMPTY);
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY, EnumPotteryType.PLANTER.getMeta(), BWMBlocks.UNFIRED_POTTERY, EnumPotteryType.VASE.getMeta(), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY, EnumPotteryType.VASE.getMeta(), BWMBlocks.UNFIRED_POTTERY, EnumPotteryType.URN.getMeta(), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY, EnumPotteryType.URN.getMeta(), null, 0, new ItemStack(Items.CLAY_BALL));
    }

    private static void addKilnRecipes() {
        addKilnRecipe(BWMBlocks.UNFIRED_POTTERY, 0, new ItemStack(BWMBlocks.COOKING_POTS, 1, BlockCookingPot.EnumType.CRUCIBLE.getMeta()));
        addKilnRecipe(BWMBlocks.UNFIRED_POTTERY, 1, new ItemStack(BWMBlocks.PLANTER));
        addKilnRecipe(BWMBlocks.UNFIRED_POTTERY, 2, new ItemStack(BWMBlocks.URN));
        addKilnRecipe(BWMBlocks.UNFIRED_POTTERY, 3, new ItemStack(BWMBlocks.VASE));
        addKilnRecipe(Blocks.CLAY, 0, new ItemStack(Blocks.HARDENED_CLAY));

    }

    public static void addKilnWood() {
        for (ItemStack stack : OreDictionary.getOres("logWood")) {
            if (stack.getItem() instanceof ItemBlock) {
                Item item = stack.getItem();
                Block block = ((ItemBlock) item).getBlock();
                int meta = stack.getItemDamage();
                if (meta == OreDictionary.WILDCARD_VALUE)
                    addKilnRecipe(block, new ItemStack(Items.COAL, 2, 1));
                else {
                    addKilnRecipe(block, meta, new ItemStack(Items.COAL, 2, 1));
                }
            }
        }
        addKilnRecipe(BWMBlocks.DEBARKED_OLD, new ItemStack(Items.COAL, 2, 1));
        addKilnRecipe(BWMBlocks.DEBARKED_NEW, new ItemStack(Items.COAL, 2, 1));
    }

    private static void addCrucibleRecipes() {

        addOreStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT), new Object[]{new OreStack("nuggetGold", 9)});
        addOreStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT), new Object[]{new OreStack("nuggetIron", 9)});
        addOreStokedCrucibleRecipe(ItemMaterial.getMaterial(EnumMaterial.DIAMOND_INGOT), new Object[]{new OreStack("nuggetDiamond", 9)});
        addOreStokedCrucibleRecipe(ItemMaterial.getMaterial(EnumMaterial.INGOT_STEEL), new Object[]{new OreStack("nuggetSoulforgedSteel", 9)});

        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 2, 0), new ItemStack[]{new ItemStack(Items.IRON_DOOR)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 8, 0), new ItemStack[]{new ItemStack(Items.IRON_HORSE_ARMOR, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 8, 0), new ItemStack[]{new ItemStack(Items.GOLDEN_HORSE_ARMOR, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 5), new ItemStack[]{new ItemStack(Items.MINECART)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 5), new ItemStack[]{new ItemStack(Items.CHEST_MINECART)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 5), new ItemStack[]{new ItemStack(Items.FURNACE_MINECART)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 3), new ItemStack[]{new ItemStack(Blocks.RAIL, 8)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 3), new ItemStack[]{new ItemStack(Blocks.IRON_BARS, 8)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 7), new ItemStack[]{new ItemStack(Items.CAULDRON)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 31), new ItemStack[]{new ItemStack(Blocks.ANVIL, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack[]{new ItemStack(Blocks.TRIPWIRE_HOOK, 2, 0)});
        addStokedCrucibleRecipe(new ItemStack(Blocks.GLASS), new ItemStack[]{new ItemStack(Blocks.SAND, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Blocks.GLASS, 3), new ItemStack[]{new ItemStack(Blocks.GLASS_PANE, 8)});
        addStokedCrucibleRecipe(new ItemStack(Blocks.STONE), new ItemStack[]{new ItemStack(Blocks.COBBLESTONE)});
        addStokedCrucibleRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 6), new ItemStack[]{new ItemStack(BWMBlocks.AESTHETIC, 1, 7)});
        addCrucibleRecipe(new ItemStack(Blocks.SPONGE, 1, 0), new ItemStack[]{new ItemStack(Blocks.SPONGE, 1, 1)});
        addCrucibleRecipe(new ItemStack(Blocks.SPONGE, 1, 0), new ItemStack(Items.WATER_BUCKET), new ItemStack[]{new ItemStack(Blocks.SPONGE, 1, 1), new ItemStack(Items.BUCKET)});
    }

    private static void addSteelAnvilRecipes() {
        addSteelShapedOreRecipe(new ItemStack(BWMBlocks.BLOCK_DISPENSER), "MMMM", "MUUM", "STTS", "SRRS", 'M', Blocks.MOSSY_COBBLESTONE, 'U', new ItemStack(BWMBlocks.URN, 1, 8), 'S', "stone", 'R', "dustRedstone", 'T', Blocks.REDSTONE_TORCH);
        addSteelShapedOreRecipe(new ItemStack(BWMBlocks.BUDDY_BLOCK), "SSLS", "LTTS", "STTL", "SLSS", 'S', "stone", 'T', Blocks.REDSTONE_TORCH, 'L', ItemMaterial.getMaterial(EnumMaterial.POLISHED_LAPIS));
        addSteelShapedOreRecipe(new ItemStack(BWMBlocks.DETECTOR), "CCCC", "LTTL", "SRRS", "SRRS", 'C', "cobblestone", 'L', ItemMaterial.getMaterial(EnumMaterial.POLISHED_LAPIS), 'T', Blocks.REDSTONE_TORCH, 'S', "stone", 'R', "dustRedstone");
        addSteelShapedOreRecipe(new ItemStack(BWMItems.STEEL_HELMET), "SSSS", "S  S", "S  S", " PP ", 'P', ItemMaterial.getMaterial(EnumMaterial.ARMOR_PLATE), 'S', "ingotSoulforgedSteel");
        addSteelShapedOreRecipe(new ItemStack(BWMItems.STEEL_CHEST), "P  P", "SSSS", "SSSS", "SSSS", 'P', ItemMaterial.getMaterial(EnumMaterial.ARMOR_PLATE), 'S', "ingotSoulforgedSteel");
        addSteelShapedOreRecipe(new ItemStack(BWMItems.STEEL_PANTS), "SSSS", "PSSP", "P  P", "P  P", 'P', ItemMaterial.getMaterial(EnumMaterial.ARMOR_PLATE), 'S', "ingotSoulforgedSteel");
        addSteelShapedOreRecipe(new ItemStack(BWMItems.STEEL_BOOTS), " SS ", " SS ", "SPPS", 'P', ItemMaterial.getMaterial(EnumMaterial.ARMOR_PLATE), 'S', "ingotSoulforgedSteel");
        addSteelShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.POLISHED_LAPIS, 2), "LLL", "LLL", "GGG", " R ", 'L', "gemLapis", 'R', "dustRedstone", 'G', "nuggetGold");
        addSteelShapedOreRecipe(new ItemStack(BWMItems.STEEL_AXE), "XX", "XH", " H", " H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(EnumMaterial.HAFT));
        addSteelShapedOreRecipe(new ItemStack(BWMItems.STEEL_HOE), "XX", " H", " H", " H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(EnumMaterial.HAFT));
        addSteelShapedOreRecipe(new ItemStack(BWMItems.STEEL_PICKAXE), "XXX", " H ", " H ", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(EnumMaterial.HAFT));
        addSteelShapedOreRecipe(new ItemStack(BWMItems.STEEL_SHOVEL), "X", "H", "H", "H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(EnumMaterial.HAFT));
        addSteelShapedOreRecipe(new ItemStack(BWMItems.STEEL_SWORD), "X", "X", "X", "H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(EnumMaterial.HAFT));
        addSteelShapedOreRecipe(new ItemStack(BWMItems.STEEL_BATTLEAXE), "XXX", "XHX", " H ", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(EnumMaterial.HAFT));
        addSteelShapedOreRecipe(new ItemStack(BWMItems.STEEL_MATTOCK), " XXX", "X H ", "  H ", "  H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(EnumMaterial.HAFT));
        addSteelShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.ARMOR_PLATE), "BSPB", 'B', ItemMaterial.getMaterial(EnumMaterial.LEATHER_STRAP), 'S', "ingotSoulforgedSteel", 'P', ItemMaterial.getMaterial(EnumMaterial.PADDING));
        addSteelShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.BROADHEAD, 6), " N ", " N ", "NNN", " N ", 'N', "nuggetSoulforgedSteel");
        addSteelShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 2), "XXXX", "XXXX", "XXXX", "XXXX", 'X', "ingotSoulforgedSteel");
        addSteelShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 6, 0), "X  X", "XXXX", 'X', "stone");
        addSteelShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.CHAIN_MAIL, 2), "N N ", " N N", "N N ", " N N", 'N', "nuggetIron");
        addSteelShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.STEEL_GEAR)," NN ","NIIN","NIIN"," NN ",'N',"nuggetSoulforgedSteel",'I',"ingotSoulforgedSteel");
        addSteelShapedOreRecipe(ItemMaterial.getMaterial(EnumMaterial.STEEL_SPRING),"NNN","NNN","NNN","NNN",'N',"nuggetSoulforgedSteel");
        for (BlockMini.EnumType type : BlockMini.EnumType.values()) {
            addSteelShapedRecipe(new ItemStack(BWMBlocks.STONE_SIDING, 8, type.getMetadata()), "XXXX", 'X', type.getBlock());
            addSteelShapedRecipe(new ItemStack(BWMBlocks.STONE_MOULDING, 8, type.getMetadata()), "XXXX", 'X', new ItemStack(BWMBlocks.STONE_SIDING, 1, type.getMetadata()));
            addSteelShapedRecipe(new ItemStack(BWMBlocks.STONE_CORNER, 8, type.getMetadata()), "XXXX", 'X', new ItemStack(BWMBlocks.STONE_MOULDING, 1, type.getMetadata()));
        }
    }

    private static void addPileRecipes() {
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.DIRT_PILE, 4), new ItemStack(Blocks.DIRT));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.DIRT), new ItemStack(BWMItems.DIRT_PILE), new ItemStack(BWMItems.DIRT_PILE), new ItemStack(BWMItems.DIRT_PILE), new ItemStack(BWMItems.DIRT_PILE));
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.DIRT_PILE, 2), new ItemStack(BWMBlocks.DIRT_SLAB));
        GameRegistry.addShapelessRecipe(new ItemStack(BWMBlocks.DIRT_SLAB), new ItemStack(BWMItems.DIRT_PILE), new ItemStack(BWMItems.DIRT_PILE));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.DIRT), new ItemStack(BWMBlocks.DIRT_SLAB), new ItemStack(BWMBlocks.DIRT_SLAB));
        GameRegistry.addRecipe(new ItemStack(BWMBlocks.DIRT_SLAB, 4), "##", '#', Blocks.DIRT);

        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.GRAVEL_PILE, 4), new ItemStack(Blocks.GRAVEL));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.GRAVEL), new ItemStack(BWMItems.GRAVEL_PILE), new ItemStack(BWMItems.GRAVEL_PILE), new ItemStack(BWMItems.GRAVEL_PILE), new ItemStack(BWMItems.GRAVEL_PILE));

        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.SAND_PILE, 4), new ItemStack(Blocks.SAND));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.SAND), new ItemStack(BWMItems.SAND_PILE), new ItemStack(BWMItems.SAND_PILE), new ItemStack(BWMItems.SAND_PILE), new ItemStack(BWMItems.SAND_PILE));
    }

    public static void addSawRecipe(Block block, int meta, ItemStack output) {
        addSawRecipe(block, meta, new ItemStack[]{output});
    }

    public static void addSawRecipe(Block block, int meta, ItemStack... outputs) {
        SawInteraction.INSTANCE.addRecipe(block, meta, outputs);
    }

    public static void addOreCauldronRecipe(ItemStack output, Object[] inputs) {
        CraftingManagerCauldron.getInstance().addOreRecipe(output, inputs);
    }

    public static void addCauldronRecipe(ItemStack output, ItemStack[] inputs) {
        CraftingManagerCauldron.getInstance().addRecipe(output, inputs);
    }

    public static void addOreCauldronRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        CraftingManagerCauldron.getInstance().addOreRecipe(output, secondary, inputs);
    }

    public static void addCauldronRecipe(ItemStack output, ItemStack secondary, ItemStack[] inputs) {
        CraftingManagerCauldron.getInstance().addRecipe(output, secondary, inputs);
    }

    public static void addOreStokedCauldronRecipe(ItemStack output, Object[] inputs) {
        CraftingManagerCauldronStoked.getInstance().addOreRecipe(output, inputs);
    }

    public static void addOreStokedCauldronRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        CraftingManagerCauldronStoked.getInstance().addOreRecipe(output, secondary, inputs);
    }

    public static void addStokedCauldronRecipe(ItemStack output, ItemStack[] inputs) {
        CraftingManagerCauldronStoked.getInstance().addRecipe(output, inputs);
    }

    public static void addStokedCauldronRecipe(ItemStack output, ItemStack secondary, ItemStack[] inputs) {
        CraftingManagerCauldronStoked.getInstance().addRecipe(output, secondary, inputs);
    }

    public static void addCrucibleRecipe(ItemStack output, ItemStack[] inputs) {
        CraftingManagerCrucible.getInstance().addRecipe(output, inputs);
    }

    public static void addCrucibleRecipe(ItemStack output, ItemStack secondary, ItemStack[] inputs) {
        CraftingManagerCrucible.getInstance().addRecipe(output, secondary, inputs);
    }

    public static void addStokedCrucibleRecipe(ItemStack output, ItemStack[] inputs) {
        CraftingManagerCrucibleStoked.getInstance().addRecipe(output, inputs);
    }

    public static void addStokedCrucibleRecipe(ItemStack output, ItemStack secondary, ItemStack[] inputs) {
        CraftingManagerCrucibleStoked.getInstance().addRecipe(output, secondary, inputs);
    }

    public static void addOreCrucibleRecipe(ItemStack output, Object[] inputs) {
        CraftingManagerCrucible.getInstance().addOreRecipe(output, inputs);
    }

    public static void addOreCrucibleRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        CraftingManagerCrucible.getInstance().addOreRecipe(output, secondary, inputs);
    }

    public static void addOreStokedCrucibleRecipe(ItemStack output, Object[] inputs) {
        CraftingManagerCrucibleStoked.getInstance().addOreRecipe(output, inputs);
    }



    public static void addOreStokedCrucibleRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        CraftingManagerCrucibleStoked.getInstance().addOreRecipe(output, secondary, inputs);
    }

    public static void addMillRecipe(ItemStack output, ItemStack... inputs) {
        CraftingManagerMill.getInstance().addRecipe(output, inputs);
    }

    public static void addMillRecipe(ItemStack output, ItemStack secondary, ItemStack... inputs) {
        CraftingManagerMill.getInstance().addRecipe(output, secondary, inputs);
    }

    public static void addMillRecipe(ItemStack output, ItemStack input) {
        CraftingManagerMill.getInstance().addRecipe(output, input);
    }

    public static void addOreMillRecipe(ItemStack output, Object[] inputs) {
        CraftingManagerMill.getInstance().addOreRecipe(output, inputs);
    }

    public static void addOreMillRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        CraftingManagerMill.getInstance().addOreRecipe(output, secondary, inputs);
    }

    public static void addOreMillRecipe(ItemStack output, Object input) {
        CraftingManagerMill.getInstance().addOreRecipe(output, input);
    }


    public static void addTurntableRecipe(Block inputBlock, int iMeta, Block outputBlock, int oMeta, ItemStack... scraps) {
        TurntableInteraction.INSTANCE.addTurntableRecipe(inputBlock, iMeta, outputBlock, oMeta, scraps);
    }

    public static void addTurntableRecipe(ItemStack inputBlock, ItemStack outputBlock, ItemStack... scraps) {
        TurntableInteraction.INSTANCE.addTurntableRecipe(inputBlock, outputBlock, scraps);
    }

    public static void addKilnRecipe(ItemStack inputBlock, ItemStack... output) {
        KilnInteraction.INSTANCE.addRecipe(inputBlock, output);
    }

    public static void addKilnRecipe(Block inputBlock, ItemStack... output) {
        KilnInteraction.INSTANCE.addRecipe(inputBlock, 0, output);
    }

    public static void addKilnRecipe(Block inputBlock, int inputMeta, ItemStack... output) {
        KilnInteraction.INSTANCE.addRecipe(inputBlock, inputMeta, output);
    }

    public static SteelShapedRecipe addSteelShapedRecipe(ItemStack output, Object... input) {
        return SteelCraftingManager.getInstance().addRecipe(output, input);
    }

    public static SteelShapedOreRecipe addSteelShapedOreRecipe(ItemStack output, Object... input) {
        return SteelCraftingManager.getInstance().addSteelShapedOreRecipe(output, input);
    }

    public static SteelShapelessRecipe addSteelShapelessRecipe(ItemStack output, Object... input) {
        return SteelCraftingManager.getInstance().addShapelessRecipe(output, input);
    }

    public static ShapelessOreRecipe addShapelessOreRecipe(ItemStack output, Object... input) {
        return SteelCraftingManager.getInstance().addShapelessOreRecipe(output, input);
    }

    public static void postInit() {
    }
}
