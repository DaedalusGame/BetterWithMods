package betterwithmods.common.blocks.mechanical;

import betterwithmods.api.capabilities.CapabilityAxle;
import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IAxle;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.blocks.tile.TileBasic;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
        byte newSignal = 0;
        int sources = 0;
        for (EnumFacing facing : getDirections()) {
            BlockPos offset = pos.offset(facing);


            IMechanicalPower mech = MechanicalUtil.getMechanicalPower(world, offset, facing);
            if (mech != null) {
                if (mech instanceof IAxle) {
                    IAxle axle = (IAxle) mech;
                    if (axle != null && axle.isFacing(this)) {
                        byte next = axle.getSignal();
                        if (next > newSignal) {
                            newSignal = next;
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }

                int power = mech.getMechanicalOutput(facing.getOpposite());
                if (power > this.power) {
                    sources++;
                    setPower(power);
                }

                if (newSignal == 0 && power <= getMaximumInput()) {
                    newSignal = (byte) (getMaximumSignal() + 1);
                }

            }
        }

        if (sources >= 2) {
            getBlock().overpower(world, pos);
            return;
        }
        if (newSignal > signal) {
            if (newSignal == 1) {
                getBlock().overpower(world, pos);
            }
            if (power > 0)
                setSignal((byte) (newSignal - 1));
        } else {
            setSignal((byte) 0);
        }

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
        //TODO
        return power;
    }

    //Not sure if this method is even useful ever.
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
        markDirty();
    }

    public void setPower(int power) {
        this.power = power;
        markDirty();
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    public int getPower() {
        return power;
    }
}

