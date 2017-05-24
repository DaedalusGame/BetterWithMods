package betterwithmods.module.compat.minetweaker;

import betterwithmods.common.registry.ChoppingRecipe;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Collections;

@ZenClass(ChopRecipe.clazz)
public class ChopRecipe {
    public static final String clazz = "mods.betterwithmods.ChopRecipe";

    @ZenMethod
    public static void add(IItemStack output, @Optional IItemStack bark, @Optional IItemStack sawdust, IIngredient log) {
        ChoppingRecipe recipe = new ChoppingRecipe(InputHelper.toStack(output), InputHelper.toStack(bark), InputHelper.toStack(sawdust), InputHelper.toObject(log));
        MineTweakerAPI.apply(new Add(recipe));
    }

    public static class Add extends BaseListAddition<IRecipe> {
        public Add(IRecipe recipe) {
            super("chopping", CraftingManager.getInstance().getRecipeList(), Collections.singletonList(recipe));
        }

        @Override
        protected String getRecipeInfo(IRecipe recipe) {
            return recipe.getRecipeOutput().getDisplayName();
        }
    }
}
