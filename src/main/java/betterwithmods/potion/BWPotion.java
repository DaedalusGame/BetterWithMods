package betterwithmods.potion;

import net.minecraft.potion.Potion;

public class BWPotion extends Potion {

    public BWPotion(boolean isBadEffectIn, int liquidColorIn,String name, int x, int y) {
        this(isBadEffectIn,liquidColorIn);
        setRegistryName(name);
        setPotionName("bwm.effect."+name);
        setIconIndex(x,y);
    }
    public BWPotion(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
    }
}
