package betterwithmods.common.world;

import betterwithmods.common.world.gen.village.BWField1;
import betterwithmods.common.world.gen.village.BWField2;
import betterwithmods.common.world.gen.village.BWStart;
import betterwithmods.module.hardcore.HCSpawn;
import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
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
        ABANDONED(0, 2000/32),
        SEMIABANDONED(2000/32, 3000/32),
        NORMAL(3000/32, Integer.MAX_VALUE);
        private int minRadius, maxRadius;
        public static final VillageStatus[] VALUES = values();

        VillageStatus(int minRadius, int maxRadius) {
            this.minRadius = minRadius;
            this.maxRadius = maxRadius;
        }

        public boolean inRadius(int x, int z) {
            int x1 = Math.abs(x), z1 = Math.abs(z);
            return (x1 >= minRadius && x1 < maxRadius) || (z1 >= minRadius && z1 < maxRadius);
        }

        public static VillageStatus getStatus(int x, int z) {
            for (VillageStatus status : VALUES)
                if (status.inRadius(x, z))
                    return status;
            return NORMAL;
        }
    }

    public static final int VILLAGE_RADIUS = HCSpawn.HARDCORE_SPAWN_RADIUS / 16;

    public int getSize() {
        return ObfuscationReflectionHelper.getPrivateValue(MapGenVillage.class, this, 1);
    }

    @Override
    protected StructureStart getStructureStart(int x, int z) {
        VillageStatus status = VillageStatus.getStatus(x, z);
        System.out.printf("%s /tp %s ~ %s\n", status.name().toLowerCase(), x * 16, z * 16);
        switch (status) {
            case ABANDONED:
                return new AbandonedStart(this.world, this.rand, x, z, getSize(), status);
            case SEMIABANDONED:
                return new AbandonedStart(this.world, this.rand, x, z, getSize(), status);
            default:
            case NORMAL:
                return super.getStructureStart(x, z);
        }
    }

    public static final Field zombify = ReflectionHelper.findField(Village.class, "isZombieInfested");

    public static void setZombify(Village village, boolean status) {
        zombify.setAccessible(true);
        try {
            zombify.set(village, status);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static class AbandonedStart extends StructureStart {
        private boolean hasMoreThanTwoComponents;
        private VillageStatus status;

        private static final Set<Class> REMOVE_STRUCTURE_ABANDONED = Sets.newHashSet(House4Garden.class, Church.class, House1.class, WoodHut.class, Hall.class, Field1.class, Field2.class, House2.class, House3.class, Torch.class);
        private static final Set<Class> REMOVE_STRUCTURE_SEMI = Sets.newHashSet(House4Garden.class, Church.class, House1.class, WoodHut.class, Hall.class, BWField1.class, BWField2.class, House2.class, House3.class, Torch.class);

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
                return true;
            }).collect(Collectors.toList());
            BWStart start = new BWStart(worldIn.getBiomeProvider(), 0, rand, (x << 4) + 2, (z << 4) + 2, pieceWeights, size);
            this.components.add(start);
            start.buildComponent(start, this.components, rand);
            List<StructureComponent> roads = start.pendingRoads;
            List<StructureComponent> houses = start.pendingHouses;
            while (!roads.isEmpty()) {
                int j = rand.nextInt(roads.size());
                StructureComponent road = roads.remove(j);
                if (road instanceof Village) {
                    Village h = (Village) road;
                    setZombify(h, true);
                }
                road.buildComponent(start, this.components, rand);
            }
            while (!houses.isEmpty()) {
                int i = rand.nextInt(houses.size());
                StructureComponent house = houses.remove(i);
                if (house instanceof Village) {
                    Village h = (Village) house;
                    setZombify(h, true);
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
