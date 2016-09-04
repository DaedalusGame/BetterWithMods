package betterwithmods.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FakeCrafter extends InventoryCrafting
{
	public FakeCrafter(int x, int y)
	{
		super(fakeContainer.instance, x, y);
	}
	
	private static class fakeContainer extends Container
	{
		public static fakeContainer instance = new fakeContainer();
		
		@Override
		public boolean canInteractWith(EntityPlayer player)
		{
			return false;
		}
	}
}
