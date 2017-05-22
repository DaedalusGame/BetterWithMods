package betterwithmods.common.world;

import betterwithmods.module.hardcore.HCSpawn;
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
    public static final int VILLAGE_RADIUS = HCSpawn.HARDCORE_SPAWN_RADIUS / 16;

    public int getSize() {
        return ObfuscationReflectionHelper.getPrivateValue(MapGenVillage.class, this, 1);
    }

    @Override
    protected StructureStart getStructureStart(int x, int z) {
        if (Math.abs(x) >= VILLAGE_RADIUS || Math.abs(z) >= VILLAGE_RADIUS) {
            System.out.printf("normal /tp %s ~ %s\n", x * 16, z * 16);
            return super.getStructureStart(x, z);
        } else {
            System.out.printf("abandoned /tp %s ~ %s\n", x * 16, z * 16);
            return new AbandonedStart(this.world, this.rand, x, z, getSize());
        }
    }


    public static class AbandonedStart extends StructureStart {
        /**
         * well ... thats what it does
         */
        private boolean hasMoreThanTwoComponents;

        public AbandonedStart() {
        }

        private static final Set<Class> REMOVE_STRUCTURE = Sets.newHashSet(House4Garden.class, Church.class, House1.class, WoodHut.class, Hall.class, Field1.class, Field2.class, House2.class, House3.class);

        public AbandonedStart(World worldIn, Random rand, int x, int z, int size) {
            super(x, z);
            List<StructureVillagePieces.PieceWeight> pieceWeights = getStructureVillageWeightedPieceList(rand, size).stream().filter(p -> !REMOVE_STRUCTURE.contains(p.villagePieceClass)).collect(Collectors.toList());
            StructureVillagePieces.Start start = new StructureVillagePieces.Start(worldIn.getBiomeProvider(), 0, rand, (x << 4) + 2, (z << 4) + 2, pieceWeights, size);
            this.components.add(start);
            start.buildComponent(start, this.components, rand);
            List<StructureComponent> roads = start.pendingRoads;
            List<StructureComponent> houses = start.pendingHouses;
            while (!roads.isEmpty()) {
                int j = rand.nextInt(roads.size());
                StructureComponent road = roads.remove(j);
                road.buildComponent(start, this.components, rand);
            }
            while (!houses.isEmpty()) {
                int i = rand.nextInt(houses.size());
                StructureComponent house = houses.remove(i);
                System.out.println(house.getClass().getSimpleName());
                if (!house.getClass().equals(Torch.class))
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
        }

        public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
        }
    }


}
