package betterwithmods.module.hardcore;

import betterwithmods.common.entity.item.EntityItemBuoy;
import betterwithmods.module.Feature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Koward
 */
public class HCBuoy extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Makes it so items float or not depending on their material properties";
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }
    /**
     * Substitute the original {@link EntityItem} by our new {@link EntityItemBuoy}.
     */
    @SubscribeEvent
    public void replaceServerEntityItem(EntityJoinWorldEvent event) {
        World world = event.getWorld();
        if (world.isRemote) return;

        if (!(event.getEntity().getClass() == EntityItem.class)) return;
        EntityItem entityItem = (EntityItem) event.getEntity();

        if (entityItem.getEntityItem().getCount() > 0) {
            event.setResult(Event.Result.DENY);
            EntityItemBuoy newEntity = new EntityItemBuoy(entityItem);
            if (entityItem.delayBeforeCanPickup == 40) {
                newEntity.setWatchItem(entityItem);
                event.setCanceled(true);
            }
            else {
                entityItem.setDead();
                entityItem.setInfinitePickupDelay();
            }
            world.spawnEntity(newEntity);
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
