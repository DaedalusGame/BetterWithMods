package betterwithmods.common.items.tools;

import betterwithmods.common.BWMItems;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.util.InvUtils;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemSoulforgedBattleAxe extends ItemAxe {
    public ItemSoulforgedBattleAxe() {
        super(BWMItems.SOULFORGED_STEEL, 9F, -2.4f);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (InvUtils.listContains(repair, OreDictionary.getOres("ingotSoulforgedSteel"))) return true;
        return super.getIsRepairable(toRepair, repair);
    }
}
