package betterwithmods.module.hardcore;

import betterwithmods.common.BWCrafting;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.module.Feature;
import betterwithmods.util.RecipeUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tyler on 4/20/17.
 */
public class HCOres extends Feature {

    private static boolean oreNuggetSmelting, dustNuggetSmelting, fixVanillaRecipes;
    private static StringList oreExclude, dustExclude;

    @Override
    public void setupConfig() {
        oreNuggetSmelting = loadPropBool("Ore to Nugget Smelting", "Make Ores (oredict ore.* )smelt into nuggets instead of ingots", true);

        oreExclude = StringList.asList(loadPropStringList("Ore Exclude", "Oredictionary entries to exclude from ore to nugget smelting. Remove the prefix of the oredictionary. example 'oreIron' would be just 'iron' ", new String[0]));
        dustExclude = StringList.asList(loadPropStringList("Dust Exclude", "Oredictionary entries to exclude from dust to nugget smelting  Remove the prefix of the oredictionary. example 'dustIron' would be just 'iron'", new String[0]));

        dustNuggetSmelting = loadPropBool("Dust to Nugget Smelting", "Make Dusts ( oredict dust.* ) smelt into nuggets instead of ingots", true);
        fixVanillaRecipes = loadPropBool("Fix Vanilla Recipes", "Make certain recipes cheaper to be more reasonable with nugget smelting, including Compass, Clock, and Bucket", true);
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

        RecipeUtils.removeRecipes(Items.COMPASS, 0);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.COMPASS), " N ", "NRN", " N ", 'N', "nuggetIron", 'R', "dustRedstone"));
        RecipeUtils.removeRecipes(Items.CLOCK, 0);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.CLOCK), " N ", "NQN", " N ", 'N', "nuggetGold", 'Q', "gemQuartz"));
        RecipeUtils.removeRecipes(Items.BUCKET, 0);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.BUCKET), "N N", " N ", 'N', "nuggetIron"));
        BWCrafting.addStokedCrucibleRecipe(new ItemStack(Items.field_191525_da, 3), new ItemStack[]{new ItemStack(Items.BUCKET)});
    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
        BWCrafting.addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 3), new ItemStack[]{new ItemStack(Items.BUCKET)});
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if (oreNuggetSmelting) {
            List<Pair<ItemStack, String>> oreSuffixes = BWOreDictionary.oreNames.stream().map(i -> Pair.of(i, BWOreDictionary.getSuffix(i, "ore"))).filter(p -> !oreExclude.contains(p.getValue())).collect(Collectors.toList());
            oreSuffixes.forEach(pair -> OreDictionary.getOres("nugget" + pair.getValue()).stream().findFirst().ifPresent(nugget -> {
                RecipeUtils.removeFurnaceRecipe(pair.getKey());
                FurnaceRecipes.instance().getSmeltingList().put(pair.getKey(), nugget);
            }));
        }
        if (dustNuggetSmelting) {
            List<Pair<ItemStack, String>> dustSuffixes = BWOreDictionary.dustNames.stream().map(i -> Pair.of(i, BWOreDictionary.getSuffix(i, "dust"))).filter(p -> !dustExclude.contains(p.getValue())).collect(Collectors.toList());
            dustSuffixes.forEach(pair -> OreDictionary.getOres("nugget" + pair.getValue()).stream().findFirst().ifPresent(nugget -> {
                RecipeUtils.removeFurnaceRecipe(pair.getKey());
                FurnaceRecipes.instance().getSmeltingList().put(pair.getKey(), nugget);
            }));
        }
    }

    private static class StringList extends ArrayList<String> {
        public static StringList asList(String[] array) {
            StringList list = new StringList();
            list.addAll(Arrays.asList(array));
            return list;
        }

        public boolean contains(String o) {
            return indexOf(o) >= 0;
        }


        public int indexOf(String var1) {
            int var2;
            if(var1 == null) {
                for(var2 = 0; var2 < this.size(); ++var2) {
                    if(this.get(var2) == null) {
                        return var2;
                    }
                }
            } else {
                for(var2 = 0; var2 < this.size(); ++var2) {
                    if(var1.equalsIgnoreCase(this.get(var2))) {
                        return var2;
                    }
                }
            }
            return -1;
        }
    }


}
