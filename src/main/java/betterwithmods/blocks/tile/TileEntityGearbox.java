package betterwithmods.blocks.tile;

import betterwithmods.api.block.IMechanical;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.api.capabilities.MechanicalCapability;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.blocks.BlockAdvGearbox;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;

public class TileEntityGearbox extends TileEntity implements ITickable, IMechanicalPower {
    private int powerLevel;
    private byte outputs;
    private int refreshTime = 0;

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 1;
    }

    @Override
    public void update() {
        if (getBlockType() instanceof BlockAdvGearbox) {
            if (((BlockAdvGearbox) getBlockType()).isGearboxOn(getWorld(), pos)) {
                if (refreshTime == 20) {
                    findOutputs();
                } else {
                    refreshTime++;
                }
            } else if (powerLevel != 0)
                powerLevel = 0;
        }
    }

    private void findOutputs() {
        outputs = 0;
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (((BlockAdvGearbox) getBlockType()).canInputPowerToSide(getWorld(), pos, facing)) {
                if (powerLevel != getMechanicalInput(facing))
                    powerLevel = getMechanicalInput(facing);
            } else if (MechanicalUtil.isAxle(getWorld().getBlockState(pos.offset(facing)).getBlock())) {
                outputs++;
            }
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        return capability == MechanicalCapability.MECHANICAL_POWER
                || super.hasCapability(capability, facing);
    }

    @Nonnull
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == MechanicalCapability.MECHANICAL_POWER)
            return MechanicalCapability.MECHANICAL_POWER.cast(this);
        return super.getCapability(capability, facing);
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        if (getBlockType() instanceof IMechanical) {
            if (((IMechanical) getBlockType()).getMechPowerLevelToFacing(getWorld(), pos, facing) > 0) {
                if (outputs > 0)
                    return powerLevel / outputs;
                else
                    return powerLevel;
            }
        }
        return 0;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        int power = powerLevel;
        if (getBlockType() instanceof IMechanicalBlock) {
            if (((IMechanicalBlock) getBlockType()).canInputPowerToSide(getWorld(), getPos(), facing)) {
                power = Math.min(MechanicalUtil.searchForAdvMechanical(getWorld(), getPos(), facing), getMaximumInput(facing));
            }
        }
        return power;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 100;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        readFromTag(tag);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        writeToTag(t);
        return t;
    }

    @Override
    public void readFromTag(NBTTagCompound tag) {
        powerLevel = tag.getInteger("Power");
        outputs = tag.getByte("Outputs");
    }

    @Override
    public NBTTagCompound writeToTag(NBTTagCompound tag) {
        tag.setInteger("Power", powerLevel);
        tag.setByte("Outputs", outputs);
        return tag;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}
