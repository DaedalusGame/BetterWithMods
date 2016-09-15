package betterwithmods;

import betterwithmods.client.container.BWGuiHandler;
import betterwithmods.config.BWConfig;
import betterwithmods.entity.EntityDynamite;
import betterwithmods.entity.EntityExtendingRope;
import betterwithmods.entity.EntityMiningCharge;
import betterwithmods.entity.item.EntityItemBuoy;
import betterwithmods.event.*;
import betterwithmods.integration.ModIntegration;
import betterwithmods.proxy.CommonProxy;
import betterwithmods.util.ColorUtils;
import betterwithmods.util.InvUtils;
import betterwithmods.util.RecipeUtils;
import betterwithmods.util.item.ItemExt;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = "betterwithmods", name = "Better With Mods", version = "0.11 Beta hotfix 1", dependencies = "before:survivalist;after:tconstruct;after:minechem;after:natura;after:terrafirmacraft;after:immersiveengineering", guiFactory = "betterwithmods.client.gui.BWGuiFactory")
public class BWMod {
    @SidedProxy(serverSide = "betterwithmods.proxy.CommonProxy", clientSide = "betterwithmods.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.Instance("betterwithmods")
    public static BWMod instance;

    public static final Logger logger = LogManager.getLogger("betterwithmods");

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        BWConfig.cfg = new Configuration(new File(evt.getModConfigurationDirectory() + "/betterwithmods.cfg"));
        BWConfig.init();
        BWRegistry.init();
        ModIntegration.preInit();
        BWCrafting.init();
        BWRegistry.registerEntity(EntityExtendingRope.class, "ExtendingRope", 64, 20, true);
        BWRegistry.registerEntity(EntityDynamite.class, "BWMDynamite", 10, 50, true);
        BWRegistry.registerEntity(EntityMiningCharge.class, "BWMMiningCharge", 10, 50, true);
		BWRegistry.registerEntity(EntityItemBuoy.class, "entityItemBuoy", 64, 20, true);
        proxy.registerRenderInformation();
        proxy.initRenderers();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new BWGuiHandler());
        proxy.registerColors();
        BWRegistry.registerHeatSources();
        GameRegistry.registerFuelHandler(new BWFuelHandler());
        BWRegistry.registerNetherWhitelist();
        ModIntegration.init();
        BWSounds.registerSounds();
        
		ItemExt.initBuoyancy();
		ItemExt.initDesserts();
		ItemExt.initWeights();
    }

    @EventHandler
    public void processIMCMessages(IMCEvent evt) {
        BWIMCHandler.processIMC(evt.getMessages());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        RecipeUtils.gatherCookableFood();
        BWRegistry.registerWood();
        InvUtils.initOreDictGathering();
        ColorUtils.initColors();
        MinecraftForge.EVENT_BUS.register(new HungerEventHandler());
        MinecraftForge.EVENT_BUS.register(new BuoyancyEventHandler());
        MinecraftForge.EVENT_BUS.register(new RespawnEventHandler());
        MinecraftForge.EVENT_BUS.register(new MobDropEvent());
        MinecraftForge.EVENT_BUS.register(new BucketEvent());
        MinecraftForge.EVENT_BUS.register(new NetherSpawnEvent());
        MinecraftForge.EVENT_BUS.register(new LogHarvestEvent());
        MinecraftForge.EVENT_BUS.register(this);
        RecipeUtils.refreshRecipes();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.getModID().equals("betterwithmods"))
            BWConfig.init();
    }
}
