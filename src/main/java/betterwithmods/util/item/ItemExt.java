package betterwithmods.util.item;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMItems;
import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.items.ItemMaterial;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Set of methods dealing with Items and ItemStacks.
 *
 * @author Koward
 */
public final class ItemExt {
    private static final ItemStackMap<Float> buoyancy = new ItemStackMap<>(-1.0F);
    private static final ItemStackMap<Integer> weights = new ItemStackMap<>(0);

    private ItemExt() {
    }

    public static void initBuoyancy() {
        buoyancy.put("plankWood", 1.0F);
        buoyancy.put("treeSapling", 1.0F);
        buoyancy.put("treeLeaves", 1.0F);
        buoyancy.put(Blocks.JUKEBOX, 1.0F);
        buoyancy.put(Items.BED, 1.0F);
        buoyancy.put(Blocks.WEB, 1.0F);
        buoyancy.put(Blocks.TALLGRASS, 1.0F);
        buoyancy.put(Blocks.DEADBUSH, 1.0F);
        buoyancy.put(Blocks.WOOL, 1.0F);
        buoyancy.put(Blocks.YELLOW_FLOWER, 1.0F);
        buoyancy.put(Blocks.RED_FLOWER, 1.0F);
        buoyancy.put(Blocks.BROWN_MUSHROOM, 1.0F);
        buoyancy.put(Blocks.RED_MUSHROOM, 1.0F);
        buoyancy.put(Blocks.TNT, 1.0F);
        buoyancy.put(Blocks.BOOKSHELF, 1.0F);
        buoyancy.put("torch", 1.0F);
        buoyancy.put("stairWood", 1.0F);
        buoyancy.put("chest", 1.0F);
        buoyancy.put("workbench", 1.0F);
        buoyancy.put("cropWheat", 1.0F);
        buoyancy.put("cropPotato", 1.0F);
        buoyancy.put("cropCarrot", 1.0F);
        buoyancy.put("cropNetherWart", 1.0F);
        buoyancy.put(Items.SIGN, 1.0F);
        buoyancy.put(Items.ACACIA_DOOR, 1.0F);
        buoyancy.put(Items.BIRCH_DOOR, 1.0F);
        buoyancy.put(Items.DARK_OAK_DOOR, 1.0F);
        buoyancy.put(Items.JUNGLE_DOOR, 1.0F);
        buoyancy.put(Items.OAK_DOOR, 1.0F);
        buoyancy.put(Items.SPRUCE_DOOR, 1.0F);
        buoyancy.put(Blocks.LADDER, 1.0F);
        buoyancy.put(Blocks.WOODEN_PRESSURE_PLATE, 1.0F);
        buoyancy.put(Blocks.REDSTONE_TORCH, 1.0F);
        buoyancy.put(Blocks.SNOW_LAYER, 1.0F);
        buoyancy.put(Blocks.ICE, 1.0F);
        buoyancy.put(Blocks.SNOW, 1.0F);
        buoyancy.put("blockCactus", 1.0F);
        buoyancy.put("sugarcane", 1.0F);
        buoyancy.put(Blocks.ACACIA_FENCE, 1.0F);
        buoyancy.put(Blocks.BIRCH_FENCE, 1.0F);
        buoyancy.put(Blocks.DARK_OAK_FENCE, 1.0F);
        buoyancy.put(Blocks.JUNGLE_FENCE, 1.0F);
        buoyancy.put(Blocks.OAK_FENCE, 1.0F);
        buoyancy.put(Blocks.SPRUCE_FENCE, 1.0F);
        buoyancy.put(Blocks.PUMPKIN, 1.0F);
        buoyancy.put(Blocks.LIT_PUMPKIN, 1.0F);
        buoyancy.put(Items.CAKE, 1.0F);
        buoyancy.put(Blocks.TRAPDOOR, 1.0F);
        buoyancy.put(Blocks.BROWN_MUSHROOM_BLOCK, 1.0F);
        buoyancy.put(Blocks.RED_MUSHROOM_BLOCK, 1.0F);
        buoyancy.put(Blocks.MELON_BLOCK, 1.0F);
        buoyancy.put("vine", 1.0F);
        buoyancy.put(Blocks.ACACIA_FENCE_GATE, 1.0F);
        buoyancy.put(Blocks.BIRCH_FENCE_GATE, 1.0F);
        buoyancy.put(Blocks.DARK_OAK_FENCE_GATE, 1.0F);
        buoyancy.put(Blocks.JUNGLE_FENCE_GATE, 1.0F);
        buoyancy.put(Blocks.OAK_FENCE_GATE, 1.0F);
        buoyancy.put(Blocks.SPRUCE_FENCE_GATE, 1.0F);
        buoyancy.put(Blocks.WATERLILY, 1.0F);
        buoyancy.put(Blocks.WOODEN_SLAB, 1.0F);
        buoyancy.put(Items.DYE, 1.0F);
        buoyancy.put(Blocks.WOODEN_BUTTON, 1.0F);
        buoyancy.put(BWMBlocks.AXLE, 1.0F);
        buoyancy.put(BWMBlocks.PUMP, 0.0F);
        buoyancy.put(BWMBlocks.WOOD_SIDING, 1.0F);
        buoyancy.put(BWMBlocks.WOOD_MOULDING, 1.0F);
        buoyancy.put(BWMBlocks.WOOD_CORNER, 1.0F);
        buoyancy.put(BWMBlocks.SINGLE_MACHINES, BlockMechMachines.EnumType.HOPPER.getMeta(), 1.0F);
        buoyancy.put(BWMBlocks.SAW, 1.0F);
        buoyancy.put(BWMBlocks.PLATFORM, 1.0F);
        buoyancy.put(BWMBlocks.SINGLE_MACHINES, BlockMechMachines.EnumType.PULLEY.getMeta(), 1.0F);
        buoyancy.put(BWMBlocks.WOLF, 1.0F);
        buoyancy.put(BWMBlocks.HEMP, 1.0F);
        buoyancy.put(BWMBlocks.ROPE, 1.0F);
        buoyancy.put(BWMBlocks.GEARBOX, 1.0F);
        buoyancy.put(BWMBlocks.BELLOWS, 1.0F);
        buoyancy.put(BWMBlocks.VASE, 1.0F);
        buoyancy.put(Items.APPLE, 1.0F);
        buoyancy.put(Items.BOW, 1.0F);
        buoyancy.put(Items.ARROW, 1.0F);
        buoyancy.put(Items.WOODEN_SWORD, OreDictionary.WILDCARD_VALUE, 1.0F);
        buoyancy.put(Items.WOODEN_SHOVEL, OreDictionary.WILDCARD_VALUE, 1.0F);
        buoyancy.put(Items.WOODEN_PICKAXE, OreDictionary.WILDCARD_VALUE, 1.0F);
        buoyancy.put(Items.WOODEN_AXE, OreDictionary.WILDCARD_VALUE, 1.0F);
        buoyancy.put("stick", 1.0F);
        buoyancy.put(Items.BOWL, 1.0F);
        buoyancy.put(Items.MUSHROOM_STEW, 1.0F);
        buoyancy.put(Items.BEETROOT_SOUP, 1.0F);
        buoyancy.put("feather", 1.0F);
        buoyancy.put(Items.WOODEN_HOE, OreDictionary.WILDCARD_VALUE, 1.0F);
        buoyancy.put(Items.BEETROOT_SEEDS, 1.0F);
        buoyancy.put(Items.MELON_SEEDS, 1.0F);
        buoyancy.put(Items.PUMPKIN_SEEDS, 1.0F);
        buoyancy.put(Items.WHEAT_SEEDS, 1.0F);
        buoyancy.put(Items.BREAD, 1.0F);
        buoyancy.put(Items.LEATHER_HELMET, OreDictionary.WILDCARD_VALUE, 1.0F);
        buoyancy.put(Items.LEATHER_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 1.0F);
        buoyancy.put(Items.LEATHER_LEGGINGS, OreDictionary.WILDCARD_VALUE, 1.0F);
        buoyancy.put(Items.LEATHER_BOOTS, OreDictionary.WILDCARD_VALUE, 1.0F);
        buoyancy.put(Items.PORKCHOP, 1.0F);
        buoyancy.put(Items.COOKED_PORKCHOP, 1.0F);
        buoyancy.put(Items.PAINTING, 1.0F);
        buoyancy.put(Items.SIGN, 1.0F);
        buoyancy.put(Items.SADDLE, 1.0F);
        buoyancy.put(Items.SNOWBALL, 1.0F);
        buoyancy.put(Items.BOAT, 1.0F);
        buoyancy.put(Items.ACACIA_BOAT, 1.0F);
        buoyancy.put(Items.BIRCH_BOAT, 1.0F);
        buoyancy.put(Items.DARK_OAK_BOAT, 1.0F);
        buoyancy.put(Items.JUNGLE_BOAT, 1.0F);
        buoyancy.put(Items.SPRUCE_BOAT, 1.0F);
        buoyancy.put("leather", 1.0F);
        buoyancy.put(Items.PAPER, 1.0F);
        buoyancy.put(Items.BOOK, 1.0F);
        buoyancy.put("slimeball", 1.0F);
        buoyancy.put(Items.FISHING_ROD, OreDictionary.WILDCARD_VALUE, 1.0F);
        buoyancy.put(Items.FISH, 1.0F);
        buoyancy.put(Items.COOKED_FISH, 1.0F);
        buoyancy.put("bone", 1.0F);
        buoyancy.put(Items.SUGAR, 1.0F);
        buoyancy.put(Items.COOKIE, 1.0F);
        buoyancy.put(Items.MAP, 1.0F);
        buoyancy.put(Items.MELON, 1.0F);
        buoyancy.put(Items.BEEF, 1.0F);
        buoyancy.put(Items.COOKED_BEEF, 1.0F);
        buoyancy.put(Items.CHICKEN, 1.0F);
        buoyancy.put(Items.COOKED_CHICKEN, 1.0F);
        buoyancy.put(Items.MUTTON, 1.0F);
        buoyancy.put(Items.COOKED_MUTTON, 1.0F);
        buoyancy.put(Items.ROTTEN_FLESH, 1.0F);
        buoyancy.put(Items.POTIONITEM, 1.0F);
        buoyancy.put(Items.GLASS_BOTTLE, 1.0F);
        buoyancy.put(Items.SPIDER_EYE, 1.0F);
        buoyancy.put(Items.FERMENTED_SPIDER_EYE, 1.0F);
        buoyancy.put(Items.MAGMA_CREAM, 1.0F);
        buoyancy.put(Items.WRITABLE_BOOK, 1.0F);
        buoyancy.put(Items.WRITTEN_BOOK, 1.0F);
        buoyancy.put(Items.ITEM_FRAME, 1.0F);
        buoyancy.put(Items.FLOWER_POT, 1.0F);
        buoyancy.put(Items.BAKED_POTATO, 1.0F);
        buoyancy.put(Items.POISONOUS_POTATO, 1.0F);
        buoyancy.put(Items.FILLED_MAP, 1.0F);
        buoyancy.put(Items.SKULL, 1.0F);
        buoyancy.put(Items.CARROT_ON_A_STICK, 1.0F);
        buoyancy.put(Items.PUMPKIN_PIE, 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("hemp").getMetadata(), 1.0F);
        buoyancy.put("gearWood", 1.0F);
        buoyancy.put("foodFlour", 1.0F);
        buoyancy.put("string", 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("scoured_leather").getMetadata(), 1.0F);
        buoyancy.put(BWMItems.DONUT, 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("dung").getMetadata(), 1.0F);
        buoyancy.put(BWMItems.WINDMILL, 1.0F);
        buoyancy.put("fabricHemp", 1.0F);
        buoyancy.put(BWMItems.DONUT, 1.0F);
        buoyancy.put(BWMItems.DONUT, 1.0F);
        buoyancy.put(BWMBlocks.GRATE, 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("tanned_leather").getMetadata(), 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("leather_strap").getMetadata(), 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("leather_belt").getMetadata(), 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("wood_blade").getMetadata(), 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("glue").getMetadata(), 0.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("tallow").getMetadata(), 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("haft").getMetadata(), 1.0F);
        buoyancy.put(BWMBlocks.URN, 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("sawdust").getMetadata(), 1.0F);
        buoyancy.put(BWMItems.DYNAMITE, 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("soul_dust").getMetadata(), 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("nether_sludge").getMetadata(), 1.0F);
        buoyancy.put(Items.CARROT, 1.0F);
        buoyancy.put(BWMItems.CREEPER_OYSTER, 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("leather_cut").getMetadata(), 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("tanned_leather_cut").getMetadata(), 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("scoured_leather_cut").getMetadata(), 1.0F);
        buoyancy.put("barkWood", 1.0F);
        buoyancy.put(Items.DYE, EnumDyeColor.BROWN.getDyeDamage(), 1.0F);
        buoyancy.put(BWMItems.MATERIAL, ItemMaterial.getMaterial("soul_flux").getMetadata(), 1.0F);
        buoyancy.put(BWMBlocks.STUMP, 1.0F);
        buoyancy.put(BWMItems.STUMP_REMOVER, 1.0F);
    }

