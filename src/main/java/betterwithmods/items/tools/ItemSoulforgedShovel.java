package betterwithmods.items.tools;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.item.ItemSpade;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSoulforgedShovel extends ItemSpade
{
    public ItemSoulforgedShovel()
    {
        super(BWRegistry.SOULFORGEDSTEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("bwm:soulforged_shovel");
        setRegistryName("steel_shovel");
        GameRegistry.register(this);
    }
}
