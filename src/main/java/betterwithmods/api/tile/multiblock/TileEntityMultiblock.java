package betterwithmods.api.tile.multiblock;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityMultiblock extends TileEntity implements IExternalCapability {
    private EnumFacing facing;
    private IMultiblock multiblock;
    private boolean isFormed;

    public IMultiblock getMultiblock() {
        return multiblock;
    }

    public void setMultiblock(IMultiblock multiblock) {
        this.multiblock = multiblock;
    }

    public void destroyMultiblock() {
        if (multiblock != null) {
            getMultiblock().destroyMultiblock(world, pos, world.getBlockState(pos), getFacing());
        }
    }

    public boolean isFormed() {
        return isFormed;
    }

    public void setFormed(boolean formed) {
        isFormed = formed;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound compound = super.writeToNBT(tag);
        compound.setString("facing", facing.getName());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        facing = EnumFacing.UP;
        if (tag.hasKey("facing")) facing = EnumFacing.byName(tag.getString("facing"));
        super.readFromNBT(tag);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager man, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }
}
