package betterwithmods.module.gameplay;

import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.registry.OreStack;
import betterwithmods.module.Feature;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

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
        CrucibleRecipes.addCrucibleRecipe(new ItemStack(Items.IRON_INGOT), new OreStack("nuggetIron", 9));
        for (ItemStack ingot : BWOreDictionary.ingotNames) {
            String suffix = BWOreDictionary.getSuffix(ingot,"ingot");
            if(suffix != null)
                CrucibleRecipes.addStokedCrucibleRecipe(ingot, new OreStack("nugget"+suffix, 9));
        }
    }
}
