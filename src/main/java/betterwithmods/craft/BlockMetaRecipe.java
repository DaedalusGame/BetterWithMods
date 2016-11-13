package betterwithmods.craft;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/11/16
 */
public class BlockMetaRecipe {
    private Block block;
    private int meta;
    private List outputs;
    public BlockMetaRecipe(Block block, int meta, List outputs) {
        this.block = block;
        this.meta = meta;
        this.outputs = outputs;
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


}