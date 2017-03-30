package betterwithmods.common.items.tools;

import betterwithmods.common.BWMItems;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.util.InvUtils;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemSoulforgedAxe extends ItemAxe {
    public ItemSoulforgedAxe() {
        super(BWMItems.SOULFORGED_STEEL, 8.0F, -3.0F);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (InvUtils.listContains(repair, OreDictionary.getOres("ingotSoulforgedSteel"))) return true;
        return super.getIsRepairable(toRepair, repair);
    }
}
