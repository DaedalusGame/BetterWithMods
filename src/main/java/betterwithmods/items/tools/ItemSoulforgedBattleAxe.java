package betterwithmods.items.tools;

import betterwithmods.BWMItems;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.item.ItemAxe;

public class ItemSoulforgedBattleAxe extends ItemAxe {
    public ItemSoulforgedBattleAxe() {
        super(BWMItems.SOULFORGED_STEEL, 9F, -2.4f);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }
}
