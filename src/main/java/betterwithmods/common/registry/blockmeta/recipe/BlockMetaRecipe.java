package betterwithmods.common.registry.blockmeta.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/11/16
 */
public class BlockMetaRecipe {
    private final Block block;
    private final int meta;
    private final List<ItemStack> outputs;

    public BlockMetaRecipe(Block block, int meta, List<ItemStack> outputs) {
        this.block = block;
        this.meta = meta;
        this.outputs = outputs;
    }

    public boolean equals(Block block, int meta) {
        return this.block == block && (this.meta == meta || meta == OreDictionary.WILDCARD_VALUE || this.meta == OreDictionary.WILDCARD_VALUE);
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public ItemStack getStack() {
        return new ItemStack(block, 1, meta);
    }

    @Override
    public String toString() {
        return String.format("%s-> %s", getStack(), getOutputs());
    }
}