package betterwithmods.config;

import net.minecraftforge.common.config.Configuration;

public class BWConfig {
    public static final String HARDCORE = "Hardcore";
    public static final String VANILLA_TWEAKS = "Vanilla Tweaks";
    public static final String DEBUG = "Debug";
    public static final String PULLEY = "Pulley";
    public static final String MOD_COMPAT = "Mod Compat";
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
    public static boolean debug;
    public static boolean produceDung;
    public static int maxPlatformBlocks;
    public static float upSpeed;
    public static float downSpeed;
    public static boolean dumpBlockData;
    public static Configuration cfg;

    public static void init() {
        try {
            hardcoreGunpowder = cfg.get(HARDCORE, "Hardcore Gunpowder", true).getBoolean(true);
            hardcoreLumber = cfg.get(HARDCORE, "Hardcore Lumberjack", true).getBoolean(true);
            hardcoreBuckets = cfg.get(HARDCORE, "Hardcore Buckets", true).getBoolean(true);
            hardcoreFluidContainer = cfg.get(HARDCORE, "Hardcore Buckets Affects Modded Fluid Containers", true).getBoolean(true);
            hardcoreHunger = cfg.get(HARDCORE, "Hardcore Hunger", true).getBoolean(true);
            hardcoreBuoy = cfg.get(HARDCORE, "Hardcore Buoy", true).getBoolean(true);
            hardcoreSpawn = cfg.get(HARDCORE, "Hardcore Spawn", true).getBoolean(true);
            hardcoreBeds = cfg.get(HARDCORE, "Hardcore Beds", true).getBoolean(true);
            hardcoreVillagers = cfg.get(HARDCORE, "Hardcore Villagers", true).getBoolean(true);
            steelRequiresEnd = cfg.get(HARDCORE, "Steel Requires End", true).getBoolean(true);
            netherSpawn = cfg.get(VANILLA_TWEAKS, "Prevent Nether Spawns on Non-Nether Materials", true).getBoolean(true);
            slimeSpawn = cfg.get(VANILLA_TWEAKS, "Prevent Slimes Spawning on Non-Stone Non-Dirt Materials", true).getBoolean(true);
            debug = cfg.get(DEBUG, "Debug Mode", false, "Prints Fake Player IDs to console.").getBoolean(false);
            produceDung = cfg.get(VANILLA_TWEAKS, "Animals Produce Dung", true).getBoolean(true);
            maxPlatformBlocks = cfg.get(PULLEY, "Max Platform Blocks", 128).getInt(128);
            upSpeed = cfg.getFloat("Vertical speed up", PULLEY, 0.1F, 0.0F, 1.0F, "The speed at which the pulley rope and platform moves up");
            downSpeed = cfg.getFloat("Vertical speed down", PULLEY, 0.1F, 0.0F, 1.0F, "The speed at which the pulley rope and platform moves down");
            dumpBlockData = cfg.get(DEBUG, "Dump Platform Block Data", false, "Dumps the platform entity's block information in xxd format when the world is saved.").getBoolean(false);
        } catch (Exception e) {
        } finally {
            if (cfg.hasChanged())
                cfg.save();
        }
    }

}
