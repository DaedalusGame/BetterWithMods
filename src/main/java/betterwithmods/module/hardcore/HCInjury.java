package betterwithmods.module.hardcore;

import betterwithmods.common.BWSounds;
import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.util.player.EntityPlayerExt;
import betterwithmods.util.player.HealthPenalty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 5/24/17.
 */
public class HCInjury extends Feature {


    @SubscribeEvent
    public void attack(LivingAttackEvent event) {

        if (event.getSource().getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getEntity();
            if (EntityPlayerExt.isSurvival(player)) {
                HealthPenalty healthPenalty = EntityPlayerExt.getHealthPenalty(player);
                double mod = healthPenalty.getModifier();
                if (mod <= 0.75) {
                    player.playSound(BWSounds.OOF, 0.75f, 1f);
                    event.setCanceled(true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public void penalty(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote)
            return;
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (!EntityPlayerExt.isSurvival(player))
                return;
            if (player != null) {
                if (!ModuleLoader.isFeatureEnabled(HCHunger.class)) {
                    EntityPlayerExt.changeSpeed(player, HCHunger.penaltySpeedUUID, "Health speed penalty", EntityPlayerExt.getHealthAndExhaustionModifier(player));
                }
                HealthPenalty healthPenalty = EntityPlayerExt.getHealthPenalty(player);
                double mod = healthPenalty.getModifier();
                if (mod <= 0.25) {
                    if (player.world.getWorldTime() % 60 == 0) {
                        player.playSound(BWSounds.OOF, 0.75f, 1f);
                        if (mod <= 0.20)
                            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 80, 0, false, false));
                    }
                }
            }
        }
    }

    @Override
    public String getFeatureDescription() {
        return "Add Penalties to lower health levels.";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
