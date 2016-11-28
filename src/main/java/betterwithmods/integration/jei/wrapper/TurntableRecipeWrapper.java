package betterwithmods.integration.jei.wrapper;

import betterwithmods.craft.TurntableInteraction;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/11/16
 */
public class TurntableRecipeWrapper extends BlockMetaWrapper {
    public TurntableRecipeWrapper(TurntableInteraction.TurntableRecipe recipe) {
        super(recipe);
        ItemStack result = recipe.getResult();
        if (result != null && result.getItem() != null)
            outputs.add(0, result);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutputs(ItemStack.class, outputs);
    }
}
