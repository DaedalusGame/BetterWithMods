package betterwithmods.integration.minetweaker;

import betterwithmods.common.registry.blockmeta.managers.BlockMetaManager;
import betterwithmods.common.registry.blockmeta.recipe.BlockMetaRecipe;
import com.blamejared.mtlib.utils.BaseListAddition;

import java.util.List;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/2/17
 */
public class BMAdd extends BaseListAddition<BlockMetaRecipe> {

    public BMAdd(String name, BlockMetaManager handler, List<BlockMetaRecipe> recipes) {
        super(name, handler.getRecipes(), recipes);
    }

    @Override
    protected String getRecipeInfo(BlockMetaRecipe recipe) {
        return recipe.getStack().getDisplayName();
    }
}