package betterwithmods.integration.minetweaker;

import betterwithmods.module.hardcore.HCPiles;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseMapAddition;
import com.google.common.collect.Maps;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
        HashMap<IBlockState, ItemStack> map = Maps.newHashMap();
        map.put(b.getStateFromMeta(a.getMetadata()), InputHelper.toStack(stack));
        MineTweakerAPI.apply(new AddPile(map));
    }

    public static class AddPile extends BaseMapAddition<IBlockState, ItemStack> {
        public AddPile(HashMap<IBlockState, ItemStack> map) {
            super("piles", HCPiles.blockStateToPile, map);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<IBlockState, ItemStack> recipe) {
            return recipe.getKey().getBlock().getLocalizedName();
        }
    }
}
