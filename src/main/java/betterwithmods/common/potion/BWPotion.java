package betterwithmods.common.potion;

import net.minecraft.potion.Potion;

public class BWPotion extends Potion {

    public BWPotion(boolean isBadEffectIn, int liquidColorIn, int x, int y) {
        this(isBadEffectIn, liquidColorIn);
        setIconIndex(x, y);
    }

    public BWPotion(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
    }
}
