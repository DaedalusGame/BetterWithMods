package betterwithmods.integration.minetweaker.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class BaseMapModification<K, V> extends BaseUndoable {
    protected final HashMap<K, V> recipes;
    protected final HashMap<K, V> successful;
    protected final Map<K, V> map;

    protected BaseMapModification(String name, Map<K, V> map) {
        super(name);
        this.map = map;
        this.recipes = new HashMap<>();
        this.successful = new HashMap<>();
    }

    @Override
    public boolean canUndo() {
        return !recipes.isEmpty();
    }

    @Override
    protected String getRecipeInfo() {
        if (!recipes.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Entry<K, V> recipe : recipes.entrySet()) {
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

    /**
     * This method must be overwritten by the extending classes. It should return
     * the name of the key item, for which the recipe is for. For example for machines
     * which produce new items, it should return the name of the output. For machines
     * which are processing items (like a pulverizer) it should return the name of the
     * the input. Another example would be the name of the enchantment for a thaumcraft
     * infusion recipe.
     */
    protected abstract String getRecipeInfo(Entry<K, V> recipe);
}
