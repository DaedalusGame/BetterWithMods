package betterwithmods.items;

import betterwithmods.client.BWCreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by tyler on 9/10/16.
 */
public class BWMItem extends Item implements IBWMItem {
    public BWMItem(String name) {
        setUnlocalizedName("bwm:"+name);
        setRegistryName(name);
        GameRegistry.register(this);
        this.setCreativeTab(BWCreativeTabs.BWTAB);
    }

}
