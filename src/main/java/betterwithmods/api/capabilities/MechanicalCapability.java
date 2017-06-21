package betterwithmods.api.capabilities;

import betterwithmods.api.tile.IMechanicalPower;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class MechanicalCapability {
    @SuppressWarnings("CanBeFinal")
    @CapabilityInject(IMechanicalPower.class)
    public static Capability<IMechanicalPower> MECHANICAL_POWER = null;

    public static class CapabilityMechanicalPower implements Capability.IStorage<IMechanicalPower> {
        @Override
        public NBTBase writeNBT(Capability<IMechanicalPower> capability, IMechanicalPower mechanical, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IMechanicalPower> capability, IMechanicalPower mechanical, EnumFacing side, NBTBase nbt) {

        }
    }

    public static class DefaultMechanicalPower implements IMechanicalPower {


        @Override
        public int getMechanicalOutput(World world, BlockPos pos, EnumFacing facing) {
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
}
