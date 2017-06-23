package betterwithmods.module.compat.minetweaker;

import betterwithmods.module.hardcore.hchunger.FoodHelper;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseMapAddition;
import com.google.common.collect.Maps;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import squeek.applecore.api.food.FoodValues;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by primetoxinz on 6/9/17.
 */
@ZenClass("mods.betterwithmods.FoodValue")
public class FoodValue {

    @ZenMethod
    public static void setFoodValue(IItemStack stack, int hunger, float saturation) {
        HashMap<ItemStack,FoodValues> map = Maps.newHashMap();
        map.put(InputHelper.toStack(stack), new FoodValues(hunger,saturation));
        MineTweakerAPI.apply(new FoodSet(map));
    }

    public static class FoodSet extends BaseMapAddition<ItemStack,FoodValues> {

        protected FoodSet(Map<ItemStack, FoodValues> map) {
            super("foodset",FoodHelper.getFoodValues(), map);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<ItemStack, FoodValues> recipe) {
            return LogHelper.getStackDescription(recipe.getKey());
        }
    }
}
