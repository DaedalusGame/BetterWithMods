package betterwithmods.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class BWConfig 
{
	public static boolean hardcoreGunpowder;
	public static boolean hardcoreLumber;
	public static boolean hardcoreBuckets;
	public static boolean hardcoreHunger;
	public static boolean hardcoreBuoy;
	public static boolean hardcoreSpawn;
	public static boolean hardcoreBeds;
	public static boolean netherSpawn;
	public static boolean slimeSpawn;
	public static boolean debug;
	public static boolean produceDung;
	public static int maxPlatformBlocks;
	public static float upSpeed;
	public static float downSpeed;
	public static boolean dumpBlockData;
	
	public static void init(File config)
	{
		Configuration cfg = new Configuration(config);
		try
		{
			cfg.load();
			hardcoreGunpowder = cfg.get("Hardcore", "Hardcore Gunpowder", true).getBoolean(true);
			hardcoreLumber = cfg.get("Hardcore", "Hardcore Lumberjack", true).getBoolean(true);
			hardcoreBuckets = cfg.get("Hardcore", "Hardcore Buckets", true).getBoolean(true);
			hardcoreHunger = cfg.get("Hardcore", "Hardcore Hunger", true).getBoolean(true);
			hardcoreBuoy = cfg.get("Hardcore", "Hardcore Buoy", true).getBoolean(true);
			hardcoreSpawn = cfg.get("Hardcore", "Hardcore Spawn", true).getBoolean(true);
			hardcoreBeds = cfg.get("Hardcore", "Hardcore Beds", true).getBoolean(true);
			netherSpawn = cfg.get("Vanilla Tweaks", "Prevent Nether Spawns on Non-Nether Materials", true).getBoolean(true);
			slimeSpawn = cfg.get("Vanilla Tweaks", "Prevent Slimes Spawning on Non-Stone Non-Dirt Materials", true).getBoolean(true);
			debug = cfg.get("Debug", "Debug Mode", false, "Prints Fake Player IDs to console.").getBoolean(false);
			produceDung = cfg.get("Vanilla Tweaks", "Animals Produce Dung", true).getBoolean(true);
			maxPlatformBlocks = cfg.get("Pulley", "Max Platform Blocks", 128).getInt(128);
			upSpeed = cfg.getFloat("Vertical speed up", "Pulley", 0.1F, 0.0F, 1.0F, "The speed at which the pulley rope and platform moves up");
			downSpeed = cfg.getFloat("Vertical speed down", "Pulley", 0.1F, 0.0F, 1.0F, "The speed at which the pulley rope and platform moves down");
			dumpBlockData = cfg.get("Debug", "Dump Platform Block Data", false, "Dumps the platform entity's block information in xxd format when the world is saved.").getBoolean(false);
		}
		catch(Exception e)
		{}
		finally
		{
			cfg.save();
		}
	}
}
