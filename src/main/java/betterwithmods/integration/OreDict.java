package betterwithmods.integration;

import betterwithmods.BWCrafting;
import betterwithmods.BWRegistry;
import net.minecraft.item.ItemStack;

public class OreDict
{
    public static void init()
    {
        BWCrafting.addOreMillRecipe(new ItemStack(BWRegistry.material, 1, 37), "cropBarley");
        BWCrafting.addOreMillRecipe(new ItemStack(BWRegistry.material, 1, 37), "cropOats");
        BWCrafting.addOreMillRecipe(new ItemStack(BWRegistry.material, 1, 37), "cropRye");
        BWCrafting.addOreMillRecipe(new ItemStack(BWRegistry.material, 1, 37), "cropRice");
    }
}
