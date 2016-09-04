package betterwithmods;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class BWFuelHandler implements IFuelHandler
{

	@Override
	public int getBurnTime(ItemStack fuel) 
	{
		Item item = fuel.getItem();
		int meta = fuel.getItemDamage();
		if(item == BWRegistry.material && meta == 1)
			return 3200;
		else if(item == BWRegistry.bark)
			return 100;
		return 0;
	}

}
