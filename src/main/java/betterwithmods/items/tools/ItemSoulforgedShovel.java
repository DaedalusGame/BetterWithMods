package betterwithmods.items.tools;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.item.ItemSpade;

public class ItemSoulforgedShovel extends ItemSpade
{
    public ItemSoulforgedShovel()
    {
        super(BWRegistry.SOULFORGEDSTEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("bwm:soulforged_shovel");
    }
}
