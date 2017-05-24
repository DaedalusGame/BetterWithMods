package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.StokedCrucibleRecipe;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class StokedCrucibleManager extends CraftingManagerBulk<StokedCrucibleRecipe> {
    private static final StokedCrucibleManager instance = new StokedCrucibleManager();
    public static StokedCrucibleManager getInstance() {
        return instance;
    }

    @Override
    public StokedCrucibleRecipe createRecipe(@Nonnull ItemStack output, @Nonnull ItemStack secondary, Object[] inputs) {
        return new StokedCrucibleRecipe(output,secondary,inputs);
    }
}
