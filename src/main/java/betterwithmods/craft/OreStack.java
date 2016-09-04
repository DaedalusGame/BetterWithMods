package betterwithmods.craft;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreStack 
{
	private String oreName;
	private int stackSize;
	
	public OreStack(String name)
	{
		this(name, 1);
	}
	
	public OreStack(String name, int stack)
	{
		this.oreName = name;
		this.stackSize = stack;
	}

	public OreStack copy()
	{
		return new OreStack(oreName, stackSize);
	}
	
	public String getOreName()
	{
		return this.oreName;
	}
	
	public List<ItemStack> getOres()
	{
		if(OreDictionary.getOres(oreName).size() > 0)
			return OreDictionary.getOres(oreName);
		return null;
	}
	
	public int getStackSize()
	{
		return this.stackSize;
	}
}
