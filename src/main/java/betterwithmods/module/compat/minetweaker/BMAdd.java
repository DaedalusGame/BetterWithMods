package betterwithmods.module.compat.minetweaker;

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
public class BMAdd<T extends BlockMetaRecipe> extends BaseListAddition<T> {

    public BMAdd(String name, BlockMetaManager<T> handler, List<T> recipes) {
        super(name, handler.getRecipes(), recipes);
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