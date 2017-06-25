package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.StokedCrucibleRecipe;
import net.minecraft.item.ItemStack;

public class StokedCrucibleManager extends CraftingManagerBulk<StokedCrucibleRecipe> {
    private static final StokedCrucibleManager instance = new StokedCrucibleManager();

    public static StokedCrucibleManager getInstance() {
        return instance;
    }

    public void addRecipe(ItemStack output, Object[] inputs) {
        addRecipe(output, ItemStack.EMPTY, inputs);
    }

    public void addRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        addRecipe(new StokedCrucibleRecipe(output, secondary, inputs));
    }

    @Override
    public StokedCrucibleRecipe addRecipe(StokedCrucibleRecipe recipe) {
        return super.addRecipe(recipe);
    }
}
