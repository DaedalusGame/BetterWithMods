package betterwithmods.common.items;

import betterwithmods.api.IMultiLocations;
import betterwithmods.config.BWConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ItemAltName extends Item implements IMultiLocations {

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (BWConfig.kidFriendly)
            return super.getUnlocalizedName() + "_kf";
        return super.getUnlocalizedName();
    }
}
