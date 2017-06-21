package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.registry.OreStack;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public abstract class CraftingManagerBulk<T extends BulkRecipe> {
    private List<T> recipes;

    protected CraftingManagerBulk() {
        this.recipes = new ArrayList<>();
    }

    public void addRecipe(T recipe) {
        if (!recipe.isEmpty())
            recipes.add(recipe);
    }

    public List<T> findRecipeForRemoval(@Nonnull ItemStack output) {
        return recipes.stream().filter(recipe -> recipe.matches(output)).collect(Collectors.toList());
    }

    public List<T> findRecipeForRemoval(@Nonnull ItemStack output, @Nonnull ItemStack secondary) {
        return recipes.stream().filter(recipe -> recipe.matches(output, secondary)).collect(Collectors.toList());
    }

    public List<T> findRecipeForRemoval(@Nonnull ItemStack output, @Nonnull ItemStack secondary, @Nonnull Object... inputs) {
        List<T> removed = Lists.newArrayList();
        if (inputs.length > 0) {
            List<T> found = findRecipeForRemoval(output, secondary);

            for (T recipe : found) {
                boolean match = true;
                for (Object input : inputs) {
                    match = hasMatch(input, recipe.getRecipeInput());
                    if (!match)
                        break;
                }
                if (match)
                    removed.add(recipe);
            }
        }
        return removed;
    }


    public boolean removeRecipe(ItemStack output, ItemStack secondary) {
        Iterator<T> iterator = recipes.iterator();
        List<T> remove = findRecipeForRemoval(output, secondary);
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (remove.contains(next))
                iterator.remove();
        }
        return remove.isEmpty();
    }

    public boolean removeRecipe(ItemStack output, ItemStack secondary, Object... inputs) {
        Iterator<T> iterator = recipes.iterator();
        List<T> remove = findRecipeForRemoval(output, secondary, inputs);
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (remove.contains(next))
                iterator.remove();
        }
        return remove.isEmpty();
    }

    private boolean hasMatch(Object input, List<Object> inputs) {
        if (input instanceof String) {
            for (Object in : inputs) {
                if (in instanceof OreStack) {
                    if (input.equals(((OreStack) in).getOreName()))
                        return true;
                } else if (in instanceof ItemStack) {
                    if (BWOreDictionary.listContains((ItemStack) in, OreDictionary.getOres((String) input)))
                        return true;
                }
            }
        } else if (input instanceof ItemStack) {
            for (Object in : inputs) {
                if (in instanceof ItemStack) {
                    if (((ItemStack) input).isItemEqual((ItemStack) in))
                        return true;
                } else if (in instanceof OreStack) {
                    if (BWOreDictionary.listContains((ItemStack) input, ((OreStack) in).getItems())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public ItemStack[] getCraftingResult(ItemStackHandler inv) {
        T recipe = getMostValidRecipe(inv);
        if (recipe != null) {
            if (recipe.matches(inv)) {
                ItemStack[] ret = new ItemStack[1];
                if (recipe.getSecondary() != null) {
                    ret = new ItemStack[2];
                    ret[1] = recipe.getSecondary();
                }
                ret[0] = recipe.getOutput();
                return ret;
            }
        }
        return null;
    }

    public T getMostValidRecipe(ItemStackHandler inv) {
        HashMap<Integer, T> recipes = getValidRecipes(inv);
        for (Map.Entry<Integer, T> entry : recipes.entrySet()) {
            if (entry.getValue().matches(inv))
                return entry.getValue();
        }
        return null;
    }

    private HashMap<Integer, T> getValidRecipes(ItemStackHandler inv) {
        HashMap<Integer, T> recipe = new HashMap<>();


        int order = 0;
        for (int i = 0; i < inv.getSlots(); i++) {
            T single = null;
            if (!inv.getStackInSlot(i).isEmpty()) {
                ItemStack stack = inv.getStackInSlot(i).copy();
                for (T r : this.recipes) {
                    if (containsIngredient(r.getRecipeInput(), stack)) {
                        if (r.getRecipeInput().size() > 1) {
                            recipe.put(order, r);
                            order++;
                        } else
                            single = r;
                    }
                }
            }
            //We're throwing this in the back of the possible valid RECIPES just in case there's another recipe with the same item alongside another.
            if (single != null) {
                recipe.put(order, single);
                order++;
            }
        }
        return recipe;
    }

    private boolean containsIngredient(List<Object> list, ItemStack stack) {
        for (Object obj : list) {
            if (obj instanceof ItemStack) {
                if (ItemStack.areItemsEqual((ItemStack) obj, stack) || (((ItemStack) obj).getItemDamage() == OreDictionary.WILDCARD_VALUE && stack.getItem() == ((ItemStack) obj).getItem())) {
                    return !((ItemStack) obj).hasTagCompound() || ItemStack.areItemStackTagsEqual(stack, (ItemStack) obj);
                }
            } else if (obj instanceof OreStack) {
                if (BWOreDictionary.listContains(stack, ((OreStack) obj).getItems()))
                    return true;
            }
        }
        return false;
    }


    public List<Object> getValidCraftingIngredients(ItemStackHandler inv) {
        T recipe = getMostValidRecipe(inv);
        if (recipe != null)
            return recipe.getRecipeInput();
        return Lists.newArrayList();
    }

    public NonNullList<ItemStack> craftItem(ItemStackHandler inv) {
        T recipe = getMostValidRecipe(inv);
        if (recipe != null) {
            NonNullList<ItemStack> list = NonNullList.withSize(2, ItemStack.EMPTY);
            list.set(0, recipe.getOutput());
            list.set(1, recipe.getSecondary());
            recipe.consumeInvIngredients(inv);
            return list;
        }
        return NonNullList.create();
    }

    public List<T> getRecipes() {
        return this.recipes;
    }

}
