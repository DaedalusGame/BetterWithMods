package betterwithmods;

import betterwithmods.api.capabilities.MechanicalCapability;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.client.container.BWGuiHandler;
import betterwithmods.config.BWConfig;
import betterwithmods.config.ConfigSyncHandler;
import betterwithmods.entity.EntityBroadheadArrow;
import betterwithmods.entity.EntityDynamite;
import betterwithmods.entity.EntityExtendingRope;
import betterwithmods.entity.EntityMiningCharge;
import betterwithmods.entity.EntityShearedCreeper;
import betterwithmods.entity.item.EntityItemBuoy;
import betterwithmods.event.*;
import betterwithmods.integration.ICompatModule;
import betterwithmods.network.BWNetwork;
import betterwithmods.proxy.IProxy;
import betterwithmods.util.ColorUtils;
import betterwithmods.util.HardcoreFunctions;
import betterwithmods.util.InvUtils;
import betterwithmods.util.RecipeUtils;
import betterwithmods.util.item.ItemExt;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mod(modid = BWMod.MODID, name = BWMod.NAME, version = BWMod.VERSION, dependencies = "required-after:Forge@[12.18.1.2076,);before:survivalist;after:tconstruct;after:minechem;after:natura;after:terrafirmacraft;after:immersiveengineering", guiFactory = "betterwithmods.client.gui.BWGuiFactory")
public class BWMod {
    public static final String MODID = "betterwithmods";
    public static final String VERSION = "0.13.1 Beta hotfix 3";
    public static final String NAME = "Better With Mods";
    private static final Set<ICompatModule> loadedModules = new HashSet<>();

    /**
     * Mod ID => Compatibility module class path.
     */
    private static final Map<String, String> compatClasses;
    public static Logger logger;
    @SuppressWarnings("CanBeFinal")
    @SidedProxy(serverSide = "betterwithmods.proxy.ServerProxy", clientSide = "betterwithmods.proxy.ClientProxy")
    public static IProxy proxy;
    @SuppressWarnings("CanBeFinal")
    @Mod.Instance(BWMod.MODID)
    public static BWMod instance;
    public static IForgeRegistry<Item> itemRegistry;

    static {
        //Avoid all direct references to class so
        //they are actually loaded only if necessary.
        Map<String, String> map = new HashMap<>();
        map.put("biomesoplenty", "betterwithmods.integration.BiomesOPlenty");
        map.put("harvestcraft", "betterwithmods.integration.Harvestcraft");
        map.put("immersiveengineering", "betterwithmods.integration.immersiveengineering.ImmersiveEngineering");
        map.put("MineTweaker3", "betterwithmods.integration.minetweaker.MineTweaker");
        map.put("Quark", "betterwithmods.integration.Quark");
        map.put("tconstruct", "betterwithmods.integration.tcon.TConstruct");
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
            } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
                BWMod.logger.error("Compatibility class " + classPath + " could not be loaded. Report this!");
                e.printStackTrace();
            }
        }
    }

    private static boolean isLoaded(String modId) {
        boolean loaded = Loader.isModLoaded(modId)
                && BWConfig.config.get(BWConfig.MOD_COMPAT, modId.toLowerCase() + "_compat", true).getBoolean();
        BWMod.logger.debug("Compat for " + modId + " is " + (loaded ? "loaded" : "not loaded"));
        BWConfig.config.save();
        return loaded;
    }

    private static void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(new BWConfig());
        MinecraftForge.EVENT_BUS.register(new HungerEventHandler());
        MinecraftForge.EVENT_BUS.register(new BuoyancyEventHandler());
        MinecraftForge.EVENT_BUS.register(new RespawnEventHandler());
        MinecraftForge.EVENT_BUS.register(new MobDropEvent());
        MinecraftForge.EVENT_BUS.register(new BucketEvent());
        MinecraftForge.EVENT_BUS.register(new NetherSpawnEvent());
        MinecraftForge.EVENT_BUS.register(new LogHarvestEvent());
        MinecraftForge.EVENT_BUS.register(new PotionEventHandler());
        MinecraftForge.EVENT_BUS.register(new MobAIEvent());
        MinecraftForge.EVENT_BUS.register(new HardcoreHardnessEventHandler());
        MinecraftForge.EVENT_BUS.register(new EggImpactEvent());
        MinecraftForge.EVENT_BUS.register(new SaveSoupEvent());
    }

    private static void registerEntities() {
        BWRegistry.registerEntity(EntityExtendingRope.class, "ExtendingRope", 64, 20, true);
        BWRegistry.registerEntity(EntityDynamite.class, "BWMDynamite", 10, 50, true);
        BWRegistry.registerEntity(EntityMiningCharge.class, "BWMMiningCharge", 10, 50, true);
        BWRegistry.registerEntity(EntityItemBuoy.class, "entityItemBuoy", 64, 20, true);
        BWRegistry.registerEntity(EntityShearedCreeper.class, "entityShearedCreeper", 64, 1, true);
        BWRegistry.registerEntity(EntityBroadheadArrow.class, "entityBroadheadArrow", 64, 1, true);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        logger = evt.getModLog();

        BWConfig.init(evt.getSuggestedConfigurationFile());

        BWMBlocks.registerBlocks();
        BWMItems.registerItems();
        BWMBlocks.registerTileEntities();

        // Cache the registry used for iterations
        itemRegistry = GameRegistry.findRegistry(Item.class);

        if (BWConfig.hardcoreHardness) HardcoreFunctions.applyHCHardness();
        if (BWConfig.earlyPickaxesRebalance) Items.STONE_PICKAXE.setMaxDamage(6 - 1);
        if (BWConfig.removeLowTools) HardcoreFunctions.removeLowTierToolRecipes();
        if (BWConfig.axeOnLeaves) { Blocks.LEAVES.setHarvestLevel("axe",0); Blocks.LEAVES2.setHarvestLevel("axe",1); }

        BWRegistry.init();
        loadCompatibilityModules();
        getLoadedModules().forEach(ICompatModule::preInit);
        BWCrafting.init();
        registerEntities();
        CapabilityManager.INSTANCE.register(IMechanicalPower.class, new MechanicalCapability.CapabilityMechanicalPower(), MechanicalCapability.DefaultMechanicalPower.class);
        BWNetwork.INSTANCE.init();
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new BWGuiHandler());
        BWRegistry.registerHeatSources();
        GameRegistry.registerFuelHandler(new BWFuelHandler());
        BWRegistry.registerNetherWhitelist();
        getLoadedModules().forEach(ICompatModule::init);
        BWSounds.registerSounds();
        ItemExt.initBuoyancy();
        ItemExt.initDesserts();
        ItemExt.initWeights();
        proxy.init();
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
        BWCrafting.postInit();
        ColorUtils.initColors();
        registerEventHandlers();
        RecipeUtils.refreshRecipes();

        getLoadedModules().forEach(ICompatModule::postInit);
        BucketEvent.editModdedFluidDispenseBehavior();
        if (evt.getSide().isServer())
            MinecraftForge.EVENT_BUS.register(new ConfigSyncHandler());
        proxy.postInit();

    }
}
