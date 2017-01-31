package betterwithmods.world;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;

/**
 * Created by blueyu2 on 11/27/16.
 */
public class BWMapGenScatteredFeature extends MapGenScatteredFeature {

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new BWMapGenScatteredFeature.Start(this.world, this.rand, chunkX, chunkZ);
    }

    public static class Start extends MapGenScatteredFeature.Start {
        public Start() {
        }

        Start(World worldIn, Random random, int chunkX, int chunkZ) {
            this(worldIn, random, chunkX, chunkZ, worldIn.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8)));
        }

        Start(World worldIn, Random random, int chunkX, int chunkZ, Biome biomeIn) {
            if (biomeIn != Biomes.JUNGLE && biomeIn != Biomes.JUNGLE_HILLS) {
                if (biomeIn == Biomes.SWAMPLAND) {
                    BWComponentScatteredFeaturePieces.SwampHut swampHut = new BWComponentScatteredFeaturePieces.SwampHut(random, chunkX * 16, chunkZ * 16);
                    this.components.add(swampHut);
                } else if (biomeIn != Biomes.DESERT && biomeIn != Biomes.DESERT_HILLS) {
                    if (biomeIn == Biomes.ICE_PLAINS || biomeIn == Biomes.COLD_TAIGA) {
                        BWComponentScatteredFeaturePieces.Igloo igloo = new BWComponentScatteredFeaturePieces.Igloo(random, chunkX * 16, chunkZ * 16);
                        this.components.add(igloo);
                    }
                } else {
                    BWComponentScatteredFeaturePieces.DesertPyramid desertPyramid = new BWComponentScatteredFeaturePieces.DesertPyramid(random, chunkX * 16, chunkZ * 16);
                    this.components.add(desertPyramid);
                }
            } else {
                BWComponentScatteredFeaturePieces.JunglePyramid junglePyramid = new BWComponentScatteredFeaturePieces.JunglePyramid(random, chunkX * 16, chunkZ * 16);
                this.components.add(junglePyramid);
            }

            this.updateBoundingBox();
        }
    }
}
