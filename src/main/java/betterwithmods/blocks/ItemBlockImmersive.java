package betterwithmods.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBlockImmersive extends ItemBlock {
    public ItemBlockImmersive(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("bwm.message.immersive.0"));
        tooltip.add(I18n.format("bwm.message.immersive.1"));
    }
}
