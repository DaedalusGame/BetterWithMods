package betterwithmods.integration.minetweaker.utils;

import betterwithmods.util.item.ItemStackMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemMapModification<T> extends BaseUndoable {
    protected final ItemStackMap<T> recipes;
    protected final ItemStackMap<T> successful;
    protected final ItemStackMap<T> overwritten;
    protected final ItemStackMap<T> map;


    protected ItemMapModification(String name, T defaultValue, ItemStackMap<T> map) {
        super(name);
        this.map = map;
        recipes = new ItemStackMap<>(defaultValue);
        successful = new ItemStackMap<>(defaultValue);
        this.overwritten = new ItemStackMap<>(defaultValue);
    }

    protected ItemMapModification(String name, T defaultValue, ItemStackMap<T> map, Map<? extends ItemStack, ? extends T> recipes) {
        this(name, defaultValue, map);
        this.recipes.putAll(recipes);
    }

    @Override
    public boolean canUndo() {
        return !recipes.isEmpty();
    }

    @Override
    protected String getRecipeInfo() {
        if (!recipes.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Item, HashMap<Integer, T>> recipe : recipes.entrySet()) {
                if (recipe != null) {
                    sb.append(getRecipeInfo(recipe)).append(", ");
                }
            }

            if (sb.length() > 0) {
                sb.setLength(sb.length() - 2);
            }

            return sb.toString();
        }

        return super.getRecipeInfo();
    }

    @Override
    public void apply() {
        if (recipes.isEmpty())
            return;
        for (Map.Entry<Item, HashMap<Integer, T>> entry :
                recipes.entrySet()) {
            Item key = entry.getKey();
            HashMap<Integer, T> value = entry.getValue();
            for (Map.Entry<Integer, T> e : value.entrySet()) {
                if (recipes.containsKey(key, e.getKey())) {
                    overwritten.put(key, e.getKey(), e.getValue());
                }
                successful.put(key, e.getKey(), e.getValue());
            }
        }
    }


    @Override
    public void undo() {
        if (successful.isEmpty() && overwritten.isEmpty())
            return;

        for (Map.Entry<Item, HashMap<Integer, T>> entry : successful.entrySet()) {
            Item key = entry.getKey();
            for (Map.Entry<Integer, T> val : entry.getValue().entrySet()) {
                T value = map.remove(key, val.getKey());
                if (value == null) {
                    LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
                }
            }
        }
        for (Map.Entry<Item, HashMap<Integer, T>> entry : overwritten.entrySet()) {
            Item key = entry.getKey();
            for (Map.Entry<Integer, T> val : entry.getValue().entrySet()) {
                T oldValue = map.remove(key, val.getKey());
                if (oldValue != null) {
                    LogHelper.logWarning(String.format("Overwritten %s Recipe which should not exist for %s", name, getRecipeInfo()));
                }
            }
        }
    }

    protected String getRecipeInfo(Map.Entry<Item, HashMap<Integer, T>> recipe) {
        return null;
    }

}
