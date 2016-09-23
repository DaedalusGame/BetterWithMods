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

@ZenClass("mods.betterwithmods.Buoyancy")
public class Buoyancy {

	@ZenMethod
	public static void set(IItemStack input, float buoyancy) {
		if (buoyancy * buoyancy >= 1) {
			LogHelper.logError("Buoyancy must be [-1.0,1.0]");
		} else {
			ItemStack stack = toStack(input);
			MineTweakerAPI.apply(new BuoyancySet(stack.getItem(), stack.getMetadata(), buoyancy));
		}
	}

	private static class BuoyancySet extends ItemMapModification<Float> {
		protected BuoyancySet(Item item, int meta, float buoyancy) {
			super("buoyancy", -1f, ItemExt.getBuoyancyRegistry());
			ItemExt.getBuoyancyRegistry().put(item, meta, buoyancy);
		}
	}
}
