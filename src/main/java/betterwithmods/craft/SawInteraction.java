package betterwithmods.craft;

import java.util.Hashtable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import betterwithmods.BWRegistry;

public class SawInteraction 
{
	private static Hashtable<String, ItemStack> woodProduct = new Hashtable();
	
	public static void addBlock(Block block, ItemStack product)
	{
		for(int i = 0; i < 4; i++)
		{
			addBlock(block, i, product);
		}
	}
	
	public static void addBlock(Block block, int meta, ItemStack product)
	{
		if(product == null)
			product = new ItemStack(BWRegistry.bark);
		woodProduct.put(block + ":" + meta, product);
	}
	
	public static boolean contains(Block block, int meta)
	{
		return woodProduct.containsKey(block + ":" + meta);
	}
	
	public static ItemStack getProduct(Block block, int meta)
	{
		return woodProduct.get(block + ":" + meta);
	}

	public static Hashtable<String, ItemStack> getWoodProducts() {
		return woodProduct;
	}
}
