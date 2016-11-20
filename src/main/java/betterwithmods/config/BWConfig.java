package betterwithmods.config;

import betterwithmods.BWMod;
import net.minecraftforge.common.config.ConfigCategory;
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
    public static ConfigCategory HARDCORE_CAT;
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
    public static boolean hardcoreMelons;
    public static boolean steelRequiresEnd;
    public static boolean hardcoreHardness;
    public static int woodDurability;
    public static int stoneDurability;
    public static int ironDurability;
    public static int diamondDurability;
    public static int goldDurability;
    public static boolean earlyPickaxesRebalance;
    public static boolean removeLowTools;
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
    public static boolean canKilnSmeltOres;
    public static boolean axeOnLeaves;
    public static boolean rawEggDrop;

    public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
    }

    private static void syncConfig() {
        HARDCORE_CAT = config.getCategory(HARDCORE);
        hardcoreGunpowder = config.get(HARDCORE, "Hardcore Gunpowder", true, "Creepers and Ghasts will drop brimstone or niter instead of gunpowder").getBoolean();
        hardcoreLumber = config.get(HARDCORE, "Hardcore Lumberjack", true, "Logs break into planks if you don't use an axe").getBoolean();
        hardcoreBuckets = config.get(HARDCORE, "Hardcore Buckets", true, "Water sources cannot be moved outside the End").getBoolean();
        hardcoreMelons = config.get(HARDCORE, "Hardcore Melons", true, "Melons and pumpkins are affected by gravity and need a saw to slice up.").getBoolean();
        hardcoreFluidContainer = config.get(HARDCORE, "Hardcore Buckets Affects Modded Fluid Containers", true).getBoolean();
        // FIXME See issue #71
        hardcoreHunger = config.get(HARDCORE, "Hardcore Hunger", true, "Saturation becomes fat, while hunger and health stats affect your movement. Vanilla food recipes are changed to use BWM content").setRequiresMcRestart(true).getBoolean();
        hardcoreBuoy = config.get(HARDCORE, "Hardcore Buoy", true, "Buoyant items will float").getBoolean();
        hardcoreSpawn = config.get(HARDCORE, "Hardcore Spawn", true, "Your spawn point will be randomized").getBoolean();
        hardcoreBeds = config.get(HARDCORE, "Hardcore Beds", true, "You will be unable to sleep in a bed").getBoolean();
        hardcoreVillagers = config.get(HARDCORE, "Hardcore Villagers", true, "Villagers can be manually bred").getBoolean();
        steelRequiresEnd = config.get(HARDCORE, "Steel Requires End", true, "Soulforged Steel will require Ender Flux").getBoolean();
        hardcoreHardness = config.get(HARDCORE, "Hardcore Hardness", true, "Hardness of blocks and durability of tools are rebalanced. Cheapest tools become unavailable.").setRequiresMcRestart(true).getBoolean();
        woodDurability = config.get(HARDCORE, "Hardcore Hardness Wood Durability", 10, "Number of usages for wooden tools.", 2, 60).setRequiresMcRestart(true).getInt();
        stoneDurability = config.get(HARDCORE, "Hardcore Hardness Stone Durability", 50, "Number of usages for stone tools.", 2, 132).setRequiresMcRestart(true).getInt();
        ironDurability = config.get(HARDCORE, "Hardcore Hardness Iron Durability", 500, "Number of usages for iron tools.", 2, 251).setRequiresMcRestart(true).getInt();
        diamondDurability = config.get(HARDCORE, "Hardcore Hardness Diamond Durability", 1561, "Number of usages for diamond tools.", 2, 1562).setRequiresMcRestart(true).getInt();
        goldDurability = config.get(HARDCORE, "Hardcore Hardness Gold Durability", 32, "Number of usages for golden tools.", 2, 33).setRequiresMcRestart(true).getInt();
        earlyPickaxesRebalance = config.get(HARDCORE, "Early pickaxes rebalance", true, "Wooden Pickaxe will have 1 usage and Stone Pickaxe 6.").setRequiresMcRestart(true).getBoolean();
        removeLowTools = config.get(HARDCORE, "Remove cheapest tools", true, "The minimum level of the hoe and the sword is iron, and the axe needs at least stone.").setRequiresMcRestart(true).getBoolean();

        netherSpawn = config.get(VANILLA_TWEAKS, "Prevent Nether Spawns on Non-Nether Materials", true)
                .getBoolean();
        slimeSpawn = config.get(VANILLA_TWEAKS, "Prevent Slimes Spawning on Non-Stone Non-Dirt Materials", true)
                .getBoolean();
        produceDung = config.get(VANILLA_TWEAKS, "Animals Produce Dung", true).getBoolean();

        dropsGearbox = config.get(MODPACK_TWEAKS, "Gearbox generating drops when overpowered", true).getBoolean();
        dropsSaw = config.get(MODPACK_TWEAKS, "Saw generating drops when overpowered", true).getBoolean();
        dropsHopper = config.get(MODPACK_TWEAKS, "Hopper generating drops when overpowered", true).getBoolean();
        dropsPulley = config.get(MODPACK_TWEAKS, "Pulley generating drops when overpowered", true).getBoolean();
        dropsMill = config.get(MODPACK_TWEAKS, "Mill generating drops when overpowered", true).getBoolean();
        dropsTurntable = config.get(MODPACK_TWEAKS, "Turntable generating drops when overpowered", true).getBoolean();

        debug = config.get(DEBUG, "Debug Mode", false, "Prints Fake Player IDs to console.").getBoolean();
        dumpBlockData = config
                .get(DEBUG, "Dump Platform Block Data", false,
                        "Dumps the platform entity's block information in xxd format when the world is saved.")
                .getBoolean();

        maxPlatformBlocks = config.get(PULLEY, "Max Platform Blocks", 128).getInt(128);
        upSpeed = config.getFloat("Vertical speed up", PULLEY, 0.1F, 0.0F, 1.0F,
                "The speed at which the pulley rope and platform moves up");
        downSpeed = config.getFloat("Vertical speed down", PULLEY, 0.1F, 0.0F, 1.0F,
                "The speed at which the pulley rope and platform moves down");
        canKilnSmeltOres = config.getBoolean("Can Kiln Smelt Ores",Configuration.CATEGORY_GENERAL,true,"Allows Ores to be smelted in the kiln");
        axeOnLeaves = config.getBoolean("Axes Effective On Leaves",Configuration.CATEGORY_GENERAL,true,"Makes axes quickly break leaves.");
        rawEggDrop = config.get(VANILLA_TWEAKS, "Eggs Drop Raw Egg When Thrown", true).getBoolean();
        config.save();
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent eventArgs) {
        if (eventArgs.getModID().equals(BWMod.MODID))
            syncConfig();
    }
}
