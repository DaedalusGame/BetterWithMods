package betterwithmods.module.tweaks;

import betterwithmods.common.BWCrafting;
import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class KilnSmelting extends Feature {
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        InvUtils.oreNames.stream().filter(ore -> ore.getItem() instanceof ItemBlock).forEach(ore -> {
            ItemStack output = FurnaceRecipes.instance().getSmeltingResult(ore);
            if (ore != ItemStack.EMPTY && output != ItemStack.EMPTY)
                BWCrafting.addKilnRecipe(ore, output);
        });
    }

    @Override
    public String getFeatureDescription() {
        return "Allows Kiln to Smelt Ores";
    }
}
