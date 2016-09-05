package betterwithmods.client.container;

import betterwithmods.blocks.BlockBTWPane;
import betterwithmods.blocks.BlockNewBTWPane;
import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Iterator;

public class ContainerFilteredHopper extends Container
{
	private TileEntityFilteredHopper tile;
	private byte lastMechPower;
	
	public ContainerFilteredHopper(EntityPlayer player, TileEntityFilteredHopper tile)
	{
		this.tile = tile;
		this.lastMechPower = 0;
		
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				addSlotToContainer(new SlotItemHandler(tile.inventory, j + i * 9, 8 + j * 18, 60 + i * 18));
			}
		}
		
		addSlotToContainer(new SlotItemHandler(tile.inventory, 18, 80, 37));
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				addSlotToContainer(new SlotItemHandler(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null), j + i * 9 + 9, 8 + j * 18, 111 + i * 18));
			}
		}
		
		for(int i = 0; i < 9; i++)
		{
			addSlotToContainer(new SlotItemHandler(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null), i, 8 + i * 18, 169));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return tile.isUseableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack clickedStack = null;
		Slot slot = this.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack processedStack = slot.getStack();
			clickedStack = processedStack.copy();
			
			if(index < 19)
			{
				if(!mergeItemStack(processedStack, 19, this.inventorySlots.size(), true))
					return null;
			}
			else if(isItemFilter(processedStack))
			{
				if(!mergeItemStack(processedStack, 18, 19, false))
					return null;
			}
			else if(!mergeItemStack(processedStack, 0, 18, false))
			{
				return null;
			}
			
			if(processedStack.stackSize == 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();
		}
		return clickedStack;
	}
	
	public boolean isItemFilter(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemBlock)
		{
			Block block = ((ItemBlock)stack.getItem()).getBlock();
			if(block instanceof BlockLadder || block instanceof BlockTrapDoor || stack.getItem() == Item.getItemFromBlock(Blocks.SOUL_SAND) || stack.getItem() == Item.getItemFromBlock(Blocks.IRON_BARS) || block instanceof BlockBTWPane || block instanceof BlockNewBTWPane)
				return true;
		}
		return false;
	}

	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		Iterator<IContainerListener> it = this.listeners.iterator();
		
		while(it.hasNext())
		{
			IContainerListener craft = it.next();
			
			if(this.lastMechPower != this.tile.power)
				craft.sendProgressBarUpdate(this, 0, this.tile.power);
		}
		this.lastMechPower = this.tile.power;
	}
	
	@Override
	public void updateProgressBar(int index, int value)
	{
		if(index == 0)
			this.tile.power = (byte)value;
	}

}
