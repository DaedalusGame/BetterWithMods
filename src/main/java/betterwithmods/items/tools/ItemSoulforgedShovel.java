package betterwithmods.items.tools;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.items.IBWMItem;
import net.minecraft.item.ItemSpade;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSoulforgedShovel extends ItemSpade implements IBWMItem
{
    public ItemSoulforgedShovel()
    {
        super(BWRegistry.SOULFORGEDSTEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("bwm:steel_shovel");
        setRegistryName("steel_shovel");
        GameRegistry.register(this);
        register();
    }
    @Override
    public String getLocation(int meta) {
        return "betterwithmods:steel_shovel";
    }
}
