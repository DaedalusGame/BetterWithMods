package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockCookingPot;
import betterwithmods.common.blocks.BlockMechMachines;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.CuttingRecipe;
import betterwithmods.common.registry.DyeWithTagRecipe;
import betterwithmods.module.Feature;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Created by tyler on 5/16/17.
 */
public class CraftingRecipes extends Feature {
    public CraftingRecipes() {
        canDisable = false;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.GEARBOX), new ItemStack(BWMBlocks.BROKEN_GEARBOX), "gearWood", "gearWood"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.STRING), "fiberHemp", "fiberHemp", "fiberHemp"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.LIGHT), " P ", "PFP", " R ", 'P', "paneGlass", 'R', "dustRedstone", 'F', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FILAMENT)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.BOOSTER), Blocks.RAIL, BWMBlocks.ROPE, "gearWood"));
        GameRegistry.addShapelessRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_FIBERS, 6), BWMBlocks.ROPE);
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NUGGET_STEEL, 9), "ingotSoulforgedSteel"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL), "III", "III", "III", 'I', "nuggetSoulforgedSteel"));
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.BAMBOO_CHIME, 1, type.getMetadata()), " S ", "SPS", "BMB", 'S', "string", 'P', Blocks.WOODEN_PRESSURE_PLATE, 'B', "sugarcane", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata())));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.METAL_CHIME, 1, type.getMetadata()), " S ", "SPS", "IMI", 'S', "string", 'P', Blocks.WOODEN_PRESSURE_PLATE, 'I', "ingotIron", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, type.getMetadata())));
        }
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.URN, 1, 9), new ItemStack(BWMBlocks.URN, 1, 8), new ItemStack(Items.ENDER_EYE), "obsidian"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SAW), "III", "GBG", "WGW", 'I', "ingotIron", 'G', "gearWood", 'W', "plankWood", 'B', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.FLINT, 9, 0), new ItemStack(BWMBlocks.AESTHETIC, 1, 5)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 5), "XXX", "XXX", "XXX", 'X', new ItemStack(Items.FLINT)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.ROPE, 9, 0), new ItemStack(BWMBlocks.AESTHETIC, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 4), "XXX", "XXX", "XXX", 'X', new ItemStack(BWMBlocks.ROPE)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 3), "XXX", "XXX", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMItems.DYNAMITE), "paper", "paper", "paper", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FUSE), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL), "dustWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.LENS), "GDG", "G G", "GLG", 'G', "ingotGold", 'D', "gemDiamond", 'L', "blockGlass"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 2), "XXX", "X X", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WINDMILL_BLADE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 1), "XXX", "X X", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WOOD_BLADE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 0), " X ", "X X", " X ", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WINDMILL_BLADE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WOOD_BLADE), "W  ", "WGW", "W  ", 'G', "slimeball", 'W', BWMBlocks.WOOD_SIDING).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_CLOTH), "XXX", "XXX", "XXX", 'X', "fiberHemp"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WINDMILL_BLADE), "CCC", "CCC", "WWW", 'W', "slabWood", 'C', "fabricHemp"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.ANCHOR), " I ", "SSS", 'S', "stone", 'I', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.HIBACHI), "HHH", "SES", "SRS", 'S', "stone", 'R', "dustRedstone", 'H', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE), 'E', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ELEMENT)));
        GameRegistry.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP, 4), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT), " L ", "L L", " L ", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.BELLOWS), "WWW", "LLL", "BGB", 'W', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT), 'B', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT), 'G', "gearWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GEARBOX), "PGP", "GLG", "PGP", 'P', "plankWood", 'G', "gearWood", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.AXLE), "X", "R", "X", 'X', "plankWood", 'R', new ItemStack(BWMBlocks.ROPE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.HAND_CRANK), "  X", " X ", "SWS", 'X', "stickWood", 'S', "cobblestone", 'W', "gearWood").setMirrored(true));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, BlockMechMachines.EnumType.TURNTABLE.getMeta() << 1), "LLL", "SCS", "SWS", 'L', new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE), 'S', "stone", 'W', "gearWood", 'C', Items.CLOCK));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, BlockMechMachines.EnumType.HOPPER.getMeta() << 1), "S S", "GPG", " C ", 'C', BWMBlocks.WOOD_CORNER, 'S', BWMBlocks.WOOD_SIDING, 'G', "gearWood", 'P', Blocks.WOODEN_PRESSURE_PLATE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, BlockMechMachines.EnumType.MILL.getMeta() << 1), "XWX", "XXX", "XXX", 'X', "stone", 'W', "gearWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, BlockMechMachines.EnumType.PULLEY.getMeta() << 1), "PIP", "GLG", "PIP", 'P', "plankWood", 'I', "ingotIron", 'G', "gearWood", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.COOKING_POTS, 1, BlockCookingPot.EnumType.CAULDRON.getMeta()), "XBX", "XWX", "XXX", 'X', "ingotCopper", 'B', Items.BONE, 'W', Items.WATER_BUCKET));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.COOKING_POTS, 1, BlockCookingPot.EnumType.CAULDRON.getMeta()), "XBX", "XWX", "XXX", 'X', "ingotIron", 'B', Items.BONE, 'W', Items.WATER_BUCKET));
        GameRegistry.addSmelting(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE), new ItemStack(Items.NETHERBRICK), 0.2F);
        GameRegistry.addSmelting(new ItemStack(BWMBlocks.AESTHETIC, 1, 7), new ItemStack(BWMBlocks.AESTHETIC, 1, 6), 0.1F);
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT), "S", "G", "X", 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP), 'G', "slimeball", 'X', BWMBlocks.WOOD_MOULDING));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR, 2), "SWS", "W W", "SWS", 'S', "stickWood", 'W', "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.SLATS, 4, 0), "SS", "SS", 'S', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE)));
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            int meta = type.getMetadata();
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GRATE, 4, meta), "WSW", "WSW", 'S', "stickWood", 'W', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta)));
            ItemStack moulding = new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta);
            GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.SLATS, 4, meta), "SS", "SS", 'S', moulding);
        }
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.GRATE, 4, 0), "WSW", "WSW", 'S', "stickWood", 'W', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PANE, 4, 2), "RRR", "RRR", 'R', Items.REEDS));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH), "GGG", " R ", 'G', "nuggetGold", 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.ROPE), "XX", "XX", "XX", 'X', "fiberHemp"));
        GameRegistry.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_CUT, 2), new ItemStack(Items.LEATHER)));
        GameRegistry.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT, 2), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER)));
        GameRegistry.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT, 2), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SHARPENING_STONE), "X ", " X", 'X', Items.FLINT).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.KNIFE_BLADE), "I ", " X", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SHARPENING_STONE), 'I', "ingotIron").setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.KNIFE), "I ", " X", 'X', "stickWood", 'I', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.KNIFE_BLADE)).setMirrored(true));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PLATFORM), "MWM", " M ", "MWM", 'M', "plankWood", 'W', new ItemStack(BWMBlocks.PANE, 1, 2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.MINING_CHARGE), "RSR", "DDD", "DDD", 'R', BWMBlocks.ROPE, 'S', "slimeball", 'D', BWMItems.DYNAMITE));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 8), "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl"));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.ENDER_PEARL, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, 8));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCREW), "II ", " II", "II ", 'I', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.PUMP), "xGx", "SsS", "SgS", 'x', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE), 'G', new ItemStack(BWMBlocks.GRATE, 1, 32767), 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, 32767), 's', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCREW), 'g', "gearWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ENDER_OCULAR), "GGG", "GEG", "GGG", 'G', "nuggetGold", 'E', "enderpearl"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.ENDER_SPECTACLES), "OSO", 'O', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ENDER_OCULAR), 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP)));
        String[] dyes = {"White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};

        for (int i = 0; i < 16; i++) {
            GameRegistry.addRecipe(new DyeWithTagRecipe(new ItemStack(BWMBlocks.VASE, 1, i), new ItemStack(BWMBlocks.VASE, 1, OreDictionary.WILDCARD_VALUE), "dye" + dyes[i]));
        }
        GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.VINE_TRAP, 1), "VVV", 'V', new ItemStack(Blocks.VINE));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.PADDING), new ItemStack(Items.FEATHER), "fabricHemp"));
        GameRegistry.addRecipe(new ShapedOreRecipe(BWMItems.BREEDING_HARNESS, "SLS", "LLL", "SLS", 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP), 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER)));
        GameRegistry.addSmelting(BWMItems.RAW_EGG, new ItemStack(BWMItems.COOKED_EGG), 0.1F);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.RAW_SCRAMBLED_EGG, 2), BWMItems.RAW_EGG, Items.MILK_BUCKET);
        GameRegistry.addSmelting(BWMItems.RAW_SCRAMBLED_EGG, new ItemStack(BWMItems.COOKED_SCRAMBLED_EGG), 0.1F);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.RAW_OMELET, 2), BWMItems.RAW_EGG, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM);
        GameRegistry.addSmelting(BWMItems.RAW_OMELET, new ItemStack(BWMItems.COOKED_OMELET), 0.1F);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.HAM_AND_EGGS, 2), BWMItems.COOKED_EGG, Items.COOKED_PORKCHOP);
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BWMItems.TASTY_SANDWICH, 2), Items.BREAD, "listAllmeatcooked"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.COMPOSITE_BOW), "GMB", "MBS", "GMB", 'G', "slimeball", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE), 'B', "bone", 'S', "string"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMItems.BROADHEAD_ARROW), "B", "S", "F", 'B', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BROADHEAD), 'S', "stickWood", 'F', "feather"));
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.BEEF_DINNER, 3), Items.COOKED_BEEF, Items.CARROT, Items.BAKED_POTATO);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.BEEF_POTATOES, 2), Items.COOKED_BEEF, Items.BAKED_POTATO);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.BEEF_POTATOES, 2), Items.COOKED_BEEF, Items.BAKED_POTATO);
        GameRegistry.addSmelting(BWMItems.RAW_KEBAB, new ItemStack(BWMItems.COOKED_KEBAB), 0.1F);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.PORK_DINNER, 3), Items.COOKED_PORKCHOP, Items.CARROT, Items.BAKED_POTATO);
        GameRegistry.addShapelessRecipe(new ItemStack(BWMItems.RAW_KEBAB, 3), Blocks.BROWN_MUSHROOM, Items.CARROT, Items.MUTTON, Items.STICK);

        GameRegistry.addRecipe(new ShapedOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), "SSS", "MEM", "FFF", 'S', Items.SUGAR, 'M', Items.MILK_BUCKET, 'E', BWMItems.RAW_EGG, 'F', "foodFlour"));
        GameRegistry.addRecipe(new ShapedOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), "FCF", 'F', "foodFlour", 'C', "foodChocolatebar"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), Blocks.PUMPKIN, Items.SUGAR, BWMItems.RAW_EGG, "foodFlour"));
        GameRegistry.addShapelessRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FLOUR));

        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), new ItemStack(Items.CAKE), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new ItemStack(Items.BREAD), 0.1F);

        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), "gemDiamond", "ingotIron", BWMItems.CREEPER_OYSTER));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_NUGGET,9), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), "NNN","NNN","NNN", 'N',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_NUGGET)));
        GameRegistry.addRecipe(new ItemStack(Items.CHAINMAIL_HELMET), "CCC", "C C", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL));
        GameRegistry.addRecipe(new ItemStack(Items.CHAINMAIL_CHESTPLATE), "C C", "CCC", "CCC", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL));
        GameRegistry.addRecipe(new ItemStack(Items.CHAINMAIL_LEGGINGS), "CCC", "C C", "C C", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL));
        GameRegistry.addRecipe(new ItemStack(Items.CHAINMAIL_BOOTS), "C C", "C C", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL));
        GameRegistry.addShapedRecipe(new ItemStack(BWMBlocks.STEEL_ANVIL), "SSS", " S ", "SSS", 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWMBlocks.ADVANCED_BELLOWS), " C " , "SBS", " G ",  'B', BWMBlocks.BELLOWS, 'C', Items.CLOCK,'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.STEEL_SPRING), 'G', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.STEEL_GEAR)));
    }

    @Override
    public String getFeatureDescription() {
        return "Adds basic crafting recipes";
    }
}
