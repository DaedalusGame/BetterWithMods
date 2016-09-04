package betterwithmods.client.container;

import java.util.Iterator;

import betterwithmods.blocks.tile.TileEntityMill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMill extends Container
{
	private TileEntityMill mill;
	private int lastMillCounter;
	
	public ContainerMill(IInventory inv, TileEntityMill mill)
	{
		this.mill = mill;
		
		for(int j = 0; j < 3; j++)
		{
			addSlotToContainer(new Slot(mill, j, 62 + j * 18, 43));
		}
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 76 + i * 18));
			}
		}
		
		for(int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inv, i, 8 + i * 18, 134));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack stack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			
			if(index < 3)
			{
				if(!mergeItemStack(stack1, 3, this.inventorySlots.size(), true))
					return null;
			}
			else if(!mergeItemStack(stack1, 0, 3, false))
				return null;
			if(stack1.stackSize == 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();
		}
		return stack;
	}

	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.mill);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		Iterator<IContainerListener> it = this.listeners.iterator();
		while(it.hasNext())
		{
			IContainerListener craft = it.next();
			if(this.lastMillCounter != this.mill.grindCounter)
			{
				craft.sendProgressBarUpdate(this, 0, this.mill.grindCounter);
			}
		}
		this.lastMillCounter = this.mill.grindCounter;
	}
	
	@Override
	public void updateProgressBar(int index, int value)
	{
		if(index == 0)
			this.mill.grindCounter = value;
	}
}
