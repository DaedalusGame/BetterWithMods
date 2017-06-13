package betterwithmods.module.hardcore;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.world.BWMapGenVillage;
import betterwithmods.common.world.gen.village.*;
import betterwithmods.module.Feature;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tyler on 5/21/17.
 */
public class HCVillages extends Feature {
    public static AtomicInteger semiabandonedRadius = new AtomicInteger(), normalRadius = new AtomicInteger();
    @Override
    public String getFeatureDescription() {
        return "Makes it so villages with in the reaches of the spawn zone are abandoned and gradually gain more resources the further out. What this means to be gained by the player.";
    }

    @Override
    public void setupConfig() {
        semiabandonedRadius.set( loadPropInt("Semi-Abandoned Village Radius", "Block radius from 0,0 at which villages are now semi-abandoned, all villages inside this radius are abandoned", 2000));
        normalRadius.set( loadPropInt("Normal Village Radius", "Block radius from 0,0 at which villages are now normal, all villages in between this and semi-abandoned are semi-abandoned", 3000));
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @Override
    public void init(FMLInitializationEvent event) {

        VillagerRegistry.instance().registerVillageCreationHandler(new BWField1());
        VillagerRegistry.instance().registerVillageCreationHandler(new BWField2());
        VillagerRegistry.instance().registerVillageCreationHandler(new BWChurch());
        VillagerRegistry.instance().registerVillageCreationHandler(new BWWoodHut());
        VillagerRegistry.instance().registerVillageCreationHandler(new BWHouse1());
        VillagerRegistry.instance().registerVillageCreationHandler(new BWHouse2());
        VillagerRegistry.instance().registerVillageCreationHandler(new BWHouse3());
        VillagerRegistry.instance().registerVillageCreationHandler(new BWHouse4());
        VillagerRegistry.instance().registerVillageCreationHandler(new BWHouse5());


        MapGenStructureIO.registerStructure(BWMapGenVillage.AbandonedStart.class, new ResourceLocation(BWMod.MODID, "BWAbandonedStart").toString());
        MapGenStructureIO.registerStructureComponent(BWField1.class, new ResourceLocation(BWMod.MODID, "EmptyField").toString());
        MapGenStructureIO.registerStructureComponent(BWField2.class, new ResourceLocation(BWMod.MODID, "EmptyField2").toString());
        MapGenStructureIO.registerStructureComponent(BWChurch.class, new ResourceLocation(BWMod.MODID, "EmptyChurch").toString());
        MapGenStructureIO.registerStructureComponent(BWWoodHut.class, new ResourceLocation(BWMod.MODID, "EmptyWoodHut").toString());
        MapGenStructureIO.registerStructureComponent(BWHouse1.class, new ResourceLocation(BWMod.MODID, "EmptyHouse1").toString());
        MapGenStructureIO.registerStructureComponent(BWHouse2.class, new ResourceLocation(BWMod.MODID, "EmptyHouse2").toString());
        MapGenStructureIO.registerStructureComponent(BWHouse3.class, new ResourceLocation(BWMod.MODID, "EmptyHouse3").toString());
        MapGenStructureIO.registerStructureComponent(BWHouse4.class, new ResourceLocation(BWMod.MODID, "EmptyHouse4").toString());
        MapGenStructureIO.registerStructureComponent(BWHouse5.class, new ResourceLocation(BWMod.MODID, "EmptyHouse5").toString());
        MapGenStructureIO.registerStructureComponent(BWStart.class, new ResourceLocation(BWMod.MODID, "Start").toString());

    }

    @SubscribeEvent
    public void biomeSpecificVillage(BiomeEvent.GetVillageBlockID event) {
        if (event.getOriginal() == BWMBlocks.WOOD_TABLE.getDefaultState()) {
            event.setReplacement(event.getOriginal().withProperty(BlockPlanks.VARIANT, plankFromBiome(event.getBiome())));
        }
        if(event.getOriginal() == Blocks.DIRT) {
           if(BiomeDictionary.hasType(event.getBiome(), BiomeDictionary.Type.SANDY)) {
               event.setReplacement(Blocks.SAND.getDefaultState());
           }
        }
    }

    public BlockPlanks.EnumType plankFromBiome(Biome biome) {
        if (biome == null)
            return BlockPlanks.EnumType.OAK;
        else if (BiomeDictionary.areSimilar(biome, Biomes.TAIGA)) {
            return BlockPlanks.EnumType.SPRUCE;
        } else if (BiomeDictionary.areSimilar(biome, Biomes.FOREST)) {
            return BlockPlanks.EnumType.OAK;
        } else if (BiomeDictionary.areSimilar(biome, Biomes.ROOFED_FOREST)) {
            return BlockPlanks.EnumType.OAK;
        } else if (BiomeDictionary.areSimilar(biome, Biomes.JUNGLE)) {
            return BlockPlanks.EnumType.JUNGLE;
        } else if (BiomeDictionary.areSimilar(biome, Biomes.BIRCH_FOREST)) {
            return BlockPlanks.EnumType.BIRCH;
        } else if (BiomeDictionary.areSimilar(biome, Biomes.SAVANNA) || BiomeDictionary.areSimilar(biome, Biomes.DESERT)) {
            return BlockPlanks.EnumType.ACACIA;
        } else {

            return BlockPlanks.EnumType.OAK;
        }
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


