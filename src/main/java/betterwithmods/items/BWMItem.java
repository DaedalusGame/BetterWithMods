package betterwithmods.items;

import betterwithmods.client.BWCreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by tyler on 9/10/16.
 */
public class BWMItem extends Item implements IBWMItem {
    private String name;
    public BWMItem(String name) {
        setUnlocalizedName("bwm:"+name);
        this.name = name;
        setRegistryName(name);
        GameRegistry.register(this);
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        register();
    }

    @Override
    public String getLocation(int meta) {
        return name;
    }
}
