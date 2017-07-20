package betterwithmods.common.blocks.tile;

import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.BWSounds;
import betterwithmods.common.blocks.mechanical.BlockGearbox;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;

public class TileGearbox extends TileEntity implements IMechanicalPower {
    private int power;

    public void onChanged() {
        int power = getMechanicalInput(getFacing());

        //TODO Redstone
        if (power != this.power) {
            if (this.power == 0) {
                world.playSound(null, pos, BWSounds.WOODCREAK, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.25F + 0.25F);
            }
            this.power = power;
            markDirty();
        }
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
        return 0;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        BlockPos pos = getPos().offset(facing);
        if (MechanicalUtil.isAxle(world, pos, facing.getOpposite())) {
            int power = MechanicalUtil.getPowerOutput(world, pos, facing.getOpposite());

            return power;
        }
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 4;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        power = tag.getInteger("power");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        tag.setInteger("power", power);
        return t;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
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
        world.scheduleBlockUpdate(pos, getBlock(), 5, 5);
    }
}
