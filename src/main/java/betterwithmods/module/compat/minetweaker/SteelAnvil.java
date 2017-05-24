package betterwithmods.module.compat.minetweaker;

import betterwithmods.common.registry.steelanvil.SteelCraftingManager;
import betterwithmods.common.registry.steelanvil.SteelShapedOreRecipe;
import betterwithmods.common.registry.steelanvil.SteelShapelessRecipe;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.google.common.collect.Lists;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/5/17
 */
@ZenClass(SteelAnvil.clazz)
public class SteelAnvil {
    public static final String clazz = "mods.betterwithmods.SteelAnvil";

    public static List toList(IIngredient[] ingredients) {
        return Arrays.asList(InputHelper.toObjects(ingredients));
    }

    @ZenMethod
    public static void addShapeless(IItemStack output, IIngredient[] ingredients) {
        MineTweakerAPI.apply(new Add(new SteelShapelessRecipe(InputHelper.toStack(output), toList(ingredients))));
    }

    @ZenMethod
    public static void addShaped(IItemStack output, IIngredient[][] ingredients) {

        int height = ingredients.length;
        int width = 0;
        for (IIngredient[] row : ingredients) {
            if (width < row.length)
                width = row.length;
        }
        Object[] input = new Object[width * height];
        int x = 0;
        for (IIngredient[] row : ingredients) {
            for (IIngredient ingredient : row) {
                input[x++] = toActualObject(ingredient);
            }
        }

        MineTweakerAPI.apply(new Add(new SteelShapedOreRecipe(InputHelper.toStack(output), input, width, height)));
    }

    @ZenMethod
    public static void remove(IItemStack target) {
        List<IRecipe> list = SteelCraftingManager.getInstance().remove(InputHelper.toStack(target));
        MineTweakerAPI.apply(new Remove(list));
    }


    public static class Add extends BaseListAddition<IRecipe> {
        protected Add(IRecipe recipe) {
            super("steelanvil", SteelCraftingManager.getInstance().getRecipeList(), Lists.newArrayList(recipe));
        }

        @Override
        protected String getRecipeInfo(IRecipe recipe) {
            return LogHelper.getCraftingDescription(recipe);
        }
    }

    public static class Remove extends BaseListRemoval<IRecipe> {

        public Remove(List<IRecipe> list) {
            super("steelanvil", SteelCraftingManager.getInstance().getRecipeList(), list);
        }

        @Override
        protected String getRecipeInfo(IRecipe recipe) {
            return LogHelper.getCraftingDescription(recipe);
        }
    }

    private static Object toActualObject(IIngredient ingredient) {
        if (ingredient == null) return null;
        else {
            if (ingredient instanceof IOreDictEntry) {
                return OreDictionary.getOres(InputHelper.toString((IOreDictEntry) ingredient));
            } else if (ingredient instanceof IItemStack) {
                return InputHelper.toStack((IItemStack) ingredient);
            } else return null;
        }
    }
}
