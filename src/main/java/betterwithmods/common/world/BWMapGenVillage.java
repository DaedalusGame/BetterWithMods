package betterwithmods.common.world;

import betterwithmods.common.world.gen.village.*;
import betterwithmods.module.hardcore.HCVillages;
import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraft.world.gen.structure.StructureVillagePieces.*;

/**
 * Created by tyler on 5/21/17.
 */
public class BWMapGenVillage extends MapGenVillage {
    public enum VillageStatus {
        NORMAL(HCVillages.normalRadius.get()/16),
        SEMIABANDONED(HCVillages.semiabandonedRadius.get()/16),
        ABANDONED(0);
        public static final VillageStatus[] VALUES = values();
        private int radius;
        VillageStatus(int radius) {
            this.radius = radius;
        }

        public boolean inRadius(int x, int z) {
            System.out.printf("Checking %s @a <%s,%s> r:%s\n", this.name(),x,z,radius);
            return Math.abs(x) >=  radius || Math.abs(z) >= radius;
        }

        public static VillageStatus getStatus(int x, int z) {
            for(VillageStatus status: VALUES) {
                if(status.inRadius(x,z))
                    return status;
            }
            return NORMAL;
        }
    }

    public int getSize() {
        return ObfuscationReflectionHelper.getPrivateValue(MapGenVillage.class, this, 1);
    }

    @Override
    protected StructureStart getStructureStart(int x, int z) {
        VillageStatus status = VillageStatus.getStatus(x, z);
        System.out.printf("%s <%s,%s> /tp %s ~ %s\n", status.name().toLowerCase(), x,z,x * 16, z * 16);
        return new AbandonedStart(this.world, this.rand, x, z, getSize(), status);
    }

    public static class AbandonedStart extends StructureStart {
        private boolean hasMoreThanTwoComponents;
        private VillageStatus status;

        private static final Set<Class> REMOVE_STRUCTURE_ABANDONED = Sets.newHashSet(House4Garden.class, Church.class, House1.class, WoodHut.class, Hall.class, Field1.class, Field2.class, House2.class, House3.class, Torch.class);
        private static final Set<Class> REMOVE_STRUCTURE_SEMI = Sets.newHashSet(House4Garden.class, Church.class, House1.class, WoodHut.class, Hall.class, BWField1.class, BWField2.class, House2.class, House3.class, Torch.class);
        private static final Set<Class> REMOVE_STRUCTURE_NORMAL = Sets.newHashSet(BWChurch.class, BWField1.class, BWField2.class, BWHouse1.class, BWHouse2.class, BWHouse3.class, BWHouse4.class, BWHouse5.class, BWWoodHut.class);
        public AbandonedStart() {
        }

        public AbandonedStart(World worldIn, Random rand, int x, int z, int size, VillageStatus status) {
            super(x, z);
            this.status = status;
            List<StructureVillagePieces.PieceWeight> pieceWeights = getStructureVillageWeightedPieceList(rand, size).stream().filter(p -> {
                if (this.status == VillageStatus.ABANDONED)
                    return !REMOVE_STRUCTURE_ABANDONED.contains(p.villagePieceClass);
                else if (this.status == VillageStatus.SEMIABANDONED)
                    return !REMOVE_STRUCTURE_SEMI.contains(p.villagePieceClass);
                else if (this.status == VillageStatus.NORMAL)
                    return !REMOVE_STRUCTURE_NORMAL.contains(p.villagePieceClass);
                return true;
            }).collect(Collectors.toList());
            BWStart start = new BWStart(status,worldIn.getBiomeProvider(), 0, rand, (x << 4) + 2, (z << 4) + 2, pieceWeights, size);
            this.components.add(start);
            start.buildComponent(start, this.components, rand);
            List<StructureComponent> roads = start.pendingRoads;
            List<StructureComponent> houses = start.pendingHouses;
            while (!roads.isEmpty()) {
                int j = rand.nextInt(roads.size());
                StructureComponent road = roads.remove(j);
                if (road instanceof Village) {
                    Village h = (Village) road;
                    if(status != VillageStatus.NORMAL)
                        h.isZombieInfested = true;
                }
                road.buildComponent(start, this.components, rand);
            }
            while (!houses.isEmpty()) {
                int i = rand.nextInt(houses.size());
                StructureComponent house = houses.remove(i);
                if (house instanceof Village) {
                    Village h = (Village) house;
                    if(status != VillageStatus.NORMAL)
                        h.isZombieInfested = true;
                }
                house.buildComponent(start, this.components, rand);
            }

            this.updateBoundingBox();
            int k = 0;

            for (StructureComponent piece : this.components) {
                if (!(piece instanceof Road)) {
                    ++k;
                }
            }

            this.hasMoreThanTwoComponents = k > 2;

        }

        /**
         * currently only defined for Villages, returns true if Village has more than 2 non-road components
         */
        public boolean isSizeableStructure() {
            return this.hasMoreThanTwoComponents;
        }

        public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
            tagCompound.setInteger("Status", this.status.ordinal());
        }

        public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
            this.status = VillageStatus.VALUES[tagCompound.getInteger("Status")];
        }
    }


}
