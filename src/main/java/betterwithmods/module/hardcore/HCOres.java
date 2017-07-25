package betterwithmods.module.hardcore;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.module.Feature;
import betterwithmods.module.gameplay.CrucibleRecipes;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by tyler on 4/20/17.
 */
public class HCOres extends Feature {

    private static boolean oreNuggetSmelting, dustNuggetSmelting, fixVanillaRecipes;
    private static Set<String> oreExclude, dustExclude;
    private static int oreProductionCount, dustProductionCount;

    @Override
    public void setupConfig() {
        oreNuggetSmelting = loadPropBool("Ore to Nugget Smelting", "Make Ores (oredict ore.* )smelt into nuggets instead of ingots", true);

        oreExclude = Arrays.stream(loadPropStringList("Ore Exclude", "Oredictionary entries to exclude from ore to nugget smelting. Remove the prefix of the oredictionary. example 'oreIron' would be just 'iron' ", new String[0])).map(String::toLowerCase).collect(Collectors.toSet());
        dustExclude = Arrays.stream(loadPropStringList("Dust Exclude", "Oredictionary entries to exclude from dust to nugget smelting  Remove the prefix of the oredictionary. example 'dustIron' would be just 'iron'", new String[0])).map(String::toLowerCase).collect(Collectors.toSet());

        dustNuggetSmelting = loadPropBool("Dust to Nugget Smelting", "Make Dusts ( oredict dust.* ) smelt into nuggets instead of ingots", true);
        fixVanillaRecipes = loadPropBool("Fix Vanilla Recipes", "Make certain recipes cheaper to be more reasonable with nugget smelting, including Compass, Clock, and Bucket", true);

        oreProductionCount = loadPropInt("Ore Production Count", "Number of Materials returned from Smelting an Ore", 1);
        dustProductionCount = loadPropInt("Dust Production Count", "Number of Materials returned from Smelting a Dust", 1);
    }

    @Override
    public String getFeatureDescription() {
        return "Makes Ores only smelt into a single nugget, making it much harder to create large amounts of metal";
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        if (fixVanillaRecipes) {
            addHardcoreRecipe(new ShapedOreRecipe(null, Items.COMPASS, " N ", "NRN", " N ", 'N', "nuggetIron", 'R', "dustRedstone").setRegistryName(new ResourceLocation("minecraft", "compass")));
            addHardcoreRecipe(new ShapedOreRecipe(null, Items.CLOCK, " N ", "NQN", " N ", 'N', "nuggetGold", 'Q', "gemQuartz").setRegistryName(new ResourceLocation("minecraft", "clock")));
            addHardcoreRecipe(new ShapedOreRecipe(null, Items.BUCKET, "N N", " N ", 'N', "nuggetIron").setRegistryName(new ResourceLocation("minecraft", "bucket")));
            addHardcoreRecipe(new ShapelessOreRecipe(null, Items.FLINT_AND_STEEL, Items.FLINT, "nuggetIron").setRegistryName(new ResourceLocation("minecraft", "flint_and_steel")));
        }
        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_NUGGET, 3), new Object[]{new ItemStack(Items.BUCKET)});
        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_NUGGET, 4), new Object[]{new ItemStack(Items.COMPASS)});
        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.GOLD_NUGGET, 4), new Object[]{new ItemStack(Items.CLOCK)});
        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_NUGGET), new Object[]{new ItemStack(Blocks.TRIPWIRE_HOOK, 2, 0)});
        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_NUGGET), new Object[]{new ItemStack(Items.FLINT_AND_STEEL)});
    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 3), new Object[]{new ItemStack(Items.BUCKET)});
        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT), new Object[]{new ItemStack(Blocks.TRIPWIRE_HOOK, 2, 0)});
        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT), new Object[]{new ItemStack(Items.FLINT_AND_STEEL, 1, OreDictionary.WILDCARD_VALUE)});
        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 4), new Object[]{new ItemStack(Items.COMPASS)});
        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.GOLD_INGOT, 4), new Object[]{new ItemStack(Items.CLOCK)});
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        Set<String> oreExcludes = Sets.union(oreExclude, Sets.newHashSet("oreDiamond"));
        if (oreNuggetSmelting) {
            for (BWOreDictionary.Ore ore : BWOreDictionary.oreNames) {
                if (!oreExcludes.contains(ore.getOre())) {
                    for (BWOreDictionary.Ore nugget : BWOreDictionary.nuggetNames) {
                        if (nugget.getSuffix().equals(ore.getSuffix())) {
                            for (ItemStack stack : nugget.getMatchingStacks()) {
                                ItemStack n = stack.copy();
                                n.setCount(dustProductionCount);
                                //Remove all furnace recipes with dust
                                Arrays.stream(ore.getMatchingStacks()).forEach(BWMRecipes::removeRecipe);
                                //Add dust -> nugget smelting recipe
                                Arrays.stream(ore.getMatchingStacks()).forEach(s -> BWMRecipes.addFurnaceRecipe(s, n));
                            }
                        }
                    }
                }
            }
        }
        Set<String> dustExcludes = Sets.union(dustExclude, Sets.newHashSet("dustDiamond"));
        if (dustNuggetSmelting) {
            for (BWOreDictionary.Ore dust : BWOreDictionary.dustNames) {
                if (!dustExcludes.contains(dust.getOre())) {
                    for (BWOreDictionary.Ore nugget : BWOreDictionary.nuggetNames) {
                        if (nugget.getSuffix().equals(dust.getSuffix())) {
                            for (ItemStack stack : nugget.getMatchingStacks()) {
                                ItemStack n = stack.copy();
                                n.setCount(dustProductionCount);
                                //Remove all furnace recipes with dust
                                Arrays.stream(dust.getMatchingStacks()).forEach(BWMRecipes::removeRecipe);
                                //Add dust -> nugget smelting recipe
                                Arrays.stream(dust.getMatchingStacks()).forEach(s -> BWMRecipes.addFurnaceRecipe(s, n));
                            }
                        }
                    }
                }
            }
        }
    }


}
