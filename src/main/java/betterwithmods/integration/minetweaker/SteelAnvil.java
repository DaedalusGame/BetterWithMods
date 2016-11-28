package betterwithmods.integration.minetweaker;

import betterwithmods.craft.OreStack;
import betterwithmods.craft.steelanvil.CraftingManagerSteelAnvil;
import betterwithmods.craft.steelanvil.ShapedSteelAnvilRecipe;
import betterwithmods.integration.minetweaker.utils.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

import static betterwithmods.integration.minetweaker.utils.InputHelper.toIItemStack;
import static betterwithmods.integration.minetweaker.utils.InputHelper.toStack;

/**
 * Created by blueyu2 on 11/25/16.
 */
@ZenClass("mods.betterwithmods.SteelAnvil")
public class SteelAnvil {
    private final static List<IRecipe> recipes = CraftingManagerSteelAnvil.INSTANCE.getRecipes();

    @ZenMethod
    public static void addShaped(IItemStack output, IIngredient[][] inputs) {
        MineTweakerAPI.apply(new SteelAnvil.Add(new ShapedSteelAnvilRecipe(output, inputs)));
    }

    @ZenMethod
    public static void addShapeless(IItemStack output, IIngredient[] inputs) {
        MineTweakerAPI.apply(new SteelAnvil.Add(new ShapelessOreRecipe(toStack(output), toInputs(inputs))));
    }

    //Crafting doesn't use stack sizes for inputs
    private static Object[] toInputs(IIngredient[] ingredients) {
        Object[] ing = InputHelper.toInputs(ingredients);
        if (ing == null)
            return null;
        else {
            Object[] obj = new Object[ing.length];
            for (int i = 0; i < ing.length; i++) {
                if (ing[i] instanceof OreStack) {
                    obj[i] = ((OreStack)ing[i]).getOreName();
                }
                else
                    obj[i] = ing[i];
            }
            return obj;
        }
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        List<IRecipe> toRemove = new ArrayList<>();
        for (IRecipe recipe : recipes) {
            if (StackHelper.matches(output, toIItemStack(recipe.getRecipeOutput()))) {
                toRemove.add(recipe);
            }
        }
        if (!toRemove.isEmpty()) {
            MineTweakerAPI.apply(new Remove(toRemove));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", "steelAnvil", output.toString()));
        }
    }

    public static class Add extends BaseListAddition<IRecipe> {

        protected Add(IRecipe recipe) {
            super("steelAnvil", SteelAnvil.recipes);
            recipes.add(recipe);
        }

        @Override
        protected String getRecipeInfo(IRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getRecipeOutput());
        }
    }

    public static class Remove extends BaseListRemoval<IRecipe> {

        protected Remove(List<IRecipe> list) {
            super("steelAnvil", SteelAnvil.recipes, list);
        }

        @Override
        protected String getRecipeInfo(IRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getRecipeOutput());
        }
    }
}
