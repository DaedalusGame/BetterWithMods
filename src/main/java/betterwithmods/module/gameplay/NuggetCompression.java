package betterwithmods.module.gameplay;

import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.registry.OreStack;
import betterwithmods.module.Feature;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.Set;

/**
 * Created by tyler on 4/21/17.
 */
public class NuggetCompression extends Feature {

    @Override
    public String getFeatureDescription() {
        return "Adds recipes to the Crucible to compact 9 Nuggets into it's corresponding Ingot.";
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        Set<String> exclude = Sets.newHashSet("diamond","soulforgedsteel");
        for (ItemStack ingot : BWOreDictionary.ingotNames) {
            String suffix = BWOreDictionary.getSuffix(ingot,"ingot");

            if(suffix != null && !exclude.contains(suffix.toLowerCase())) {
                OreStack nugget = new OreStack("nugget"+suffix, 9);
                if(!nugget.isEmpty())
                    CrucibleRecipes.addStokedCrucibleRecipe(ingot, nugget);
            }

        }
    }
}
