package betterwithmods.integration.jei.wrapper;

import betterwithmods.common.registry.BlockMetaRecipe;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/11/16
 */
public class BlockMetaWrapper extends BlankRecipeWrapper {
    public final List<ItemStack> outputs = Lists.newArrayList();
    public final ItemStack input;

    public BlockMetaWrapper(BlockMetaRecipe recipe) {
        this.input = recipe.getStack();
        outputs.addAll(recipe.getOutputs());
    }

    @Override
    public void getIngredients(@Nonnull  IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutputs(ItemStack.class, outputs);
    }
}
