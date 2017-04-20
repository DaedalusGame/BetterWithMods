package betterwithmods.module.hardcore;

import betterwithmods.module.Feature;
import betterwithmods.util.player.EntityPlayerExt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class HCBeds extends Feature {
    public static final EntityPlayer.SleepResult TOO_RESTLESS = EnumHelper.addEnum(EntityPlayer.SleepResult.class, "TOO_RESTLESS", new Class[0], new Object[0]);

    @Override
    public String getFeatureDescription() {
        return "Disables the ability to sleep in a bed and can no longer set spawn";
    }

    /**
     * Disable Beds
     */
    @SubscribeEvent
    public void onSleepInBed(PlayerSleepInBedEvent event) {
        if (EntityPlayerExt.isSurvival(event.getEntityPlayer())) {
            event.getEntityPlayer().sendStatusMessage(new TextComponentTranslation("tile.bed.tooRestless", new Object[0]), true);
            event.setResult(TOO_RESTLESS);
        }
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
