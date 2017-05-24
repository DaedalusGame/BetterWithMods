package betterwithmods.common.registry.bulk.recipes;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by tyler on 5/16/17.
 */
public class StokedCrucibleRecipe extends BulkRecipe {
    protected StokedCrucibleRecipe(ItemStack output, Object... inputs) {
        super(output, inputs);
    }

    public StokedCrucibleRecipe(@Nonnull ItemStack output, @Nonnull ItemStack secondaryOutput, Object... inputs) {
        super(output, secondaryOutput, inputs);
    }
}
