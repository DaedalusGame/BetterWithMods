package betterwithmods.event;

import betterwithmods.common.entity.ai.EndermanAgro;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/27/16
 */
public class HardcoreEndermenEvent {
    @SubscribeEvent
    public void onTeleport(EnderTeleportEvent evt) {
        evt.getEntityLiving().getEntityWorld().playSound(null, evt.getEntityLiving().getPosition(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.HOSTILE, 1, 1);
    }

    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent evt) {
        if (evt.getEntity() instanceof EntityEnderman) {
            if (!evt.getWorld().isRemote) {
                EntityEnderman entity = (EntityEnderman) evt.getEntity();
                entity.targetTasks.taskEntries.clear();
                entity.targetTasks.addTask(1, new EndermanAgro(entity));
                entity.targetTasks.addTask(2, new EntityAIHurtByTarget(entity, true, new Class[0]));
                entity.targetTasks.addTask(3, new EntityAINearestAttackableTarget(entity, EntityEndermite.class, 10, true, false, mite -> ((EntityEndermite) mite).isSpawnedByPlayer()));
            }
        }
    }
}
