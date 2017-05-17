package betterwithmods.common.registry.bulk.recipes;

import net.minecraft.item.ItemStack;

/**
 * Created by tyler on 5/16/17.
 */
public class CrucibleRecipe extends BulkRecipe {
    protected CrucibleRecipe(ItemStack output, Object... inputs) {
        super(output, inputs);
    }

    public CrucibleRecipe(ItemStack output, ItemStack secondaryOutput, Object... inputs) {
        super(output, secondaryOutput, inputs);
    }
}
