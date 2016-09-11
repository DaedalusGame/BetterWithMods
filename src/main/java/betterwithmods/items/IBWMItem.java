package betterwithmods.items;

import betterwithmods.BWMod;
import net.minecraft.item.Item;

/**
 * Created by tyler on 9/10/16.
 */
public interface IBWMItem {
    default String getLocation(int meta) { return "inventory"; }
    default int getMaxMeta() { return 1;}
    default void register() { if(this instanceof Item) BWMod.proxy.addItemBlockModel((Item)this);}
}
