package betterwithmods.integration.minetweaker;

import betterwithmods.common.registry.BlockMetaHandler;
import betterwithmods.common.registry.BlockMetaRecipe;
import com.blamejared.mtlib.utils.BaseListAddition;

import java.util.List;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/2/17
 */
public class BMAdd extends BaseListAddition<BlockMetaRecipe> {

    public BMAdd(String name, BlockMetaHandler handler, List<BlockMetaRecipe> recipes) {
        super(name, handler.getRecipes(), recipes);
    }

    @Override
    protected String getRecipeInfo(BlockMetaRecipe recipe) {
        return recipe.getStack().getDisplayName();
    }
}