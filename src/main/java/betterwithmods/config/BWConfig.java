package betterwithmods.config;

import betterwithmods.BWMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class BWConfig {
    public static final String HARDCORE = "Hardcore";
    public static final String VANILLA_TWEAKS = "Vanilla Tweaks";
    public static final String MODPACK_TWEAKS = "Modpack Tweaks";
    public static final String DEBUG = "Debug";
    public static final String PULLEY = "Pulley";
    public static final String MOD_COMPAT = "Mod Compat";
    public static Configuration config;
    public static boolean hardcoreGunpowder;
    public static boolean hardcoreLumber;
    public static boolean hardcoreBuckets;
    public static boolean hardcoreFluidContainer;
    public static boolean hardcoreHunger;
    public static boolean hardcoreBuoy;
    public static boolean hardcoreSpawn;
    public static boolean hardcoreBeds;
    public static boolean hardcoreVillagers;
    public static boolean steelRequiresEnd;
    public static boolean netherSpawn;
    public static boolean slimeSpawn;
    public static boolean produceDung;
    public static boolean dropsGearbox;
    public static boolean dropsSaw;
    public static boolean dropsHopper;
    public static boolean dropsPulley;
    public static boolean dropsMill;
    public static boolean dropsTurntable;
    public static boolean debug;
    public static boolean dumpBlockData;
    public static int maxPlatformBlocks;
    public static float upSpeed;
    public static float downSpeed;

    public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        //config.addCustomCategoryComment(HARDCORE, "Hardcore settings");
        hardcoreGunpowder = config.get(HARDCORE, "Hardcore Gunpowder", true, "Creepers and Ghasts will drop brimstone or niter instead of gunpowder").getBoolean(true);
        hardcoreLumber = config.get(HARDCORE, "Hardcore Lumberjack", true, "Logs break into planks if you don't use an axe").getBoolean(true);
        hardcoreBuckets = config.get(HARDCORE, "Hardcore Buckets", true, "Water sources cannot be moved outside the End").getBoolean(true);
        hardcoreFluidContainer = config.get(HARDCORE, "Hardcore Buckets Affects Modded Fluid Containers", true).getBoolean(true);
        hardcoreHunger = config.get(HARDCORE, "Hardcore Hunger", true, "Saturation becomes fat, while hunger and health stats affect your movement").getBoolean(true);
        hardcoreBuoy = config.get(HARDCORE, "Hardcore Buoy", true, "Buoyant items will float").getBoolean(true);
        hardcoreSpawn = config.get(HARDCORE, "Hardcore Spawn", true, "Your spawn point will be randomized").getBoolean(true);
        hardcoreBeds = config.get(HARDCORE, "Hardcore Beds", true, "You will be unable to sleep in a bed").getBoolean(true);
        hardcoreVillagers = config.get(HARDCORE, "Hardcore Villagers", true, "Villagers can be manually bred").getBoolean(true);
        steelRequiresEnd = config.get(HARDCORE, "Steel Requires End", true, "Soulforged Steel will require Ender Flux").getBoolean(true);

        //config.addCustomCategoryComment(VANILLA_TWEAKS, "Vanilla tweaks settings");
        netherSpawn = config.get(VANILLA_TWEAKS, "Prevent Nether Spawns on Non-Nether Materials", true)
                .getBoolean(true);
        slimeSpawn = config.get(VANILLA_TWEAKS, "Prevent Slimes Spawning on Non-Stone Non-Dirt Materials", true)
                .getBoolean(true);
        produceDung = config.get(VANILLA_TWEAKS, "Animals Produce Dung", true).getBoolean(true);

        //config.addCustomCategoryComment(MODPACK_TWEAKS, "Tweaks for modpack makers");
        dropsGearbox = config.get(MODPACK_TWEAKS, "Gearbox generating drops when overpowered", true).getBoolean(true);
        dropsSaw = config.get(MODPACK_TWEAKS, "Saw generating drops when overpowered", true).getBoolean(true);
        dropsHopper = config.get(MODPACK_TWEAKS, "Hopper generating drops when overpowered", true).getBoolean(true);
        dropsPulley = config.get(MODPACK_TWEAKS, "Pulley generating drops when overpowered", true).getBoolean(true);
        dropsMill = config.get(MODPACK_TWEAKS, "Mill generating drops when overpowered", true).getBoolean(true);
        dropsTurntable = config.get(MODPACK_TWEAKS, "Turntable generating drops when overpowered", true).getBoolean(true);

        //config.addCustomCategoryComment(DEBUG, "Debug settings");
        debug = config.get(DEBUG, "Debug Mode", false, "Prints Fake Player IDs to console.").getBoolean(false);
        dumpBlockData = config
                .get(DEBUG, "Dump Platform Block Data", false,
                        "Dumps the platform entity's block information in xxd format when the world is saved.")
                .getBoolean(false);

        //config.addCustomCategoryComment(PULLEY, "Pulley settings");
        maxPlatformBlocks = config.get(PULLEY, "Max Platform Blocks", 128).getInt(128);
        upSpeed = config.getFloat("Vertical speed up", PULLEY, 0.1F, 0.0F, 1.0F,
                "The speed at which the pulley rope and platform moves up");
        downSpeed = config.getFloat("Vertical speed down", PULLEY, 0.1F, 0.0F, 1.0F,
                "The speed at which the pulley rope and platform moves down");

        config.save();
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent eventArgs) {
        if (eventArgs.getModID().equals(BWMod.MODID))
            syncConfig();
    }
}
