package betterwithmods.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBark extends Item implements ITannin
{
	public String[] names = {"Oak", "Spruce", "Birch", "Jungle", "Acacia", "DarkOak", "BloodWood"};
	
	public ItemBark()
	{
		super();
		this.setHasSubtypes(true);
		this.setUnlocalizedName("bwm:bark");
		this.setMaxDamage(0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
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
