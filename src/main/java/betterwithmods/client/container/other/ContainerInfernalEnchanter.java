package betterwithmods.client.container.other;

import betterwithmods.common.blocks.tile.TileEntityInfernalEnchanter;
import betterwithmods.common.items.ItemArcaneScroll;
import betterwithmods.util.InvUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

/**
 * Created by tyler on 9/11/16.
 */
public class ContainerInfernalEnchanter extends Container {
    public int[] enchantLevels;
    public int xpSeed;
    private TileEntityInfernalEnchanter tile;
    private ItemStackHandler handler;

    public ContainerInfernalEnchanter(EntityPlayer player, TileEntityInfernalEnchanter tile) {
        this.tile = tile;
        this.enchantLevels = new int[5];
        handler = new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                onContextChanged(this);
            }
        };
        this.xpSeed = player.getXPSeed();
        IItemHandler playerInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        addSlotToContainer(new SlotItemHandler(handler, 0, 17, 37));
        addSlotToContainer(new SlotItemHandler(handler, 1, 17, 75));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new SlotItemHandler(playerInv, j + i * 9 + 9, 8 + j * 18, 129 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new SlotItemHandler(playerInv, i, 8 + i * 18, 187));
        }

    }


    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        if (id > 0 && id < 3) {
            enchantLevels[id] = data;
        } else if (id == 3) {
            xpSeed = data;
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : this.listeners) {
            this.broadcastData(listener);
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        broadcastData(listener);
    }

    public void broadcastData(IContainerListener listener) {
        listener.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
        listener.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
        listener.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
        listener.sendProgressBarUpdate(this, 3, this.xpSeed & -16);
    }

    public void onContextChanged(IItemHandler handler) {

        ItemStack scroll = handler.getStackInSlot(0);
        ItemStack toEnchant = handler.getStackInSlot(1);

        Enchantment enchantment = null;
        int enchantCount = 1;
        int maxBookcase = tile.getBookcaseCount();
        if (!scroll.isEmpty() && !toEnchant.isEmpty()) {
            enchantment = scroll.getTagCompound() != null ? Enchantment.getEnchantmentByID(scroll.getTagCompound().getInteger("enchant")) : null;
            enchantCount = EnchantmentHelper.getEnchantments(toEnchant).size() + 1;


            //1,2,3,4
            //8,15,23,30
//            System.out.println(enchantment.getTranslatedName(-1) + "," + enchantCount + "," + maxBookcase + "," + enchantment.getMaxLevel());

        }
        for (int i = 1; i <= enchantLevels.length; i++) {
            if (enchantment == null || i > enchantment.getMaxLevel())
                enchantLevels[i - 1] = 0;
            else
                enchantLevels[i - 1] = (int) Math.ceil(30 / Math.min(enchantLevels.length, enchantment.getMaxLevel())) * i * enchantCount;
        }
        detectAndSendChanges();
    }

    @Override
    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack previous = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            // [...] Custom behaviour
            try {
                if (index > 1) {
                    if (current.getItem() instanceof ItemArcaneScroll) {
                        if (!this.mergeItemStack(current, 0, 1, true))
                            return ItemStack.EMPTY;
                    } else if (!this.mergeItemStack(current, 1, 2, true))
                        return ItemStack.EMPTY;
                } else {
                    if (!this.mergeItemStack(current, 2, inventorySlots.size() - 2, false))
                        return ItemStack.EMPTY;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (current.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();

            if (current.getCount() == previous.getCount())
                return ItemStack.EMPTY;
            slot.onTake(playerIn, current);
        }
        return previous;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);

            if (!stack.isEmpty() && !playerIn.getEntityWorld().isRemote)
                InvUtils.ejectStack(playerIn.getEntityWorld(), playerIn.posX, playerIn.posY, playerIn.posZ, stack);
        }
    }

}
