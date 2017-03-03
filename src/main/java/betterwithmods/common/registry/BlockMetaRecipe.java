package betterwithmods.common.registry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

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
    private final String type;

    public BlockMetaRecipe(String type, Block block, int meta, List<ItemStack> outputs) {
        this.block = block;
        this.meta = meta;
        this.outputs = outputs;
        this.type = type;
    }

    public boolean equals(Block block, int meta) {
        return this.block == block && this.meta == meta;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public ItemStack getStack() {
        return new ItemStack(block, 1, meta);
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%s-> %s", getStack(), getOutputs());
    }
}