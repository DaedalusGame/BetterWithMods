package betterwithmods.common.items.tools;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.util.InvUtils;
import betterwithmods.util.item.ToolsManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemSoulforgedMattock extends ItemSoulforgedPickaxe {

    public ItemSoulforgedMattock() {
        super();
        setCreativeTab(BWCreativeTabs.BWTAB);
        ToolsManager.setToolAsEffectiveAgainst(this, Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (InvUtils.listContains(repair, OreDictionary.getOres("ingotSoulforgedSteel"))) return true;
        return super.getIsRepairable(toRepair, repair);
    }

}
