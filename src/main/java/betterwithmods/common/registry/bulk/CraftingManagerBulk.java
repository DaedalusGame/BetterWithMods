package betterwithmods.common.registry.bulk;

import betterwithmods.common.registry.OreStack;
import betterwithmods.util.InvUtils;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class CraftingManagerBulk {
    private final String craftType;
    private List<BulkRecipe> recipes;

    public CraftingManagerBulk(String craftType) {
        this.craftType = craftType;
        this.recipes = new ArrayList<>();
    }

    public void addOreRecipe(ItemStack output, Object[] inputs) {
        addOreRecipe(output, null, inputs);
    }

    public void addOreRecipe(ItemStack output, ItemStack secondary, Object input) {
        Object[] inputs = new Object[1];
        inputs[0] = input;
        addOreRecipe(output, secondary, inputs);
    }

    public void addOreRecipe(ItemStack output, Object input) {
        addOreRecipe(output, null, input);
    }

    public void addOreRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        BulkRecipe recipe = createRecipe(output, secondary, inputs);
        this.recipes.add(recipe);
    }

    public void addRecipe(ItemStack output, ItemStack[] inputs) {
        addRecipe(output, null, inputs);
    }

    public void addRecipe(ItemStack output, ItemStack secondary, ItemStack input) {
        ItemStack[] inputs = new ItemStack[1];
        inputs[0] = input.copy();
        addRecipe(output, secondary, inputs);
    }

    public void addRecipe(ItemStack output, ItemStack input) {
        addRecipe(output, null, input);
    }

    public void addRecipe(ItemStack output, ItemStack secondary, ItemStack[] inputs) {
        BulkRecipe recipe = createRecipe(output, secondary, inputs);
        this.recipes.add(recipe);
    }

    public List<BulkRecipe> removeRecipes(ItemStack output) {
        List<BulkRecipe> removed = Lists.newArrayList();
        for (BulkRecipe ir : recipes) {
            if (ir.getOutput().isItemEqual(output)) {
                removed.add(ir);
            }
        }
        return removed;
    }

    public List<BulkRecipe> removeRecipes(ItemStack output, Object... inputs) {
        List<BulkRecipe> removed = Lists.newArrayList();
        for (BulkRecipe ir : recipes) {
            if (ir.getOutput().isItemEqual(output)) {
                if (inputs.length > 0) {
                    boolean match = true;
                    for (Object input : inputs) {
                        match = hasMatch(input, ir.getRecipeInput());
                        if (!match)
                            break;
                    }
                    if (match)
                        removed.add(ir);
                } else {
                    removed.add(ir);
                }
            }
        }
        return removed;
    }

    private boolean hasMatch(Object input, List<Object> inputs) {
        if (input instanceof String) {
            for (Object in : inputs) {
                if (in instanceof OreStack) {
                    if (input.equals(((OreStack)in).getOreName()))
                        return true;
                }
                else if (in instanceof ItemStack) {
                    if (InvUtils.listContains((ItemStack)in, OreDictionary.getOres((String)input)))
                        return true;
                }
            }
        } else if (input instanceof ItemStack) {
            for (Object in : inputs) {
                if (in instanceof ItemStack) {
                    if (((ItemStack)input).isItemEqual((ItemStack)in))
                        return true;
                }
                else if (in instanceof OreStack) {
                    if (InvUtils.listContains((ItemStack)input, ((OreStack)in).getOres())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean removeRecipe(ItemStack output, Object[] inputs) {
        return removeRecipe(output, null, inputs);
    }

    public boolean removeRecipe(ItemStack output, ItemStack secondary, Object input) {
        Object[] inputs = new Object[1];
        inputs[0] = input;
        return removeRecipe(output, secondary, inputs);
    }

    public boolean removeRecipe(ItemStack output, Object input) {
        return removeRecipe(output, null, input);
    }

    public boolean removeRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        BulkRecipe recipe = createRecipe(output, secondary, inputs);
        int matchingIndex = getMatchingRecipeIndex(recipe);

        if (matchingIndex >= 0) {
            this.recipes.remove(matchingIndex);
            return true;
        }
        return false;
    }

    public ItemStack[] getCraftingResult(ItemStackHandler inv) {
        BulkRecipe recipe = getMostValidRecipe(inv);
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

    private BulkRecipe getMostValidRecipe(ItemStackHandler inv) {
        HashMap<Integer, BulkRecipe> recipes = getValidRecipes(inv);
        if (!recipes.isEmpty()) {
            for (int i = 0; i < recipes.size(); i++) {
                BulkRecipe recipe = recipes.get(i);
                if (recipe.matches(inv))
                    return recipe;
            }
        }
        return null;
    }

    private HashMap<Integer, BulkRecipe> getValidRecipes(ItemStackHandler inv) {
        HashMap<Integer, BulkRecipe> recipe = new HashMap<>();
        int order = 0;
        for (int i = 0; i < inv.getSlots(); i++) {
            BulkRecipe single = null;
            if (inv.getStackInSlot(i) != ItemStack.EMPTY) {
                ItemStack stack = inv.getStackInSlot(i).copy();
                for (BulkRecipe r : this.recipes) {
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
                if (InvUtils.listContains(stack, ((OreStack) obj).getOres()))
                    return true;
            }
        }
        return false;
    }

    public List<Object> getValidCraftingIngredients(ItemStackHandler inv) {
        BulkRecipe recipe = getMostValidRecipe(inv);
        if (recipe != null)
            return recipe.getRecipeInput();
        return null;
    }

    public ItemStack[] craftItem(ItemStackHandler inv) {
        BulkRecipe recipe = getMostValidRecipe(inv);
        if (recipe != null) {
            ItemStack[] ret = new ItemStack[1];
            if (recipe.getSecondary() != null) {
                ret = new ItemStack[2];
                ret[1] = recipe.getSecondary();
            }
            if (recipe.getOutput() == null) {
                return null;
            }
            ret[0] = recipe.getOutput();
            recipe.consumeInvIngredients(inv);
            return ret;
        }
        return null;
    }

    private BulkRecipe createRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        return new BulkRecipe(craftType, output, secondary, inputs);
    }

    private int getMatchingRecipeIndex(BulkRecipe recipe) {
        for (int i = 0; i < this.recipes.size(); i++) {
            BulkRecipe tempRecipe = this.recipes.get(i);
            if (tempRecipe.matches(recipe))
                return i;
        }
        return -1;
    }

    public List<BulkRecipe> getRecipes() {
        return this.recipes;
    }

    //Lazy way of ensuring the ore dictionary entries were properly implemented.
    public void refreshRecipes() {
        List<BulkRecipe> recipes = getRecipes();
        if (!recipes.isEmpty()) {
            this.recipes = new ArrayList<>();
            for (BulkRecipe r : recipes) {
                this.recipes.add(createRecipe(r.getOutput(), r.getSecondary(), r.input.toArray()));
            }
        }
    }
}
