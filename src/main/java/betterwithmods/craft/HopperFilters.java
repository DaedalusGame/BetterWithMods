package betterwithmods.craft;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/13/16
 */
public class HopperFilters {
    public static BiMap<Integer,Pair<ItemStack,Predicate<ItemStack>>> filters = HashBiMap.create();

    public static void addFilter(int type, Item item, int meta, Predicate<ItemStack> allowed) {
        addFilter(type, new ItemStack(item,1,meta), allowed);
    }

    public static void addFilter(int type, Block block, int meta, Predicate<ItemStack> allowed) {
        addFilter(type, new ItemStack(block,1,meta), allowed);
    }
    public static void addFilter(int type,ItemStack filter, Predicate<ItemStack> allowed) {
        filters.put(type,Pair.of(filter, allowed));
    }

    public static ItemStack getFilter(int type) {
        return filters.get(type).getLeft();
    }

    public static Predicate<ItemStack> getAllowedItems(int type) {
        return filters.get(type).getRight();
    }

    public static int getFilterType(ItemStack filter) {
        Optional<Integer> type = filters.inverse().keySet().stream().filter(p -> (p.getLeft().isItemEqual(filter) || (p.getLeft().getItem() == filter.getItem() && p.getLeft().getMetadata() == OreDictionary.WILDCARD_VALUE))).map(p -> filters.inverse().get(p)).findFirst();
        if(type.isPresent())
            return type.get();
        return 0;
    }

}
