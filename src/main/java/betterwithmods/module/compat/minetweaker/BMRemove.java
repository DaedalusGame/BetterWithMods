package betterwithmods.module.compat.minetweaker;

import betterwithmods.common.registry.blockmeta.managers.BlockMetaManager;
import betterwithmods.common.registry.blockmeta.recipe.BlockMetaRecipe;
import com.blamejared.mtlib.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/2/17
 */
public class BMRemove extends BaseListRemoval<BlockMetaRecipe> {
    protected BMRemove(String name, BlockMetaManager recipes, ItemStack output) {
        super(name, recipes.getRecipes(), recipes.removeRecipes(output));
    }

    @Override
    protected String getRecipeInfo(BlockMetaRecipe recipe) {
        return recipe.getStack().getDisplayName();
    }

    @Override
    public String getJEICategory(BlockMetaRecipe recipe) {
        return name;
    }
}