package betterwithmods.module.compat;

import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Module;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.CrucibleRecipes;
import betterwithmods.module.hardcore.HCDiamond;
import betterwithmods.util.RecipeUtils;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

import static net.minecraftforge.fml.common.LoaderState.ModState.INITIALIZED;

/**
 * Created by tyler on 5/24/17.
 */
public class CompatModule extends Module {
    private HashMap<String, String> compatRegistry = Maps.newHashMap();
    private HashMap<Pair<LoaderState.ModState, String>, Runnable> quickCompat = Maps.newHashMap();

    public void registerCompatFeature(String modid, String clazz) {
        compatRegistry.put(modid, clazz);
    }

    public void registerQuickCompat(String modid, LoaderState.ModState state, Runnable runnable) {
        quickCompat.put(Pair.of(state, modid), runnable);
    }

    @Override
    public void addFeatures() {
        registerCompatFeature("biomesoplenty", "betterwithmods.module.compat.bop.BiomesOPlenty");
        registerCompatFeature("harvestcraft", "betterwithmods.module.compat.Harvestcraft");
        registerCompatFeature("crafttweaker", "betterwithmods.module.compat.minetweaker.MineTweaker");
        registerCompatFeature("quark", "betterwithmods.module.compat.Quark");
        registerCompatFeature("nethercore", "betterwithmods.module.compat.NetherCore");
        registerCompatFeature("actuallyadditions", "betterwithmods.module.compat.ActuallyAdditions");
        registerCompatFeature("immersiveengineering", "betterwithmods.module.compat.immersiveengineering.ImmersiveEngineering");
        registerCompatFeature("rustic", "betterwithmods.module.compat.Rustic");
        registerCompatFeature("tconstruct", "betterwithmods.module.compat.tcon.TConstruct");

        registerQuickCompat("chisel", INITIALIZED, () -> {
            if (ModuleLoader.isFeatureEnabled(HCDiamond.class)) {
                ItemStack chisel_diamond = getItem("chisel:chisel_diamond");
                RecipeUtils.removeRecipes(chisel_diamond);
                RecipeUtils.addOreRecipe(chisel_diamond, " D", "S ", 'D', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), 'S', "stickWood").setMirrored(true);
                CrucibleRecipes.addCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT), new Object[]{getItem("chisel:chisel_diamond", 1, OreDictionary.WILDCARD_VALUE)});
            }
            CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT), new Object[]{getItem("chisel:chisel_iron", 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT), new Object[]{getItem("chisel:chisel_iron", 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT), new Object[]{getItem("chisel:chisel_hitech", 1, OreDictionary.WILDCARD_VALUE)});
        });
        this.load();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        for (Map.Entry<Pair<LoaderState.ModState, String>, Runnable> entry : quickCompat.entrySet()) {
            if (event.getModState().equals(entry.getKey().getKey()) && isLoaded(entry.getKey().getValue())) {
                entry.getValue().run();
            }
        }

    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        for (Map.Entry<Pair<LoaderState.ModState, String>, Runnable> entry : quickCompat.entrySet()) {
            if (event.getModState().equals(entry.getKey().getKey()) && isLoaded(entry.getKey().getValue())) {
                entry.getValue().run();
            }
        }

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        for (Map.Entry<Pair<LoaderState.ModState, String>, Runnable> entry : quickCompat.entrySet()) {
            if (event.getModState().equals(entry.getKey().getKey()) && isLoaded(entry.getKey().getValue())) {
                entry.getValue().run();
            }
        }
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
