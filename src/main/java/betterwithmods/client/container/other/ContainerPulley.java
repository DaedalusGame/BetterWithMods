package betterwithmods.client.container.other;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.tile.TileEntityPulley;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPulley extends Container {
    private final TileEntityPulley tile;

    public ContainerPulley(EntityPlayer player, TileEntityPulley tile) {
        this.tile = tile;

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotItemHandler(tile.inventory, i, 53 + i * 18, 52) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return super.isItemValid(stack) && stack.getItem() == Item.getItemFromBlock(BWMBlocks.ROPE);
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
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack processedStack = slot.getStack();
            clickedStack = processedStack.copy();

            if (index < 4) {
                if (!mergeItemStack(processedStack, 4, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!mergeItemStack(processedStack, 0, 3, false)) {
                return ItemStack.EMPTY;
            }

            if (processedStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return clickedStack;
    }
}
