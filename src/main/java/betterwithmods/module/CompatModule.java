package betterwithmods.module;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tyler on 5/24/17.
 */
public class CompatModule extends Module {
    private HashMap<String, String> compatRegistry = Maps.newHashMap();

    public void registerCompatFeature(String modid, String clazz) {
        compatRegistry.put(modid, clazz);
    }


    public void addCompatFeatures() {
        registerCompatFeature("biomesoplenty", "betterwithmods.module.compat.bop.BiomesOPlenty");
        registerCompatFeature("harvestcraft", "betterwithmods.module.compat.Harvestcraft");
        registerCompatFeature("crafttweaker", "betterwithmods.module.compat.minetweaker.MineTweaker");
        registerCompatFeature("quark", "betterwithmods.module.compat.Quark");
        registerCompatFeature("nethercore", "betterwithmods.module.compat.NetherCore");
        registerCompatFeature("actuallyadditions", "betterwithmods.module.compat.ActuallyAdditions");
        registerCompatFeature("immersiveengineering", "betterwithmods.module.compat.immersiveengineering.ImmersiveEngineering");
        registerCompatFeature("rustic", "betterwithmods.module.compat.Rustic");
        registerCompatFeature("tconstruct", "betterwithmods.module.compat.tcon.TConstruct");

    }

    @Override
    public void addFeatures() {
        this.addCompatFeatures();
        this.load();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    public void load() {
        for (Map.Entry<String, String> feature : compatRegistry.entrySet()) {
            String modId = feature.getKey();
            String classPath = feature.getValue();
            if (isLoaded(modId)) try {
                registerFeature(Class.forName(classPath).asSubclass(CompatFeature.class).newInstance());
                FMLLog.info(" [BWM] Successfully load compat for " + modId);
            } catch (ExceptionInInitializerError | InstantiationException | ClassNotFoundException | IllegalAccessException ignore) {
                FMLLog.info(" [BWM] Compatibility class " + classPath + " could not be loaded. Report this!");
            }
        }
    }

    private boolean isLoaded(String modId) {
        boolean loaded = Loader.isModLoaded(modId) && loadPropBool(modId.toLowerCase() + "_compat", "compatibility for ", true);
        return loaded;
    }

    public ItemStack getItem(String location) {
        return getItem(new ResourceLocation(location), 1, 0);
    }

    public ItemStack getItem(ResourceLocation location, int count, int meta) {
        return new ItemStack(Item.REGISTRY.getObject(location), count, meta);
    }

    public ItemStack getItem(String location, int count, int meta) {
        return getItem(new ResourceLocation(location), count, meta);
    }

    public ItemStack getBlock(ResourceLocation location, int count, int meta) {
        return new ItemStack(Block.REGISTRY.getObject(location), count, meta);
    }

    public ItemStack getBlock(String location, int count, int meta) {
        return getBlock(new ResourceLocation(location), count, meta);
    }

    public ItemStack getBlock(String location) {
        return getBlock(new ResourceLocation(location), 1, 0);
    }
}
