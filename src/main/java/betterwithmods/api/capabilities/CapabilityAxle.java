package betterwithmods.api.capabilities;

import betterwithmods.api.tile.IAxle;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityAxle {
    @SuppressWarnings("CanBeFinal")
    @CapabilityInject(IAxle.class)
    public static Capability<IAxle> AXLE = null;

    public static class Impl implements Capability.IStorage<IAxle> {
        @Override
        public NBTBase writeNBT(Capability<IAxle> capability, IAxle mechanical, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IAxle> capability, IAxle mechanical, EnumFacing side, NBTBase nbt) {

        }
    }

    public static class Default implements IAxle {

        @Override
        public byte getSignal() {
            return 0;
        }

        @Override
        public byte getMaximumSignal() {
            return 0;
        }

        @Override
        public int getMaximumInput() {
            return 0;
        }

        @Override
        public EnumFacing[] getDirections() {
            return new EnumFacing[0];
        }
    }
}
