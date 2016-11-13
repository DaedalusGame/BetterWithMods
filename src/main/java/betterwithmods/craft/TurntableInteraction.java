package betterwithmods.craft;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class TurntableInteraction extends BlockMetaHandler {
    public static TurntableInteraction INSTANCE = new TurntableInteraction();
    public void addTurntableRecipe(ItemStack inputBlock, ItemStack outputBlock, ItemStack... scraps) {
        assert inputBlock.getItem() instanceof ItemBlock;
        assert outputBlock == null || outputBlock.getItem() instanceof ItemBlock;
        if(outputBlock == null)
            addTurntableRecipe(((ItemBlock) inputBlock.getItem()).getBlock(), inputBlock.getMetadata(), null, 0,scraps);
        else
            addTurntableRecipe(((ItemBlock) inputBlock.getItem()).getBlock(), inputBlock.getMetadata(), ((ItemBlock) outputBlock.getItem()).getBlock(), outputBlock.getMetadata(),scraps);
    }
    public void addTurntableRecipe(Block block, int meta, Block result, int resultMeta, ItemStack... scraps) {
        addRecipe(new TurntableRecipe(block, meta, result, resultMeta, Arrays.asList(scraps)));
    }
    public class TurntableRecipe extends BlockMetaRecipe {
        private Block result;
        private int resultMeta;
        public TurntableRecipe(Block block, int meta, Block result, int resultMeta, List scraps) {
            super(block, meta, scraps);
            this.result = result;
            this.resultMeta = resultMeta;
        }
        public ItemStack getResult() {
            return new ItemStack(result,1,resultMeta);
        }


    }
}
