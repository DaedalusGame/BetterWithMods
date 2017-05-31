package betterwithmods.module.compat.minetweaker;

import betterwithmods.module.hardcore.HCBuoy;
import betterwithmods.util.item.Stack;
import betterwithmods.util.item.StackMap;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseMapAddition;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Map;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/25/17
 */
@ZenClass(Buoyancy.clazz)
public class Buoyancy {

    public static final String clazz = "mods.betterwithmods.Buoyancy";

    @ZenMethod
    public static void set(IItemStack stack, float value) {
        StackMap<Float> map = new StackMap(1.0);
        map.put(new Stack(InputHelper.toStack(stack)),value);
        MineTweakerAPI.apply(new Add(map));
    }

    public static class Add extends BaseMapAddition<Stack, Float> {
        protected Add(StackMap<Float> map) {
            super("buoyancy", HCBuoy.buoyancy, map);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<Stack, Float> recipe) {
            return recipe.getKey().toString() + " -> " + recipe.getValue();
        }

    }
}
