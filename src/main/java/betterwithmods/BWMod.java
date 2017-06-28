package betterwithmods;

import betterwithmods.client.BWGuiHandler;
import betterwithmods.common.BWIMCHandler;
import betterwithmods.common.BWRegistry;
import betterwithmods.event.*;
import betterwithmods.module.ModuleLoader;
import betterwithmods.network.*;
import betterwithmods.proxy.IProxy;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod(modid = BWMod.MODID, name = BWMod.NAME, version = BWMod.VERSION, dependencies = BWMod.DEPENDENCIES, guiFactory = "betterwithmods.client.gui.BWGuiFactory")
public class BWMod {
    public static final String MODID = "betterwithmods";
    public static final String VERSION = "1.2.9-1.11.2";
    public static final String NAME = "Better With Mods";
    public static final String DEPENDENCIES = "before:survivalist;after:mantle;after:tconstruct;after:minechem;after:natura;after:terrafirmacraft;after:immersiveengineering;after:mekanism;after:thermalexpansion";

    public static Logger logger;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @SidedProxy(serverSide = "betterwithmods.proxy.ServerProxy", clientSide = "betterwithmods.proxy.ClientProxy")
    public static IProxy proxy;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @Mod.Instance(BWMod.MODID)
    public static BWMod instance;

    @Mod.EventHandler
    public void onConstruct(FMLConstructionEvent event) {
        ForgeModContainer.fullBoundingBoxLadders = true;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        logger = evt.getModLog();
        ModuleLoader.preInit(evt);
        BWRegistry.preInit();
        NetworkHandler.register(MessageSyncModule.class, Side.CLIENT);
        NetworkHandler.register(MessageGuiShake.class, Side.CLIENT);
        NetworkHandler.register(MessageFat.class, Side.CLIENT);
        proxy.preInit(evt);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt) {
        BWRegistry.init();
        ModuleLoader.init(evt);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new BWGuiHandler());
        proxy.init(evt);
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        BWRegistry.postInit();
        ModuleLoader.postInit(evt);
        MinecraftForge.EVENT_BUS.register(new FakePlayerHandler());
        MinecraftForge.EVENT_BUS.register(new PotionEventHandler());
        MinecraftForge.EVENT_BUS.register(new BlastingOilEvent());
        MinecraftForge.EVENT_BUS.register(new BreedingHardnessEvent());
        MinecraftForge.EVENT_BUS.register(new FeedWolfchopEvent());
        if (evt.getSide().isServer())
            MinecraftForge.EVENT_BUS.register(new ModuleSync());
        proxy.postInit(evt);
    }

    @Mod.EventHandler
    public void processIMCMessages(IMCEvent evt) {
        BWIMCHandler.processIMC(evt.getMessages());
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent evt) {
        ModuleLoader.serverStarting(evt);
    }

}
