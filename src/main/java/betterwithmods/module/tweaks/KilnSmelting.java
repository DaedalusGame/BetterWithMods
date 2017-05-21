package betterwithmods.module.tweaks;

import betterwithmods.common.BWOreDictionary;
import betterwithmods.module.Feature;
import betterwithmods.module.gameplay.KilnRecipes;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class KilnSmelting extends Feature {
    private static int oreProductionCount;
    @Override
    public void setupConfig() {
        oreProductionCount = loadPropInt("Ore Production Count","Number of Materials returned from Smelting an Ore in the Kiln", 1);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        BWOreDictionary.oreNames.stream().filter(ore -> ore.getItem() instanceof ItemBlock).forEach(ore -> {
            ItemStack output = FurnaceRecipes.instance().getSmeltingResult(ore).copy();
            output.setCount(oreProductionCount);
            if (!ore.isEmpty() && !output.isEmpty())
                KilnRecipes.addKilnRecipe(ore, output);
        });
    }

    @Override
    public String getFeatureDescription() {
        return "Allows Kiln to Smelt Ores";
    }
}
