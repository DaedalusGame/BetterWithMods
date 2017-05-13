package betterwithmods.module.hardcore;

import betterwithmods.client.gui.GuiGloom;
import betterwithmods.module.Feature;
import betterwithmods.util.player.EntityPlayerExt;
import betterwithmods.util.player.GloomPenalty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by tyler on 5/13/17.
 */
public class HCGloom extends Feature {
    private static final DataParameter<Integer> GLOOM_TICK = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);

    @SubscribeEvent
    public void onEntityInit(EntityEvent.EntityConstructing event) {
        if(event.getEntity() instanceof EntityPlayer) {
            event.getEntity().getDataManager().register(GLOOM_TICK,0);
        }
    }
    @SubscribeEvent
    public void inDarkness(TickEvent.PlayerTickEvent e) {

        EntityPlayer player = e.player;
        World world = player.getEntityWorld();
        if(world.isRemote)
            return;
        int light = world.getLight(player.getPosition());
        int tick = getGloomTime(player);
        if(light <= 7) {
            incrementGloomTime(player);
        } else if(tick != 0) {
               setGloomTick(player,0);
        }
    }

    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent event) {
        GloomPenalty penalty = EntityPlayerExt.getGloomPenalty(event.getEntity());
        if(penalty != GloomPenalty.NO_PENALTY) {
            event.setNewfov(event.getFov() + 1/penalty.getModifier());
        }
    }
    public static int getGloomTime(EntityPlayer player) {
        return player.getDataManager().get(GLOOM_TICK);
    }

    public static void incrementGloomTime(EntityPlayer player) {
        setGloomTick(player, getGloomTime(player)+1);
    }
    public static void setGloomTick(EntityPlayer player, int value) {
        player.getDataManager().set(GLOOM_TICK, value);
    }

    @Override
    public String getFeatureDescription() {
        return "Be afraid of the dark...";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    private GuiGloom guiGloom = null;

    @SubscribeEvent
    public void renderStatus(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
                if(guiGloom == null)
                    guiGloom = new GuiGloom();

                guiGloom.draw();
        }
    }
}
