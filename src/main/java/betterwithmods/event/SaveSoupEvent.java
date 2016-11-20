package betterwithmods.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by blueyu2 on 11/18/16.
 */
public class SaveSoupEvent {
    @SubscribeEvent
    public void saveSoup(LivingEntityUseItemEvent.Finish event)
    {
        if(event.getItem() != null)
        {
            if(event.getItem().getItem() instanceof ItemSoup)
            {
                if(event.getItem().stackSize > 0)
                {
                    ItemStack result = event.getResultStack();
                    event.setResultStack(event.getItem());
                    if(event.getEntityLiving() instanceof EntityPlayer)
                    {
                        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                        if (!player.inventory.addItemStackToInventory(result))
                        {
                            player.dropItem(result, false);
                        }
                    }
                }
            }
        }
    }
}
