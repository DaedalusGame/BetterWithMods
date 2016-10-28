package betterwithmods.util.item;

import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Map to handle ItemStacks. Maybe should be replaced with just a class that hashcodes well ItemStacks.
 *
 * @param <T>
 * @author Koward
 */
public class ItemStackMap<T> {
    private final HashMap<Item, HashMap<Integer, T>> map = new HashMap<>();
    private final T defaultValue;

    public ItemStackMap(T defaultValueIn) {
        defaultValue = defaultValueIn;
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(ItemStack stack) {
        return containsKey(stack.getItem(), stack.getMetadata());
    }

    public boolean containsKey(Item item, int meta) {
        if (map.containsKey(item)) {
            HashMap<Integer, T> metaToValue = map.get(item);
            if (metaToValue.containsKey(meta)) return true;
            else if (metaToValue.containsKey(OreDictionary.WILDCARD_VALUE)) return true;
            return false;
        }
        return false;
    }

    public T get(ItemStack stack) {
        return get(stack.getItem(), stack.getMetadata());
    }

    public T get(Item item, int meta) {
        if (map.containsKey(item)) {
            HashMap<Integer, T> metaToValue = map.get(item);
            if (metaToValue.containsKey(meta)) return metaToValue.get(meta);
            else if (metaToValue.containsKey(OreDictionary.WILDCARD_VALUE))
                return metaToValue.get(OreDictionary.WILDCARD_VALUE);
            return defaultValue;
        }
        return defaultValue;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public T put(Item item, int meta, T value) {
        if (map.containsKey(item)) {
            map.get(item).put(meta, value);
        } else {
            HashMap<Integer, T> metaToValue = new HashMap<>();
            metaToValue.put(meta, value);
            map.put(item, metaToValue);
        }
        return value;
    }

    public T put(Item item, T value) {
        return put(item, 0, value);
    }

    public T put(Block block, int meta, T value) {
        try {
            ItemStack stack = new ItemStack(block, meta);
            return put(stack, value);
        } catch (NullPointerException e) {
            CrashReport report = new CrashReport("The block " + block.getRegistryName() + " cannot be converted into an item.", e);
            throw new ReportedException(report);
        }
    }

    public T put(Block block, T value) {
        return put(block, 0, value);
    }

    public T put(String oreDictName, T value) {
        List<ItemStack> itemStacks = OreDictionary.getOres(oreDictName);
        for (ItemStack stack : itemStacks) {
            put(stack, value);
        }
        return value;
    }

    public T put(ItemStack stack, T value) {
        return put(stack.getItem(), stack.getMetadata(), value);
    }

    public void putAll(Map<? extends ItemStack, ? extends T> arg0) {
        for (Entry<? extends ItemStack, ? extends T> entry : arg0.entrySet())
            put(entry.getKey(), entry.getValue());
    }

    public Set<Entry<Item, HashMap<Integer, T>>> entrySet() {
        return map.entrySet();
    }

    public T remove(Item item, int meta) {
        if (containsKey(item, meta)) {
            HashMap<Integer, T> prev = map.remove(item);
            return prev.get(meta);
        }
        return null;
    }
}
