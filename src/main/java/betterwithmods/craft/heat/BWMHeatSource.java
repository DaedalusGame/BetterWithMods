package betterwithmods.craft.heat;

import net.minecraft.block.Block;

public class BWMHeatSource {
    public final Block block;
    public final int meta;
    public final int value;

    public BWMHeatSource(Block block, int meta, int value) {
        this.block = block;
        this.meta = meta;
        this.value = value;
    }
}
