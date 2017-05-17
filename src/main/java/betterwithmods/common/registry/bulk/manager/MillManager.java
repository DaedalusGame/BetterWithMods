package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import net.minecraft.item.ItemStack;

public class MillManager extends CraftingManagerBulk<MillRecipe> {
    private static final MillManager instance = new MillManager();
    public static MillManager getInstance() {
        return instance;
    }
    @Override
    public MillRecipe createRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        return new MillRecipe(output,secondary,inputs);
    }
}
