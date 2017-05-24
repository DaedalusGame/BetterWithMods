package betterwithmods.common.registry.blockmeta.managers;

import betterwithmods.BWMod;
import betterwithmods.common.registry.blockmeta.recipe.BlockMetaRecipe;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/11/16
 */
public abstract class BlockMetaManager<T extends BlockMetaRecipe> {

    private final ArrayList<T> recipes = Lists.newArrayList();

    public abstract T createRecipe(Block block, int meta, List<ItemStack> outputs);

    public void addRecipe(ItemStack input, ItemStack... products) {
        if (!input.isEmpty() && input.getItem() instanceof ItemBlock) {
            addRecipe(((ItemBlock) input.getItem()).getBlock(), input.getMetadata(), products);
        } else {
            BWMod.logger.info("BlockMeta inputs: %s must be a block", input);
        }
    }

    public void addRecipe(Block block, int meta, ItemStack... products) {
        addRecipe(createRecipe(block, meta, Arrays.asList(products)));
    }

    public void addRecipe(T recipe) {
        recipes.add(recipe);
    }

    public boolean contains(ItemStack stack) {
        return !(stack == null || stack.isEmpty() || !(stack.getItem() instanceof ItemBlock)) && contains(((ItemBlock) stack.getItem()).getBlock(), stack.getMetadata());
    }

    public boolean contains(Block block, int meta) {
        return recipes.stream().filter(r -> r.equals(block, meta)).findFirst().isPresent();
    }

    public ArrayList<T> getRecipes() {
        return recipes;
    }

    public T getRecipe(ItemStack stack) {
        assert stack.getItem() instanceof ItemBlock;
        return getRecipe(((ItemBlock) stack.getItem()).getBlock(), stack.getMetadata());
    }

    public T getRecipe(Block block, int meta) {
        Optional<T> recipe = recipes.stream().filter(r -> r.equals(block, meta)).findFirst();
        return recipe.orElse(null);
    }

    public NonNullList<ItemStack> getProducts(Block block, int meta) {
        T recipe = getRecipe(block, meta);
        if (recipe != null)
            return recipe.getOutputs();
        return NonNullList.create();
    }

    public List<T> removeRecipes(ItemStack input) {
        List<T> removed = Lists.newArrayList();
        for (T ir : recipes) {
            if (ir.getStack().isItemEqual(input)) {
                removed.add(ir);
            }
        }
        return removed;
    }

}
