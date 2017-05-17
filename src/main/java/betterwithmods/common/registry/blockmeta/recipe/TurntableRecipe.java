package betterwithmods.common.registry.blockmeta.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by tyler on 4/20/17.
 */
public class TurntableRecipe extends BlockMetaRecipe {
    private Block result;
    private int resultMeta;
    public TurntableRecipe(ItemStack inputBlock, ItemStack outputBlock, List<ItemStack> scraps) {
        this(((ItemBlock)inputBlock.getItem()).getBlock(), inputBlock.getMetadata(),((ItemBlock)outputBlock.getItem()).getBlock(), outputBlock.getMetadata(),scraps);
    }
    public TurntableRecipe(Block block, int meta, Block result, int resultMeta, List<ItemStack> scraps) {
        super(block, meta, scraps);
        this.result = result;
        this.resultMeta = resultMeta;
    }

    public ItemStack getResult() {
        if (result == null)
            return ItemStack.EMPTY;
        return new ItemStack(result, 1, resultMeta);
    }

    @Override
    public String toString() {
        return String.format("%s-> %s + [%s]", getStack(), getResult(), getOutputs());
    }
}
