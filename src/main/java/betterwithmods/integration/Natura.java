package betterwithmods.integration;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import betterwithmods.BWRegistry;
import betterwithmods.craft.HardcoreWoodInteraction;
import betterwithmods.craft.SawInteraction;
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
