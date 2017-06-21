package betterwithmods.common.blocks.tile.gen;

import betterwithmods.api.block.IMechanical;
import betterwithmods.api.capabilities.MechanicalCapability;
import betterwithmods.api.tile.IMechanicalPower;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by tyler on 8/5/16.
 */
public class TileEntityCreativeGen extends TileEntity implements IMechanicalPower {
    //Unless you increase this, expect to see the TESR to pop in as you approach.
    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return super.getMaxRenderDistanceSquared() * 3.0D;
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
    public int getMechanicalOutput(World world, BlockPos pos, EnumFacing facing) {
        if (getBlockType() instanceof IMechanical) {
            if (((IMechanical) getBlockType()).getMechPowerLevelToFacing(getWorld(), pos, facing) > 0)
                return 20;
        }
        return 0;
    }

    @Override
    public int getMechanicalInput(World world, BlockPos pos, EnumFacing facing) {
        return 0;
    }

    @Override
    public boolean canInputPower(Mode mode, EnumFacing facing) {
        return false;
    }
}
