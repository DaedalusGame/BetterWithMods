package betterwithmods.craft;

import betterwithmods.BWMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/13/16
 */
public class HopperFilters {
    public static final HashMap<Integer, Predicate<ItemStack>> filters = new HashMap<>();
    public static final HashMap<ItemStack, Integer> filterTypes = new HashMap<>();

    public static boolean containsStack(Set<ItemStack> set, ItemStack stack) {
        Optional<ItemStack> found = set.stream().filter(s -> s.isItemEqual(stack)).findFirst();
        return found.isPresent();
    }

    public static void addFilter(ItemStack stack, Set<ItemStack> allowedItems) {
        addFilter(filters.size() + 1, stack, s -> containsStack(allowedItems, s));
    }

    public static void addFilter(int type, Block block, int meta, Predicate<ItemStack> allowed) {
        addFilter(type, new ItemStack(block, 1, meta), allowed);
    }

    public static void addFilter(int type, ItemStack filter, Predicate<ItemStack> allowed) {
        if (filterTypes.containsKey(filter)) {
            throw new IllegalArgumentException("Filter " + filter.getDisplayName() + "For Type " + type + " Already exists");
        }
        if (filters.containsKey(type)) {
            throw new IllegalArgumentException("Filter " + type + " Already exists");
        }
        BWMod.logger.info("Adding Filter " + filter.getDisplayName() + "," + type);
        filterTypes.put(filter, type);
        filters.put(type, allowed);
    }

    public static List<ItemStack> getFilter(int type) {
        return filterTypes.entrySet().stream().filter(entry -> entry.getValue() == type).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public static Predicate<ItemStack> getAllowedItems(int type) {
        return filters.get(type);
    }

    public static int getFilterType(ItemStack filter) {
        Optional<Integer> type = filterTypes.entrySet().stream().
                filter(e -> e.getKey().getItem() == filter.getItem() && (e.getKey().getMetadata() == filter.getMetadata() || e.getKey().getMetadata() == OreDictionary.WILDCARD_VALUE))
                .map(Map.Entry::getValue).findAny();
        if (type.isPresent())
            return type.get();
        return 0;
    }
}
