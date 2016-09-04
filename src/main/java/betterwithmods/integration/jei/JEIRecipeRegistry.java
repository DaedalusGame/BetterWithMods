package betterwithmods.integration.jei;

import betterwithmods.craft.bulk.*;
import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.ArrayList;
import java.util.List;

public class JEIRecipeRegistry
{
    public static List<CauldronRecipeWrapper> getCauldronRecipes()
    {
        return getCauldronRecipes(CraftingManagerCauldron.getInstance());
    }

    public static List<StokedCauldronRecipeWrapper> getStokedCauldronRecipes()
    {
        return getStokedCauldronRecipes(CraftingManagerCauldronStoked.getInstance());
    }

    public static List<CrucibleRecipeWrapper> getCrucibleRecipes()
    {
        return getCrucibleRecipes(CraftingManagerCrucible.getInstance());
    }

    public static List<StokedCrucibleRecipeWrapper> getStokedCrucibleRecipes()
    {
        return getStokedCrucibleRecipes(CraftingManagerCrucibleStoked.getInstance());
    }

    public static List<MillRecipeWrapper> getMillRecipes()
    {
        return getMillRecipes(CraftingManagerMill.getInstance());
    }

    private static List<CrucibleRecipeWrapper> getCrucibleRecipes(CraftingManagerCrucible bulk)
    {
        List<CrucibleRecipeWrapper> recipes = new ArrayList();
        for(BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new CrucibleRecipeWrapper(recipe));
        return recipes;
    }

    private static List<StokedCrucibleRecipeWrapper> getStokedCrucibleRecipes(CraftingManagerCrucibleStoked bulk)
    {
        List<StokedCrucibleRecipeWrapper> recipes = new ArrayList();
        for(BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new StokedCrucibleRecipeWrapper(recipe));
        return recipes;
    }

    private static List<CauldronRecipeWrapper> getCauldronRecipes(CraftingManagerCauldron bulk)
    {
        List<CauldronRecipeWrapper> recipes = new ArrayList();
        for(BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new CauldronRecipeWrapper(recipe));
        return recipes;
    }

    private static List<StokedCauldronRecipeWrapper> getStokedCauldronRecipes(CraftingManagerCauldronStoked bulk)
    {
        List<StokedCauldronRecipeWrapper> recipes = new ArrayList();
        for(BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new StokedCauldronRecipeWrapper(recipe));
        return recipes;
    }

    private static List<MillRecipeWrapper> getMillRecipes(CraftingManagerMill bulk)
    {
        List<MillRecipeWrapper> recipes = new ArrayList();
        for(BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new MillRecipeWrapper(recipe));
        return recipes;
    }
}
