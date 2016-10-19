package betterwithmods;

import betterwithmods.blocks.BlockUnfiredPottery;
import betterwithmods.blocks.BlockUnfiredPottery.EnumPotteryType;
import betterwithmods.config.BWConfig;
import betterwithmods.craft.KilnInteraction;
import betterwithmods.craft.OreStack;
import betterwithmods.craft.SawInteraction;
import betterwithmods.craft.TurntableInteraction;
import betterwithmods.craft.bulk.*;
import betterwithmods.items.ItemBark;
import betterwithmods.items.ItemMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BWCrafting {
    public static void init() {
        addVanillaRecipes();
        addCauldronRecipes();
        addCrucibleRecipes();
        addMillRecipes();
        addTurntableRecipes();
        addKilnRecipes();
        addHERecipes();
    }

    private static void addHERecipes() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 1), new Object[] {"PIP", "GLG", "PIP", Character.valueOf('P'), new ItemStack(BWMBlocks.WOOD_SIDING, 1, 32767), Character.valueOf('I'), "ingotIron", Character.valueOf('G'), "gearWood", Character.valueOf('L'), ItemMaterial.getMaterial("redstone_latch")}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PLATFORM), new Object[] {"MWM", " M ", "MWM", Character.valueOf('M'), new ItemStack(BWMBlocks.WOOD_MOULDING, 1, 32767), Character.valueOf('W'), new ItemStack(BWMBlocks.PANE, 1, 2)}));
        GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.AXLE), "M", "R", "M", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE), 'R', BWMBlocks.ROPE);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GEARBOX), "SGS", "GLG", "SGS", 'L', ItemMaterial.getMaterial("redstone_latch"), 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'G', "gearWood"));
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            addSawRecipe(BWMBlocks.WOOD_CORNER, type.getMetadata(), ItemMaterial.getMaterial("gear", 2));
            addSawRecipe(BWMBlocks.WOOD_MOULDING, type.getMetadata(), new ItemStack(BWMBlocks.WOOD_CORNER, 2, type.getMetadata()));
            addSawRecipe(BWMBlocks.WOOD_SIDING, type.getMetadata(), new ItemStack(BWMBlocks.WOOD_MOULDING, 2, type.getMetadata()));
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
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.BOOK), new Object[]{ItemMaterial.getMaterial("leather_cut"), "paper", "paper", "paper"}));
        GameRegistry.addShapedRecipe(new ItemStack(Items.ITEM_FRAME), "MMM", "MLM", "MMM", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE), 'L', ItemMaterial.getMaterial("leather_cut"));
        GameRegistry.addShapedRecipe(new ItemStack(Items.BOWL, 6), "S S", " S ", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SAW), new Object[]{"III", "GBG", "WGW", Character.valueOf('I'), "ingotIron", Character.valueOf('G'), "gearWood", Character.valueOf('W'), BWMBlocks.WOOD_SIDING, Character.valueOf('B'), ItemMaterial.getMaterial("leather_belt")}));

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
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.BUDDY_BLOCK),"SLS","LTL","SLS",'S',"stone",'T',Blocks.REDSTONE_TORCH,'L', ItemMaterial.getMaterial("polished_lapis")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.BLOCK_DISPENSER), "MMM", "MUM", "SRS", 'M', Blocks.MOSSY_COBBLESTONE, 'U', new ItemStack(BWMBlocks.URN, 1, 8), 'S', "stone", 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_AXE), "XX ", "XH ", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial("haft")).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_HOE), "XX ", " H ", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial("haft")).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_PICKAXE), "XXX", " H ", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial("haft")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_SHOVEL), "X", "H", "H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial("haft")));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.STEEL_SWORD), "X", "X", "H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial("haft")));

        GameRegistry.addSmelting(ItemMaterial.getMaterial("flour"), new ItemStack(Items.BREAD), 0.1F);
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.URN, 1, 9), new Object[]{new ItemStack(BWMBlocks.URN, 1, 8), new ItemStack(Items.ENDER_EYE), "obsidian"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SAW), new Object[]{"III", "GBG", "WGW", Character.valueOf('I'), "ingotIron", Character.valueOf('G'), "gearWood", Character.valueOf('W'), "plankWood", Character.valueOf('B'), ItemMaterial.getMaterial("leather_belt")}));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.FLINT, 9, 0), new Object[]{new ItemStack(BWMBlocks.AESTHETIC, 1, 5)}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 5), new Object[]{"XXX", "XXX", "XXX", Character.valueOf('X'), new ItemStack(Items.FLINT)}));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.ROPE, 9, 0), new Object[]{new ItemStack(BWMBlocks.AESTHETIC, 1, 4)}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 4), new Object[]{"XXX", "XXX", "XXX", Character.valueOf('X'), new ItemStack(BWMBlocks.ROPE)}));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("concentrated_hellfire", 9), new Object[]{new ItemStack(BWMBlocks.AESTHETIC, 1, 3)}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 3), new Object[]{"XXX", "XXX", "XXX", Character.valueOf('X'), ItemMaterial.getMaterial("concentrated_hellfire")}));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("ingot_steel", 9), new Object[]{new ItemStack(BWMBlocks.AESTHETIC, 1, 2)}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 2), new Object[]{"XXX", "XXX", "XXX", Character.valueOf('X'), "ingotSoulforgedSteel"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 6, 0), new Object[]{"XXX", "XXX", Character.valueOf('X'), "stone"}));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMItems.DYNAMITE), new Object[]{"paper", "paper", "paper", ItemMaterial.getMaterial("fuse"), ItemMaterial.getMaterial("blasting_oil"), "dustWood"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.LENS), new Object[]{"GDG", "G G", "GLG", Character.valueOf('G'), "ingotGold", Character.valueOf('D'), "gemDiamond", Character.valueOf('L'), "blockGlass"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.DETECTOR), new Object[]{"GSG", "SCS", "SSS", Character.valueOf('S'), "cobblestone", Character.valueOf('C'), Items.COMPARATOR, Character.valueOf('G'), ItemMaterial.getMaterial("polished_lapis")}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("polished_lapis", 2), new Object[]{"LLL", "LLL", "GRG", Character.valueOf('L'), "gemLapis", Character.valueOf('R'), "dustRedstone", Character.valueOf('G'), "nuggetGold"}));
        //Wood Blade 10 Windmill Blade 11
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 2), new Object[]{"XXX", "X X", "XXX", Character.valueOf('X'), ItemMaterial.getMaterial("windmill_blade")}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 1), new Object[]{"XXX", "X X", "XXX", Character.valueOf('X'), ItemMaterial.getMaterial("wood_blade")}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 0), new Object[]{" X ", "X X", " X ", Character.valueOf('X'), ItemMaterial.getMaterial("windmill_blade")}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("wood_blade"), new Object[]{"W  ", "WGW", "W  ", Character.valueOf('G'), "slimeball", Character.valueOf('W'), BWMBlocks.WOOD_SIDING}).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("hemp_cloth"), new Object[]{"XXX", "XXX", "XXX", Character.valueOf('X'), "fiberHemp"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("windmill_blade"), new Object[]{"CCC", "CCC", "WWW", Character.valueOf('W'), "slabWood", Character.valueOf('C'), "fabricHemp"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.ANCHOR), new Object[]{" I ", "SSS", Character.valueOf('S'), "stone", Character.valueOf('I'), "ingotIron"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.HIBACHI), new Object[]{"HHH", "SES", "SRS", Character.valueOf('S'), "stone", Character.valueOf('R'), "dustRedstone", Character.valueOf('H'), ItemMaterial.getMaterial("concentrated_hellfire"), Character.valueOf('E'), ItemMaterial.getMaterial("element")}));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("leather_strap", 4), new Object[]{"craftingToolKnife", ItemMaterial.getMaterial("tanned_leather_cut")}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("leather_belt"), new Object[]{" L ", "L L", " L ", Character.valueOf('L'), ItemMaterial.getMaterial("leather_strap")}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.BELLOWS), new Object[]{"WWW", "LLL", "BGB", Character.valueOf('W'), new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), Character.valueOf('L'), ItemMaterial.getMaterial("tanned_leather_cut"), Character.valueOf('B'), ItemMaterial.getMaterial("leather_belt"), Character.valueOf('G'), "gearWood"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GEARBOX), new Object[]{"PGP", "GLG", "PGP", Character.valueOf('P'), "plankWood", Character.valueOf('G'), "gearWood", Character.valueOf('L'), ItemMaterial.getMaterial("redstone_latch")}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 5), new Object[]{"LLL", "SCS", "SWS", Character.valueOf('L'), new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), Character.valueOf('S'), "stone", Character.valueOf('W'), "gearWood", Character.valueOf('C'), Items.CLOCK}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.TORCH, 4), new Object[]{"X", "S", Character.valueOf('S'), "stickWood", Character.valueOf('X'), ItemMaterial.getMaterial("nethercoal")}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 4), new Object[]{"S S", "GPG", " C ", 'C', BWMBlocks.WOOD_CORNER, 'S', BWMBlocks.WOOD_SIDING, 'G', "gearWood", 'P', Blocks.WOODEN_PRESSURE_PLATE}));
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.singleMachines, 1, 4), new Object[] {"X X", "XCX", " X ", Character.valueOf('X'), "slabWood", Character.valueOf('C'), Blocks.CHEST}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AXLE), new Object[]{"X", "R", "X", Character.valueOf('X'), "plankWood", Character.valueOf('R'), new ItemStack(BWMBlocks.ROPE)}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.HAND_CRANK), new Object[]{"  X", " X ", "SWS", Character.valueOf('X'), "stickWood", Character.valueOf('S'), "cobblestone", Character.valueOf('W'), "gearWood"}).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 0), new Object[]{"XWX", "XXX", "XXX", Character.valueOf('X'), "stone", Character.valueOf('W'), "gearWood"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 3), new Object[]{"XBX", "XWX", "XXX", Character.valueOf('X'), "ingotCopper", Character.valueOf('B'), Items.BONE, Character.valueOf('W'), Items.WATER_BUCKET}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 3), new Object[]{"XBX", "XWX", "XXX", Character.valueOf('X'), "ingotIron", Character.valueOf('B'), Items.BONE, Character.valueOf('W'), Items.WATER_BUCKET}));
        GameRegistry.addSmelting(ItemMaterial.getMaterial("nether_sludge"), new ItemStack(Items.NETHERBRICK), 0.2F);
        GameRegistry.addSmelting(BWMBlocks.DEBARKED_OLD, new ItemStack(Items.COAL, 1, 1), 0.1F);
        GameRegistry.addSmelting(BWMBlocks.DEBARKED_NEW, new ItemStack(Items.COAL, 1, 1), 0.1F);
        GameRegistry.addSmelting(new ItemStack(BWMBlocks.AESTHETIC, 1, 7), new ItemStack(BWMBlocks.AESTHETIC, 1, 6), 0.1F);
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("haft"), new Object[]{"S", "G", "X", Character.valueOf('S'), ItemMaterial.getMaterial("leather_strap"), Character.valueOf('G'), "slimeball", Character.valueOf('X'), BWMBlocks.WOOD_MOULDING}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("gear", 2), new Object[]{"SWS", "W W", "SWS", Character.valueOf('S'), "stickWood", Character.valueOf('W'), "plankWood"}));
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.pane, 4, 0), new Object[]{"SSS", "SSS", Character.valueOf('S'), new ItemStack(BWMItems.material, 1, 38)}));
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.grate, 4, 0), new Object[] {"WSW", "WSW", Character.valueOf('S'), "stickWood", Character.valueOf('W'), new ItemStack(BWMBlocks.woodMoulding, 1, OreDictionary.WILDCARD_VALUE)}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SLATS, 4, 0), new Object[]{"SS", "SS", Character.valueOf('S'), new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE)}));
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
        	int meta = type.getMetadata();
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GRATE, 4, meta), new Object[]{"WSW", "WSW", Character.valueOf('S'), "stickWood", Character.valueOf('W'), new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta)}));
            //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.grate, 4, i), new Object[] {"WSW", "WSW", Character.valueOf('S'), new ItemStack(BWMItems.material, 1, 38), Character.valueOf('W'), new ItemStack(Blocks.planks, 1, i)}));
            ItemStack moulding = new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta);
            GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.SLATS, 4, meta), new Object[]{"SS", "SS", Character.valueOf('S'), moulding});
            //GameRegistry.addRecipe(new ShapedRecipe(new ItemStack(BWMBlocks.slats, 4, i), new Object[] {"SSS", "SSS", Character.valueOf('S'), new ItemStack(Blocks.wooden_slab, 1, i)}));
        }
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GRATE, 4, 0), new Object[]{"WSW", "WSW", Character.valueOf('S'), "stickWood", Character.valueOf('W'), new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE)}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PANE, 4, 2), new Object[]{"RRR", "RRR", Character.valueOf('R'), Items.REEDS}));
        //GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.GUNPOWDER, 1, 0), new Object[] {"dustSulfur", "dustSaltpeter", "dustCoal"}));
        //GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.GUNPOWDER, 2, 0), new Object[] {"dustSulfur", "dustSaltpeter", "dustCharcoal"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("redstone_latch"), new Object[]{"GGG", " R ", Character.valueOf('G'), "nuggetGold", Character.valueOf('R'), "dustRedstone"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.ROPE), new Object[]{"XX", "XX", "XX", Character.valueOf('X'), "fiberHemp"}));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("leather_cut", 2), new Object[]{Items.LEATHER, "craftingToolKnife"}));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("tanned_leather_cut", 2), new Object[]{ItemMaterial.getMaterial("tanned_leather"), "craftingToolKnife"}));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial("scoured_leather_cut", 2), new Object[]{ItemMaterial.getMaterial("scoured_leather"), "craftingToolKnife"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("sharpening_stone"), new Object[]{"X ", " X", Character.valueOf('X'), Items.FLINT}).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("knife_blade"), new Object[]{"I ", " X", Character.valueOf('X'), ItemMaterial.getMaterial("sharpening_stone"), Character.valueOf('I'), "ingotIron"}).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.KNIFE), new Object[]{"I ", " X", Character.valueOf('X'), "stickWood", Character.valueOf('I'), ItemMaterial.getMaterial("knife_blade")}).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 1), new Object[] {"PIP", "GLG", "PIP", Character.valueOf('P'), "plankWood", Character.valueOf('I'), "ingotIron", Character.valueOf('G'), "gearWood", Character.valueOf('L'), ItemMaterial.getMaterial("redstone_latch")}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PLATFORM), new Object[] {"MWM", " M ", "MWM", Character.valueOf('M'), "plankWood", Character.valueOf('W'), new ItemStack(BWMBlocks.PANE, 1, 2)}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.MINING_CHARGE), new Object[] {"RSR", "DDD", "DDD", Character.valueOf('R'), BWMBlocks.ROPE, Character.valueOf('S'), "slimeball", 'D', BWMItems.DYNAMITE}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.AESTHETIC,1,8), new Object[]{"enderpearl","enderpearl","enderpearl","enderpearl","enderpearl","enderpearl","enderpearl","enderpearl","enderpearl",}));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.ENDER_PEARL,9), new Object[]{new ItemStack(BWMBlocks.AESTHETIC,1,8)});
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("screw"), new Object[] {"II ", " II", "II ", Character.valueOf('I'), "ingotIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PUMP), new Object[] {"xGx", "SsS", "SgS", Character.valueOf('x'), ItemMaterial.getMaterial("glue"), Character.valueOf('G'), new ItemStack(BWMBlocks.GRATE, 1, 32767), Character.valueOf('S'), new ItemStack(BWMBlocks.WOOD_SIDING, 1, 32767), Character.valueOf('s'), ItemMaterial.getMaterial("screw"), Character.valueOf('g'), "gearWood"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial("ender_ocular"), new Object[]{ "GGG","GEG","GGG", 'G', "nuggetGold", 'E', "enderpearl"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.ENDER_SPECTACLES), new Object[]{ "OSO", 'O', ItemMaterial.getMaterial("ender_ocular"),'S', ItemMaterial.getMaterial("leather_strap") }));
        String[] dyes = {"White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};

        for(int i = 0; i < 16; i++) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.VASE, 1, i), new Object[]{new ItemStack(BWMBlocks.VASE, 1, OreDictionary.WILDCARD_VALUE), "dye" + dyes[i]}));
        }
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
    }


    private static void addTurntableRecipes() {
        addTurntableRecipe(Blocks.CLAY, BWMBlocks.UNFIRED_POTTERY.getDefaultState().withProperty(BlockUnfiredPottery.POTTERYTYPE, EnumPotteryType.CRUCIBLE), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY.getDefaultState().withProperty(BlockUnfiredPottery.POTTERYTYPE, EnumPotteryType.CRUCIBLE),
        		BWMBlocks.UNFIRED_POTTERY.getDefaultState().withProperty(BlockUnfiredPottery.POTTERYTYPE, EnumPotteryType.PLANTER), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY.getDefaultState().withProperty(BlockUnfiredPottery.POTTERYTYPE, EnumPotteryType.PLANTER),
        		BWMBlocks.UNFIRED_POTTERY.getDefaultState().withProperty(BlockUnfiredPottery.POTTERYTYPE, EnumPotteryType.VASE), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY.getDefaultState().withProperty(BlockUnfiredPottery.POTTERYTYPE, EnumPotteryType.VASE),
                BWMBlocks.UNFIRED_POTTERY.getDefaultState().withProperty(BlockUnfiredPottery.POTTERYTYPE, EnumPotteryType.URN));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY.getDefaultState().withProperty(BlockUnfiredPottery.POTTERYTYPE, EnumPotteryType.URN),
        		Blocks.AIR.getDefaultState(), new ItemStack(Items.CLAY_BALL));
    }

    private static void addKilnRecipes() {
        addKilnRecipe(BWMBlocks.UNFIRED_POTTERY, 0, new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 2));
        addKilnRecipe(BWMBlocks.UNFIRED_POTTERY, 1, new ItemStack(BWMBlocks.PLANTER));
        addKilnRecipe(BWMBlocks.UNFIRED_POTTERY, 2, new ItemStack(BWMBlocks.URN));
        addKilnRecipe(BWMBlocks.UNFIRED_POTTERY, 3, new ItemStack(BWMBlocks.VASE));
        addKilnRecipe(Blocks.CLAY, 0, new ItemStack(Blocks.HARDENED_CLAY));
    	addKilnRecipe(Blocks.END_STONE,0, new ItemStack(BWMBlocks.AESTHETIC,1,7), ItemMaterial.getMaterial(BWConfig.steelRequiresEnd ? "ender_slag" :"brimstone") );
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
        addStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel", 2), new ItemStack[]{new ItemStack(BWMItems.STEEL_HOE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel", 2), new ItemStack[]{new ItemStack(BWMItems.STEEL_SWORD, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel", 3), new ItemStack[]{new ItemStack(BWMItems.STEEL_PICKAXE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel", 3), new ItemStack[]{new ItemStack(BWMItems.STEEL_AXE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel"), new ItemStack[]{new ItemStack(BWMItems.STEEL_SHOVEL, 1, OreDictionary.WILDCARD_VALUE)});
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
        addOreStokedCrucibleRecipe(ItemMaterial.getMaterial("ingot_steel"), new ItemStack(BWMBlocks.URN, 1, 0), new Object[]{"dustCoal", new ItemStack(BWMBlocks.URN, 1, 8), "ingotIron", BWConfig.steelRequiresEnd ? ItemMaterial.getMaterial("soul_flux") : null});
        addStokedCrucibleRecipe(new ItemStack(Blocks.GLASS), new ItemStack[]{new ItemStack(Blocks.SAND, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCrucibleRecipe(new ItemStack(Blocks.GLASS, 3), new ItemStack[]{new ItemStack(Blocks.GLASS_PANE, 8)});
        addStokedCrucibleRecipe(new ItemStack(Blocks.STONE), new ItemStack[]{new ItemStack(Blocks.COBBLESTONE)});
    	addStokedCrucibleRecipe(new ItemStack(BWMBlocks.AESTHETIC,1,6), new ItemStack[]{new ItemStack(BWMBlocks.AESTHETIC,1,7)});
        addCrucibleRecipe(new ItemStack(Blocks.SPONGE, 1, 0), new ItemStack[] {new ItemStack(Blocks.SPONGE, 1, 1)});
        addCrucibleRecipe(new ItemStack(Blocks.SPONGE, 1, 0), new ItemStack(Items.WATER_BUCKET), new ItemStack[] {new ItemStack(Blocks.SPONGE, 1, 1), new ItemStack(Items.BUCKET)});
    }

    public static void addSawRecipe(Block block, int meta, ItemStack output) {
        addSawRecipe(block, meta, new ItemStack[]{output});
    }

    public static void addSawRecipe(Block block, int meta, ItemStack... outputs) {
        SawInteraction.addBlock(block, meta, outputs);
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

    public static void addMillRecipe(ItemStack output, ItemStack[] inputs) {
        CraftingManagerMill.getInstance().addRecipe(output, inputs);
    }

    public static void addMillRecipe(ItemStack output, ItemStack secondary, ItemStack[] inputs) {
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

    public static void addTurntableRecipe(Block block, IBlockState output, ItemStack... scrap) {
        TurntableInteraction.addBlockRecipe(block, output, scrap);
    }

    public static void addTurntableRecipe(IBlockState input, IBlockState output, ItemStack... scrap) {
        TurntableInteraction.addBlockRecipe(input, output, scrap);
    }

    public static void addKilnRecipe(Block inputBlock, ItemStack... output) {
        KilnInteraction.addBlockRecipe(inputBlock, output);
    }

    public static void addKilnRecipe(Block inputBlock, int inputMeta, ItemStack... output) {
        KilnInteraction.addBlockRecipe(inputBlock, inputMeta, output);
    }
}
