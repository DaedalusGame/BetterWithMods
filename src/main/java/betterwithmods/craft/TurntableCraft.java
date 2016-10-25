package betterwithmods.craft;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TurntableCraft {
    private IBlockState outputState;
    private List<ItemStack> scrap;

    public TurntableCraft(IBlockState output, ItemStack... scraps) {
        this.scrap = new ArrayList<ItemStack>();
        if (scraps.length > 0 && scraps != null) {
            for (ItemStack scrap : scraps)
                this.scrap.add(scrap);
        }
        if (output != null)
            this.outputState = output;
        else
            this.outputState = Blocks.AIR.getDefaultState();
    }

    public IBlockState getOutputState() {
        return outputState;
    }

    public List<ItemStack> getScrap() {
        return scrap;
    }
}
