package betterwithmods.craft.steelanvil;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blueyu2 on 11/22/16.
 */
public class CraftingManagerSteelAnvil {
    public static CraftingManagerSteelAnvil INSTANCE = new CraftingManagerSteelAnvil();
    private List<IRecipe> recipes = new ArrayList<>();

    public ShapedSteelAnvilRecipe addRecipe(ItemStack result, Object... recipe) {
        ShapedSteelAnvilRecipe craft = new ShapedSteelAnvilRecipe(result, recipe);
        recipes.add(craft);
        return craft;
    }

    public ShapelessOreRecipe addShapelessRecipe(ItemStack result, Object... ingredients) {
        ShapelessOreRecipe recipe = new ShapelessOreRecipe(result, ingredients);
        recipes.add(recipe);
        return recipe;
    }

    public void removeRecipe(ItemStack result) {
        List<IRecipe> toRemove = new ArrayList<>();
        for (IRecipe recipe : this.recipes) {
            if (OreDictionary.itemMatches(recipe.getRecipeOutput(), result, true)) {
                toRemove.add(recipe);
            }
        }
        for (IRecipe recipe : toRemove)
            this.recipes.remove(recipe);
    }

    public ItemStack findMatchingRecipe(InventoryCrafting matrix, World world) {
        for (IRecipe recipe : this.recipes) {
            if (recipe.matches(matrix, world)) {
                return recipe.getCraftingResult(matrix);
            }
        }

        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting craftMatrix, World worldIn) {
        for (IRecipe recipe : this.recipes) {
            if (recipe.matches(craftMatrix, worldIn)) {
                return recipe.getRemainingItems(craftMatrix);
            }
        }

        NonNullList<ItemStack> aitemstack = NonNullList.create();

        for (int i = 0; i < craftMatrix.getSizeInventory(); ++i) {
            aitemstack.set(i, craftMatrix.getStackInSlot(i));
        }

        return aitemstack;
    }

    public List<IRecipe> getRecipes() {
        return this.recipes;
    }
}
