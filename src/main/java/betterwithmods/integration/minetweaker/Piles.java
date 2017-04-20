package betterwithmods.integration.minetweaker;

import betterwithmods.module.hardcore.HCPiles;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.helpers.StackHelper;
import com.blamejared.mtlib.utils.BaseMapAddition;
import com.google.common.collect.Maps;
import minetweaker.MineTweakerAPI;
import minetweaker.api.block.IBlock;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tyler on 4/20/17.
 */
@ZenClass(Piles.clazz)
public class Piles {
    public static final String clazz = "mods.betterwithmods.Piles";

    @ZenMethod
    public static void addPile(IItemStack block, IItemStack stack) {
        ItemStack a = InputHelper.toStack(block);
        Block b = null;
        if (InputHelper.isABlock(block) && (a.getItem() instanceof ItemBlock)) {
            b = ((ItemBlock) a.getItem()).getBlock();
        }
        HashMap<Block, ItemStack> map = Maps.newHashMap();
        map.put(b, InputHelper.toStack(stack));
        MineTweakerAPI.apply(new AddPile(map));
    }

    public static class AddPile extends BaseMapAddition<Block, ItemStack> {
        public AddPile(HashMap<Block, ItemStack> map) {
            super("piles", HCPiles.blockToPile, map);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<Block, ItemStack> recipe) {
            return recipe.getKey().getLocalizedName();
        }
    }
}
