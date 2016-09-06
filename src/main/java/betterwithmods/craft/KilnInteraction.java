package betterwithmods.craft;

import java.util.Hashtable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class KilnInteraction 
{
	private static Hashtable<String, ItemStack> cookables = new Hashtable();
	
	public static void addBlockRecipe(Block block, ItemStack output)
	{
		for(IBlockState state : block.getBlockState().getValidStates()) {
			if(!contains(block, block.damageDropped(state)))
				cookables.put(block + ":" + block.damageDropped(state), output);
		}
	}
	
	public static void addBlockRecipe(Block block, int meta, ItemStack output)
	{
		cookables.put(block + ":" + meta, output);
	}
	
	public static boolean contains(Block block, int meta)
	{
		return cookables.containsKey(block + ":" + meta);
	}
	
	public static ItemStack getProduct(Block block, int meta)
	{
		return cookables.get(block + ":" + meta);
	}

	public static Hashtable<String, ItemStack> getCookables()
	{
		return cookables;
	}
}
