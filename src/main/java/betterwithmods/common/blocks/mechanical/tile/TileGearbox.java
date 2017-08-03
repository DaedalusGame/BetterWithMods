package betterwithmods.common.blocks.mechanical.tile;

import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.blocks.mechanical.BlockGearbox;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;

public class TileGearbox extends TileEntity implements IMechanicalPower {
    private int power;
    private int maxPower;

    public TileGearbox() {
    }

    public TileGearbox(int maxPower) {
        this.maxPower = maxPower;
    }

    public void onChanged() {

        int power = this.getMechanicalInput(getFacing());
        if (MechanicalUtil.isRedstonePowered(world,pos)) {
            setPower(0);
            return;
        }
        if (world.rand.nextInt(10) == 0 && power > getMaximumInput(getFacing())) {
            getBlock().overpower(world, pos);
        }
        if (power != this.power) {
            setPower(power);
        }
        markDirty();
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 1;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        return capability == CapabilityMechanicalPower.MECHANICAL_POWER
                || super.hasCapability(capability, facing);
    }

    @Nonnull
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityMechanicalPower.MECHANICAL_POWER)
            return CapabilityMechanicalPower.MECHANICAL_POWER.cast(this);
        return super.getCapability(capability, facing);
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        if (facing != getFacing())
            return power;
        return -1;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        BlockPos pos = getPos().offset(facing);
        if (!(MechanicalUtil.getMechanicalPower(world, pos, facing.getOpposite()) instanceof TileGearbox))
            return MechanicalUtil.getPowerOutput(world, pos, facing.getOpposite());
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return this.maxPower;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        power = tag.getInteger("power");
        maxPower = tag.getInteger("maxPower");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        tag.setInteger("power", power);
        tag.setInteger("maxPower", maxPower);
        return t;
    }


    public BlockGearbox getBlock() {
        return (BlockGearbox) getBlockType();
    }

    public EnumFacing getFacing() {
        return getBlock().getFacing(world, pos);
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        getBlock().setActive(world, pos, power > 0);
    }

    @Override
    public String toString() {
        return String.format("%s", power);
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }

    @Override
    public BlockPos getPos() {
        return super.getPos();
    }

}
