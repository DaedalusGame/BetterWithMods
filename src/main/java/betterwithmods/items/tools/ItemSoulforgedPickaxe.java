package betterwithmods.items.tools;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.item.ItemPickaxe;

public class ItemSoulforgedPickaxe extends ItemPickaxe
{
    public ItemSoulforgedPickaxe()
    {
        super(BWRegistry.SOULFORGEDSTEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("bwm:soulforged_pickaxe");
    }
}
