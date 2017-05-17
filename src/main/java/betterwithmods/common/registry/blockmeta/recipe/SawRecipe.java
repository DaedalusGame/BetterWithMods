package betterwithmods.common.registry.blockmeta.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by tyler on 5/16/17.
 */
public class SawRecipe extends BlockMetaRecipe {
    public SawRecipe(Block block, int meta, List<ItemStack> outputs) {
        super(block, meta, outputs);
    }
}
