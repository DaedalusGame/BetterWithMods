package betterwithmods.integration.minetweaker;

import betterwithmods.integration.minetweaker.utils.ItemMapModification;
import betterwithmods.integration.minetweaker.utils.LogHelper;
import betterwithmods.util.item.ItemExt;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static betterwithmods.integration.minetweaker.utils.InputHelper.toStack;

@ZenClass("mods.betterwithmods.Bouyancy")
public class Bouyancy {

    @ZenMethod
    public static void set(IItemStack input, float bouyancy) {
        if(bouyancy*bouyancy >=1) {
            LogHelper.logError("Bouyancy must be [-1.0,1.0]");
        } else {
            ItemStack stack = toStack(input);
            MineTweakerAPI.apply(new BouyancySet(stack.getItem(), stack.getMetadata(), bouyancy));
        }
    }

    private static class BouyancySet extends ItemMapModification<Float> {
        protected BouyancySet(Item item, int meta, float bouyancy) {
            super("bouyancy", -1f, ItemExt.getBuoyancyRegistry());
            ItemExt.getBuoyancyRegistry().put(item,meta,bouyancy);
        }
    }
}
