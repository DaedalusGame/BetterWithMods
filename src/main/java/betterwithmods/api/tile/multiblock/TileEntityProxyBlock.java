package betterwithmods.api.tile.multiblock;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileEntityProxyBlock extends TileEntity {
    private BlockPos controller;

    public BlockPos getController() {
        return controller;
    }

    public void setController(BlockPos controller) {
        this.controller = controller;
        this.markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound compound = super.writeToNBT(tag);
        if (controller != null) compound.setTag("controller", NBTUtil.createPosTag(controller));
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("controller")) controller = NBTUtil.getPosFromTag(tag.getCompoundTag("controller"));
    }

    public void notifyControllerOnBreak() {
        if (controller != null && this.world.getTileEntity(controller) instanceof TileEntityMultiblock) {
            ((TileEntityMultiblock) this.world.getTileEntity(controller)).destroyMultiblock();
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (controller != null && this.world.getTileEntity(controller) instanceof IExternalCapability)
            return ((IExternalCapability) this.world.getTileEntity(controller)).hasExternalCapability(pos, capability, facing);
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (controller != null && this.world.getTileEntity(controller) instanceof IExternalCapability)
            return ((IExternalCapability) this.world.getTileEntity(controller)).getExternalCapability(pos, capability, facing);
        return super.getCapability(capability, facing);
    }
}
