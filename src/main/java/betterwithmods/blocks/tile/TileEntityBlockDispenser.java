package betterwithmods.blocks.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityBlockDispenser extends TileBasicInventory {
    public int nextIndex;

    public TileEntityBlockDispenser() {
        this.nextIndex = 0;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }


    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("NextSlot"))
            this.nextIndex = tag.getInteger("NextSlot");
    }

    @Override
    public SimpleItemStackHandler createItemStackHandler() {
        return new SimpleItemStackHandler(this, true, 16);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        t.setInteger("NextSlot", this.nextIndex);
        return t;
    }

    public void addStackToInventory(ItemStack stack, BlockPos pos) {
        if (stack == null) return;
        for (int i = 0; i < 16; i++) {
            ItemStack check = this.inventory.getStackInSlot(i);
            if (ItemStack.areItemsEqual(stack, check) && check.stackSize < check.getMaxStackSize()) {
                if (check.hasTagCompound() || stack.hasTagCompound()) {
                    if (!ItemStack.areItemStackTagsEqual(stack, check))
                        continue;
                }
                check.stackSize++;
                this.inventory.setStackInSlot(i, check);
                return;
            }
        }
        int firstSlot = findFirstNullStack();
        if (firstSlot > -1) {
            this.inventory.setStackInSlot(firstSlot, stack);
        } else {
            EntityItem item = new EntityItem(worldObj, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
            item.setDefaultPickupDelay();
            worldObj.spawnEntityInWorld(item);
        }
    }

    private int findFirstNullStack() {
        for (int i = 0; i < 16; i++) {
            if (this.inventory.getStackInSlot(i) == null)
                return i;
        }
        return -1;
    }

    public ItemStack getNextStackFromInv() {
        ItemStack nextStack;

        if (this.nextIndex >= this.inventory.getSlots() || this.inventory.getStackInSlot(this.nextIndex) == null) {
            int slot = findNextValidSlot(this.nextIndex);

            if (slot < 0)
                return null;

            this.nextIndex = slot;
        }

        nextStack = this.inventory.getStackInSlot(this.nextIndex);

        int slot = findNextValidSlot(this.nextIndex);

        if (slot < 0)
            this.nextIndex = 0;
        else
            this.nextIndex = slot;

        return nextStack;
    }

    private int findNextValidSlot(int currentSlot) {
        for (int slot = currentSlot + 1; slot < this.inventory.getSlots(); slot++) {
            if (this.inventory.getStackInSlot(slot) != null)
                return slot;
        }

        for (int slot = 0; slot < currentSlot; slot++) {
            if (this.inventory.getStackInSlot(slot) != null)
                return slot;
        }

        if (this.inventory.getStackInSlot(currentSlot) != null)
            return currentSlot;

        return -1;
    }

}
