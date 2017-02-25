package betterwithmods.common.items.tools;

import betterwithmods.common.BWMItems;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.item.ItemSword;

public class ItemSoulforgedSword extends ItemSword {
    public ItemSoulforgedSword() {
        super(BWMItems.SOULFORGED_STEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }
}
