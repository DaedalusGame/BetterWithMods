package betterwithmods.util;

import betterwithmods.BWCrafting;
import betterwithmods.BWMod;
import betterwithmods.craft.bulk.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public final class RecipeUtils {
    private RecipeUtils() {
    }

    /**
     * Remove all recipes.
     *
     * @param item Item to remove recipes of.
     * @param meta Metavalue.
     *             If {@link OreDictionary#WILDCARD_VALUE} all recipes of the item will be removed.
     */
    public static void removeRecipes(Item item, int meta) {
        List<IRecipe> recipeList = CraftingManager.getInstance().getRecipeList();
        final ListIterator<IRecipe> li = recipeList.listIterator();
        boolean found = false;
        while (li.hasNext()) {
            ItemStack output = li.next().getRecipeOutput();
            if (output != null && output.getItem() == item) {
                if(meta == OreDictionary.WILDCARD_VALUE || output.getMetadata() == meta) {
                    li.remove();
                    found = true;
                }
            }
        }
        if (!found)
            BWMod.logger.error("No matching recipe found.");
    }

    public static void gatherCookableFood() {
        Map<ItemStack, ItemStack> furnace = FurnaceRecipes.instance().getSmeltingList();

        for (ItemStack input : furnace.keySet()) {
            if (input != null) {
                if (input.getItem() instanceof ItemFood && input.getItem() != Items.BREAD) {
                    ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
                    if (output != null) {
                        BWCrafting.addCauldronRecipe(output.copy(), new ItemStack[]{input.copy()});
                    }
                }
            }
        }
    }

    public static void refreshRecipes() {
        CraftingManagerCauldron.getInstance().refreshRecipes();
        CraftingManagerCauldronStoked.getInstance().refreshRecipes();
        CraftingManagerCrucible.getInstance().refreshRecipes();
        CraftingManagerCrucibleStoked.getInstance().refreshRecipes();
        CraftingManagerMill.getInstance().refreshRecipes();
    }
}
