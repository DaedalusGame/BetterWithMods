package betterwithmods.util;

import betterwithmods.BWCrafting;
import betterwithmods.craft.bulk.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Map;

public class RecipeUtils
{
    public static void gatherCookableFood()
    {
        Map<ItemStack, ItemStack> furnace = FurnaceRecipes.instance().getSmeltingList();

        for(ItemStack input : furnace.keySet())
        {
            if(input != null)
            {
                if(input.getItem() instanceof ItemFood && input.getItem() != Items.BREAD)
                {
                    ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
                    if(output != null) {
                        BWCrafting.addCauldronRecipe(output.copy(), new ItemStack[]{input.copy()});
                    }
                }
            }
        }
    }

    public static void refreshRecipes()
    {
        CraftingManagerCauldron.getInstance().refreshRecipes();
        CraftingManagerCauldronStoked.getInstance().refreshRecipes();
        CraftingManagerCrucible.getInstance().refreshRecipes();
        CraftingManagerCrucibleStoked.getInstance().refreshRecipes();
        CraftingManagerMill.getInstance().refreshRecipes();
    }

    private static boolean hasOreName(ItemStack stack, String oreName)
    {
        if(stack.getItem() == null)
            return false;
        int ID = OreDictionary.getOreID(oreName);
        for(int i : OreDictionary.getOreIDs(stack))
            if(i == ID) return true;
        return false;
    }


}
