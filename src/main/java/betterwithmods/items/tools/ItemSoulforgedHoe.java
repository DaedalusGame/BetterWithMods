package betterwithmods.items.tools;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.item.ItemHoe;

public class ItemSoulforgedHoe extends ItemHoe
{
    public ItemSoulforgedHoe()
    {
        super(BWRegistry.SOULFORGEDSTEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("bwm:soulforged_hoe");
    }
}
