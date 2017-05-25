package betterwithmods.common.registry.bulk.recipes;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by tyler on 5/16/17.
 */
public class MillRecipe extends BulkRecipe {
    private int grindType;
    public MillRecipe(int grindType, @Nonnull ItemStack output, @Nonnull ItemStack secondaryOutput, Object... inputs) {
        super(output, secondaryOutput, inputs);
        this.grindType = grindType;
    }

    public int getGrindType() {
        return grindType;
    }
}
