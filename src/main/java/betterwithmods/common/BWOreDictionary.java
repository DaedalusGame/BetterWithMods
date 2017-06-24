package betterwithmods.common;

import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.items.ItemBark;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.OreStack;
import betterwithmods.util.RecipeUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import li.cil.manual.client.manual.provider.OreDictImageProvider;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by tyler on 5/10/17.
 */
public class BWOreDictionary {

    public static List<ItemStack> cropNames;
    public static List<ItemStack> dustNames;
    public static List<ItemStack> oreNames;
    public static List<ItemStack> ingotNames;

    public static List<Wood> woods;

    public static List<ItemStack> planks;
    public static List<ItemStack> logs;

    public static void registerOres() {

        registerOre("book", BWMItems.MANUAL);
        registerOre("gearSoulforgedSteel", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.STEEL_GEAR));
        registerOre("gearWood", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR));
        registerOre("cropHemp", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP));
        registerOre("dyeBrown", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG));
        registerOre("dung", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG));
        registerOre("slimeball", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE));
        registerOre("ingotSoulforgedSteel", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL));
        registerOre("dustNetherrack", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GROUND_NETHERRACK));
        registerOre("dustHellfire", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HELLFIRE_DUST));
        registerOre("dustSoul", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOUL_DUST));
        registerOre("ingotConcentratedHellfire", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE));
        registerOre("dustCoal", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.COAL_DUST));
        registerOre("dustPotash", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH));
        registerOre("dustWood", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST));
        registerOre("dustSulfur", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BRIMSTONE));
        registerOre("dustSaltpeter", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NITER));
        registerOre("nuggetIron", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NUGGET_IRON));
        registerOre("nuggetSoulforgedSteel", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NUGGET_STEEL));
        registerOre("foodFlour", BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD));
        registerOre("dustCharcoal", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHARCOAL_DUST));
        registerOre("foodCocoapowder", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.COCOA_POWDER));
        registerOre("dustCarbon", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.COAL_DUST), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHARCOAL_DUST));
        registerOre("coal", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL));
        registerOre("foodChocolatebar", new ItemStack(BWMItems.CHOCOLATE));

        registerOre("blockSoulforgedSteel", new ItemStack(BWMBlocks.AESTHETIC, 1, 2));
        registerOre("blockConcentratedHellfire", new ItemStack(BWMBlocks.AESTHETIC, 1, 3));
        //Added bark subtype entries for Roots compatibility
        registerOre("barkWood", new ItemStack(BWMItems.BARK, 1, OreDictionary.WILDCARD_VALUE));
        registerOre("barkOak", new ItemStack(BWMItems.BARK, 1, 0));
        registerOre("barkSpruce", new ItemStack(BWMItems.BARK, 1, 1));
        registerOre("barkBirch", new ItemStack(BWMItems.BARK, 1, 2));
        registerOre("barkJungle", new ItemStack(BWMItems.BARK, 1, 3));
        registerOre("barkAcacia", new ItemStack(BWMItems.BARK, 1, 4));
        registerOre("barkDarkOak", new ItemStack(BWMItems.BARK, 1, 5));

        registerOre("hideTanned", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER));

        registerOre("slabWood", new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        registerOre("sidingWood", new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        registerOre("mouldingWood", new ItemStack(BWMBlocks.WOOD_MOULDING, 1, OreDictionary.WILDCARD_VALUE));
        registerOre("cornerWood", new ItemStack(BWMBlocks.WOOD_CORNER, 1, OreDictionary.WILDCARD_VALUE));

        registerOre("sidingStone", new ItemStack(BWMBlocks.STONE_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        registerOre("mouldingStone", new ItemStack(BWMBlocks.STONE_MOULDING, 1, OreDictionary.WILDCARD_VALUE));
        registerOre("cornerStone", new ItemStack(BWMBlocks.STONE_CORNER, 1, OreDictionary.WILDCARD_VALUE));

        registerOre("fiberHemp", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_FIBERS));
        registerOre("fabricHemp", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_CLOTH));

        registerOre("ingotDiamond", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT));
        registerOre("nuggetDiamond", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_NUGGET));

        registerOre("listAllmeat", Items.PORKCHOP, Items.BEEF, Items.CHICKEN, Items.FISH, Items.MUTTON);
        registerOre("listAllmeat", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
        registerOre("listAllmeatcooked", Items.COOKED_PORKCHOP, Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_FISH, Items.COOKED_MUTTON, Items.COOKED_RABBIT);
        registerOre("listAllmeatcooked", new ItemStack(Items.COOKED_FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));

        registerOre("tallow", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TALLOW));

        registerOre("blockHellfire", new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.HELLFIRE.getMeta()));

        registerOre("blockPlanter", new ItemStack(BWMBlocks.PLANTER, 1, OreDictionary.WILDCARD_VALUE));

        registerOre("pile", new ItemStack(BWMItems.DIRT_PILE), new ItemStack(BWMItems.SAND_PILE), new ItemStack(BWMItems.RED_SAND_PILE), new ItemStack(BWMItems.GRAVEL_PILE));

        woods = Lists.newArrayList(
                new Wood(new ItemStack(Blocks.LOG, 1, 0), new ItemStack(Blocks.PLANKS, 1, 0), ItemBark.getStack("oak", 1)),
                new Wood(new ItemStack(Blocks.LOG, 1, 1), new ItemStack(Blocks.PLANKS, 1, 1), ItemBark.getStack("spruce", 1)),
                new Wood(new ItemStack(Blocks.LOG, 1, 2), new ItemStack(Blocks.PLANKS, 1, 2), ItemBark.getStack("birch", 1)),
                new Wood(new ItemStack(Blocks.LOG, 1, 3), new ItemStack(Blocks.PLANKS, 1, 3), ItemBark.getStack("jungle", 1)),
                new Wood(new ItemStack(Blocks.LOG2, 1, 0), new ItemStack(Blocks.PLANKS, 1, 4), ItemBark.getStack("acacia", 1)),
                new Wood(new ItemStack(Blocks.LOG2, 1, 1), new ItemStack(Blocks.PLANKS, 1, 5), ItemBark.getStack("dark_oak", 1))
        );
        List<ItemStack> logs = OreDictionary.getOres("logWood").stream().filter( stack -> !stack.getItem().getRegistryName().getResourceDomain().equalsIgnoreCase("minecraft")).collect(Collectors.toList());
        for(ItemStack log: logs) {
            ItemStack plank = RecipeUtils.getRecipeOutput(log);
            if(isOre(plank,"plankWood")) {
                Wood wood = new Wood(log,plank);
                woods.add(wood);
            }
        }
    }

    public static void registerOre(String ore, ItemStack... items) {
        for (ItemStack i : items)
            OreDictionary.registerOre(ore, i);
    }

    public static void registerOre(String ore, Item... items) {
        for (Item item : items)
            registerOre(ore, new ItemStack(item));
    }

    public static void postInitOreDictGathering() {
        dustNames = getOreNames("dust");
        oreNames = getOreNames("ore");
        ingotNames = getOreNames("ingot");
        cropNames = getOreNames("crop");
    }

    public static String getSuffix(ItemStack stack, String startingPrefix) {
        return IntStream.of(OreDictionary.getOreIDs(stack)).mapToObj(OreDictionary::getOreName).map(ore -> ore.substring(startingPrefix.length())).findFirst().orElse(null);
    }

    public static List<ItemStack> getOreNames(String prefix) {
        return Arrays.stream(OreDictionary.getOreNames()).filter(n -> n.startsWith(prefix)).flatMap(n -> OreDictionary.getOres(n).stream()).collect(Collectors.toList());
    }

    public static int listContains(Object obj, List<Object> list) {
        if (list != null && list.size() > 0 && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (obj instanceof ItemStack && list.get(i) instanceof ItemStack) {
                    ItemStack stack = (ItemStack) obj;
                    ItemStack toCheck = (ItemStack) list.get(i);
                    if (ItemStack.areItemsEqual(stack, toCheck)) {
                        if (toCheck.hasTagCompound()) {
                            if (ItemStack.areItemStackTagsEqual(stack, toCheck))
                                return i;
                        } else if (stack.hasTagCompound()) {
                            return -1;
                        } else
                            return i;
                    }
                } else if (obj instanceof OreStack && list.get(i) instanceof OreStack) {
                    OreStack stack = (OreStack) obj;
                    OreStack toCheck = (OreStack) list.get(i);
                    if (stack.getOreName().equals(toCheck.getOreName()))
                        return i;
                }
            }
        }
        return -1;
    }

    public static boolean isOre(ItemStack stack, String ore) {
        return listContains(stack, OreDictionary.getOres(ore));
    }

    public static boolean listContains(ItemStack check, List<ItemStack> list) {
        if (list != null) {
            if (list.isEmpty()) return false;
            for (ItemStack item : list) {
                if (ItemStack.areItemsEqual(check, item) || (check.getItem() == item.getItem() && item.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                    return !item.hasTagCompound() || ItemStack.areItemStackTagsEqual(check, item);
                }
            }
        }
        return false;
    }

    public static boolean hasSuffix(ItemStack stack, String suffix) {
        return listContains(stack, getOreNames(suffix));
    }


    public static class Wood {
        public ItemStack log, plank, bark;

        public Wood(ItemStack log, ItemStack plank) {
            this.log = log;
            this.plank = plank;

            //TODO add custom bark render for all bark
            this.bark = ItemBark.getStack("oak",1);
        }

        public Wood(ItemStack log, ItemStack plank, ItemStack bark) {
            this.log = log;
            this.plank = plank;
            this.bark = bark;
        }

        public ItemStack getLog(int count) {
            ItemStack copy = log.copy();
            copy.setCount(count);
            return copy;
        }

        public ItemStack getPlank(int count) {
            ItemStack copy = plank.copy();
            copy.setCount(count);
            return copy;
        }

        public ItemStack getBark(int count) {
            ItemStack copy = bark.copy();
            copy.setCount(count);
            return copy;
        }
        public ItemStack getSawdust(int count) {
            ItemStack copy = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST,count);
            return copy;
        }
    }
}
