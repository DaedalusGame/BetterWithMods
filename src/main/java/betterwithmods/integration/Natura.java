package betterwithmods.integration;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import betterwithmods.util.NetherSpawnWhitelist;

public class Natura 
{
	public static void whitelistNetherBlocks()
	{

		if(Block.REGISTRY.getObject(new ResourceLocation("Natura", "soil.tainted")) != null)
			NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("Natura", "soil.tainted")));
		if(Block.REGISTRY.getObject(new ResourceLocation("Natura", "heatsand")) != null)
			NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("Natura", "heatsand")));
	}
}
