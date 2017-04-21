package betterwithmods.common.items;

import betterwithmods.api.IMultiLocations;
import betterwithmods.module.gameplay.Gameplay;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemAltNameFood extends ItemFood implements IMultiLocations {
    public ItemAltNameFood(int amount, float saturation, boolean wolfFood) {
        super(amount, saturation, wolfFood);
    }

    public ItemAltNameFood(int amount, boolean wolfFood) {
        super(amount, wolfFood);
    }

    @Override
    public String[] getLocations() {
        if (Gameplay.kidFriendly)
            return new String[]{"creeper_oyster_kf"};
        else
            return new String[]{"creeper_oyster"};
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (Gameplay.kidFriendly)
            return super.getUnlocalizedName() + "_kf";
        return super.getUnlocalizedName();
    }
}
