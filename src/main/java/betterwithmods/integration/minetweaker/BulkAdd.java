package betterwithmods.integration.minetweaker;

import betterwithmods.craft.bulk.BulkRecipe;
import betterwithmods.craft.bulk.CraftingManagerBulk;
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