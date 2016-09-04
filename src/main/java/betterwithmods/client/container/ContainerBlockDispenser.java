package betterwithmods.client.container;

import java.util.Iterator;

import betterwithmods.blocks.tile.TileEntityBlockDispenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerBlockDispenser extends Container
{
	private TileEntityBlockDispenser tile;
	private int nextSlot;
	
	public ContainerBlockDispenser(IInventory inv, TileEntityBlockDispenser tile)
	{
		this.tile = tile;
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				addSlotToContainer(new Slot(tile, j + i * 4, 53 + j * 18, 17 + i * 18));
			}
		}
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
			}
		}
		
		for(int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inv, i, 8 + i * 18, 160));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return tile.isUseableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
	{
		ItemStack stack = null;
		Slot slot = this.inventorySlots.get(slotIndex);
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			
			if(slotIndex < 16)
			{
				if(!mergeItemStack(stack1, 16, this.inventorySlots.size(), true))
					return null;
			}
			else if(!mergeItemStack(stack1, 0, 16, false))
				return null;
			
			if(stack1.stackSize == 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();
		}
		return stack;
	}
	
	@Override
	public ItemStack slotClick(int x, int dragType, ClickType type, EntityPlayer player)
	{
		if(x < 16)
			this.tile.nextIndex = 0;
		
		return super.slotClick(x, dragType, type, player);
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

		for(IContainerListener craft : this.listeners)
		{
			if(this.nextSlot != this.tile.getField(0))
				craft.sendProgressBarUpdate(this, 0, this.tile.getField(0));
		}
		
		this.nextSlot = this.tile.getField(0);
	}
	
	@Override
	public void updateProgressBar(int index, int value)
	{
		this.tile.setField(index, value);
	}

}
