package betterwithmods.items.tools;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.item.ItemAxe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSoulforgedAxe extends ItemAxe
{
    public ItemSoulforgedAxe()
    {
        super(BWRegistry.SOULFORGEDSTEEL, 8.0F, -3.0F);
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("bwm:soulforged_axe");
        setRegistryName("steel_axe");
        GameRegistry.register(this);
    }
}
