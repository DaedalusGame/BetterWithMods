package betterwithmods.module.hardcore;

import betterwithmods.common.world.BWComponentScatteredFeaturePieces;
import betterwithmods.common.world.BWMapGenScatteredFeature;
import betterwithmods.module.Feature;
import betterwithmods.util.RecipeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class HCStructures extends Feature {
    private boolean disableRecipes;

    @Override
    public String getFeatureDescription() {
        return "Makes it so structures are looted within a radius of spawn and unlooted outside of that radius. \nEncourages exploration.\n" +
                "Also makes unlooted structures the only source of Enchanting Tables and Brewing Stands.";
    }

    @Override
    public void setupConfig() {
        disableRecipes = loadPropBool("Disable Recipes","Disable Recipes for blocks that generate only in structures, including Enchanting Tables and Brewing Stands", true);
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        if(disableRecipes) {
            RecipeUtils.removeRecipes(Blocks.ENCHANTING_TABLE);
            RecipeUtils.removeRecipes(Items.BREWING_STAND, 0);
        }
        MapGenStructureIO.registerStructure(BWMapGenScatteredFeature.Start.class, "BWTemple");
        MapGenStructureIO.registerStructureComponent(BWComponentScatteredFeaturePieces.DesertPyramid.class, "BWTeDP");
        MapGenStructureIO.registerStructureComponent(BWComponentScatteredFeaturePieces.JunglePyramid.class, "BWTeJP");
        MapGenStructureIO.registerStructureComponent(BWComponentScatteredFeaturePieces.SwampHut.class, "BWTeSH");
        MapGenStructureIO.registerStructureComponent(BWComponentScatteredFeaturePieces.Igloo.class, "BWIglu");
    }

    public static boolean isInRadius(World world, int x, int z) {
        BlockPos center = world.getSpawnPoint();
        return Math.sqrt(Math.pow(x - center.getX(), 2) + Math.pow(z - center.getZ(), 2)) < HCSpawn.HARDCORE_SPAWN_RADIUS;
    }
    @SubscribeEvent
    public void overrideScatteredFeature(InitMapGenEvent event) {
        if (event.getType().equals(InitMapGenEvent.EventType.SCATTERED_FEATURE))
            event.setNewGen(new BWMapGenScatteredFeature());
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public boolean hasTerrainSubscriptions() {
        return true;
    }
}
