package betterwithmods.api.capabilities;

import betterwithmods.api.tile.IMechanicalPower;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class MechanicalCapability
{
    @CapabilityInject(IMechanicalPower.class)
    public static Capability<IMechanicalPower> MECHANICAL_POWER = null;

    public static class CapabilityMechanicalPower<T extends IMechanicalPower> implements Capability.IStorage<IMechanicalPower>
    {
        @Override
        public NBTBase writeNBT(Capability<IMechanicalPower> capability, IMechanicalPower mechanical, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IMechanicalPower> capability, IMechanicalPower mechanical, EnumFacing side, NBTBase nbt) {

        }
    }

    public static class DefaultMechanicalPower implements IMechanicalPower
    {
        @Override
        public int getMechanicalOutput(EnumFacing facing) {
            return 0;
        }

        @Override
        public int getMechanicalInput(EnumFacing facing) {
            return 0;
        }

        @Override
        public int getMinimumInput(EnumFacing facing) {
            return 0;
        }

        @Override
        public int getMaximumInput(EnumFacing facing) {
            return 0;
        }

        @Override
        public void readFromTag(NBTTagCompound tag) {

        }

        @Override
        public NBTTagCompound writeToTag(NBTTagCompound tag) {
            return new NBTTagCompound();
        }
    }
}
