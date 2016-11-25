package betterwithmods.integration.minetweaker;

import betterwithmods.BWMod;
import betterwithmods.craft.HopperFilters;
import betterwithmods.integration.minetweaker.utils.BaseMultiModification;
import betterwithmods.integration.minetweaker.utils.BaseUndoable;
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
        Set<ItemStack> set = Sets.<ItemStack>newHashSet(toStacks(toStacks(allowed)));
        MineTweakerAPI.apply(new HopperFilter.Add(toStack(filter), set));
    }

    @ZenMethod
    public static void addFilter(IItemStack filter, IItemStack newfilter) {
        MineTweakerAPI.apply(new HopperFilter.AddFilter(toStack(filter), toStack(newfilter)));
    }

    public static class Add extends BaseUndoable {
        ItemStack filter;
        Set<ItemStack> allowed;

        protected Add(ItemStack filter, Set<ItemStack> allowed) {
            super("hopperfilter");
            this.filter = filter;
            this.allowed = allowed;
        }

        @Override
        public void apply() {
            BWMod.logger.info("hopper:"+allowed);
            HopperFilters.addFilter(filter,allowed);
        }

        @Override
        public boolean canUndo() {
            return false;
        }

        @Override
        public void undo() {
            //NO-OP
        }
    }

    public static class AddFilter extends BaseUndoable {
        ItemStack filter;
        ItemStack newfilter;

        protected AddFilter(ItemStack filter, ItemStack newfilter) {
            super("hopperfilter");
            this.filter = filter;
            this.newfilter = newfilter;
        }

        @Override
        public void apply() {
            HopperFilters.addFilterItem(filter,newfilter);
        }

        @Override
        public boolean canUndo() {
            return false;
        }

        @Override
        public void undo() {
            //NO-OP
        }

    }
}
