package betterwithmods.integration;

import betterwithmods.module.hardcore.HCHunger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;

/**
 * Created by tyler on 5/13/17.
 */
public class ActuallyAdditions implements ICompatModule {
    @Override
    public void init() {
        HCHunger.setDessert(getItem(new ResourceLocation("actuallyadditions","item_food")));
    }

    public static ItemFood getItem(ResourceLocation location) {
        Item item = Item.REGISTRY.getObject(location);
        if(item instanceof ItemFood)
            return (ItemFood) item;
        return null;
    }
}
