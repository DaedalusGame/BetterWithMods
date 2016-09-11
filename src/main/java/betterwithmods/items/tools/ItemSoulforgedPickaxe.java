package betterwithmods.items.tools;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.item.ItemPickaxe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSoulforgedPickaxe extends ItemPickaxe
{
    public ItemSoulforgedPickaxe()
    {
        super(BWRegistry.SOULFORGEDSTEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("bwm:soulforged_pickaxe");
        setRegistryName("steel_pickaxe");
        GameRegistry.register(this);
    }
}
