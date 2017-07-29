package betterwithmods.client.gui;

import betterwithmods.BWMod;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Items;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by tyler on 5/13/17.
 */
@Mod.EventBusSubscriber(modid = BWMod.MODID)
@SideOnly(Side.CLIENT)
public class GuiStatus {

    @SideOnly(Side.CLIENT)
    public static GuiStatus INSTANCE = new GuiStatus();
    public static boolean isGloomLoaded, isHungerLoaded, isInjuryLoaded;
    public static int offsetX, offsetY;
    private final Minecraft mc = Minecraft.getMinecraft();

    @SideOnly(Side.CLIENT)
    public void draw() {
        if (!PlayerHelper.isSurvival(mc.player))
            return;
        ScaledResolution scale = ((GuiIngameForge) mc.ingameGUI).getResolution();
        int left = scale.getScaledWidth() / 2 + 91 + offsetX;
        int top = scale.getScaledHeight() - GuiIngameForge.right_height - 10 + offsetY;
        drawPenaltyText(left, top);
    }

    @SideOnly(Side.CLIENT)
    private boolean drawPenaltyText(int left, int top) {
        if (this.mc.player.isDead) {
            return false;
        } else {
            int y = top;
            FontRenderer fontRenderer = this.mc.fontRenderer;
            String injury = PlayerHelper.getHealthPenalty(mc.player).getDescription();
            String gloom = PlayerHelper.getGloomPenalty(mc.player).getDescription();
            String hunger = PlayerHelper.getWorstPenalty(mc.player).getDescription();

            if (isInjuryLoaded && !injury.isEmpty()) {
                int width = fontRenderer.getStringWidth(injury);
                fontRenderer.drawStringWithShadow(injury, left - width, y,
                        16777215);
                y -= 10;
            }

            if (isGloomLoaded && !gloom.isEmpty()) {
                int width = fontRenderer.getStringWidth(gloom);
                fontRenderer.drawStringWithShadow(gloom, left - width, y,
                        16777215);
                y -= 10;
            }
            if (isHungerLoaded && !hunger.isEmpty()) {
                int width = fontRenderer.getStringWidth(hunger);
                fontRenderer.drawStringWithShadow(hunger, left - width, y,
                        16777215);
                y -= 10;
            }
            GuiIngameForge.right_height += y;
            return false;
        }
    }

    @SubscribeEvent
    public static void onFOV(FOVUpdateEvent event) {
        float f = 1.0F;

        if (event.getEntity().capabilities.isFlying) {
            f *= 1.1F;
        }

//        IAttributeInstance iattributeinstance = event.getEntity().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
//        f = (float)((double)f * ((iattributeinstance.getAttributeValue() / (double)event.getEntity().capabilities.getWalkSpeed() + 1.0D) / 2.0D));
//
//        if (event.getEntity().capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f))
//        {
//            f = 1.0F;
//        }

        if (event.getEntity().isHandActive() && event.getEntity().getActiveItemStack().getItem() == Items.BOW) {
            int i = event.getEntity().getItemInUseMaxCount();
            float f1 = (float) i / 20.0F;

            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 = f1 * f1;
            }

            f *= 1.0F - f1 * 0.15F;
        }
        event.setNewfov(f);
    }
}