    public static void initWeights() {
        weights.put(Items.CHAINMAIL_HELMET, OreDictionary.WILDCARD_VALUE, 3);
        weights.put(Items.CHAINMAIL_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 4);
        weights.put(Items.CHAINMAIL_LEGGINGS, OreDictionary.WILDCARD_VALUE, 4);
        weights.put(Items.CHAINMAIL_BOOTS, OreDictionary.WILDCARD_VALUE, 2);

        weights.put(Items.IRON_HELMET, OreDictionary.WILDCARD_VALUE, 5);
        weights.put(Items.IRON_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 8);
        weights.put(Items.IRON_LEGGINGS, OreDictionary.WILDCARD_VALUE, 7);
        weights.put(Items.IRON_BOOTS, OreDictionary.WILDCARD_VALUE, 4);

        weights.put(Items.DIAMOND_HELMET, OreDictionary.WILDCARD_VALUE, 5);
        weights.put(Items.DIAMOND_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 8);
        weights.put(Items.DIAMOND_LEGGINGS, OreDictionary.WILDCARD_VALUE, 7);
        weights.put(Items.DIAMOND_BOOTS, OreDictionary.WILDCARD_VALUE, 4);

        weights.put(Items.GOLDEN_HELMET, OreDictionary.WILDCARD_VALUE, 5);
        weights.put(Items.GOLDEN_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 8);
        weights.put(Items.GOLDEN_LEGGINGS, OreDictionary.WILDCARD_VALUE, 7);
        weights.put(Items.GOLDEN_BOOTS, OreDictionary.WILDCARD_VALUE, 4);
    }

    public static void initDesserts() {
        setDessert((ItemFood) Items.COOKIE);
        setDessert((ItemFood) Items.PUMPKIN_PIE);
    }

    private static void setDessert(ItemFood food) {
        food.setAlwaysEdible();
    }

    public static float getBuoyancy(ItemStack stack) {
        return buoyancy.get(stack);
    }

    public static ItemStackMap<Float> getBuoyancyRegistry() {
        return buoyancy;
    }

    public static float getWeight(ItemStack stack) {
        return weights.get(stack);
    }
}
