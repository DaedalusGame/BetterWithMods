package betterwithmods.module.compat.minetweaker;


import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import com.blamejared.mtlib.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/2/17
 */
public class BulkRemove<T extends BulkRecipe> extends BaseListRemoval<T> {
    protected BulkRemove(String name, CraftingManagerBulk<T> recipes, ItemStack output, ItemStack secondary) {
        super(name, recipes.getRecipes(), recipes.findRecipeForRemoval(output,secondary));
    }

    protected BulkRemove(String name, CraftingManagerBulk<T> recipes, ItemStack output, ItemStack secondary, Object... inputs) {
        super(name, recipes.getRecipes(), recipes.findRecipeForRemoval(output, secondary, inputs));
    }

    @Override
    protected String getRecipeInfo(BulkRecipe recipe) {
        return recipe.getOutput().getDisplayName();
    }

    @Override
    public String getJEICategory(BulkRecipe recipe) {
        return name;
    }
}