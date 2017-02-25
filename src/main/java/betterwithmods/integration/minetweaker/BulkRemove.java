package betterwithmods.integration.minetweaker;


import betterwithmods.craft.bulk.BulkRecipe;
import betterwithmods.craft.bulk.CraftingManagerBulk;
import com.blamejared.mtlib.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/2/17
 */
public class BulkRemove extends BaseListRemoval<BulkRecipe> {
    protected BulkRemove(String name, CraftingManagerBulk recipes, ItemStack output) {
        super(name, recipes.getRecipes(), recipes.removeRecipes(output));
    }
    @Override
    protected String getRecipeInfo(BulkRecipe recipe) {
        return recipe.getOutput().getDisplayName();
    }
}