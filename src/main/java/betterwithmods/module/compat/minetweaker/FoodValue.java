package betterwithmods.module.compat.minetweaker;

import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.HCHunger;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseUndoable;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Created by primetoxinz on 6/9/17.
 */
@ZenClass("mods.betterwithmods.FoodValue")
public class FoodValue {

    @ZenMethod
    public static void setFat(IItemStack input, float fat) {
        if(ModuleLoader.isFeatureEnabled(HCHunger.class)) {
            setSaturation(input,fat);
        }
    }
    @ZenMethod
    public static void setSaturation(IItemStack input, float saturation) {
        ItemStack stack = InputHelper.toStack(input);
        if(stack.getItem() instanceof ItemFood) {
            HCHunger.modifySaturation((ItemFood)stack.getItem(), saturation);
        }
    }

    @ZenMethod
    public static void setFoodValue(IItemStack input, int value) {
        ItemStack stack = InputHelper.toStack(input);
        if(stack.getItem() instanceof ItemFood) {
            MineTweakerAPI.apply(new Food((ItemFood) stack.getItem(),value));
        }
    }

    public static class Food extends BaseUndoable {
        private static int previous;
        private int current;
        private ItemFood food;
        protected Food(ItemFood food, int value) {
            super("food");
            this.food = food;
            previous = food.getHealAmount(new ItemStack(food));
            current = value;
        }

        @Override
        public void apply() {
            HCHunger.modifyFood(food,current);
        }

        @Override
        public void undo() {
            HCHunger.modifyFood(food,previous);
        }
    }
}
