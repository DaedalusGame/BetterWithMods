package betterwithmods.integration.minetweaker;

import betterwithmods.common.registry.BlockMetaHandler;
import betterwithmods.common.registry.BlockMetaRecipe;
import com.blamejared.mtlib.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/2/17
 */
public class BMRemove extends BaseListRemoval<BlockMetaRecipe> {
    protected BMRemove(String name, BlockMetaHandler recipes, ItemStack output) {
        super(name, recipes.getRecipes(), recipes.removeRecipes(output));
    }

    @Override
    protected String getRecipeInfo(BlockMetaRecipe recipe) {
        return recipe.getStack().getDisplayName();
    }
}