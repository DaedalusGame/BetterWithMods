package betterwithmods.client.gui;


import betterwithmods.util.BWMFoodStats;
import betterwithmods.util.player.EntityPlayerExt;
import betterwithmods.util.player.HungerPenalty;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraftforge.client.GuiIngameForge;

import java.util.Random;

/**
 * GUI for hunger levels and player penalties.
 *
 * @author Koward
 */
public class GuiHunger {
    private final Random rand = new Random();
    private final Minecraft mc = Minecraft.getMinecraft();
    private int shakeCounter = 0;

    public void draw() {
        ScaledResolution scale = ((GuiIngameForge) mc.ingameGUI).getResolution();

        int left = scale.getScaledWidth() / 2 + 91;
        int top = scale.getScaledHeight() - GuiIngameForge.right_height;

        drawFoodOverlay(left, top);
        if (!this.mc.player.isInsideOfMaterial(Material.WATER)
                && this.mc.player.getAir() >= 300) {
            left = scale.getScaledWidth() / 2 + 91;
            top = scale.getScaledHeight() - GuiIngameForge.right_height;
            drawPenaltyText(left, top);
        }
    }

    private void drawFoodOverlay(int left, int top) {
        boolean shakeTriggered = false; //TODO trigger shake effect

        EntityPlayer player = this.mc.player;
        BWMFoodStats foodStats = (BWMFoodStats) player.getFoodStats();
        int foodLevel = foodStats.getFoodLevel();
        int castedFat = (int) ((foodStats.getSaturationLevel() + 0.124F) * 4.0F);
        int level = foodLevel / 6;

        if (shakeTriggered) {
            this.shakeCounter = 20;
            shakeTriggered = false;
        } else if (shakeCounter > 0) {
            --this.shakeCounter;
        }

        GlStateManager.enableBlend();
        for (int i = 0; i < 10; ++i) {
            int y = top;
            int icon = 16;
            byte background = 0;

            if (player.isPotionActive(MobEffects.HUNGER)) {
                icon += 36;
                background = 13;
            } else if (i < castedFat >> 3) {
                background = 1;
            }

            if (EntityPlayerExt.getHungerPenalty(player) != HungerPenalty.NO_PENALTY && mc.ingameGUI.getUpdateCounter() % (foodLevel * 5 + 1) == 0) {
                y = top + (rand.nextInt(3) - 1);
            } else if (shakeCounter > 0) {
                int move = rand.nextInt(2) == 0 ? 1 : -1;
                y = top + move;
            }

            int x = left - i * 8 - 9;

            mc.ingameGUI.drawTexturedModalRect(x, y, 16 + background * 9, 27, 9, 9);

            int remainder;
            if (i == castedFat >> 3 && !player.isPotionActive(MobEffects.HUNGER)) {
                remainder = castedFat % 8;

                if (remainder != 0) {
                    mc.ingameGUI.drawTexturedModalRect(x + 8 - remainder, y, 33 - remainder,
                            27, 1 + remainder, 9);
                }
            }

            if (i < level) {
                mc.ingameGUI.drawTexturedModalRect(x, y, icon + 36, 27, 9, 9);
            } else if (i == level) {
                remainder = foodLevel % 6;

                if (remainder != 0) {
                    mc.ingameGUI.drawTexturedModalRect(x + 7 - remainder, y, icon + 36 + 7
                            - remainder, 27, 3 + remainder, 9);
                }
            }
        }

        GlStateManager.disableBlend();
        GuiIngameForge.right_height += 10;
    }

    private boolean drawPenaltyText(int left, int top) {
        if (this.mc.player.isDead) {
            return false;
        } else {
            FontRenderer fontRenderer = this.mc.fontRendererObj;
            String text = EntityPlayerExt.getWorstPenalty(mc.player).getDescription();
            if (!text.equals("")) {
                int width = fontRenderer.getStringWidth(text);
                fontRenderer.drawStringWithShadow(text, left - width, top,
                        16777215);
                GuiIngameForge.right_height += 10;
                return true;
            }
            return false;
        }
    }
}
