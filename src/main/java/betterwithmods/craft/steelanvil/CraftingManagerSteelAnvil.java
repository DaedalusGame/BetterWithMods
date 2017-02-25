package betterwithmods.craft.steelanvil;

import com.google.common.collect.Lists;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Iterator;
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

    public List<IRecipe> removeRecipes(ItemStack result) {
        List<IRecipe> removed = Lists.newArrayList();
        Iterator<IRecipe> it = recipes.iterator();
        while(it.hasNext())
        {
            IRecipe ir = it.next();
            if(OreDictionary.itemMatches(ir.getRecipeOutput(), result, true))
            {
                removed.add(ir);
            }
        }
        return removed;
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
