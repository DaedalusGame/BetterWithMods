package betterwithmods.integration.minetweaker;

import betterwithmods.craft.ChoppingRecipe;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

@ZenClass(ChopRecipe.clazz)
public class ChopRecipe {
    public static final String clazz = "mods.betterwithmods.ChopRecipe";

    @ZenMethod
    public static void add(IItemStack output, @Optional IItemStack bark, @Optional IItemStack sawdust, IItemStack log) {
        ChoppingRecipe recipe = new ChoppingRecipe(InputHelper.toStack(output), InputHelper.toStack(bark), InputHelper.toStack(sawdust), InputHelper.toStack(log));
        MineTweakerAPI.apply(new Add(recipe));
    }

    public static class Add extends BaseListAddition<IRecipe> {
        public Add(IRecipe recipe) {
            super("chopping", CraftingManager.getInstance().getRecipeList(), Arrays.asList(recipe));
        }

        @Override
        protected String getRecipeInfo(IRecipe recipe) {
            return recipe.getRecipeOutput().getDisplayName();
        }
    }
}
