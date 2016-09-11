package betterwithmods.items.tools;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.items.IBWMItem;
import net.minecraft.item.ItemHoe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSoulforgedHoe extends ItemHoe implements IBWMItem
{
    public ItemSoulforgedHoe()
    {
        super(BWRegistry.SOULFORGEDSTEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("bwm:steel_hoe");
        setRegistryName("steel_hoe");
        GameRegistry.register(this);
        register();
    }

    @Override
    public String getLocation(int meta) {
        return "betterwithmods:steel_hoe";
    }
}
