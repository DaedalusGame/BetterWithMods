package betterwithmods.integration.minetweaker;

import betterwithmods.craft.HopperFilters;
import betterwithmods.integration.minetweaker.utils.BaseMultiModification;
import com.google.common.collect.Sets;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Set;

import static betterwithmods.integration.minetweaker.utils.InputHelper.toStack;
import static betterwithmods.integration.minetweaker.utils.InputHelper.toStacks;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/17/16
 */
@ZenClass("mods.betterwithmods.HopperFilter")
public class HopperFilter {

    @ZenMethod
    public static void add(IItemStack filter, IIngredient[] allowed) {
        //in toStacks converts IIngredient[] to IItemStack[], outer toStacks converts IItemStack[] to ItemStack[]
        Set<ItemStack> set = Sets.newHashSet(toStacks(toStacks(allowed)));
        MineTweakerAPI.apply(new HopperFilter.Add(toStack(filter), set));
    }

    public static class Add extends BaseMultiModification {
        protected Add(ItemStack filter, Set<ItemStack> allowed) {
            super("hopperfilter");
            HopperFilters.addFilter(filter, allowed);
        }
        @Override
        public boolean canUndo() {
            return false;
        }

    }
}
