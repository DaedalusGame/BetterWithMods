package betterwithmods.client.container;

import java.util.Iterator;

import betterwithmods.blocks.tile.TileEntityCookingPot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerCookingPot extends Container
{
	private TileEntityCookingPot tile;
	private int lastCookCounter;
	
	public ContainerCookingPot(IInventory inv, TileEntityCookingPot tile)
	{
		this.tile = tile;
		this.lastCookCounter = 0;
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(tile, j + i * 9, 8 + j * 18, 43 + i * 18));
			}
		}
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 111 + i * 18));
			}
		}
		
		for(int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inv, i, 8 + i * 18, 169));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return this.tile.isUseableByPlayer(player);
	}
	
	@Override
	public ItemStack slotClick(int x, int dragType, ClickType type, EntityPlayer player)
	{
		this.tile.markDirty();
		return super.slotClick(x, dragType, type, player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
	{
		ItemStack stack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotIndex);
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			
			if(slotIndex < 27)
			{
				if(!mergeItemStack(stack1, 27, this.inventorySlots.size(), true))
					return null;
			}
			else if(!mergeItemStack(stack1, 0, 27, false))
				return null;
			
			if(stack1.stackSize < 1)
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
		listener.sendAllWindowProperties(this, this.tile);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		Iterator it = this.listeners.iterator();
		
		while(it.hasNext())
		{
			IContainerListener craft = (IContainerListener) it.next();
			
			if(this.lastCookCounter != this.tile.scaledCookCounter)
				craft.sendProgressBarUpdate(this, 0, this.tile.scaledCookCounter);
		}
		this.lastCookCounter = this.tile.scaledCookCounter;
	}
	
	@Override
	public void updateProgressBar(int index, int value)
	{
		if(index == 0)
			this.tile.scaledCookCounter = value;
	}

}
