package betterwithmods.integration.jei;

import betterwithmods.craft.KilnInteraction;
import betterwithmods.craft.SawInteraction;
import betterwithmods.craft.bulk.*;
import betterwithmods.integration.jei.wrapper.*;
import mezz.jei.api.IJeiHelpers;

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

    public static List<CauldronRecipeWrapper> getCauldronRecipes(IJeiHelpers helper) {
        return getCauldronRecipes(CraftingManagerCauldron.getInstance(), helper);
    }

    public static List<StokedCauldronRecipeWrapper> getStokedCauldronRecipes(IJeiHelpers helper) {
        return getStokedCauldronRecipes(CraftingManagerCauldronStoked.getInstance(), helper);
    }

    public static List<CrucibleRecipeWrapper> getCrucibleRecipes(IJeiHelpers helper) {
        return getCrucibleRecipes(CraftingManagerCrucible.getInstance(), helper);
    }

    public static List<StokedCrucibleRecipeWrapper> getStokedCrucibleRecipes(IJeiHelpers helper) {
        return getStokedCrucibleRecipes(CraftingManagerCrucibleStoked.getInstance(), helper);
    }

    public static List<MillRecipeWrapper> getMillRecipes(IJeiHelpers helper) {
        return getMillRecipes(CraftingManagerMill.getInstance(), helper);
    }

    private static List<CrucibleRecipeWrapper> getCrucibleRecipes(CraftingManagerCrucible bulk, IJeiHelpers helper) {
        List<CrucibleRecipeWrapper> recipes = new ArrayList<CrucibleRecipeWrapper>();
        for (BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new CrucibleRecipeWrapper(helper, recipe));
        return recipes;
    }

    private static List<StokedCrucibleRecipeWrapper> getStokedCrucibleRecipes(CraftingManagerCrucibleStoked bulk, IJeiHelpers helper) {
        List<StokedCrucibleRecipeWrapper> recipes = new ArrayList<StokedCrucibleRecipeWrapper>();
        for (BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new StokedCrucibleRecipeWrapper(helper, recipe));
        return recipes;
    }

    private static List<CauldronRecipeWrapper> getCauldronRecipes(CraftingManagerCauldron bulk, IJeiHelpers helper) {
        List<CauldronRecipeWrapper> recipes = new ArrayList<CauldronRecipeWrapper>();
        for (BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new CauldronRecipeWrapper(helper, recipe));
        return recipes;
    }

    private static List<StokedCauldronRecipeWrapper> getStokedCauldronRecipes(CraftingManagerCauldronStoked bulk, IJeiHelpers helper) {
        List<StokedCauldronRecipeWrapper> recipes = new ArrayList<StokedCauldronRecipeWrapper>();
        for (BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new StokedCauldronRecipeWrapper(helper, recipe));
        return recipes;
    }

    private static List<MillRecipeWrapper> getMillRecipes(CraftingManagerMill bulk, IJeiHelpers helper) {
        List<MillRecipeWrapper> recipes = new ArrayList<MillRecipeWrapper>();
        for (BulkRecipe recipe : bulk.getRecipes())
            recipes.add(new MillRecipeWrapper(helper, recipe));
        return recipes;
    }
}
