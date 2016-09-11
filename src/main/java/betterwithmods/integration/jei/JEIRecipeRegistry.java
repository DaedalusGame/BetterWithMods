package betterwithmods.integration.jei;

import betterwithmods.craft.KilnInteraction;
import betterwithmods.craft.SawInteraction;
import betterwithmods.craft.bulk.*;
import betterwithmods.integration.jei.wrapper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JEIRecipeRegistry {
    public static List<KilnWrapper> getKilnRecipes() {
        return KilnInteraction.getCookables().entrySet().stream().map(entry -> new KilnWrapper(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public static List<SawWrapper> getSawRecipes() {

        return SawInteraction.getWoodProducts().entrySet().stream().map(entry -> new SawWrapper(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public static List<CauldronRecipeWrapper> getCauldronRecipes() {
        return getCauldronRecipes(CraftingManagerCauldron.getInstance());
    }

    public static List<StokedCauldronRecipeWrapper> getStokedCauldronRecipes() {
        return getStokedCauldronRecipes(CraftingManagerCauldronStoked.getInstance());
    }

    public static List<CrucibleRecipeWrapper> getCrucibleRecipes() {
        return getCrucibleRecipes(CraftingManagerCrucible.getInstance());
    }

    public static List<StokedCrucibleRecipeWrapper> getStokedCrucibleRecipes() {
        return getStokedCrucibleRecipes(CraftingManagerCrucibleStoked.getInstance());
    }

    public static List<MillRecipeWrapper> getMillRecipes() {
        return getMillRecipes(CraftingManagerMill.getInstance());
    }

    private static List<CrucibleRecipeWrapper> getCrucibleRecipes(CraftingManagerCrucible bulk) {
        List<CrucibleRecipeWrapper> recipes = new ArrayList<CrucibleRecipeWrapper>();
        for (BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new CrucibleRecipeWrapper(recipe));
        return recipes;
    }

    private static List<StokedCrucibleRecipeWrapper> getStokedCrucibleRecipes(CraftingManagerCrucibleStoked bulk) {
        List<StokedCrucibleRecipeWrapper> recipes = new ArrayList<StokedCrucibleRecipeWrapper>();
        for (BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new StokedCrucibleRecipeWrapper(recipe));
        return recipes;
    }

    private static List<CauldronRecipeWrapper> getCauldronRecipes(CraftingManagerCauldron bulk) {
        List<CauldronRecipeWrapper> recipes = new ArrayList<CauldronRecipeWrapper>();
        for (BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new CauldronRecipeWrapper(recipe));
        return recipes;
    }

    private static List<StokedCauldronRecipeWrapper> getStokedCauldronRecipes(CraftingManagerCauldronStoked bulk) {
        List<StokedCauldronRecipeWrapper> recipes = new ArrayList<StokedCauldronRecipeWrapper>();
        for (BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new StokedCauldronRecipeWrapper(recipe));
        return recipes;
    }

    private static List<MillRecipeWrapper> getMillRecipes(CraftingManagerMill bulk) {
        List<MillRecipeWrapper> recipes = new ArrayList<MillRecipeWrapper>();
        for (BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new MillRecipeWrapper(recipe));
        return recipes;
    }
}
