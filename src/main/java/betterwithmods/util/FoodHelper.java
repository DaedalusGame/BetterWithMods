package betterwithmods.util;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockCake;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import squeek.applecore.api.food.FoodValues;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by primetoxinz on 6/20/17.
 */
public class FoodHelper {
    private static final HashMap<ItemStack, FoodValues> FOOD_VALUES = Maps.newHashMap();

    public static boolean isDessert(ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock) {
            return ((ItemBlock) stack.getItem()).getBlock() instanceof BlockCake;
        } else if (stack.getItem() instanceof ItemFood) {
            ItemFood food = (ItemFood) stack.getItem();
            return ReflectionHelper.getPrivateValue(ItemFood.class, food, 4);
        }
        return false;
    }

    public static Optional<FoodValues> getFoodValue(ItemStack stack) {
        return FOOD_VALUES.entrySet().stream().filter(entry -> match(entry.getKey(), stack)).map(Map.Entry::getValue).findFirst();
    }

    public static void registerFood(ItemStack item, int hunger) {
        registerFood(item, hunger, 0);
    }

    public static void registerFood(ItemStack stack, int hunger, int saturation) {
        registerFood(stack, hunger, saturation, false);
    }


    public static void registerFood(ItemStack stack, int hunger, int saturation, boolean alwaysEdible) {
        registerFood(stack, new FoodValues(hunger, saturation), alwaysEdible);
    }

    public static void registerFood(ItemStack stack, FoodValues values) {
        registerFood(stack, values, false);
    }

    public static void registerFood(ItemStack stack, FoodValues values, boolean alwaysEdible) {
        if (alwaysEdible) {
            setAlwaysEdible(stack);
        }
        FOOD_VALUES.put(stack, values);
    }


    public static void setAlwaysEdible(ItemStack stack) {
        if (stack.getItem() instanceof ItemFood) {
            ItemFood food = (ItemFood) stack.getItem();
            food.setAlwaysEdible();
        }
    }

    private static boolean match(ItemStack a, ItemStack b) {
        return a.getItem().equals(b.getItem()) && (a.getMetadata() == b.getMetadata() || (a.getMetadata() == OreDictionary.WILDCARD_VALUE || b.getMetadata() == OreDictionary.WILDCARD_VALUE));
    }
}
