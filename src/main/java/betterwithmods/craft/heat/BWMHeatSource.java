package betterwithmods.craft.heat;

import net.minecraft.block.Block;

public class BWMHeatSource {
    public Block block;
    public int meta;
    public int value;

    public BWMHeatSource(Block block, int meta, int value) {
        this.block = block;
        this.meta = meta;
        this.value = value;
    }
}
