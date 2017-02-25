package betterwithmods.event;

import betterwithmods.config.BWConfig;
import betterwithmods.common.entity.item.EntityItemBuoy;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Koward
 */
public class BuoyancyEventHandler {
    /**
     * Substitute the original {@link EntityItem} by our new {@link EntityItemBuoy}.
     */
    @SubscribeEvent
    public void replaceServerEntityItem(EntityJoinWorldEvent event) {
        if (!BWConfig.hardcoreBuoy) return;
        World world = event.getWorld();
        if (world.isRemote) return;

        if (!(event.getEntity().getClass() == EntityItem.class)) return;
        EntityItem entityItem = (EntityItem) event.getEntity();

        if (entityItem.getEntityItem().getCount() > 0) {
            event.setResult(Result.DENY);
            event.setCanceled(true);
            EntityItemBuoy newEntity = new EntityItemBuoy(entityItem);
            entityItem.setDead();
            entityItem.setInfinitePickupDelay();
            world.spawnEntity(newEntity);
        }
    }
}
