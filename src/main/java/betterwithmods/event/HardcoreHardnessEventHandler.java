package betterwithmods.event;

import betterwithmods.config.BWConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Koward
 */
public class HardcoreHardnessEventHandler {
    /**
     * Sets the wooden pickaxe to 1 usage. Why:
     * {@link Item#setMaxDamage} used with "1" gives 2 usages, and with "0" gives unbreakable item.
     * So we needed another solution to set it to 1 usage.
     */
    @SubscribeEvent
    public void onBreaking(BlockEvent.BreakEvent event) {
        if (!BWConfig.earlyPickaxesRebalance) return;
        EntityPlayer player = event.getPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        if (stack == ItemStack.field_190927_a || stack.getItem() == null) return;
        if (stack.getItem() == Items.WOODEN_PICKAXE) {
            destroyItem(stack, player);
        }
    }

    private void destroyItem(ItemStack stack, EntityLivingBase entity) {
        //FIXME No sound triggered.
        int damage = stack.getMaxDamage() + 1;
        stack.damageItem(damage, entity);
    }
}
