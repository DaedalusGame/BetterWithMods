package betterwithmods.common.registry.blockmeta.managers;

import betterwithmods.common.registry.blockmeta.recipe.TurntableRecipe;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TurntableManager extends BlockMetaManager<TurntableRecipe> {
    public static TurntableManager INSTANCE = new TurntableManager();

    @Override
    public TurntableRecipe createRecipe(Block block, int meta, List<ItemStack> outputs) {
      return null;
    }

    public void addTurntableRecipe(ItemStack inputBlock, ItemStack outputBlock, ItemStack... scraps) {
        if (inputBlock != ItemStack.EMPTY && inputBlock.getItem() instanceof ItemBlock) {
            Block iBlock = ((ItemBlock) inputBlock.getItem()).getBlock();
            int iMeta = inputBlock.getMetadata();
            if (outputBlock == ItemStack.EMPTY)
                addTurntableRecipe(iBlock, iMeta, null, 0, scraps);
            else if (outputBlock.getItem() instanceof ItemBlock) {
                Block oBlock = ((ItemBlock) outputBlock.getItem()).getBlock();
                int oMeta = outputBlock.getMetadata();
                addTurntableRecipe(iBlock, iMeta, oBlock, oMeta, scraps);
            }
        }
    }

    public void addTurntableRecipe(Block block, int meta, Block result, int resultMeta, ItemStack... scraps) {
        addRecipe(new TurntableRecipe(block, meta, result,resultMeta, Lists.newArrayList(scraps)));
    }


}
