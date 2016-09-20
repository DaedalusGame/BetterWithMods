package betterwithmods.items;

import net.minecraft.block.BlockPlanks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import betterwithmods.BWMod;
import betterwithmods.client.BWCreativeTabs;

public class ItemBark extends Item implements ITannin, IBWMItem
{

	public ItemBark()
	{
		super();
    	this.setCreativeTab(BWCreativeTabs.BWTAB);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);

	}

	@Override
	public int getMaxMeta() {
		return BlockPlanks.EnumType.values().length;
	}

	@Override
	public String getLocation(int meta) {
		return BWMod.MODID + ":bark_" + BlockPlanks.EnumType.byMetadata(meta).getName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for(int i = 0; i < 6; i++)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + stack.getItemDamage();
	}

	@Override
	public int getStackSizeForTanning(int meta) 
	{
		switch(meta)
		{
		case 0:
			return 5;
		case 1:
			return 3;
		case 3:
			return 2;
		case 4:
			return 4;
		case 5:
			return 2;
			default: return 8;
		}
	}

}
