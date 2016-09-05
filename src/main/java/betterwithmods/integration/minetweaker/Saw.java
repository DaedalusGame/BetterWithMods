package betterwithmods.integration.minetweaker;

import betterwithmods.craft.SawInteraction;
import betterwithmods.integration.minetweaker.utils.BaseMapAddition;
import betterwithmods.integration.minetweaker.utils.BaseMapRemoval;
import betterwithmods.integration.minetweaker.utils.LogHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Hashtable;
import java.util.Map;

import static betterwithmods.integration.minetweaker.utils.InputHelper.toIItemStack;
import static betterwithmods.integration.minetweaker.utils.InputHelper.toStack;
import static betterwithmods.integration.minetweaker.utils.StackHelper.matches;

/**
 * Created by tyler on 9/3/16.
 */
@ZenClass("mods.betterwithmods.Saw")
public class Saw {
    public static Hashtable<String,ItemStack> woodProduct = SawInteraction.getWoodProducts();
    @ZenMethod
    public static void add(IItemStack input,  IItemStack output) {
        ItemStack stack = toStack(input);
        Block block = Block.getBlockFromItem(stack.getItem());
        MineTweakerAPI.apply(new Add(block,stack.getMetadata(),toStack(output)));
    }
    @ZenMethod
    public static void remove(IIngredient output) {
        Map<String,ItemStack> toRemove = new Hashtable<>();
        for(Map.Entry<String,ItemStack> sawRecipe : toRemove.entrySet()) {
            if(sawRecipe != null && matches(output, toIItemStack(sawRecipe.getValue()))) {
                toRemove.put(sawRecipe.getKey(), sawRecipe.getValue());
            }
        }
        if(!toRemove.isEmpty()) {
            MineTweakerAPI.apply(new Remove(toRemove));
        } else {
          LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", "saw", output.toString()));
        }
    }

    private static class Add extends BaseMapAddition<String, ItemStack> {

        protected Add(Block block, int meta, ItemStack product) {
            super("saw", woodProduct);
            recipes.put(block+":"+meta, product);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<String, ItemStack> recipe) {
            return LogHelper.getStackDescription(recipe.getKey());
        }
    }

    private static class Remove extends BaseMapRemoval<String, ItemStack> {
        protected Remove(Map<String,ItemStack> map) {
            super("saw", map, woodProduct);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<String, ItemStack> recipe) {
            return LogHelper.getStackDescription(recipe.getKey());
        }
    }
}
