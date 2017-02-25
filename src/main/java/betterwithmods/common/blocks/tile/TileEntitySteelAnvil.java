package betterwithmods.common.blocks.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/**
 * Created by blueyu2 on 11/21/16.
 */
public class TileEntitySteelAnvil extends TileEntity implements IInventory, ISidedInventory {

    private ItemStack matrix[] = new ItemStack[16];

    public TileEntitySteelAnvil() {
        super();
        for (int i = 0; i < 16; i++)
            matrix[i] = ItemStack.EMPTY;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        for (int i = 0; i < matrix.length; i++)
            matrix[i] = new ItemStack(tag.getCompoundTag("matrix" + i));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tag = super.writeToNBT(nbt);
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] != ItemStack.EMPTY)
                tag.setTag("matrix" + i, matrix[i].writeToNBT(new NBTTagCompound()));
            else
                tag.removeTag("matrix" + i);
        }
        return tag;
    }

    @Override
    public int getSizeInventory() {
        return 16;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : matrix) {
            if (stack == ItemStack.EMPTY) return true;
        }
        return false;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < matrix.length)
            return matrix[index];
        return ItemStack.EMPTY;
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index < matrix.length) {
            if (matrix[index] != ItemStack.EMPTY) {
                ItemStack split = matrix[index].splitStack(count);
                if (matrix[index].getCount() <= 0)
                    matrix[index] = ItemStack.EMPTY;
                return split;
            }
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index < matrix.length) {
            if (matrix[index] != ItemStack.EMPTY) {
                ItemStack ingredient = matrix[index];
                matrix[index] = ItemStack.EMPTY;
                return ingredient;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        if (index < matrix.length)
            matrix[index] = stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.getWorld().getTileEntity(pos) == this && player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < matrix.length; i++)
            this.matrix[i] = ItemStack.EMPTY;
    }

    @Override
    public String getName() {
        return "inv.steel_anvil.name";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }
}
