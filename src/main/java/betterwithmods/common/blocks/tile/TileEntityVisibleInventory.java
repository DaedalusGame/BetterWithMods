package betterwithmods.common.blocks.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityVisibleInventory extends TileEntityDirectional {
    public short occupiedSlots;
    public ItemStackHandler inventory = createItemStackHandler();

    public abstract ItemStackHandler createItemStackHandler();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        return super.getCapability(capability, facing);
    }

    public abstract String getName();

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.merge(inventory.serializeNBT());
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        inventory = createItemStackHandler();
        inventory.deserializeNBT(tag);
        super.readFromNBT(tag);
    }


    public int filledSlots() {
        int fill = 0;
        for (int i = 0; i < this.getMaxVisibleSlots(); i++) {
            if (inventory.getStackInSlot(i) != ItemStack.EMPTY)
                fill++;
        }
        return fill;
    }

    //Mostly for aesthetic purposes, primarily so the filter in the filtered hopper doesn't reclaimCount.
    public abstract int getMaxVisibleSlots();
}
