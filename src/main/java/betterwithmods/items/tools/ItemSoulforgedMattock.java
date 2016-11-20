package betterwithmods.items.tools;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.util.item.ToolsManager;
import net.minecraft.init.Blocks;

public class ItemSoulforgedMattock extends ItemSoulforgedPickaxe {

    public ItemSoulforgedMattock() {
        super();
        setCreativeTab(BWCreativeTabs.BWTAB);
        ToolsManager.setToolAsEffectiveAgainst(this, Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH);
    }

}
