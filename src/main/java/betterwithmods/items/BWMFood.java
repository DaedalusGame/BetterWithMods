package betterwithmods.items;

import net.minecraft.item.ItemFood;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by tyler on 9/10/16.
 */
public class BWMFood extends ItemFood {
    public BWMFood(String name,int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        setUnlocalizedName("bwm:"+name);
        setRegistryName(name);
        GameRegistry.register(this);
    }

    public BWMFood(String name, int amount, boolean isWolfFood) {
        super(amount, isWolfFood);
        setUnlocalizedName("bwm:"+name);
        setRegistryName(name);
        GameRegistry.register(this);
    }
}
