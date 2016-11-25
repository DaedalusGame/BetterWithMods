package betterwithmods.craft;

import betterwithmods.BWMod;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.tuple.Pair;

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
    public static HashMap<ItemStack,Integer> filtertypes = new HashMap<>();
    //public static BiMap<Integer,Pair<ItemStack,Predicate<ItemStack>>> filters = HashBiMap.create();
    public static HashMap<Integer,Predicate<ItemStack>> filters = new HashMap<>();

    //This had to change because apparently soul urn recipes are hardcoded to type 6 or whatever.
    public static int newType() {
        int t = 0;
        for (int type:filtertypes.values()) {
            t = Math.max(t,type+1);
        }
        BWMod.logger.info(t);
        return t;
    }
    public static boolean containsStack(Set<ItemStack> set, ItemStack stack) {
        Optional<ItemStack> found = set.stream().filter( s -> s.isItemEqual(stack)).findFirst();
        return found.isPresent();
    }
    public static void addFilter(ItemStack stack, Set<ItemStack> allowedItems) {
        addFilter(newType(),stack, s -> containsStack(allowedItems,s));
    }

    public static void addFilter(int type,Item item, int meta, Predicate<ItemStack> allowed) {
        addFilter(type,new ItemStack(item,1,meta), allowed);
    }

    public static void addFilter(int type,Block block, int meta, Predicate<ItemStack> allowed) {
        addFilter(type,new ItemStack(block,1,meta), allowed);
    }

    public static void addFilter(int type,ItemStack filter, Predicate<ItemStack> allowed) {
        if(getFilterType(filter) != 0) {
            throw new IllegalArgumentException(String.format("Filter type %s already exists with ItemStack: %s", getFilterType(filter), filter.getDisplayName()));
        }
        if(!filters.containsKey(type)) {
            filters.put(type, allowed);
            filtertypes.put(filter,type);
        }
        else {
            throw new IllegalArgumentException(String.format("Filter type %s already exists with ItemStack: %s", type, filter.getDisplayName()));
        }
    }

    public static void addFilterItem(int type,ItemStack filter) {
        if(!filtertypes.containsKey(filter)) {
            filtertypes.put(filter,type);
        }
        else {
            throw new IllegalArgumentException(String.format("Filter type %s already exists with ItemStack: %s", type, filter.getDisplayName()));
        }
    }

    public static void addFilterItem(ItemStack otherfilter, ItemStack filter)
    {
        addFilterItem(getFilterType(otherfilter),filter);
    }

    public static void removeFilterItem(ItemStack filter) {
        filtertypes.entrySet().removeIf(p -> p.getKey() == filter);
    }

    public static void removeFilterType(int type) {
        filtertypes.entrySet().removeIf(p -> p.getValue() == type);
        filters.remove(type);
    }

    public static ArrayList<ItemStack> getFilters(int type) {
        return filtertypes.entrySet().stream().filter(entry -> entry.getValue() == type).map(Map.Entry::getKey).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Predicate<ItemStack> getAllowedItems(int type) {
        return filters.get(type);
    }

    public static int getFilterType(ItemStack filter) {
        Optional<ItemStack> type = filtertypes.keySet().stream().filter(p -> (p.isItemEqual(filter) || (p.getItem() == filter.getItem() && p.getMetadata() == OreDictionary.WILDCARD_VALUE))).findFirst();
        if(type.isPresent())
            return filtertypes.get(type.get());
        return 0;
    }

}
