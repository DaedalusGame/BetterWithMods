package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.StokedCauldronRecipe;
import net.minecraft.item.ItemStack;

public class StokedCauldronManager extends CraftingManagerBulk<StokedCauldronRecipe> {
    private static final StokedCauldronManager instance = new StokedCauldronManager();

    public static StokedCauldronManager getInstance() {
        return instance;
    }

    public void addRecipe(ItemStack output, Object[] inputs) {
        addRecipe(output, ItemStack.EMPTY, inputs);
    }

    public void addRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        addRecipe(new StokedCauldronRecipe(output, secondary, inputs));
    }
}
