package betterwithmods.api;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InterModCommsHelper 
{
	public static void registerLogInteractionRecipe(String message, Block inputBlock, int inputMeta, ItemStack output)
	{
		ItemStack input = new ItemStack(inputBlock, 1, inputMeta);
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound inputTag = new NBTTagCompound();
		input.writeToNBT(inputTag);
		tag.setTag("Input", inputTag);
		if(output != null)
		{
			NBTTagCompound outputTag = new NBTTagCompound();
			output.writeToNBT(outputTag);
			tag.setTag("Output", outputTag);
		}
		sendMessage(message, tag);
	}
	
	public static void registerWhitelistRecipe(String message, Block block, int meta)
	{
		ItemStack input = new ItemStack(block, 1, meta);
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound inputTag = new NBTTagCompound();
		input.writeToNBT(inputTag);
		tag.setTag("Spawn", inputTag);
		sendMessage(message, tag);
	}
	
	public static void registerHeatInteractionRecipe(String message, Block inputBlock, int inputMeta, int heatValue)
	{
		ItemStack input = new ItemStack(inputBlock, 1, inputMeta);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("HeatValue", heatValue);
		NBTTagCompound inputTag = new NBTTagCompound();
		input.writeToNBT(inputTag);
		tag.setTag("Input", inputTag);
		sendMessage(message, tag);
	}

	public static void registerTurntableRecipe(Block input, int inputMeta, Block output, int outputMeta, ItemStack... scrap) {
		ItemStack stack = new ItemStack(input, 1, inputMeta);
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound recipeTag = new NBTTagCompound();
		stack.writeToNBT(recipeTag);
		tag.setTag("Input", recipeTag);
		stack = new ItemStack(output, 1, outputMeta);
		recipeTag = new NBTTagCompound();
		stack.writeToNBT(recipeTag);
		tag.setTag("Output", recipeTag);
		recipeTag = new NBTTagCompound();
		if(scrap.length > 0) {
			recipeTag.setInteger("ScrapCount", scrap.length);
			for(int i = 0; i < scrap.length; i++) {
				NBTTagCompound itemTag = new NBTTagCompound();
				scrap[i].writeToNBT(itemTag);
				recipeTag.setTag("Item_" + i, itemTag);
			}
			tag.setTag("Scrap", recipeTag);
		}
		sendMessage("addTurntableRecipe", tag);
	}
	
	public static void registerInteractionRecipe(String message, Block inputBlock, int inputMeta, ItemStack output)
	{
		ItemStack input = new ItemStack(inputBlock, 1, inputMeta);
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound inputTag = new NBTTagCompound();
		input.writeToNBT(inputTag);
		tag.setTag("Input", inputTag);
		NBTTagCompound outputTag = new NBTTagCompound();
		output.writeToNBT(outputTag);
		tag.setTag("Output", outputTag);
		sendMessage(message, tag);
	}
	
	public static void registerBulkRecipe(String message, ItemStack output, ItemStack secondary, Object[] inputs)
	{
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound outputTags = new NBTTagCompound();
		NBTTagCompound item = new NBTTagCompound();
		output.writeToNBT(item);
		outputTags.setTag("Output", item);
		if(secondary != null)
		{
			item = new NBTTagCompound();
			secondary.writeToNBT(item);
			outputTags.setTag("Secondary", item);
		}
		tag.setTag("Outputs", outputTags);
		NBTTagCompound inputTags = new NBTTagCompound();
		int inputLength = inputs.length;
		inputTags.setInteger("InputLength", inputLength);
		for(int i = 0; i < inputLength; i++)
		{
			item = new NBTTagCompound();
			Object obj = inputs[i];
			if(obj instanceof Item)
			{
				ItemStack stack = new ItemStack((Item)obj);
				stack.writeToNBT(item);
				inputTags.setTag("Input_" + i, item);
			}
			else if(obj instanceof Block)
			{
				ItemStack stack = new ItemStack((Block)obj);
				stack.writeToNBT(item);
				inputTags.setTag("Input_" + i, item);
			}
			else if(obj instanceof ItemStack)
			{
				((ItemStack)obj).writeToNBT(item);
				inputTags.setTag("Input_" + i, item);
			}
			else if(obj instanceof String)
			{
				inputTags.setString("Input_" + i, (String)obj);
			}
			else if(obj instanceof Integer)
			{
				inputTags.setInteger("Input_" + i, (Integer)obj);
			}
		}
		tag.setTag("Inputs", inputTags);
		sendMessage(message, tag);
	}
	
	public static void sendMessage(String message, NBTTagCompound tag)
	{
		if(!Loader.isModLoaded("betterwithmods") || Loader.instance().activeModContainer() == null)
			return;
		try
		{
			FMLInterModComms.sendMessage("betterwithmods", message, tag);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
