package betterwithmods.common.registry.blockmeta.managers;

import betterwithmods.common.registry.blockmeta.recipe.KilnRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.List;

public class KilnManager extends BlockMetaManager<KilnRecipe> {
    public static final KilnManager INSTANCE = new KilnManager();

    @Override
    public KilnRecipe createRecipe(Block block, int meta, List<ItemStack> outputs) {
        return new KilnRecipe(block, meta, outputs);
    }
}
