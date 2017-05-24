package betterwithmods.event;

import betterwithmods.client.gui.GuiStatusEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 5/24/17.
 */
public class StatusEffectEvent {
    @SubscribeEvent
    public void renderStatus(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            if (GuiStatusEffect.INSTANCE == null)
                GuiStatusEffect.INSTANCE = new GuiStatusEffect();
            GuiStatusEffect.INSTANCE.draw();
        }
    }
}
