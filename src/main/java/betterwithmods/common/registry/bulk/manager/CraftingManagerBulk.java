package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.registry.OreStack;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
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

    public T addRecipe(T recipe) {
        if (!recipe.isEmpty())
            recipes.add(recipe);
        return recipe;
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
        System.out.println(input);
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
        TreeSet<T> recipes = getValidRecipes(inv);
        if (recipes.isEmpty())
            return null;
        return recipes.first();
    }

    private TreeSet<T> getValidRecipes(ItemStackHandler inv) {
        TreeSet<T> recipes = new TreeSet<>();
        for (T recipe : this.recipes) {
            if (recipe.matches(inv))
                recipes.add(recipe);
        }
        return recipes;
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

    public NonNullList<ItemStack> craftItem(World world, TileEntity tile, ItemStackHandler inv) {
        T recipe = getMostValidRecipe(inv);
        if (recipe != null) {
            return recipe.onCraft(world, tile, inv);
        }
        return NonNullList.create();
    }

    public List<T> getRecipes() {
        return this.recipes;
    }

}
