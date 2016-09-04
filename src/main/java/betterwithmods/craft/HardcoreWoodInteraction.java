package betterwithmods.craft;

import java.util.Hashtable;

import betterwithmods.BWRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class HardcoreWoodInteraction 
{
	private static Hashtable<String, ItemStack> woodProduct = new Hashtable();
	
	public static void addBlock(Block block, ItemStack barkOverride)
	{
		for(int i = 0; i < 4; i++)
		{
			addBlock(block, i, barkOverride);
		}
	}
	
	public static void addBlock(Block block, int meta, ItemStack barkOverride)
	{
		if(barkOverride == null)
			barkOverride = new ItemStack(BWRegistry.bark);
		woodProduct.put(block + ":" + meta, barkOverride);
	}
	
	public static boolean contains(Block block, int meta)
	{
		return woodProduct.containsKey(block + ":" + meta);
	}
	
	public static ItemStack getBarkOverride(Block block, int meta)
	{
		return woodProduct.get(block + ":" + meta);
	}

	public static Hashtable<String, ItemStack> getWoodProducts()
	{
		return woodProduct;
	}
}
