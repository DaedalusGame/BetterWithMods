package betterwithmods.integration;

import net.minecraftforge.fml.common.registry.GameRegistry;
import betterwithmods.util.NetherSpawnWhitelist;

public class Natura 
{
	public static void whitelistNetherBlocks()
	{
		if(GameRegistry.findBlock("Natura", "soil.tainted") != null)
			NetherSpawnWhitelist.addBlock(GameRegistry.findBlock("Natura", "soil.tainted"));
		if(GameRegistry.findBlock("Natura", "heatsand") != null)
			NetherSpawnWhitelist.addBlock(GameRegistry.findBlock("Natura", "heatsand"));
	}
}
