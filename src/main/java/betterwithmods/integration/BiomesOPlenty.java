package betterwithmods.integration;

import betterwithmods.util.NetherSpawnWhitelist;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class BiomesOPlenty extends ModIntegration {
    @Override
    public void init() {
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("biomesoplenty", "grass")), 1);
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("biomesoplenty", "grass")), 6);
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("biomesoplenty", "flesh")));
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("biomesoplenty", "ash_block")));
    }
}
