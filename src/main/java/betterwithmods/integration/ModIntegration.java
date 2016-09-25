package betterwithmods.integration;

import betterwithmods.BWMod;
import betterwithmods.config.BWConfig;
import betterwithmods.integration.immersiveengineering.ImmersiveEngineering;
import betterwithmods.integration.minetweaker.MineTweaker;
import betterwithmods.integration.tcon.TConstruct;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ModIntegration {
    public static Map<String, Class<? extends ModIntegration>> compatClasses = new HashMap<>();
    public static Set<ModIntegration> loadedModules = new HashSet<>();

    static {
        compatClasses.put("biomesoplenty", BiomesOPlenty.class);
        compatClasses.put("harvestcraft", Harvestcraft.class);
        compatClasses.put("immersiveengineering", ImmersiveEngineering.class);
        compatClasses.put("MineTweaker3", MineTweaker.class);
        compatClasses.put("quark", Quark.class);
        compatClasses.put("tconstruct", TConstruct.class);
    }

    public static void loadPreInit() {
        compatClasses.entrySet().stream().filter(e -> isLoaded(e.getKey())).forEach( e -> {
            try {
                ModIntegration mod = e.getValue().newInstance();
                loadedModules.add(mod);
                mod.preInit();
            } catch(Exception ex) {
                BWMod.logger.error("Compat module for "+e.getKey()+" could not be preInitialized. Report this!");
            }
        });
    }

    public static void loadInit() {
        loadedModules.stream().forEach(ModIntegration::init);
    }

    public static void loadPostInit() {
        loadedModules.stream().forEach(ModIntegration::postInit);
    }

    public void preInit() {
    }

    public void init() {
    }

    public void postInit() {
    }

    public static boolean isLoaded(String modid) {
        boolean loaded = Loader.isModLoaded(modid) && BWConfig.cfg.get(BWConfig.MOD_COMPAT, modid.toLowerCase() + "_compat", true).getBoolean();
        BWMod.logger.info("Compat for %s is %s", modid, loaded ? "loaded" : "not loaded");
        BWConfig.cfg.save();
        return loaded;
    }
}
