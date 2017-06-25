package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import net.minecraft.item.ItemStack;

public class MillManager extends CraftingManagerBulk<MillRecipe> {
    private static final MillManager instance = new MillManager();

    public static MillManager getInstance() {
        return instance;
    }

    public void addRecipe(int grindType, ItemStack output, Object[] inputs) {
        addRecipe(grindType, output, ItemStack.EMPTY, inputs);
    }

    public void addRecipe(int grindType, ItemStack output, ItemStack secondary, Object[] inputs) {
        addRecipe(new MillRecipe(grindType, output, secondary, inputs));
    }

    @Override
    public MillRecipe addRecipe(MillRecipe recipe) {
        return super.addRecipe(recipe);
    }
}
