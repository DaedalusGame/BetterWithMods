package betterwithmods;

import betterwithmods.blocks.BlockUnfiredPottery.EnumPotteryType;
import betterwithmods.config.BWConfig;
import betterwithmods.craft.*;
import betterwithmods.craft.bulk.CraftingManagerCauldron;
import betterwithmods.craft.bulk.CraftingManagerCauldronStoked;
import betterwithmods.craft.bulk.CraftingManagerCrucible;
import betterwithmods.craft.bulk.CraftingManagerCrucibleStoked;
import betterwithmods.craft.bulk.CraftingManagerMill;
import betterwithmods.items.ItemBark;
import betterwithmods.items.ItemMaterial;
import betterwithmods.util.InvUtils;
import betterwithmods.util.RecipeUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;
import java.util.Random;

public class BWCrafting {
    public static void init() {
        addVanillaRecipes();
        addCauldronRecipes();
        addCrucibleRecipes();
        addMillRecipes();
        addKilnRecipes();
        addTurntableRecipes();
        addSawRecipes();
        addHERecipes();
    }

    public static void postInit() {
      addKilnOres();
    }
    private static void addHERecipes() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 1), "PIP", "GLG", "PIP", 'P', new ItemStack(BWMBlocks.WOOD_SIDING, 1, 32767), 'I', "ingotIron", 'G', "gearWood", 'L', ItemMaterial.getMaterial("redstone_latch")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PLATFORM), "MWM", " M ", "MWM", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, 32767), 'W', new ItemStack(BWMBlocks.PANE, 1, 2)));
        GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.AXLE), "M", "R", "M", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE), 'R', BWMBlocks.ROPE);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GEARBOX), "SGS", "GLG", "SGS", 'L', ItemMaterial.getMaterial("redstone_latch"), 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'G', "gearWood"));
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            GameRegistry.addShapelessRecipe(new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata()), new ItemStack(BWMBlocks.WOOD_CORNER, 1, type.getMetadata()), new ItemStack(BWMBlocks.WOOD_CORNER, 1, type.getMetadata()));
            GameRegistry.addShapelessRecipe(new ItemStack(BWMBlocks.WOOD_SIDING, 1, type.getMetadata()), new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata()), new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata()));
            GameRegistry.addShapelessRecipe(new ItemStack(Blocks.PLANKS, 1, type.getMetadata()), new ItemStack(BWMBlocks.WOOD_SIDING, 1, type.getMetadata()), new ItemStack(BWMBlocks.WOOD_SIDING, 1, type.getMetadata()));
            GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.WOOD_TABLE, 4, type.getMetadata()), "SSS", " M ", " M ", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, type.getMetadata()), 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata()));
            GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.WOOD_BENCH, 4, type.getMetadata()), "SSS", " M ", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, type.getMetadata()), 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata()));
        }
        for (int i = 0; i < 4; i++)
            GameRegistry.addShapelessRecipe(new ItemStack(Blocks.PLANKS, 3, i), new ItemStack(BWMBlocks.DEBARKED_OLD, 1, i));
        for (int i = 0; i < 2; i++)
            GameRegistry.addShapelessRecipe(new ItemStack(Blocks.PLANKS, 3, 4 + i), new ItemStack(BWMBlocks.DEBARKED_NEW, 1, i));
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.BOOKSHELF), "SSS", "BBB", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'B', Items.BOOK);
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.CHEST), "SSS", "S S", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.NOTEBLOCK), "SSS", "SRS", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.JUKEBOX), "SSS", "SDS", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'D', "gemDiamond"));
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.LADDER, 4), "M M", "MMM", "M M", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.TRAPDOOR, 2), "SSS", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapedRecipe(new ItemStack(Items.SIGN, 3), "S", "M", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.STICK), new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.BOOK), ItemMaterial.getMaterial("leather_cut"), "paper", "paper", "paper"));
        GameRegistry.addShapedRecipe(new ItemStack(Items.ITEM_FRAME), "MMM", "MLM", "MMM", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE), 'L', ItemMaterial.getMaterial("leather_cut"));
        GameRegistry.addShapedRecipe(new ItemStack(Items.BOWL, 6), "S S", " S ", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SAW), "III", "GBG", "WGW", 'I', "ingotIron", 'G', "gearWood", 'W', BWMBlocks.WOOD_SIDING, 'B', ItemMaterial.getMaterial("leather_belt")));

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

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.LIGHT), " P ", "PFP", " R ", 'P', "paneGlass", 'R', "dustRedstone", 'F', ItemMaterial.getMaterial("filament")));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.BOOSTER), Blocks.RAIL, BWMBlocks.ROPE, "gearWood"));
        GameRegistry.addShapelessRecipe(ItemMaterial.getMaterial("hemp_fibers", 6), BWMBlocks.ROPE);
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("nugget_iron", 9), "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.IRON_INGOT), "III", "III", "III", 'I', "nuggetIron"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("nugget_steel", 9), "ingotSoulforgedSteel"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("ingot_steel"), "III", "III", "III", 'I', "nuggetSoulforgedSteel"));

        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.BAMBOO_CHIME, 1, type.getMetadata()), " S ", "SPS", "BMB", 'S', "string", 'P', Blocks.WOODEN_PRESSURE_PLATE, 'B', "sugarcane", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata())));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.METAL_CHIME, 1, type.getMetadata()), " S ", "SPS", "IMI", 'S', "string", 'P', Blocks.WOODEN_PRESSURE_PLATE, 'I', "ingotIron", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata())));
        }
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.BUDDY_BLOCK), "SLS", "LTL", "SLS", 'S', "stone", 'T', Blocks.REDSTONE_TORCH, 'L', ItemMaterial.getMaterial("polished_lapis")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.BLOCK_DISPENSER), "MMM", "MUM", "SRS", 'M', Blocks.MOSSY_COBBLESTONE, 'U', new ItemStack(BWMBlocks.URN, 1, 8), 'S', "stone", 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_AXE), "XX ", "XH ", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial("haft")).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_HOE), "XX ", " H ", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial("haft")).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_PICKAXE), "XXX", " H ", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial("haft")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_SHOVEL), "X", "H", "H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial("haft")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_SWORD), "X", "X", "H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial("haft")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_BATTLEAXE), "XXX", "XHX", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial("haft")).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_MATTOCK), "XXX", " HX", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial("haft")));

        GameRegistry.addSmelting(ItemMaterial.getMaterial("flour"), new ItemStack(Items.BREAD), 0.1F);
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.URN, 1, 9), new ItemStack(BWMBlocks.URN, 1, 8), new ItemStack(Items.ENDER_EYE), "obsidian"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SAW), "III", "GBG", "WGW", 'I', "ingotIron", 'G', "gearWood", 'W', "plankWood", 'B', ItemMaterial.getMaterial("leather_belt")));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.FLINT, 9, 0), new ItemStack(BWMBlocks.AESTHETIC, 1, 5)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 5), "XXX", "XXX", "XXX", 'X', new ItemStack(Items.FLINT)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.ROPE, 9, 0), new ItemStack(BWMBlocks.AESTHETIC, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 4), "XXX", "XXX", "XXX", 'X', new ItemStack(BWMBlocks.ROPE)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("concentrated_hellfire", 9), new ItemStack(BWMBlocks.AESTHETIC, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 3), "XXX", "XXX", "XXX", 'X', ItemMaterial.getMaterial("concentrated_hellfire")));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("ingot_steel", 9), new ItemStack(BWMBlocks.AESTHETIC, 1, 2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 2), "XXX", "XXX", "XXX", 'X', "ingotSoulforgedSteel"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 6, 0), "XXX", "XXX", 'X', "stone"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMItems.DYNAMITE), "paper", "paper", "paper", ItemMaterial.getMaterial("fuse"), ItemMaterial.getMaterial("blasting_oil"), "dustWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.LENS), "GDG", "G G", "GLG", 'G', "ingotGold", 'D', "gemDiamond", 'L', "blockGlass"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.DETECTOR), "GSG", "SCS", "SSS", 'S', "cobblestone", 'C', Items.COMPARATOR, 'G', ItemMaterial.getMaterial("polished_lapis")));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("polished_lapis", 2), "LLL", "LLL", "GRG", 'L', "gemLapis", 'R', "dustRedstone", 'G', "nuggetGold"));
        //Wood Blade 10 Windmill Blade 11
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 2), "XXX", "X X", "XXX", 'X', ItemMaterial.getMaterial("windmill_blade")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 1), "XXX", "X X", "XXX", 'X', ItemMaterial.getMaterial("wood_blade")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 0), " X ", "X X", " X ", 'X', ItemMaterial.getMaterial("windmill_blade")));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("wood_blade"), "W  ", "WGW", "W  ", 'G', "slimeball", 'W', BWMBlocks.WOOD_SIDING).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("hemp_cloth"), "XXX", "XXX", "XXX", 'X', "fiberHemp"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("windmill_blade"), "CCC", "CCC", "WWW", 'W', "slabWood", 'C', "fabricHemp"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.ANCHOR), " I ", "SSS", 'S', "stone", 'I', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.HIBACHI), "HHH", "SES", "SRS", 'S', "stone", 'R', "dustRedstone", 'H', ItemMaterial.getMaterial("concentrated_hellfire"), 'E', ItemMaterial.getMaterial("element")));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("leather_strap", 4), "craftingToolKnife", ItemMaterial.getMaterial("tanned_leather_cut")));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("leather_belt"), " L ", "L L", " L ", 'L', ItemMaterial.getMaterial("leather_strap")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.BELLOWS), "WWW", "LLL", "BGB", 'W', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'L', ItemMaterial.getMaterial("tanned_leather_cut"), 'B', ItemMaterial.getMaterial("leather_belt"), 'G', "gearWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GEARBOX), "PGP", "GLG", "PGP", 'P', "plankWood", 'G', "gearWood", 'L', ItemMaterial.getMaterial("redstone_latch")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 5), "LLL", "SCS", "SWS", 'L', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'S', "stone", 'W', "gearWood", 'C', Items.CLOCK));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.TORCH, 4), "X", "S", 'S', "stickWood", 'X', ItemMaterial.getMaterial("nethercoal")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 4), "S S", "GPG", " C ", 'C', BWMBlocks.WOOD_CORNER, 'S', BWMBlocks.WOOD_SIDING, 'G', "gearWood", 'P', Blocks.WOODEN_PRESSURE_PLATE));
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.singleMachines, 1, 4), new Object[] {"X X", "XCX", " X ", Character.valueOf('X'), "slabWood", Character.valueOf('C'), Blocks.CHEST}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AXLE), "X", "R", "X", 'X', "plankWood", 'R', new ItemStack(BWMBlocks.ROPE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.HAND_CRANK), "  X", " X ", "SWS", 'X', "stickWood", 'S', "cobblestone", 'W', "gearWood").setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 0), "XWX", "XXX", "XXX", 'X', "stone", 'W', "gearWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 3), "XBX", "XWX", "XXX", 'X', "ingotCopper", 'B', Items.BONE, 'W', Items.WATER_BUCKET));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 3), "XBX", "XWX", "XXX", 'X', "ingotIron", 'B', Items.BONE, 'W', Items.WATER_BUCKET));
        GameRegistry.addSmelting(ItemMaterial.getMaterial("nether_sludge"), new ItemStack(Items.NETHERBRICK), 0.2F);
        GameRegistry.addSmelting(BWMBlocks.DEBARKED_OLD, new ItemStack(Items.COAL, 1, 1), 0.1F);
        GameRegistry.addSmelting(BWMBlocks.DEBARKED_NEW, new ItemStack(Items.COAL, 1, 1), 0.1F);
        GameRegistry.addSmelting(new ItemStack(BWMBlocks.AESTHETIC, 1, 7), new ItemStack(BWMBlocks.AESTHETIC, 1, 6), 0.1F);
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("haft"), "S", "G", "X", 'S', ItemMaterial.getMaterial("leather_strap"), 'G', "slimeball", 'X', BWMBlocks.WOOD_MOULDING));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("gear", 2), "SWS", "W W", "SWS", 'S', "stickWood", 'W', "plankWood"));
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.pane, 4, 0), new Object[]{"SSS", "SSS", Character.valueOf('S'), new ItemStack(BWMItems.material, 1, 38)}));
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.grate, 4, 0), new Object[] {"WSW", "WSW", Character.valueOf('S'), "stickWood", Character.valueOf('W'), new ItemStack(BWMBlocks.woodMoulding, 1, OreDictionary.WILDCARD_VALUE)}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SLATS, 4, 0), "SS", "SS", 'S', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE)));
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            int meta = type.getMetadata();
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GRATE, 4, meta), "WSW", "WSW", 'S', "stickWood", 'W', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta)));
            //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.grate, 4, i), new Object[] {"WSW", "WSW", Character.valueOf('S'), new ItemStack(BWMItems.material, 1, 38), Character.valueOf('W'), new ItemStack(Blocks.planks, 1, i)}));
            ItemStack moulding = new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta);
            GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.SLATS, 4, meta), "SS", "SS", 'S', moulding);
            //GameRegistry.addRecipe(new ShapedRecipe(new ItemStack(BWMBlocks.slats, 4, i), new Object[] {"SSS", "SSS", Character.valueOf('S'), new ItemStack(Blocks.wooden_slab, 1, i)}));
        }
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GRATE, 4, 0), "WSW", "WSW", 'S', "stickWood", 'W', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PANE, 4, 2), "RRR", "RRR", 'R', Items.REEDS));
        //GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.GUNPOWDER, 1, 0), new Object[] {"dustSulfur", "dustSaltpeter", "dustCoal"}));
        //GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.GUNPOWDER, 2, 0), new Object[] {"dustSulfur", "dustSaltpeter", "dustCharcoal"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("redstone_latch"), "GGG", " R ", 'G', "nuggetGold", 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.ROPE), "XX", "XX", "XX", 'X', "fiberHemp"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("leather_cut", 2), Items.LEATHER, "craftingToolKnife"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("tanned_leather_cut", 2), ItemMaterial.getMaterial("tanned_leather"), "craftingToolKnife"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("scoured_leather_cut", 2), ItemMaterial.getMaterial("scoured_leather"), "craftingToolKnife"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("sharpening_stone"), "X ", " X", 'X', Items.FLINT).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("knife_blade"), "I ", " X", 'X', ItemMaterial.getMaterial("sharpening_stone"), 'I', "ingotIron").setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.KNIFE), "I ", " X", 'X', "stickWood", 'I', ItemMaterial.getMaterial("knife_blade")).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 1), "PIP", "GLG", "PIP", 'P', "plankWood", 'I', "ingotIron", 'G', "gearWood", 'L', ItemMaterial.getMaterial("redstone_latch")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PLATFORM), "MWM", " M ", "MWM", 'M', "plankWood", 'W', new ItemStack(BWMBlocks.PANE, 1, 2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.MINING_CHARGE), "RSR", "DDD", "DDD", 'R', BWMBlocks.ROPE, 'S', "slimeball", 'D', BWMItems.DYNAMITE));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 8), "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl"));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.ENDER_PEARL, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, 8));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("screw"), "II ", " II", "II ", 'I', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PUMP), "xGx", "SsS", "SgS", 'x', ItemMaterial.getMaterial("glue"), 'G', new ItemStack(BWMBlocks.GRATE, 1, 32767), 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, 32767), 's', ItemMaterial.getMaterial("screw"), 'g', "gearWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("ender_ocular"), "GGG", "GEG", "GGG", 'G', "nuggetGold", 'E', "enderpearl"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.ENDER_SPECTACLES), "OSO", 'O', ItemMaterial.getMaterial("ender_ocular"), 'S', ItemMaterial.getMaterial("leather_strap")));
        String[] dyes = {"White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};

        for (int i = 0; i < 16; i++) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.VASE, 1, i), new ItemStack(BWMBlocks.VASE, 1, OreDictionary.WILDCARD_VALUE), "dye" + dyes[i]));
        }

        GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.VINE_TRAP, 1), "VVV", 'V', new ItemStack(Blocks.VINE));
        GameRegistry.addShapelessRecipe(ItemMaterial.getMaterial("padding"), new ItemStack(Items.FEATHER), ItemMaterial.getMaterial("hemp_cloth"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("armor_plate"), " B", "SP","B ", 'B', ItemMaterial.getMaterial("leather_strap"), 'S', ItemMaterial.getMaterial("ingot_steel"), 'P', ItemMaterial.getMaterial("padding")));
        GameRegistry.addRecipe(new ShapedOreRecipe(BWMItems.BREEDING_HARNESS,"SLS","LLL","SLS", 'S', ItemMaterial.getMaterial("leather_strap"), 'L', ItemMaterial.getMaterial("tanned_leather")));
        GameRegistry.addSmelting(BWMItems.RAW_EGG, new ItemStack(BWMItems.COOKED_EGG), 0.1F);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.RAW_SCRAMBLED_EGG, 2), BWMItems.RAW_EGG, Items.MILK_BUCKET);
        GameRegistry.addSmelting(BWMItems.RAW_SCRAMBLED_EGG, new ItemStack(BWMItems.COOKED_SCRAMBLED_EGG), 0.1F);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.RAW_OMELET, 2), BWMItems.RAW_EGG, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM);
        GameRegistry.addSmelting(BWMItems.RAW_OMELET, new ItemStack(BWMItems.COOKED_OMELET), 0.1F);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.HAM_AND_EGGS, 2), BWMItems.COOKED_EGG, Items.COOKED_PORKCHOP);
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMItems.TASTY_SANDWICH, 2), Items.BREAD, "listAllmeatcooked"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.COMPOSITE_BOW), "GMB","MBS","GMB", 'G',"slimeball", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING,1,OreDictionary.WILDCARD_VALUE),'B',"bone",'S',"string"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("broadhead",5)," N "," N ", "NNN",'N',"nuggetSoulforgedSteel"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.BROADHEAD_ARROW), "B","S","F",'B',ItemMaterial.getMaterial("broadhead"),'S', "stickWood",'F',"feather"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_HELMET), "SSS","P P", 'P',ItemMaterial.getMaterial("armor_plate"),'S',ItemMaterial.getMaterial("ingot_steel")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_CHEST), "P P","SSS","SSS", 'P',ItemMaterial.getMaterial("armor_plate"),'S',ItemMaterial.getMaterial("ingot_steel")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_PANTS), "SSS","P P","P P", 'P',ItemMaterial.getMaterial("armor_plate"),'S',ItemMaterial.getMaterial("ingot_steel")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_BOOTS), "S S","P P", 'P',ItemMaterial.getMaterial("armor_plate"),'S',ItemMaterial.getMaterial("ingot_steel")));
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.BEEF_DINNER, 3), Items.COOKED_BEEF, Items.CARROT, Items.BAKED_POTATO);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.BEEF_POTATOES, 2), Items.COOKED_BEEF, Items.BAKED_POTATO);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.BEEF_POTATOES, 2), Items.COOKED_BEEF, Items.BAKED_POTATO);
        GameRegistry.addSmelting(BWMItems.RAW_KEBAB, new ItemStack(BWMItems.COOKED_KEBAB), 0.1F);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.PORK_DINNER, 3), Items.COOKED_PORKCHOP, Items.CARROT, Items.BAKED_POTATO);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.RAW_KEBAB, 3), Blocks.BROWN_MUSHROOM, Items.CARROT, Items.MUTTON, Items.STICK);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.RAW_PASTRY, 1, 0), "SSS", "MEM", "FFF", 'S', Items.SUGAR, 'M', Items.MILK_BUCKET, 'E', BWMItems.RAW_EGG, 'F', "foodFlour"));
        GameRegistry.addSmelting(new ItemStack(BWMBlocks.RAW_PASTRY, 1, 0), new ItemStack(Items.CAKE), 0.1F);
        addKilnRecipe(BWMBlocks.RAW_PASTRY, 0, new ItemStack(Items.CAKE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.RAW_PASTRY, 1, 2), "FCF", 'F', "foodFlour", 'C', BWMItems.CHOCOLATE));
        GameRegistry.addSmelting(new ItemStack(BWMBlocks.RAW_PASTRY, 1, 2), new ItemStack(Items.COOKIE, BWConfig.hardcoreHunger ? 8 : 16), 0.1F);
        addKilnRecipe(BWMBlocks.RAW_PASTRY, 2, new ItemStack(Items.COOKIE, BWConfig.hardcoreHunger ? 8 : 16));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.RAW_PASTRY, 1, 1), Blocks.PUMPKIN, Items.SUGAR, BWMItems.RAW_EGG, "foodFlour"));
        GameRegistry.addSmelting(new ItemStack(BWMBlocks.RAW_PASTRY, 1, 1), new ItemStack(Items.PUMPKIN_PIE, BWConfig.hardcoreHunger ? 1 : 2), 0.1F);
        addKilnRecipe(BWMBlocks.RAW_PASTRY, 1, new ItemStack(Items.PUMPKIN_PIE, BWConfig.hardcoreHunger ? 1 : 2));
        if(BWConfig.hardcoreHunger) {
            RecipeUtils.removeRecipes(Items.BREAD, 0);
            RecipeUtils.removeRecipes(Items.MUSHROOM_STEW, 0);
            addCauldronRecipe(new ItemStack(Items.MUSHROOM_STEW), new ItemStack(Items.BUCKET), new ItemStack[]{new ItemStack(Blocks.BROWN_MUSHROOM, 3), new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.BOWL)});
            RecipeUtils.removeRecipes(Items.CAKE, 0);
            RecipeUtils.removeRecipes(Items.COOKIE, 0);
            RecipeUtils.removeRecipes(Items.PUMPKIN_PIE, 0);
            RecipeUtils.removeRecipes(Items.RABBIT_STEW, 0); //Eat Hearty Stew Instead
            RecipeUtils.removeRecipes(Items.BEETROOT_SOUP, 0);
            addCauldronRecipe(new ItemStack(Items.BEETROOT_SOUP), new ItemStack[]{new ItemStack(Items.BEETROOT, 6), new ItemStack(Items.BOWL)});
        }
    }

    private static void addSawRecipes() {
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            addSawRecipe(BWMBlocks.WOOD_CORNER, type.getMetadata(), ItemMaterial.getMaterial("gear", 2));
            addSawRecipe(BWMBlocks.WOOD_MOULDING, type.getMetadata(), new ItemStack(BWMBlocks.WOOD_CORNER, 2, type.getMetadata()));
            addSawRecipe(BWMBlocks.WOOD_SIDING, type.getMetadata(), new ItemStack(BWMBlocks.WOOD_MOULDING, 2, type.getMetadata()));
        }
        addSawRecipe(Blocks.VINE, 0, new ItemStack(Blocks.VINE));
        addSawRecipe(Blocks.PUMPKIN, 0, new ItemStack(Blocks.PUMPKIN));
        SawInteraction.INSTANCE.addRecipe(new BlockMetaRecipe(Blocks.MELON_BLOCK,0,null){
            @Override
            public List<ItemStack> getOutputs() {
                Random random = new Random();
                return Lists.newArrayList(new ItemStack(Items.MELON,3 + random.nextInt(5)));
            }
        });
    }

    private static void addMillRecipes() {
        addMillRecipe(new ItemStack(Items.STRING, 10), new ItemStack(Items.DYE, 3, 1), new ItemStack[]{new ItemStack(BWMBlocks.WOLF)});
        addMillRecipe(ItemMaterial.getMaterial("flour"), new ItemStack(Items.WHEAT));
        addMillRecipe(new ItemStack(Items.SUGAR, 2, 0), new ItemStack(Items.REEDS));
        addOreMillRecipe(ItemMaterial.getMaterial("hemp_fibers", 3), new Object[]{"cropHemp"});
        addMillRecipe(ItemMaterial.getMaterial("coal_dust"), new ItemStack(Items.COAL, 1, 0));
        addMillRecipe(ItemMaterial.getMaterial("charcoal_dust"), new ItemStack(Items.COAL, 1, 1));
        addMillRecipe(new ItemStack(Items.DYE, 6, 15), new ItemStack(Items.BONE));
        addMillRecipe(new ItemStack(Items.DYE, 10, 15), new ItemStack(Items.SKULL, 1, 0));
        addMillRecipe(new ItemStack(Items.DYE, 10, 15), new ItemStack(Items.SKULL, 1, 1));
        addMillRecipe(ItemMaterial.getMaterial("ground_netherrack"), new ItemStack(Blocks.NETHERRACK));
        addMillRecipe(ItemMaterial.getMaterial("scoured_leather"), new ItemStack(Items.LEATHER));
        addMillRecipe(ItemMaterial.getMaterial("scoured_leather_cut"), new ItemStack(Items.RABBIT_HIDE));
        addMillRecipe(ItemMaterial.getMaterial("scoured_leather_cut"), ItemMaterial.getMaterial("leather_cut"));
        addMillRecipe(ItemMaterial.getMaterial("scoured_leather_cut"), ItemMaterial.getMaterial("leather_strap"));
        addMillRecipe(new ItemStack(Items.BLAZE_POWDER,0,3), new ItemStack(Items.BLAZE_ROD));

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
        addOreMillRecipe(ItemMaterial.getMaterial("flour"), "cropBarley");
        addOreMillRecipe(ItemMaterial.getMaterial("flour"), "cropOats");
        addOreMillRecipe(ItemMaterial.getMaterial("flour"), "cropRye");
        addOreMillRecipe(ItemMaterial.getMaterial("flour"), "cropRice");
        addMillRecipe(ItemMaterial.getMaterial("cocoa_powder"), new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()));
    }

    private static void addCauldronRecipes() {
        addOreCauldronRecipe(ItemMaterial.getMaterial("nether_sludge", 8), new Object[]{"dustPotash", new OreStack("powderedHellfire", 4)});
        addOreCauldronRecipe(ItemMaterial.getMaterial("nethercoal", 4), new Object[]{"powderedHellfire", "dustCoal"});
        //Flour OreDict is foodFlour, Donuts need sugar
        addOreCauldronRecipe(new ItemStack(BWMItems.DONUT, 4, 0), new Object[]{"foodFlour", Items.SUGAR});
        addOreCauldronRecipe(new ItemStack(Items.BREAD), new Object[]{"foodFlour"});
        addOreCauldronRecipe(ItemMaterial.getMaterial("concentrated_hellfire"), new Object[]{new OreStack("powderedHellfire", 8)});
        addOreCauldronRecipe(new ItemStack(Items.DYE, 1, 2), new Object[]{"blockCactus"});
        addOreCauldronRecipe(ItemMaterial.getMaterial("filament"), new Object[]{"string", "dustGlowstone", "dustRedstone"});
        addOreCauldronRecipe(ItemMaterial.getMaterial("element"), new Object[]{Items.BLAZE_POWDER, "dustRedstone", "string"});

        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            int meta = type.getMetadata();
            addOreCauldronRecipe(ItemMaterial.getMaterial("tanned_leather"), new Object[]{ItemMaterial.getMaterial("scoured_leather"), new ItemStack(BWMItems.BARK, ItemBark.getTanningStackSize(meta), meta)});
            addOreCauldronRecipe(ItemMaterial.getMaterial("tanned_leather_cut", 2), new Object[]{ItemMaterial.getMaterial("scoured_leather_cut", 2), new ItemStack(BWMItems.BARK, ItemBark.getTanningStackSize(meta), meta)});
        }
        addCauldronRecipe(ItemMaterial.getMaterial("tanned_leather"), new ItemStack[]{ItemMaterial.getMaterial("scoured_leather"), ItemMaterial.getMaterial("dung")});
        addCauldronRecipe(ItemMaterial.getMaterial("tanned_leather_cut", 2), new ItemStack[]{ItemMaterial.getMaterial("scoured_leather_cut", 2), ItemMaterial.getMaterial("dung")});
        addOreCauldronRecipe(new ItemStack(Items.GUNPOWDER, 2, 0), new Object[]{"dustSulfur", "dustSaltpeter", "dustCharcoal"});
        addOreCauldronRecipe(new ItemStack(Items.GUNPOWDER, 2, 0), new Object[]{"dustSulfur", "dustSaltpeter", "dustCoal"});
        addOreCauldronRecipe(ItemMaterial.getMaterial("blasting_oil", 2), new Object[]{"powderedHellfire", ItemMaterial.getMaterial("tallow")});
        addOreCauldronRecipe(ItemMaterial.getMaterial("fuse"), new Object[]{Items.GUNPOWDER, "string"});

        addStokedCauldronRecipe(ItemMaterial.getMaterial("glue"), new ItemStack[]{new ItemStack(Items.LEATHER)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("glue"), new ItemStack[]{ItemMaterial.getMaterial("tanned_leather")});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("glue"), new ItemStack[]{ItemMaterial.getMaterial("scoured_leather")});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("glue"), new ItemStack[]{ItemMaterial.getMaterial("leather_strap", 2)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("glue"), new ItemStack[]{ItemMaterial.getMaterial("leather_cut", 2)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("glue"), new ItemStack[]{ItemMaterial.getMaterial("tanned_leather_cut", 2)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("glue"), new ItemStack[]{ItemMaterial.getMaterial("scoured_leather_cut", 2)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("glue", 2), new ItemStack[]{new ItemStack(Items.LEATHER_HELMET, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("glue", 2), new ItemStack[]{new ItemStack(Items.LEATHER_BOOTS, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("glue", 3), new ItemStack[]{new ItemStack(Items.LEATHER_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("glue", 4), new ItemStack[]{new ItemStack(Items.LEATHER_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("tallow"), new ItemStack[]{new ItemStack(Items.COOKED_PORKCHOP)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("tallow"), new ItemStack[]{new ItemStack(Items.PORKCHOP)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("tallow"), new ItemStack[]{new ItemStack(Items.COOKED_BEEF, 4)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("tallow"), new ItemStack[]{new ItemStack(Items.BEEF, 4)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("tallow"), new ItemStack[]{new ItemStack(Items.MUTTON, 6)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("tallow"), new ItemStack[]{new ItemStack(Items.COOKED_MUTTON, 6)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("tallow"), new ItemStack[]{new ItemStack(Items.ROTTEN_FLESH, 10)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial("brimstone"), BWConfig.steelRequiresEnd ? ItemMaterial.getMaterial("soul_flux") : null, new ItemStack[]{ItemMaterial.getMaterial("ender_slag")});
        addOreStokedCauldronRecipe(ItemMaterial.getMaterial("potash"), new Object[]{"logWood"});
        addOreStokedCauldronRecipe(ItemMaterial.getMaterial("potash"), new Object[]{new OreStack("plankWood", 6)});
        addOreStokedCauldronRecipe(ItemMaterial.getMaterial("potash"), new Object[]{new OreStack("dustWood", 16)});
        addCauldronRecipe(new ItemStack(BWMItems.CHICKEN_SOUP, 3), new ItemStack[]{new ItemStack(Items.COOKED_CHICKEN), new ItemStack(Items.CARROT), new ItemStack(Items.BAKED_POTATO), new ItemStack(Items.BOWL, 3)});
        addCauldronRecipe(new ItemStack(BWMItems.CHOCOLATE, 2), new ItemStack(Items.BUCKET), new ItemStack[]{ItemMaterial.getMaterial("cocoa_powder"), new ItemStack(Items.SUGAR), new ItemStack(Items.MILK_BUCKET)});
        addCauldronRecipe(new ItemStack(BWMItems.CHOWDER, 2), new ItemStack(Items.BUCKET), new ItemStack[]{new ItemStack(Items.COOKED_FISH), new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.BOWL, 2)});
        addCauldronRecipe(new ItemStack(BWMItems.CHOWDER, 2), new ItemStack(Items.BUCKET), new ItemStack[]{new ItemStack(Items.COOKED_FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()), new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.BOWL, 2)});
        addOreCauldronRecipe(new ItemStack(BWMItems.HEARTY_STEW, 5), new Object[]{"listAllmeatcooked", Items.CARROT, Items.BAKED_POTATO, new ItemStack(Items.BOWL, 5), new ItemStack(Blocks.BROWN_MUSHROOM, 3), "foodFlour"});
    }


    private static void addTurntableRecipes() {

        addTurntableRecipe(Blocks.CLAY,0, BWMBlocks.UNFIRED_POTTERY,EnumPotteryType.CRUCIBLE.getMeta(), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY,EnumPotteryType.CRUCIBLE.getMeta(), BWMBlocks.UNFIRED_POTTERY,EnumPotteryType.PLANTER.getMeta(), null);
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY,EnumPotteryType.PLANTER.getMeta(), BWMBlocks.UNFIRED_POTTERY,EnumPotteryType.VASE.getMeta(), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY,EnumPotteryType.VASE.getMeta(), BWMBlocks.UNFIRED_POTTERY,EnumPotteryType.URN.getMeta(), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY,EnumPotteryType.URN.getMeta(), null,0, new ItemStack(Items.CLAY_BALL));
    }

    private static void addKilnRecipes() {
        addKilnRecipe(BWMBlocks.UNFIRED_POTTERY, 0, new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 2));
        addKilnRecipe(BWMBlocks.UNFIRED_POTTERY, 1, new ItemStack(BWMBlocks.PLANTER));
        addKilnRecipe(BWMBlocks.UNFIRED_POTTERY, 2, new ItemStack(BWMBlocks.URN));
        addKilnRecipe(BWMBlocks.UNFIRED_POTTERY, 3, new ItemStack(BWMBlocks.VASE));
        addKilnRecipe(Blocks.CLAY, 0, new ItemStack(Blocks.HARDENED_CLAY));
        addKilnRecipe(Blocks.END_STONE, 0, new ItemStack(BWMBlocks.AESTHETIC, 1, 7), ItemMaterial.getMaterial(BWConfig.steelRequiresEnd ? "ender_slag" : "brimstone"));

    }
    private static void addKilnOres() {
        if(BWConfig.canKilnSmeltOres) {
            InvUtils.oreNames.forEach(ore -> {
                ItemStack output = FurnaceRecipes.instance().getSmeltingResult(ore);
                if (ore != null && output != null)
                    addKilnRecipe(ore, output);
            });
        }
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
        addOreStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel"), new ItemStack(BWMBlocks.URN, 1, 0), new Object[]{"dustCoal", new ItemStack(BWMBlocks.URN, 1, 8), "ingotIron", BWConfig.steelRequiresEnd ? ItemMaterial.getMaterial("soul_flux") : null});
        addStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel", 2), new ItemStack[]{new ItemStack(BWMItems.STEEL_HOE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel", 2), new ItemStack[]{new ItemStack(BWMItems.STEEL_SWORD, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel", 3), new ItemStack[]{new ItemStack(BWMItems.STEEL_PICKAXE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel", 3), new ItemStack[]{new ItemStack(BWMItems.STEEL_AXE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel"), new ItemStack[]{new ItemStack(BWMItems.STEEL_SHOVEL, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel", 4), new ItemStack[]{new ItemStack(BWMItems.STEEL_MATTOCK, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel", 5), new ItemStack[]{new ItemStack(BWMItems.STEEL_BATTLEAXE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 3, 0), new ItemStack[]{new ItemStack(Items.GOLDEN_PICKAXE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 2, 0), new ItemStack[]{new ItemStack(Items.GOLDEN_HOE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 2, 0), new ItemStack[]{new ItemStack(Items.GOLDEN_SWORD, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 1, 0), new ItemStack[]{new ItemStack(Items.GOLDEN_SHOVEL, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 3, 0), new ItemStack[]{new ItemStack(Items.GOLDEN_AXE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 4, 0), new ItemStack[]{new ItemStack(Items.GOLDEN_BOOTS, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 8, 0), new ItemStack[]{new ItemStack(Items.GOLDEN_HORSE_ARMOR, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 5, 0), new ItemStack[]{new ItemStack(Items.GOLDEN_HELMET, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 8, 0), new ItemStack[]{new ItemStack(Items.GOLDEN_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 7, 0), new ItemStack[]{new ItemStack(Items.GOLDEN_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 3, 0), new ItemStack[]{new ItemStack(Items.IRON_PICKAXE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 2, 0), new ItemStack[]{new ItemStack(Items.IRON_HOE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 2, 0), new ItemStack[]{new ItemStack(Items.IRON_SWORD, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 1, 0), new ItemStack[]{new ItemStack(Items.IRON_SHOVEL, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 3, 0), new ItemStack[]{new ItemStack(Items.IRON_AXE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 2, 0), new ItemStack[]{new ItemStack(Items.SHEARS, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 4, 0), new ItemStack[]{new ItemStack(Items.IRON_BOOTS, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 8, 0), new ItemStack[]{new ItemStack(Items.IRON_HORSE_ARMOR, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 5, 0), new ItemStack[]{new ItemStack(Items.IRON_HELMET, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 8, 0), new ItemStack[]{new ItemStack(Items.IRON_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 7, 0), new ItemStack[]{new ItemStack(Items.IRON_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 2, 0), new ItemStack[]{new ItemStack(Items.IRON_DOOR)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 3), new ItemStack[]{new ItemStack(Items.BUCKET)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 5), new ItemStack[]{new ItemStack(Items.MINECART)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 5), new ItemStack[]{new ItemStack(Items.CHEST_MINECART)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 5), new ItemStack[]{new ItemStack(Items.FURNACE_MINECART)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 3), new ItemStack[]{new ItemStack(Blocks.RAIL, 8)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 3), new ItemStack[]{new ItemStack(Blocks.IRON_BARS, 8)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 7), new ItemStack[]{new ItemStack(Items.CAULDRON)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 7), new ItemStack[]{new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 3)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 31), new ItemStack[]{new ItemStack(Blocks.ANVIL, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack[]{new ItemStack(Blocks.TRIPWIRE_HOOK, 2, 0)});
        addStokedCrucibleRecipe(new ItemStack(Blocks.GLASS), new ItemStack[]{new ItemStack(Blocks.SAND, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Blocks.GLASS, 3), new ItemStack[]{new ItemStack(Blocks.GLASS_PANE, 8)});
        addStokedCrucibleRecipe(new ItemStack(Blocks.STONE), new ItemStack[]{new ItemStack(Blocks.COBBLESTONE)});
        addStokedCrucibleRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 6), new ItemStack[]{new ItemStack(BWMBlocks.AESTHETIC, 1, 7)});
        addCrucibleRecipe(new ItemStack(Blocks.SPONGE, 1, 0), new ItemStack[]{new ItemStack(Blocks.SPONGE, 1, 1)});
        addCrucibleRecipe(new ItemStack(Blocks.SPONGE, 1, 0), new ItemStack(Items.WATER_BUCKET), new ItemStack[]{new ItemStack(Blocks.SPONGE, 1, 1), new ItemStack(Items.BUCKET)});
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


    public static void addTurntableRecipe(Block inputBlock, int iMeta, Block outputBlock, int oMeta, ItemStack... scraps){
        TurntableInteraction.INSTANCE.addTurntableRecipe(inputBlock, iMeta, outputBlock, oMeta, scraps);
    }

    public static void addTurntableRecipe(ItemStack inputBlock, ItemStack outputBlock, ItemStack... scraps){
        TurntableInteraction.INSTANCE.addTurntableRecipe(inputBlock, outputBlock, scraps);
    }

    public static void addKilnRecipe(ItemStack inputBlock, ItemStack... output) {
        KilnInteraction.INSTANCE.addRecipe(inputBlock, output);
    }

    public static void addKilnRecipe(Block inputBlock, ItemStack... output) {
        KilnInteraction.INSTANCE.addRecipe(inputBlock,0, output);
    }

    public static void addKilnRecipe(Block inputBlock, int inputMeta, ItemStack... output) {
        KilnInteraction.INSTANCE.addRecipe(inputBlock, inputMeta, output);
    }
}
