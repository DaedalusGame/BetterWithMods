package betterwithmods.module.compat.minetweaker;

import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.google.common.collect.Lists;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/2/17
 */
public class BulkAdd extends BaseListAddition<BulkRecipe> {

    public BulkAdd(String name, CraftingManagerBulk recipes, BulkRecipe recipe) {
        super(name, recipes.getRecipes(), Lists.newArrayList(recipe));
    }

    @Override
    protected String getRecipeInfo(BulkRecipe recipe) {
        return recipe.getOutput().getDisplayName();
    }
}