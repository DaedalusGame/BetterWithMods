package betterwithmods.client.gui;

import betterwithmods.util.player.PlayerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by tyler on 5/13/17.
 */
public class GuiStatus {

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
                y-=10;
            }

            if (isGloomLoaded && !gloom.isEmpty()) {
                int width = fontRenderer.getStringWidth(gloom);
                fontRenderer.drawStringWithShadow(gloom, left - width, y,
                        16777215);
                y-=10;
            }
            if (isHungerLoaded && !hunger.isEmpty()) {
                int width = fontRenderer.getStringWidth(hunger);
                fontRenderer.drawStringWithShadow(hunger, left - width, y,
                        16777215);
                y-=10;
            }
            GuiIngameForge.right_height+=y;
            return false;
        }
    }
}
