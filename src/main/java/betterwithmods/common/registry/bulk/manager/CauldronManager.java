package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CauldronManager extends CraftingManagerBulk<CauldronRecipe> {
    private static final CauldronManager instance = new CauldronManager();
    public static CauldronManager getInstance() {
        return instance;
    }

    @Override
    public CauldronRecipe createRecipe(@Nonnull ItemStack output, @Nonnull ItemStack secondary, Object[] inputs) {
        return new CauldronRecipe(output,secondary,inputs);
    }
}
