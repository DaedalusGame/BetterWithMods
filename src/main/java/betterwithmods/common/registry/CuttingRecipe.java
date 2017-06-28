package betterwithmods.common.registry;

import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;

/**
 * Created by blueyu2 on 12/12/16.
 */
public class CuttingRecipe extends ToolDamageRecipe {
    public CuttingRecipe(ItemStack result, ItemStack input) {
        super(result, input, stack -> stack.getItem() instanceof ItemShears);
    }
}
