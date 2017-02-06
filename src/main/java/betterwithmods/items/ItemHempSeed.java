package betterwithmods.items;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemHempSeed extends ItemBlock
{
    public ItemHempSeed(Block block) {
        super(block);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
        if (target instanceof EntityChicken) {
            if (((EntityChicken)target).getGrowingAge() == 0) {
                ((EntityChicken) target).setInLove(playerIn);
                stack.shrink(1);
                if (stack.getCount() == 0)
                    stack = ItemStack.EMPTY;
                return true;
            }
        }
        return false;
    }
}
