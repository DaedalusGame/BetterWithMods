package betterwithmods.client.container.inventory;

import betterwithmods.blocks.tile.TileEntitySteelAnvil;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

/**
 * Created by blueyu2 on 11/22/16.
 */
public class InventorySteelAnvilCrafting extends InventoryCrafting {
    private TileEntitySteelAnvil anvil;
    private Container container;

    public InventorySteelAnvilCrafting(Container containerIn, TileEntitySteelAnvil anvilIn) {
        super(containerIn, 4, 4);
        anvil = anvilIn;
        container = containerIn;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? ItemStack.EMPTY : anvil.getStackInSlot(index);
    }

    @Override
    public ItemStack getStackInRowAndColumn(int row, int column) {
        return row >= 0 && row < 4 && column >= 0 && column <= 4 ? this.getStackInSlot(row + column * 4) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = anvil.getStackInSlot(index);
        if (stack != ItemStack.EMPTY) {
            ItemStack itemstack;
            if (stack.getCount() <= count) {
                itemstack = stack.copy();
                anvil.setInventorySlotContents(index, ItemStack.EMPTY);
                this.container.onCraftMatrixChanged(this);
                return itemstack;
            } else {
                itemstack = stack.splitStack(count);
                this.container.onCraftMatrixChanged(this);
                return itemstack;
            }
        } else
            return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        anvil.setInventorySlotContents(index, stack);
        this.container.onCraftMatrixChanged(this);
    }
}
