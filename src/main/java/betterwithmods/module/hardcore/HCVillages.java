package betterwithmods.module.hardcore;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.world.BWMapGenVillage;
import betterwithmods.common.world.gen.village.Church;
import betterwithmods.common.world.gen.village.Field1;
import betterwithmods.common.world.gen.village.Field2;
import betterwithmods.common.world.gen.village.WoodHut;
import betterwithmods.module.Feature;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

/**
 * Created by tyler on 5/21/17.
 */
public class HCVillages extends Feature {
    @Override
    public void init(FMLInitializationEvent event) {

        VillagerRegistry.instance().registerVillageCreationHandler(new Field1());
        VillagerRegistry.instance().registerVillageCreationHandler(new Field2());
        VillagerRegistry.instance().registerVillageCreationHandler(new Church());
        VillagerRegistry.instance().registerVillageCreationHandler(new WoodHut());

        MapGenStructureIO.registerStructure(BWMapGenVillage.AbandonedStart.class, new ResourceLocation(BWMod.MODID,"BWAbandonedStart").toString());
        MapGenStructureIO.registerStructureComponent(Field1.class, new ResourceLocation(BWMod.MODID,"EmptyField").toString());
        MapGenStructureIO.registerStructureComponent(Field2.class, new ResourceLocation(BWMod.MODID,"EmptyField2").toString());
        MapGenStructureIO.registerStructureComponent(Church.class, new ResourceLocation(BWMod.MODID,"EmptyChurch").toString());
        MapGenStructureIO.registerStructureComponent(WoodHut.class, new ResourceLocation(BWMod.MODID,"EmptyWoodHut").toString());
    }

    @SubscribeEvent
    public void biomeSpecificVillage(BiomeEvent.GetVillageBlockID event) {
        if (event.getOriginal() == BWMBlocks.WOOD_TABLE.getDefaultState()) {
            event.setReplacement(event.getOriginal().withProperty(BlockPlanks.VARIANT, plankFromBiome(event.getBiome())));
        }
    }

    public BlockPlanks.EnumType plankFromBiome(Biome biome) {
        if (BiomeDictionary.areSimilar(biome, Biomes.TAIGA)) {
            return BlockPlanks.EnumType.SPRUCE;
        } else if (BiomeDictionary.areSimilar(biome, Biomes.FOREST)) {
            return BlockPlanks.EnumType.OAK;
        } else if (BiomeDictionary.areSimilar(biome, Biomes.ROOFED_FOREST)) {
            return BlockPlanks.EnumType.OAK;
        } else if (BiomeDictionary.areSimilar(biome, Biomes.JUNGLE)) {
            return BlockPlanks.EnumType.JUNGLE;
        } else if (BiomeDictionary.areSimilar(biome, Biomes.BIRCH_FOREST)) {
            return BlockPlanks.EnumType.BIRCH;
        } else if (BiomeDictionary.areSimilar(biome, Biomes.SAVANNA)) {
            return BlockPlanks.EnumType.ACACIA;
        }
        return BlockPlanks.EnumType.OAK;
    }

    @SubscribeEvent
    public void onGenerate(InitMapGenEvent event) {
        if (event.getType() == InitMapGenEvent.EventType.VILLAGE) {
            event.setNewGen(new BWMapGenVillage());
        }
    }

    @Override
    public boolean hasTerrainSubscriptions() {
        return true;
    }


}


