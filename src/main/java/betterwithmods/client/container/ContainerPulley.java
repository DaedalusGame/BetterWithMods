package betterwithmods.client.container;

import betterwithmods.BWRegistry;
import betterwithmods.blocks.tile.TileEntityPulley;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPulley extends Container {
	private TileEntityPulley tile;

	public ContainerPulley(EntityPlayer player, TileEntityPulley tile) {
		this.tile = tile;

		for (int i = 0; i < 4; i++) {
			addSlotToContainer(new SlotItemHandler(tile.inventory, i, 53 + i * 18, 52) {
				@Override
				public boolean isItemValid(ItemStack stack) {
					return super.isItemValid(stack) && stack.getItem() == Item.getItemFromBlock(BWRegistry.rope);
				}
			});
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(
						new SlotItemHandler(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null),
								j + i * 9 + 9, 8 + j * 18, 93 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new SlotItemHandler(
					player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), i, 8 + i * 18, 151));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack clickedStack = null;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack processedStack = slot.getStack();
			clickedStack = processedStack.copy();

			if (index < 4) {
				if (!mergeItemStack(processedStack, 4, this.inventorySlots.size(), true))
					return null;
			} else if (!mergeItemStack(processedStack, 0, 3, false)) {
				return null;
			}

			if (processedStack.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return clickedStack;
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
	}

//	@Override
//	public void detectAndSendChanges() {
//		super.detectAndSendChanges();
//
//		Iterator<IContainerListener> it = this.listeners.iterator();
//
//		while (it.hasNext()) {
//			IContainerListener craft = it.next();
//
//			if (this.lastMechPower != this.tile.power)
//				craft.sendProgressBarUpdate(this, 0, this.tile.power);
//		}
//		this.lastMechPower = this.tile.power;
//	}

//	@Override
//	public void updateProgressBar(int index, int value) {
//		if (index == 0)
//			this.tile.power = (byte) value;
//	}

}
