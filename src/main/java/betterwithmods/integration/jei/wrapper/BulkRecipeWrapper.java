package betterwithmods.integration.jei.wrapper;

import betterwithmods.craft.bulk.BulkRecipe;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BulkRecipeWrapper extends BWMRecipeWrapper {
    public BulkRecipeWrapper(@Nonnull BulkRecipe recipe) {
        super(recipe);
    }

    @Nonnull
    @Deprecated
    @Override
    public List<ItemStack> getInputs() {
        List<ItemStack> inputs = new ArrayList<>();
        for (ItemStack stack : getRecipe().getInput()) {
            if (stack != null && stack.getItem() != null)
                inputs.add(stack.copy());
        }
        return inputs;
    }

    @Nonnull
    @Deprecated
    @Override
    public List<ItemStack> getOutputs() {
        List<ItemStack> outputs = new ArrayList<>();
        outputs.add(getRecipe().getOutput().copy());
        if (getRecipe().getSecondary() != null)
            outputs.add(getRecipe().getSecondary().copy());
        return outputs;
    }

    @Nonnull
    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, getInputs());
        ingredients.setOutputs(ItemStack.class, getOutputs());
    }
}
