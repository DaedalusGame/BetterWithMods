package betterwithmods;

import java.io.File;

import betterwithmods.util.InvUtils;
import betterwithmods.util.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import betterwithmods.client.container.BWGuiHandler;
import betterwithmods.config.BWConfig;
import betterwithmods.entity.EntityDynamite;
import betterwithmods.event.BucketEvent;
import betterwithmods.event.LogHarvestEvent;
import betterwithmods.event.MobDropEvent;
import betterwithmods.event.NetherSpawnEvent;
import betterwithmods.integration.ModIntegration;
import betterwithmods.proxy.CommonProxy;
import betterwithmods.util.ColorUtils;

@Mod(modid="betterwithmods", name="Better With Mods", version="0.9.2 Beta", dependencies="before:survivalist;after:tcontruct;after:minechem;after:natura;after:terrafirmacraft;after:immersiveengineering")
public class BWMod
{
	@SidedProxy(serverSide="betterwithmods.proxy.CommonProxy", clientSide="betterwithmods.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	@Mod.Instance("betterwithmods")
	public static BWMod instance;
	
	public static final Logger logger = LogManager.getLogger("betterwithmods");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		BWConfig.init(new File(evt.getModConfigurationDirectory() + "/betterwithmods.cfg"));
		BWRegistry.init();
		BWCrafting.init();
		EntityRegistry.registerModEntity(EntityDynamite.class, "BWMDynamite", 1, this, 10, 50, true);
		proxy.registerRenderInformation();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new BWGuiHandler());
		proxy.registerColors();
		BWRegistry.registerHeatSources();
		GameRegistry.registerFuelHandler(new BWFuelHandler());
		BWRegistry.registerNetherWhitelist();
		BWRegistry.registerWood();
		ModIntegration.init();
		BWSounds.registerSounds();
	}
	
	@EventHandler
	public void processIMCMessages(IMCEvent evt)
	{
		BWIMCHandler.processIMC(evt.getMessages());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{
		RecipeUtils.sawRecipeInit();
		RecipeUtils.gatherCookableFood();
		InvUtils.initOreDictGathering();
		proxy.postRender();
		ColorUtils.initColors();
		MinecraftForge.EVENT_BUS.register(new MobDropEvent());
		MinecraftForge.EVENT_BUS.register(new BucketEvent());
		MinecraftForge.EVENT_BUS.register(new NetherSpawnEvent());
		MinecraftForge.EVENT_BUS.register(new LogHarvestEvent());
		RecipeUtils.refreshRecipes();
	}

	@EventHandler
	public void onMissingMapping(FMLMissingMappingsEvent evt)
	{
		for(FMLMissingMappingsEvent.MissingMapping mapping : evt.get())
		{
			if(mapping.name.contains("betterwithmods:"))
			{
				logger.warn("Converting " + mapping.name + " to " + convertToLowercase(mapping.name) + " to conform to future standards. Downgrading Better With Mods may remove this item!");
				if(mapping.type == GameRegistry.Type.BLOCK)
					mapping.remap(Block.REGISTRY.getObject(new ResourceLocation(convertToLowercase(mapping.name))));
				if(mapping.type == GameRegistry.Type.ITEM)
					mapping.remap(Item.REGISTRY.getObject(new ResourceLocation(convertToLowercase(mapping.name))));
			}
		}
	}

	private String convertToLowercase(String name)
	{
		char[] chars = name.toCharArray();
		StringBuilder build = new StringBuilder();

		for(char ch : chars)
		{
			if(ch >= 65 && ch <= 90) {
				ch = (char)(ch + 32);
				build.append('_');
			}
			build.append(ch);
		}
		return build.toString();
	}
}
