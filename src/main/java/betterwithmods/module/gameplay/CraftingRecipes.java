package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.*;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.CuttingRecipe;
import betterwithmods.common.registry.DyeWithTagRecipe;
import betterwithmods.module.Feature;
import betterwithmods.util.RecipeUtils;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by tyler on 5/16/17.
 */
public class CraftingRecipes extends Feature {
    public CraftingRecipes() {
        canDisable = false;
    }

    @Override
    public void init(FMLInitializationEvent event) {

        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.LIGHT), " P ", "PFP", " R ", 'P', "paneGlass", 'R', "dustRedstone", 'F', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FILAMENT));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.SAW), "III", "GBG", "WGW", 'I', "ingotIron", 'G', "gearWood", 'W', "plankWood", 'B', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 5), "XXX", "XXX", "XXX", 'X', new ItemStack(Items.FLINT));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 4), "XXX", "XXX", "XXX", 'X', new ItemStack(BWMBlocks.ROPE));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 3), "XXX", "XXX", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE));

        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.LENS), "GDG", "G G", "GLG", 'G', "ingotGold", 'D', "gemDiamond", 'L', "blockGlass");
        RecipeUtils.addOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 2), "XXX", "X X", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WINDMILL_BLADE));
        RecipeUtils.addOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 1), "XXX", "X X", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WOOD_BLADE));
        RecipeUtils.addOreRecipe(new ItemStack(BWMItems.WINDMILL, 1, 0), " X ", "X X", " X ", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WINDMILL_BLADE));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.ANCHOR), " I ", "SSS", 'S', "stone", 'I', "ingotIron");
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.HIBACHI), "HHH", "SES", "SRS", 'S', "stone", 'R', "dustRedstone", 'H', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE), 'E', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ELEMENT));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.BELLOWS), "WWW", "LLL", "BGB", 'W', "sidingWood", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT), 'B', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT), 'G', "gearWood");
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.GEARBOX), "PGP", "GLG", "PGP", 'P', "plankWood", 'G', "gearWood", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.AXLE), "X", "R", "X", 'X', "plankWood", 'R', new ItemStack(BWMBlocks.ROPE));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.HAND_CRANK), "  X", " X ", "SWS", 'X', "stickWood", 'S', "cobblestone", 'W', "gearWood").setMirrored(true);
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.SLATS, 4, 0), "SS", "SS", 'S', "sidingWood");
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.GRATE, 4, 0), "WSW", "WSW", 'S', "stickWood", 'W', "mouldingWood");
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.PANE, 4, 2), "RRR", "RRR", 'R', Items.REEDS);
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, BlockMechMachines.EnumType.PULLEY.getMeta() << 1), "PIP", "GLG", "PIP", 'P', "plankWood", 'I', "ingotIron", 'G', "gearWood", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.ROPE), "XX", "XX", "XX", 'X', "fiberHemp");
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.PLATFORM), "MWM", " M ", "MWM", 'M', "plankWood", 'W', new ItemStack(BWMBlocks.PANE, 1, 2));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.MINING_CHARGE), "RSR", "DDD", "DDD", 'R', BWMBlocks.ROPE, 'S', "slimeball", 'D', BWMItems.DYNAMITE);
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.PUMP), "xGx", "SsS", "SgS", 'x', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE), 'G', new ItemStack(BWMBlocks.GRATE, 1, 32767), 'S', "sidingWood", 's', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCREW), 'g', "gearWood");
        RecipeUtils.addOreRecipe(new ItemStack(BWMItems.ENDER_SPECTACLES), "OSO", 'O', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ENDER_OCULAR), 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP));
        RecipeUtils.addOreRecipe(new ItemStack(BWMItems.BREEDING_HARNESS), "SLS", "LLL", "SLS", 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP), 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER));
        RecipeUtils.addOreRecipe(new ItemStack(BWMItems.COMPOSITE_BOW), "GMB", "MBS", "GMB", 'G', "slimeball", 'M', "mouldingWood", 'B', "bone", 'S', "string");
        RecipeUtils.addOreRecipe(new ItemStack(BWMItems.BROADHEAD_ARROW), "B", "S", "F", 'B', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BROADHEAD), 'S', "stickWood", 'F', "feather");
        RecipeUtils.addOreRecipe(new ItemStack(Items.CHAINMAIL_HELMET), "CCC", "C C", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL));
        RecipeUtils.addOreRecipe(new ItemStack(Items.CHAINMAIL_CHESTPLATE), "C C", "CCC", "CCC", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL));
        RecipeUtils.addOreRecipe(new ItemStack(Items.CHAINMAIL_LEGGINGS), "CCC", "C C", "C C", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL));
        RecipeUtils.addOreRecipe(new ItemStack(Items.CHAINMAIL_BOOTS), "C C", "C C", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.VINE_TRAP, 1), "VVV", 'V', new ItemStack(Blocks.VINE));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.STEEL_ANVIL), "SSS", " S ", "SSS", 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL));
        RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.ADVANCED_BELLOWS), " C ", "SBS", " G ", 'B', BWMBlocks.BELLOWS, 'C', Items.CLOCK, 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.STEEL_SPRING), 'G', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.STEEL_GEAR));


        //ItemMaterial
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL), "III", "III", "III", 'I', "nuggetSoulforgedSteel");
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT), "S", "G", "X", 'S', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP), 'G', "slimeball", 'X', "mouldingWood");
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR, 2), "SWS", "W W", "SWS", 'S', "stickWood", 'W', "plankWood");
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH), "GGG", " R ", 'G', "nuggetGold", 'R', "dustRedstone");
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SHARPENING_STONE), "X ", " X", 'X', Items.FLINT).setMirrored(true);
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.KNIFE_BLADE), "I ", " X", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SHARPENING_STONE), 'I', "ingotIron").setMirrored(true);
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCREW), "II ", " II", "II ", 'I', "ingotIron");
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ENDER_OCULAR), "GGG", "GEG", "GGG", 'G', "nuggetGold", 'E', "enderpearl");
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WOOD_BLADE), "W  ", "WGW", "W  ", 'G', "slimeball", 'W', BWMBlocks.WOOD_SIDING).setMirrored(true);
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_CLOTH), "XXX", "XXX", "XXX", 'X', "fiberHemp");
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WINDMILL_BLADE), "CCC", "CCC", "WWW", 'W', "slabWood", 'C', "fabricHemp");
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT), " L ", "L L", " L ", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP));
        RecipeUtils.addOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), "NNN", "NNN", "NNN", 'N', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_NUGGET));


        //Type Blocks
        RecipeUtils.addOreRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.WICKER), "XX", "XX", 'X', new ItemStack(BWMBlocks.PANE, 1, BlockBWMPane.EnumPaneType.WICKER.getMeta()));
        RecipeUtils.addOreRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.DUNG), "XXX", "XXX", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG));
        RecipeUtils.addOreRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.PADDING), "XXX", "XXX", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.PADDING));
        RecipeUtils.addOreRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.SOAP), "XXX", "XXX", "XXX", 'X', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOAP));
        RecipeUtils.addOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), "SSS", "MEM", "FFF", 'S', Items.SUGAR, 'M', Items.MILK_BUCKET, 'E', BWMItems.RAW_EGG, 'F', "foodFlour");
        RecipeUtils.addOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), "FCF", 'F', "foodFlour", 'C', "foodChocolatebar");
        RecipeUtils.addOreRecipe(BlockMechMachines.getStack(BlockMechMachines.EnumType.TURNTABLE), "LLL", "SCS", "SWS", 'L', "sidingWood", 'S', "stone", 'W', "gearWood", 'C', Items.CLOCK);
        RecipeUtils.addOreRecipe(BlockMechMachines.getStack(BlockMechMachines.EnumType.HOPPER), "S S", "GPG", " C ", 'C', "cornerWood", 'S', "sidingWood", 'G', "gearWood", 'P', Blocks.WOODEN_PRESSURE_PLATE);
        RecipeUtils.addOreRecipe(BlockMechMachines.getStack(BlockMechMachines.EnumType.MILL), "XWX", "XXX", "XXX", 'X', "stone", 'W', "gearWood");
        RecipeUtils.addOreRecipe(BlockCookingPot.getStack(BlockCookingPot.EnumType.CAULDRON), "XBX", "XWX", "XXX", 'X', "ingotCopper", 'B', Items.BONE, 'W', Items.WATER_BUCKET);
        RecipeUtils.addOreRecipe(BlockCookingPot.getStack(BlockCookingPot.EnumType.CAULDRON), "XBX", "XWX", "XXX", 'X', "ingotIron", 'B', Items.BONE, 'W', Items.WATER_BUCKET);

        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            int meta = type.getMetadata();
            RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.GRATE, 4, meta), "WSW", "WSW", 'S', "stickWood", 'W', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta));
            RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.SLATS, 4, meta), "SS", "SS", 'S', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta));
            RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.BAMBOO_CHIME, 1, meta), " S ", "SPS", "BMB", 'S', "string", 'P', Blocks.WOODEN_PRESSURE_PLATE, 'B', "sugarcane", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta));
            RecipeUtils.addOreRecipe(new ItemStack(BWMBlocks.METAL_CHIME, 1, meta), " S ", "SPS", "IMI", 'S', "string", 'P', Blocks.WOODEN_PRESSURE_PLATE, 'I', "ingotIron", 'M', new ItemStack(BWMBlocks.WOOD_MOULDING, 1, meta));
        }

        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMBlocks.GEARBOX), new ItemStack(BWMBlocks.BROKEN_GEARBOX), "gearWood", "gearWood");
        RecipeUtils.addShapelessOreRecipe(new ItemStack(Items.STRING), "fiberHemp", "fiberHemp", "fiberHemp");
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMBlocks.BOOSTER), Blocks.RAIL, BWMBlocks.ROPE, "gearWood");
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMBlocks.URN, 1, 9), new ItemStack(BWMBlocks.URN, 1, 8), new ItemStack(Items.ENDER_EYE), "obsidian");
        RecipeUtils.addShapelessOreRecipe(new ItemStack(Items.FLINT, 9, 0), new ItemStack(BWMBlocks.AESTHETIC, 1, 5));
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMBlocks.ROPE, 9, 0), new ItemStack(BWMBlocks.AESTHETIC, 1, 4));
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMItems.DYNAMITE), "paper", "paper", "paper", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FUSE), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL), "dustWood");
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMBlocks.AESTHETIC, 1, 8), "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl", "enderpearl");
        RecipeUtils.addShapelessOreRecipe(new ItemStack(Items.ENDER_PEARL, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, 8));
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMItems.RAW_SCRAMBLED_EGG, 2), BWMItems.RAW_EGG, Items.MILK_BUCKET);
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMItems.RAW_OMELET, 2), BWMItems.RAW_EGG, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM);
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMItems.HAM_AND_EGGS, 2), BWMItems.COOKED_EGG, Items.COOKED_PORKCHOP);
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMItems.BEEF_DINNER, 3), Items.COOKED_BEEF, Items.CARROT, Items.BAKED_POTATO);
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMItems.BEEF_POTATOES, 2), Items.COOKED_BEEF, Items.BAKED_POTATO);
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMItems.PORK_DINNER, 3), Items.COOKED_PORKCHOP, Items.CARROT, Items.BAKED_POTATO);
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMItems.RAW_KEBAB, 3), Blocks.BROWN_MUSHROOM, Items.CARROT, Items.MUTTON, Items.STICK);
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMItems.TASTY_SANDWICH, 2), Items.BREAD, "listAllmeatcooked");
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMItems.MANUAL), "gearWood", Items.BOOK);

        RecipeUtils.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_FIBERS, 6), BWMBlocks.ROPE);
        RecipeUtils.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NUGGET_STEEL, 9), "ingotSoulforgedSteel");
        RecipeUtils.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.PADDING), new ItemStack(Items.FEATHER), "fabricHemp");
        RecipeUtils.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), "gemDiamond", "ingotIron", BWMItems.CREEPER_OYSTER);
        RecipeUtils.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_NUGGET, 9), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT));
        RecipeUtils.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, 3));
        RecipeUtils.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.DUNG.getMeta()));
        RecipeUtils.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.PADDING, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.PADDING.getMeta()));
        RecipeUtils.addShapelessOreRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOAP, 9), new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.SOAP.getMeta()));
        RecipeUtils.addShapelessOreRecipe(new ItemStack(BWMBlocks.PANE, 4, BlockBWMPane.EnumPaneType.WICKER.getMeta()), new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.WICKER.getMeta()));
        RecipeUtils.addShapelessOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), Blocks.PUMPKIN, Items.SUGAR, BWMItems.RAW_EGG, "foodFlour");
        RecipeUtils.addShapelessOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.APPLE), Items.APPLE, Items.SUGAR, BWMItems.RAW_EGG, "foodFlour");
        RecipeUtils.addShapelessOreRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FLOUR));


        RecipeUtils.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP, 4), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT)));
        RecipeUtils.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_CUT, 2), new ItemStack(Items.LEATHER)));
        RecipeUtils.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT, 2), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER)));
        RecipeUtils.addRecipe(new CuttingRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT, 2), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER)));
        String[] dyes = new String[]{"White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black",};
        for (int i = 0; i < dyes.length; i++) {
            RecipeUtils.addRecipe(new DyeWithTagRecipe(new ItemStack(BWMBlocks.VASE, 1, i), new ItemStack(BWMBlocks.VASE, 1, OreDictionary.WILDCARD_VALUE), "dye" + dyes[i]));
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



        RecipeUtils.removeFurnaceRecipe(new ItemStack(Items.NETHERBRICK));
    }

    @Override
    public String getFeatureDescription() {
        return "Adds basic crafting recipes";
    }
}
