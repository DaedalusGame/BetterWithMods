package betterwithmods.common.items;

import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

public class ItemHempSeed extends ItemBlock {
    public ItemHempSeed(Block block) {
        super(block);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (target instanceof EntityChicken) {
            EntityChicken chicken = (EntityChicken) target;
            if (chicken.getGrowingAge() == 0 && !chicken.isInLove()) {
                chicken.setInLove(playerIn);
                InvUtils.usePlayerItem(playerIn, EnumFacing.UP, stack, 1);
                return true;
            }
        }
        return false;
    }
}
