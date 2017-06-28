package betterwithmods.module.hardcore;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.module.Feature;
import betterwithmods.module.gameplay.CrucibleRecipes;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
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
            //TODO
//            BWMRecipes.removeRecipes(Items.COMPASS, 0);
//            BWMRecipes.removeRecipes(Items.CLOCK, 0);
//            BWMRecipes.removeRecipes(Items.BUCKET, 0);
//            BWMRecipes.removeRecipes(Items.FLINT_AND_STEEL, 0);

            BWMRecipes.addOreRecipe(new ItemStack(Items.COMPASS), " N ", "NRN", " N ", 'N', "nuggetIron", 'R', "dustRedstone");
            BWMRecipes.addOreRecipe(new ItemStack(Items.CLOCK), " N ", "NQN", " N ", 'N', "nuggetGold", 'Q', "gemQuartz");
            BWMRecipes.addOreRecipe(new ItemStack(Items.BUCKET), "N N", " N ", 'N', "nuggetIron");
            BWMRecipes.addShapelessOreRecipe(new ItemStack(Items.FLINT_AND_STEEL), Items.FLINT, "nuggetIron");
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
        Set<String> oreExcludes = Sets.union(oreExclude, Sets.newHashSet("diamond"));
        if (oreNuggetSmelting) {
            List<Pair<ItemStack, String>> oreSuffixes = BWOreDictionary.oreNames.stream().map(i -> Pair.of(i, BWOreDictionary.getSuffix(i, "ore"))).filter(p -> !oreExcludes.contains(p.getValue().toLowerCase())).collect(Collectors.toList());
            oreSuffixes.forEach(pair -> OreDictionary.getOres("nugget" + pair.getValue()).stream().findFirst().ifPresent(nugget -> {
                ItemStack n = nugget.copy();
                n.setCount(oreProductionCount);
                BWMRecipes.removeFurnaceRecipe(pair.getKey());
                FurnaceRecipes.instance().getSmeltingList().put(pair.getKey(), n);
            }));
        }
        Set<String> dustExcludes = Sets.union(dustExclude, Sets.newHashSet("diamond"));
        if (dustNuggetSmelting) {
            List<Pair<ItemStack, String>> dustSuffixes = BWOreDictionary.dustNames.stream().map(i -> Pair.of(i, BWOreDictionary.getSuffix(i, "dust"))).filter(p -> !dustExcludes.contains(p.getValue().toLowerCase())).collect(Collectors.toList());
            dustSuffixes.forEach(pair -> OreDictionary.getOres("nugget" + pair.getValue()).stream().findFirst().ifPresent(nugget -> {
                ItemStack n = nugget.copy();
                n.setCount(dustProductionCount);
                BWMRecipes.removeFurnaceRecipe(pair.getKey());
                FurnaceRecipes.instance().getSmeltingList().put(pair.getKey(), n);
            }));
        }
    }


}
