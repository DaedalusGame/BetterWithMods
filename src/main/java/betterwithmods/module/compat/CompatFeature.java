package betterwithmods.module.compat;

import betterwithmods.module.Feature;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;

/**
 * Created by tyler on 5/24/17.
 */
public class CompatFeature extends Feature {
    protected final String modid;

    public CompatFeature(String modid) {
        this.modid = modid;
    }


    public Item getItem(ResourceLocation location) {
        return Item.REGISTRY.getObject(location);
    }

    public Block getBlock(ResourceLocation location) {
        return Block.REGISTRY.getObject(location);
    }


    public ItemFood getItemFood(ResourceLocation location) {
        Item item = getItem(location);
        if (item instanceof ItemFood)
            return (ItemFood) item;
        return null;
    }


}
