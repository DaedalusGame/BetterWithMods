package betterwithmods.common.blocks.tile;

import betterwithmods.api.capabilities.MechanicalCapability;
import betterwithmods.api.tile.IMechanicalMachine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by primetoxinz on 6/20/17.
 */
public abstract class TileVisibleMachine extends TileEntityVisibleInventory implements IMechanicalMachine {
    protected boolean active;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setBoolean("active", active);
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("active"))
            active = compound.getBoolean("active");
        super.readFromNBT(compound);
    }

    @Override
    public boolean isActive() {
        return active;
    }
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == MechanicalCapability.MECHANICAL_POWER || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == MechanicalCapability.MECHANICAL_POWER) {
            return MechanicalCapability.MECHANICAL_POWER.cast(this);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void validateContents() {
        validate(world,pos);
    }
}
