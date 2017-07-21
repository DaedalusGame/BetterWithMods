package betterwithmods.common.blocks.tile;

import betterwithmods.api.block.IOverpower;
import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.blocks.mechanical.BlockGearbox;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;

public class TileGearbox extends TileEntity implements IMechanicalPower, IOverpower {
    private int power;

    public void onChanged() {
        int power = this.getMechanicalInput(getFacing());
        if (getBlock().isRedstonePowered(world, pos)) {
            setPower(0);
            return;
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
        if (MechanicalUtil.isAxle(world, pos, facing.getOpposite())) {
            return MechanicalUtil.getPowerOutput(world, pos, facing.getOpposite());
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
    public void overpower(World world, BlockPos pos) {
        for (int i = 0; i < 10; i++) {
            world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.45F);
        }
        for (int i = 0; i < 5; i++) {
            float flX = pos.getX() + world.rand.nextFloat();
            float flY = pos.getY() + world.rand.nextFloat() * 0.5F + 1.0F;
            float flZ = pos.getZ() + world.rand.nextFloat();

            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, flX, flY, flZ, 0.0D, 0.0D, 0.0D);
        }
        world.setBlockState(pos, Blocks.PLANKS.getDefaultState());
    }
}
