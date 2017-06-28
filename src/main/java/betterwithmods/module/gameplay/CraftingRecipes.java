package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockCookingPot;
import betterwithmods.common.blocks.BlockMechMachines;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.CuttingRecipe;
import betterwithmods.module.Feature;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by tyler on 5/16/17.
 */
public class CraftingRecipes extends Feature {
    public CraftingRecipes() {
        canDisable = false;
    }

    @Override
    public void init(FMLInitializationEvent event) {

        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.LIGHT), " P ", "PFP", " R ", 'P', "paneGlass", 'R', "dustRedstone", 'F', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FILAMENT));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.SAW), "III", "GBG", "WGW", 'I', "ingotIron", 'G', "gearWood", 'W', "plankWood", 'B', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 5), "XXX", "XXX", "XXX", 'X', new ItemStack(Items.FLINT));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 4), "XXX", "XXX", "XXX", 'X', new ItemStack(BWMBlocks.ROPE));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 3), "XXX", "XXX", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE));

        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.LENS), "GDG", "G G", "GLG", 'G', "ingotGold", 'D', "gemDiamond", 'L', "blockGlass");
        BWMRecipes.addOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 2), "XXX", "X X", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WINDMILL_BLADE));
        BWMRecipes.addOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 1), "XXX", "X X", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WOOD_BLADE));
        BWMRecipes.addOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 0), " X ", "X X", " X ", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WINDMILL_BLADE));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.ANCHOR), " I ", "SSS", 'S', "stone", 'I', "ingotIron");
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.HIBACHI), "HHH", "SES", "SRS", 'S', "stone", 'R', "dustRedstone", 'H', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE), 'E', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ELEMENT));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.BELLOWS), "WWW", "LLL", "BGB", 'W', "sidingWood", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT), 'B', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT), 'G', "gearWood");
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.GEARBOX), "PGP", "GLG", "PGP", 'P', "plankWood", 'G', "gearWood", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.AXLE), "X", "R", "X", 'X', "plankWood", 'R', new ItemStack(BWMBlocks.ROPE));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.HAND_CRANK), "  X", " X ", "SWS", 'X', "stickWood", 'S', "cobblestone", 'W', "gearWood");
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.SLATS, 4, 0), "SS", "SS", 'S', "sidingWood");
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.GRATE, 4, 0), "WSW", "WSW", 'S', "stickWood", 'W', "mouldingWood");
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.WICKER, 4, 2), "RRR", "RRR", 'R', Items.REEDS);
        BWMRecipes.addOreRecipe(BlockMechMachines.getStack(BlockMechMachines.EnumType.PULLEY), "PIP", "GLG", "PIP", 'P', "plankWood", 'I', "ingotIron", 'G', "gearWood", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.ROPE), "XX", "XX", "XX", 'X', "fiberHemp");
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.PLATFORM), "MWM", " M ", "MWM", 'M', "plankWood", 'W', new ItemStack(BWMBlocks.WICKER, 1, 2));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.MINING_CHARGE), "RSR", "DDD", "DDD", 'R', BWMBlocks.ROPE, 'S', "slimeball", 'D', BWMItems.DYNAMITE);
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.PUMP), "xGx", "SsS", "SgS", 'x', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE), 'G', new ItemStack(BWMBlocks.GRATE, 1, 32767), 'S', "sidingWood", 's', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCREW), 'g', "gearWood");
        BWMRecipes.addOreRecipe(new ItemStack(BWMItems.ENDER_SPECTACLES), "OSO", 'O', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ENDER_OCULAR), 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP));
        BWMRecipes.addOreRecipe(new ItemStack(BWMItems.BREEDING_HARNESS), "SLS", "LLL", "SLS", 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP), 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER));
        BWMRecipes.addOreRecipe(new ItemStack(BWMItems.COMPOSITE_BOW), "GMB", "MBS", "GMB", 'G', "slimeball", 'M', "mouldingWood", 'B', "bone", 'S', "string");
        BWMRecipes.addOreRecipe(new ItemStack(BWMItems.BROADHEAD_ARROW), "B", "S", "F", 'B', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BROADHEAD), 'S', "stickWood", 'F', "feather");
        BWMRecipes.addOreRecipe(new ItemStack(Items.CHAINMAIL_HELMET), "CCC", "C C", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL));
        BWMRecipes.addOreRecipe(new ItemStack(Items.CHAINMAIL_CHESTPLATE), "C C", "CCC", "CCC", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL));
        BWMRecipes.addOreRecipe(new ItemStack(Items.CHAINMAIL_LEGGINGS), "CCC", "C C", "C C", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL));
        BWMRecipes.addOreRecipe(new ItemStack(Items.CHAINMAIL_BOOTS), "C C", "C C", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.VINE_TRAP, 1), "VVV", 'V', new ItemStack(Blocks.VINE));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.STEEL_ANVIL), "SSS", " S ", "SSS", 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL));
        BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.ADVANCED_BELLOWS), " C ", "SBS", " G ", 'B', BWMBlocks.BELLOWS, 'C', Items.CLOCK, 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.STEEL_SPRING), 'G', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.STEEL_GEAR));


        //ItemMaterial
        BWMRecipes.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL), "III", "III", "III", 'I', "nuggetSoulforgedSteel");
        BWMRecipes.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT), "S", "G", "X", 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP), 'G', "slimeball", 'X', "mouldingWood");
        BWMRecipes.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR, 2), "SWS", "W W", "SWS", 'S', "stickWood", 'W', "plankWood");
        BWMRecipes.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH), "GGG", " R ", 'G', "nuggetGold", 'R', "dustRedstone");
        BWMRecipes.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SHARPENING_STONE), "X ", " X", 'X', Items.FLINT);
        BWMRecipes.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCREW), "II ", " II", "II ", 'I', "ingotIron");
        BWMRecipes.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ENDER_OCULAR), "GGG", "GEG", "GGG", 'G', "nuggetGold", 'E', "enderpearl");
        BWMRecipes.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WOOD_BLADE), "W  ", "WGW", "W  ", 'G', "slimeball", 'W', BWMBlocks.WOOD_SIDING);
        BWMRecipes.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_CLOTH), "XXX", "XXX", "XXX", 'X', "fiberHemp");
        BWMRecipes.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WINDMILL_BLADE), "CCC", "CCC", "WWW", 'W', "slabWood", 'C', "fabricHemp");
        BWMRecipes.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT), " L ", "L L", " L ", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP));
        BWMRecipes.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), "NNN", "NNN", "NNN", 'N', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_NUGGET));


        //Type Blocks
        BWMRecipes.addOreRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.PADDING), "XXX", "XXX", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.PADDING));
        BWMRecipes.addOreRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.SOAP), "XXX", "XXX", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOAP));
        BWMRecipes.addOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), "SSS", "MEM", "FFF", 'S', Items.SUGAR, 'M', Items.MILK_BUCKET, 'E', BWMItems.RAW_EGG, 'F', "foodFlour");
        BWMRecipes.addOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), "FCF", 'F', "foodFlour", 'C', "foodChocolatebar");
        BWMRecipes.addOreRecipe(BlockMechMachines.getStack(BlockMechMachines.EnumType.TURNTABLE), "LLL", "SCS", "SWS", 'L', "sidingWood", 'S', "stone", 'W', "gearWood", 'C', Items.CLOCK);
        BWMRecipes.addOreRecipe(BlockMechMachines.getStack(BlockMechMachines.EnumType.HOPPER), "S S", "GPG", " C ", 'C', "cornerWood", 'S', "sidingWood", 'G', "gearWood", 'P', Blocks.WOODEN_PRESSURE_PLATE);
        BWMRecipes.addOreRecipe(BlockMechMachines.getStack(BlockMechMachines.EnumType.MILL), "XWX", "XXX", "XXX", 'X', "stone", 'W', "gearWood");
        BWMRecipes.addOreRecipe(BlockCookingPot.getStack(BlockCookingPot.EnumType.CAULDRON), "XBX", "XWX", "XXX", 'X', "ingotCopper", 'B', Items.BONE, 'W', Items.WATER_BUCKET);
        BWMRecipes.addOreRecipe(BlockCookingPot.getStack(BlockCookingPot.EnumType.CAULDRON), "XBX", "XWX", "XXX", 'X', "ingotIron", 'B', Items.BONE, 'W', Items.WATER_BUCKET);

        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            int meta = type.getMetadata();
            BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.GRATE, 4, meta), "WSW", "WSW", 'S', "stickWood", 'W', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta));
            BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.SLATS, 4, meta), "SS", "SS", 'S', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta));
            BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.BAMBOO_CHIME, 1, meta), " S ", "SPS", "BMB", 'S', "string", 'P', Blocks.WOODEN_PRESSURE_PLATE, 'B', "sugarcane", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta));
            BWMRecipes.addOreRecipe(new ItemStack(BWMBlocks.METAL_CHIME, 1, meta), " S ", "SPS", "IMI", 'S', "string", 'P', Blocks.WOODEN_PRESSURE_PLATE, 'I', "ingotIron", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta));
        }

        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMBlocks.GEARBOX), new ItemStack(BWMBlocks.BROKEN_GEARBOX), "gearWood", "gearWood");
        BWMRecipes.addShapelessOreRecipe(new ItemStack(Items.STRING), "fiberHemp", "fiberHemp", "fiberHemp");
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMBlocks.BOOSTER), Blocks.RAIL, BWMBlocks.ROPE, "gearWood");
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMBlocks.URN, 1, 9), new ItemStack(BWMBlocks.URN, 1, 8), new ItemStack(Items.ENDER_EYE), "obsidian");
        BWMRecipes.addShapelessOreRecipe(new ItemStack(Items.FLINT, 9, 0), new ItemStack(BWMBlocks.AESTHETIC, 1, 5));
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMBlocks.ROPE, 9, 0), new ItemStack(BWMBlocks.AESTHETIC, 1, 4));
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMItems.DYNAMITE), "paper", "paper", "paper", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FUSE), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL), "dustWood");
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 8), "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl");
        BWMRecipes.addShapelessOreRecipe(new ItemStack(Items.ENDER_PEARL, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, 8));
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMItems.RAW_SCRAMBLED_EGG, 2), BWMItems.RAW_EGG, Items.MILK_BUCKET);
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMItems.RAW_OMELET, 2), BWMItems.RAW_EGG, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM);
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMItems.HAM_AND_EGGS, 2), BWMItems.COOKED_EGG, Items.COOKED_PORKCHOP);
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMItems.BEEF_DINNER, 3), Items.COOKED_BEEF, Items.CARROT, Items.BAKED_POTATO);
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMItems.BEEF_POTATOES, 2), Items.COOKED_BEEF, Items.BAKED_POTATO);
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMItems.PORK_DINNER, 3), Items.COOKED_PORKCHOP, Items.CARROT, Items.BAKED_POTATO);
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMItems.RAW_KEBAB, 3), Blocks.BROWN_MUSHROOM, Items.CARROT, Items.MUTTON, Items.STICK);
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMItems.TASTY_SANDWICH, 2), Items.BREAD, "listAllmeatcooked");
        BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMItems.MANUAL), "gearWood", Items.BOOK);

        BWMRecipes.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_FIBERS, 6), BWMBlocks.ROPE);
        BWMRecipes.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NUGGET_STEEL, 9), "ingotSoulforgedSteel");
        BWMRecipes.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.PADDING), new ItemStack(Items.FEATHER), "fabricHemp");
        BWMRecipes.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), "gemDiamond", "ingotIron", BWMItems.CREEPER_OYSTER);
        BWMRecipes.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_NUGGET, 9), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT));
        BWMRecipes.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, 3));
        BWMRecipes.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.PADDING, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.PADDING.getMeta()));
        BWMRecipes.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOAP, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.SOAP.getMeta()));
        BWMRecipes.addShapelessOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), Blocks.PUMPKIN, Items.SUGAR, BWMItems.RAW_EGG, "foodFlour");
        BWMRecipes.addShapelessOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.APPLE), Items.APPLE, Items.SUGAR, BWMItems.RAW_EGG, "foodFlour");

        BWMRecipes.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP, 4), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT)));
        BWMRecipes.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_CUT, 2), new ItemStack(Items.LEATHER)));
        BWMRecipes.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT, 2), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER)));
        BWMRecipes.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT, 2), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER)));
        String[] dyes = new String[]{"White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black",};
        for (int i = 0; i < dyes.length; i++) {
            BWMRecipes.addShapelessOreRecipe(new ItemStack(BWMBlocks.VASE, 1, i), "blockVase", "dye" + dyes[i]);
        }

        GameRegistry.addSmelting(BWMItems.RAW_EGG, new ItemStack(BWMItems.COOKED_EGG), 0.1F);
        GameRegistry.addSmelting(BWMItems.RAW_SCRAMBLED_EGG, new ItemStack(BWMItems.COOKED_SCRAMBLED_EGG), 0.1F);
        GameRegistry.addSmelting(BWMItems.RAW_OMELET, new ItemStack(BWMItems.COOKED_OMELET), 0.1F);
        GameRegistry.addSmelting(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE), new ItemStack(Items.NETHERBRICK), 0.2F);
        GameRegistry.addSmelting(new ItemStack(BWMBlocks.AESTHETIC, 1, 7), new ItemStack(BWMBlocks.AESTHETIC, 1, 6), 0.1F);
        GameRegistry.addSmelting(BWMItems.WOLF_CHOP, new ItemStack(BWMItems.COOKED_WOLF_CHOP), 0.5f);
        GameRegistry.addSmelting(BWMItems.MYSTERY_MEAT, new ItemStack(BWMItems.COOKED_MYSTERY_MEAT), 0.5f);
        GameRegistry.addSmelting(BWMItems.RAW_KEBAB, new ItemStack(BWMItems.COOKED_KEBAB), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), new ItemStack(Items.CAKE), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new ItemStack(Items.BREAD), 0.1F);


        BWMRecipes.removeFurnaceRecipe(new ItemStack(Items.NETHERBRICK));
    }

    @Override
    public String getFeatureDescription() {
        return "Adds basic crafting recipes";
    }
}
