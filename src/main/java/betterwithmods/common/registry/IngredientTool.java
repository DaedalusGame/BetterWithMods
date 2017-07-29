package betterwithmods.common.registry;

import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IngredientTool extends Ingredient {
    private Predicate<ItemStack> tool;
    private ItemStack example;
    public static HashMap<ItemStack, ItemStack[]> TOOLS = Maps.newHashMap();

    public IngredientTool(Predicate<ItemStack> tool, ItemStack exampleStack) {
        this.tool = tool;
        example = exampleStack;
        if (!TOOLS.keySet().stream().anyMatch(s -> s.isItemEqual(example))) {
            TOOLS.put(example, collectAllTools(tool));
        }
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        ItemStack stack = TOOLS.keySet().stream().filter(s -> s.isItemEqual(example)).findFirst().orElse(null);
        if (stack != null)
            return TOOLS.get(stack);
        return new ItemStack[]{example};
    }

    @Override
    public boolean apply(ItemStack stack) {
        return tool.test(stack);
    }

    public static ItemStack[] collectAllTools(Predicate<ItemStack> tool) {
        List<ItemStack> list = Streams.stream(ForgeRegistries.ITEMS).map(ItemStack::new).filter(tool).collect(Collectors.toList());
        ItemStack[] stacks = new ItemStack[list.size()];
        return list.toArray(stacks);

    }
}
