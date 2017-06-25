package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.CrucibleRecipe;
import net.minecraft.item.ItemStack;

public class CrucibleManager extends CraftingManagerBulk<CrucibleRecipe> {
    private static final CrucibleManager instance = new CrucibleManager();

    public static CrucibleManager getInstance() {
        return instance;
    }

    public void addRecipe(ItemStack output,Object[] inputs) {
       addRecipe(output,ItemStack.EMPTY,inputs);
    }
    public void addRecipe(ItemStack output, ItemStack secondary, Object[] inputs) {
        addRecipe(new CrucibleRecipe(output,secondary,inputs));
    }

    @Override
    public CrucibleRecipe addRecipe(CrucibleRecipe recipe) {
        return super.addRecipe(recipe);
    }
}
