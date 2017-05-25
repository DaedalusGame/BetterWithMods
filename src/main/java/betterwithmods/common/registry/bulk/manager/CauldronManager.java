package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;
import net.minecraft.item.ItemStack;

public class CauldronManager extends CraftingManagerBulk<CauldronRecipe> {
    private static final CauldronManager instance = new CauldronManager();

    public static CauldronManager getInstance() {
        return instance;
    }

    public void addRecipe(ItemStack output, Object[] inputs) {
        addRecipe(output, ItemStack.EMPTY, inputs);
    }

    public void addRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        addRecipe(new CauldronRecipe(output, secondary, inputs));
    }
}
