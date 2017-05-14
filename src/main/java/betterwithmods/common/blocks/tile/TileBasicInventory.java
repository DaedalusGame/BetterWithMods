package betterwithmods.common.blocks.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by tyler on 9/4/16.
 */
public abstract class TileBasicInventory extends TileBasic {

    public SimpleStackHandler inventory = createItemStackHandler();
    public abstract int getInventorySize();

    public SimpleStackHandler createItemStackHandler() {
        return new SimpleStackHandler(getInventorySize(),this);
    }

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

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.merge(inventory.serializeNBT());
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if(inventory == null)
            inventory = createItemStackHandler();
        inventory.deserializeNBT(compound);
        super.readFromNBT(compound);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        writeToNBT(new NBTTagCompound());
    }
}
