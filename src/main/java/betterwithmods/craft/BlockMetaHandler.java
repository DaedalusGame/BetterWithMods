package betterwithmods.craft;

import betterwithmods.BWMod;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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
public abstract class BlockMetaHandler {

    private final ArrayList<BlockMetaRecipe> recipes = Lists.newArrayList();

    public void addRecipe(ItemStack input, ItemStack... products) {
        if (input.getItem() != null && input.getItem() instanceof ItemBlock) {
            addRecipe(((ItemBlock) input.getItem()).getBlock(), input.getMetadata(), products);
        } else {
            BWMod.logger.info("BlockMeta input: %s must be a block", input);
        }
    }

    public void addRecipe(Block block, int meta, ItemStack... products) {
        addRecipe(new BlockMetaRecipe(block, meta, Arrays.asList(products)));
    }

    public void addRecipe(BlockMetaRecipe recipe) {
        recipes.add(recipe);
    }

    public boolean contains(ItemStack stack) {
        if (stack == null)
            return false;
        assert stack.getItem() instanceof ItemBlock;
        return contains(((ItemBlock) stack.getItem()).getBlock(), stack.getMetadata());
    }

    public boolean contains(Block block, int meta) {
        return recipes.stream().filter(r -> r.equals(block, meta)).findFirst().isPresent();
    }

    public ArrayList<BlockMetaRecipe> getRecipes() {
        return recipes;
    }

    public BlockMetaRecipe getRecipe(ItemStack stack) {
        if (stack == null)
            return null;
        assert stack.getItem() instanceof ItemBlock;
        return getRecipe(((ItemBlock) stack.getItem()).getBlock(), stack.getMetadata());
    }

    public BlockMetaRecipe getRecipe(Block block, int meta) {
        Optional<BlockMetaRecipe> recipe = recipes.stream().filter(r -> r.equals(block, meta)).findFirst();
        if (recipe.isPresent())
            return recipe.get();
        return null;
    }

    public List<ItemStack> getProducts(Block block, int meta) {
        BlockMetaRecipe recipe = getRecipe(block, meta);
        if (recipe != null)
            return recipe.getOutputs();
        return null;
    }

}
