package betterwithmods.client.container.bulk;

import betterwithmods.BWMod;
import betterwithmods.common.blocks.tile.TileEntityCookingPot;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCookingPot extends GuiContainer {
    private static final int fireHeight = 12;
    private static final int stokedHeight = 28;
    private final TileEntityCookingPot tile;


    public GuiCookingPot(EntityPlayer player, TileEntityCookingPot tile) {
        super(new ContainerCookingPot(player, tile));
        this.ySize = 193;
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        String s = I18n.format(tile.getName());
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f,
                                                   int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.renderEngine.bindTexture(new ResourceLocation(BWMod.MODID, "textures/gui/cauldron.png"));
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);

        if (this.tile.isCooking()) {
            int iconHeight = this.tile.getFireIntensity() > 5 ? GuiCookingPot.stokedHeight : GuiCookingPot.fireHeight;
            int scaledIconHeight = this.tile.getCookingProgressScaled(12);

            drawTexturedModalRect(xPos + 81, yPos + 19 + 12 - scaledIconHeight, 176, iconHeight - scaledIconHeight, 14, scaledIconHeight + 2);
        }
    }
}
