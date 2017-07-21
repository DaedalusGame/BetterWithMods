package betterwithmods.common.blocks.mechanical;

import betterwithmods.api.capabilities.CapabilityAxle;
import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IAxle;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.blocks.tile.TileBasic;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by primetoxinz on 7/18/17.
 */
public class TileAxle extends TileBasic implements IMechanicalPower, IAxle {
    private final byte maxSignal;
    private final int maxPower;

    private byte signal;
    private int power;


    public TileAxle() {
        this.maxSignal = 3;
        this.maxPower = 4;
    }

    public TileAxle(int maxPower, byte maxSignal) {
        this.maxPower = maxPower;
        this.maxSignal = maxSignal;
    }

    public void onChanged() {

        byte findSignal = 0;
        int findPower = 0;
        int sources = 0;

        for (EnumFacing facing : getDirections()) {
            BlockPos offset = pos.offset(facing);
            IMechanicalPower mech = MechanicalUtil.getMechanicalPower(world, offset, facing);
            if (mech != null) {

                IAxle axle = MechanicalUtil.getAxle(world, offset, facing);
                if (axle != null) {
                    if (isFacing(axle)) {
                        byte next = axle.getSignal();
                        if (next > findSignal) {
                            findSignal = next;
                        }
                    }
                }
                int power = mech.getMechanicalOutput(facing.getOpposite());
                if (power >= 0) {
                    if (power > findPower) {
                        sources++;
                        if (axle != null) {
                            if (axle.getSignal() >= findSignal)
                                findPower = power;
                        } else {
                            findPower = power;
                        }
                    }
                    if (axle == null && power <= getMaximumInput()) {
                        findSignal = (byte) (getMaximumSignal() + 1);
                    }
                }

            }
        }

        setPower(findPower);

        if (sources >= 2 || this.power > this.maxPower) {
            getBlock().overpower(world, pos);
            return;
        }
        byte newSignal = 0;
        if (findSignal > signal) {
            if (findSignal == 1) {
                getBlock().overpower(world, pos);
            }
            if (power > 0)
                newSignal = (byte) (findSignal - 1);
        } else {
            newSignal = 0;
            setPower(0);
        }
        if (newSignal != this.signal)
            setSignal(newSignal);
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setByte("signal", signal);
        compound.setInteger("power", power);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.signal = compound.getByte("signal");
        this.power = compound.getInteger("power");
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return super.hasCapability(capability, facing) || capability == CapabilityMechanicalPower.MECHANICAL_POWER || capability == CapabilityAxle.AXLE;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityMechanicalPower.MECHANICAL_POWER)
            return CapabilityMechanicalPower.MECHANICAL_POWER.cast(this);
        if (capability == CapabilityAxle.AXLE)
            return CapabilityAxle.AXLE.cast(this);
        return super.getCapability(capability, facing);
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        if (facing.getAxis() == getAxis()) {
            IAxle axle = MechanicalUtil.getAxle(world, pos.offset(facing), facing);
            if (axle != null && axle.getSignal() > this.getSignal())
                return 0;
            return power;
        }
        return 0;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        return maxPower;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return getMaximumInput();
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public byte getSignal() {
        return signal;
    }

    @Override
    public byte getMaximumSignal() {
        return maxSignal;
    }

    @Override
    public int getMaximumInput() {
        return maxPower;
    }

    public EnumFacing[] getDirections() {
        return getBlock().getAxisDirections(world.getBlockState(pos));
    }

    @Override
    public EnumFacing.Axis getAxis() {
        return getBlock().getAxis(world.getBlockState(pos));
    }

    public BlockAxle getBlock() {
        return (BlockAxle) this.getBlockType();
    }

    public void setSignal(byte signal) {
        this.signal = signal;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        getBlock().setActive(world, pos, power > 0);
    }

    public int getPower() {
        return power;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", signal, power, pos);
    }
}

