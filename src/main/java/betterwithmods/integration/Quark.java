package betterwithmods.integration;

import betterwithmods.util.NetherSpawnWhitelist;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class Quark extends ModIntegration {

    @Override
    public void init() {
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("quark", "basalt")), 0);
    }
}
