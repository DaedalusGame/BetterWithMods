package betterwithmods.integration.jei.wrapper;

import betterwithmods.craft.HopperFilters;
import betterwithmods.craft.HopperInteractions;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/20/16
 */
public class HopperRecipeWrapper extends BlankRecipeWrapper {

    private ItemStack input, filter;
    private List<ItemStack> outputs;

    public HopperRecipeWrapper(HopperInteractions.HopperRecipe recipe) {
        this.input = recipe.getInput();

        this.outputs = Lists.newArrayList(recipe.getOutput());
        if (!recipe.getSecondaryOutput().isEmpty())
            this.outputs.addAll(recipe.getSecondaryOutput());
        this.filter = HopperFilters.getFilter(recipe.getFilterType());
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, Lists.newArrayList(filter, input));
        ingredients.setOutputs(ItemStack.class, outputs);
    }
}
