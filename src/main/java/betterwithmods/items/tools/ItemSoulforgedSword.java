package betterwithmods.items.tools;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.items.IBWMItem;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSoulforgedSword extends ItemSword implements IBWMItem {
    public ItemSoulforgedSword() {
        super(BWRegistry.SOULFORGEDSTEEL);
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("bwm:steel_sword");
        setRegistryName("steel_sword");
        GameRegistry.register(this);
        register();
    }

    @Override
    public String getLocation(int meta) {
        return "betterwithmods:steel_shovel";
    }
}
