package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.CrucibleRecipe;
import net.minecraft.item.ItemStack;

public class CrucibleManager extends CraftingManagerBulk<CrucibleRecipe> {
    private static final CrucibleManager instance = new CrucibleManager();

    public static CrucibleManager getInstance() {
        return instance;
    }
    @Override
    public CrucibleRecipe createRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        return new CrucibleRecipe(output,secondary,inputs);
    }
}
