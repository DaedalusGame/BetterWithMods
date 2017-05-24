package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.StokedCauldronRecipe;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class StokedCauldronManager extends CraftingManagerBulk<StokedCauldronRecipe> {
    private static final StokedCauldronManager instance = new StokedCauldronManager();
    public static StokedCauldronManager getInstance() {
        return instance;
    }
    @Override
    public StokedCauldronRecipe createRecipe(@Nonnull ItemStack output, @Nonnull ItemStack secondary, Object[] inputs) {
        return new StokedCauldronRecipe(output,secondary,inputs);
    }
}
