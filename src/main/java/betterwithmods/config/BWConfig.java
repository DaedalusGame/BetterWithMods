package betterwithmods.config;

import java.io.File;

import betterwithmods.BWMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BWConfig {
	public static Configuration config;

	public static final String HARDCORE = "Hardcore";
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

	public static final String VANILLA_TWEAKS = "Vanilla Tweaks";
	public static boolean netherSpawn;
	public static boolean slimeSpawn;
	public static boolean produceDung;

	public static final String DEBUG = "Debug";
	public static boolean debug;
	public static boolean dumpBlockData;

	public static final String PULLEY = "Pulley";
	public static int maxPlatformBlocks;
	public static float upSpeed;
	public static float downSpeed;

	public static final String MOD_COMPAT = "Mod Compat";

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent eventArgs) {
		if (eventArgs.getModID().equals(BWMod.MODID))
			syncConfig();
	}

	public static void init(File file) {
		config = new Configuration(file);
		syncConfig();
	}

	public static void syncConfig() {
		//config.addCustomCategoryComment(HARDCORE, "Hardcore settings");
		hardcoreGunpowder = config.get(HARDCORE, "Hardcore Gunpowder", true).getBoolean(true);
		hardcoreLumber = config.get(HARDCORE, "Hardcore Lumberjack", true).getBoolean(true);
		hardcoreBuckets = config.get(HARDCORE, "Hardcore Buckets", true).getBoolean(true);
        hardcoreFluidContainer = config.get(HARDCORE, "Hardcore Buckets Affects Modded Fluid Containers", true).getBoolean(true);
		hardcoreHunger = config.get(HARDCORE, "Hardcore Hunger", true).getBoolean(true);
		hardcoreBuoy = config.get(HARDCORE, "Hardcore Buoy", true).getBoolean(true);
		hardcoreSpawn = config.get(HARDCORE, "Hardcore Spawn", true).getBoolean(true);
		hardcoreBeds = config.get(HARDCORE, "Hardcore Beds", true).getBoolean(true);
		hardcoreVillagers = config.get(HARDCORE, "Hardcore Villagers", true).getBoolean(true);
		steelRequiresEnd = config.get(HARDCORE, "Steel Requires End", true).getBoolean(true);

		//config.addCustomCategoryComment(VANILLA_TWEAKS, "Vanilla tweaks settings");
		netherSpawn = config.get(VANILLA_TWEAKS, "Prevent Nether Spawns on Non-Nether Materials", true)
				.getBoolean(true);
		slimeSpawn = config.get(VANILLA_TWEAKS, "Prevent Slimes Spawning on Non-Stone Non-Dirt Materials", true)
				.getBoolean(true);
		produceDung = config.get(VANILLA_TWEAKS, "Animals Produce Dung", true).getBoolean(true);

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
}
