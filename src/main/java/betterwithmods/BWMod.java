package betterwithmods;

import betterwithmods.client.container.BWGuiHandler;
import betterwithmods.common.BWCrafting;
import betterwithmods.common.BWIMCHandler;
import betterwithmods.common.BWRegistry;
import betterwithmods.event.*;
import betterwithmods.integration.CompatMap;
import betterwithmods.integration.ICompatModule;
import betterwithmods.module.ModuleLoader;
import betterwithmods.network.MessageSyncModule;
import betterwithmods.network.ModuleSync;
import betterwithmods.network.NetworkHandler;
import betterwithmods.proxy.IProxy;
import betterwithmods.util.RecipeUtils;
import net.minecraft.world.MinecraftException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mod(modid = BWMod.MODID, name = BWMod.NAME, version = BWMod.VERSION, dependencies = "before:survivalist;after:mantle;after:tconstruct;after:minechem;after:natura;after:terrafirmacraft;after:immersiveengineering;after:quark;after:mekanism;after:thermalexpansion", guiFactory = "betterwithmods.client.gui.BWGuiFactory")
public class BWMod {
    public static final String MODID = "betterwithmods";
    public static final String VERSION = "%VERSION%";
    public static final String NAME = "Better With Mods";
    private static final Set<ICompatModule> loadedModules = new HashSet<>();

    /**
     * Mod ID => Compatibility module class path.
     */
    private static final Map<String, String> compatClasses;
    public static Logger logger;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @SidedProxy(serverSide = "betterwithmods.proxy.ServerProxy", clientSide = "betterwithmods.proxy.ClientProxy")
    public static IProxy proxy;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @Mod.Instance(BWMod.MODID)
    public static BWMod instance;

    static {
        CompatMap map = new CompatMap(
                "biomesoplenty", "betterwithmods.integration.BiomesOPlenty",
                "harvestcraft", "betterwithmods.integration.Harvestcraft",
                "crafttweaker", "betterwithmods.integration.minetweaker.MineTweaker",
                "quark", "betterwithmods.integration.Quark",
                "nethercore", "betterwithmods.integration.NetherCore",
                "actuallyadditions", "betterwithmods.integration.ActuallyAdditions");
        //map.put("tconstruct", "betterwithmods.integration.tcon.TConstruct");
        //map.put("immersiveengineering", "betterwithmods.integration.immersiveengineering.ImmersiveEngineering");
        compatClasses = Collections.unmodifiableMap(map);
    }

    public static Set<ICompatModule> getLoadedModules() {
        return loadedModules;
    }

    private static void loadCompatibilityModules() {
        for (Map.Entry<String, String> entry : compatClasses.entrySet()) {
            String modId = entry.getKey();
            String classPath = entry.getValue();
            if (isLoaded(modId)) try {
                loadedModules.add(Class.forName(classPath).asSubclass(ICompatModule.class).newInstance());
                BWMod.logger.info("Successfully load compat for " + modId);
            } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
                BWMod.logger.error("Compatibility class " + classPath + " could not be loaded. Report this!");
                e.printStackTrace();
            }
        }
    }

    private static boolean isLoaded(String modId) {
        boolean loaded = Loader.isModLoaded(modId) && ModuleLoader.config.get("Mod Compat", modId.toLowerCase() + "_compat", true).getBoolean();
        BWMod.logger.debug("Compat for " + modId + " is " + (loaded ? "loaded" : "not loaded"));
        ModuleLoader.config.save();
        return loaded;
    }

    private static void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(new FakePlayerHandler());
        MinecraftForge.EVENT_BUS.register(new LogHarvestEvent());
        MinecraftForge.EVENT_BUS.register(new PotionEventHandler());
        MinecraftForge.EVENT_BUS.register(new BlastingOilEvent());
        MinecraftForge.EVENT_BUS.register(new BreedingHardnessEvent());
        MinecraftForge.EVENT_BUS.register(new FeedWolfchopEvent());
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        logger = evt.getModLog();

        ModuleLoader.preInit(evt);
        BWRegistry.preInit();

        BWCrafting.init();
        loadCompatibilityModules();
        getLoadedModules().forEach(ICompatModule::preInit);

        NetworkHandler.register(MessageSyncModule.class, Side.CLIENT);
        proxy.preInit();

    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        BWRegistry.init();
        ModuleLoader.init(evt);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new BWGuiHandler());
        getLoadedModules().forEach(ICompatModule::init);
        proxy.init();
    }

    @EventHandler
    public void processIMCMessages(IMCEvent evt) {
        BWIMCHandler.processIMC(evt.getMessages());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        BWRegistry.postInit();
        ModuleLoader.postInit(evt);
        BWCrafting.postInit();

        registerEventHandlers();
        RecipeUtils.refreshRecipes();

        getLoadedModules().forEach(ICompatModule::postInit);
        if (evt.getSide().isServer())
            MinecraftForge.EVENT_BUS.register(new ModuleSync());
        proxy.postInit();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent evt) {
        ModuleLoader.serverStarting(evt);
    }

    @Mod.EventHandler
    public void remap(FMLMissingMappingsEvent evt) throws MinecraftException {
        for (FMLMissingMappingsEvent.MissingMapping mapping : evt.get()) {
            switch (mapping.type) {
                case ITEM:
                    break;
                case BLOCK:
                    break;
            }
        }
    }
}
