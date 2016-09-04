package betterwithmods.items.tools;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.item.ItemSword;

public class ItemSoulforgedSword extends ItemSword
{
    public ItemSoulforgedSword()
    {
        super(BWRegistry.SOULFORGEDSTEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("bwm:soulforged_sword");
    }
}
