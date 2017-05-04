package betterwithmods.common.items.tools;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.BWMItems;
import betterwithmods.util.InvUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.oredict.OreDictionary;

public class ItemSoulforgedSword extends ItemSword {
    public ItemSoulforgedSword() {
        super(BWMItems.SOULFORGED_STEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return InvUtils.listContains(repair, OreDictionary.getOres("ingotSoulforgedSteel")) || super.getIsRepairable(toRepair, repair);
    }
}
