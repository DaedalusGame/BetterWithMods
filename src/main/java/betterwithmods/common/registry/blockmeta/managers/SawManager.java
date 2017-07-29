package betterwithmods.common.registry.blockmeta.managers;

import betterwithmods.common.registry.blockmeta.recipe.SawRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.List;

public class SawManager extends BlockMetaManager<SawRecipe> {
    public static final SawManager WOOD_SAW = new SawManager();

    public static final SawManager STEEL_SAW = new SawManager();

    @Override
    public SawRecipe createRecipe(Block block, int meta, List<ItemStack> outputs) {
        return new SawRecipe(block, meta, outputs);
    }
}
