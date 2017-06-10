package betterwithmods.common.blocks.mini;

import betterwithmods.common.blocks.ItemBlockMeta;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockMini extends ItemBlockMeta {
    public ItemBlockMini(Block block) {
        super(block);
        this.setMaxDamage(0).setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
