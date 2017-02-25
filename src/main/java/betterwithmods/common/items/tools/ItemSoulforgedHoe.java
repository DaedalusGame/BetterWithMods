package betterwithmods.common.items.tools;

import betterwithmods.common.BWMItems;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.item.ItemHoe;

public class ItemSoulforgedHoe extends ItemHoe {
    public ItemSoulforgedHoe() {
        super(BWMItems.SOULFORGED_STEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }
}
