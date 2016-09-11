package betterwithmods.items.tools;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.items.IBWMItem;
import net.minecraft.item.ItemAxe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSoulforgedAxe extends ItemAxe implements IBWMItem
{
    public ItemSoulforgedAxe()
    {
        super(BWRegistry.SOULFORGEDSTEEL, 8.0F, -3.0F);
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("bwm:steel_axe");
        setRegistryName("steel_axe");
        GameRegistry.register(this);
        register();
    }

    @Override
    public String getLocation(int meta) {
        return "betterwithmods:steel_axe";
    }
}
