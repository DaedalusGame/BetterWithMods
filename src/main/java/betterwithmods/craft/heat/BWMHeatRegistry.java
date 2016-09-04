package betterwithmods.craft.heat;

import java.util.Hashtable;

import net.minecraft.block.Block;

public class BWMHeatRegistry
{
	private static Hashtable<String, BWMHeatSource> heatSources = new Hashtable();
	
	public static void setBlockHeatRegistry(Block block, int meta, int value)
	{
		BWMHeatSource entry = new BWMHeatSource(block, meta, value);
		heatSources.put(block + ":" + meta, entry);
	}
	
	public static void setBlockHeatRegistry(Block block, int value)
	{
		for(int i = 0; i < 16; i++)
		{
			setBlockHeatRegistry(block, i, value);
		}
	}
	
	public static boolean contains(Block block, int meta)
	{
		return heatSources.containsKey(block + ":" + meta);
	}
	
	public static BWMHeatSource get(Block block, int meta)
	{
		return heatSources.get(block + ":" + meta);
	}
	
	public static Hashtable<String, BWMHeatSource> getHeatRegistry()
	{
		return heatSources;
	}
}
