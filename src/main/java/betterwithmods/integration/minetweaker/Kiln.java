package betterwithmods.integration.minetweaker;


import betterwithmods.craft.KilnInteraction;
import betterwithmods.integration.minetweaker.utils.BaseMapAddition;
import betterwithmods.integration.minetweaker.utils.BaseMapRemoval;
import betterwithmods.integration.minetweaker.utils.LogHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static betterwithmods.integration.minetweaker.utils.InputHelper.*;
import static betterwithmods.integration.minetweaker.utils.StackHelper.matches;

/**
 * Created by tyler on 9/3/16.
 */
@ZenClass("mods.betterwithmods.Kiln")
public class Kiln {
    public static Hashtable<String,List<ItemStack>> cookables = KilnInteraction.getCookables();
    @ZenMethod
    public static void add(IItemStack input,  IItemStack[] output) {
        ItemStack stack = toStack(input);
        Block block = Block.getBlockFromItem(stack.getItem());
        MineTweakerAPI.apply(new Add(block,stack.getMetadata(),toStacks(output)));
    }
    @ZenMethod
    public static void remove(IIngredient output) {
        Map<String,List<ItemStack>> toRemove = new Hashtable<>();
        for(Map.Entry<String,List<ItemStack>> sawRecipe : toRemove.entrySet()) {
            if(sawRecipe != null && matches(output, toIItemStack(sawRecipe.getValue().get(0)))) {
                toRemove.put(sawRecipe.getKey(), sawRecipe.getValue());
            }
        }
        if(!toRemove.isEmpty()) {
            MineTweakerAPI.apply(new Remove(toRemove));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", "saw", output.toString()));
        }
    }

    private static class Add extends BaseMapAddition<String, List<ItemStack>> {

        @SuppressWarnings("unchecked")
		protected Add(Block block, int meta, ItemStack... product) {
            super("kiln", cookables);
            recipes.put(block+":"+meta, Arrays.asList(product));
        }

        @Override
        protected String getRecipeInfo(Map.Entry<String, List<ItemStack>> recipe) {
            return LogHelper.getStackDescription(recipe.getKey());
        }
    }

    private static class Remove extends BaseMapRemoval<String, List<ItemStack>> {
        protected Remove(Map<String,List<ItemStack>> map) {
            super("saw", map, cookables);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<String, List<ItemStack>> recipe) {
            return LogHelper.getStackDescription(recipe.getKey());
        }
    }
}
