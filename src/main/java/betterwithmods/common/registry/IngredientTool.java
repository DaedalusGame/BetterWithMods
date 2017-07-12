package betterwithmods.common.registry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Predicate;

public class IngredientTool extends Ingredient {
    private Predicate<ItemStack> tool;
    private ItemStack example;

    public IngredientTool(Predicate<ItemStack> tool, ItemStack exampleStack) {
        this.tool = tool;
        example = exampleStack;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        return new ItemStack[] {example};
    }

    @Override
    public boolean apply (ItemStack stack) {
        return tool.test(stack);
    }
}
