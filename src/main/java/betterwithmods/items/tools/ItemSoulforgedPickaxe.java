package betterwithmods.items.tools;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.items.IBWMItem;
import net.minecraft.item.ItemPickaxe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSoulforgedPickaxe extends ItemPickaxe implements IBWMItem
{
    public ItemSoulforgedPickaxe()
    {
        super(BWRegistry.SOULFORGEDSTEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("bwm:steel_pickaxe");
        setRegistryName("steel_pickaxe");
        GameRegistry.register(this);
        register();
    }

    @Override
    public String getLocation(int meta) {
        return "betterwithmods:steel_pickaxe";
    }
}
