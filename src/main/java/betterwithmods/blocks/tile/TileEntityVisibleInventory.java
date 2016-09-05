package betterwithmods.blocks.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityVisibleInventory extends TileEntityDirectional {
    public short occupiedSlots;
    public SimpleItemStackHandler inventory = createItemStackHandler();

    public abstract SimpleItemStackHandler createItemStackHandler();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) inventory;
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

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = getUpdateTag();
        return new SPacketUpdateTileEntity(this.getPos(), getBlockMetadata(), tag);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager mgr, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.getNbtCompound();
        readFromNBT(tag);
        IBlockState state = worldObj.getBlockState(this.pos);
        worldObj.notifyBlockUpdate(this.pos, state, state, 3);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    public int filledSlots() {
        int fill = 0;
        int third = this.inventory.getSlots() / 3;
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            if (inventory.getStackInSlot(i) != null)
                fill++;
        }
        int percent = 0;
        if (fill > 0 && fill < third)
            percent = 1;
        else if (fill >= third && fill < (third * 2))
            percent = 2;
        else if (fill != 0 && fill <= this.inventory.getSlots())
            percent = 3;
        return percent;
    }
}
