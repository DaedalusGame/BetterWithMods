package betterwithmods.client.container.other;

import betterwithmods.BWMod;
import betterwithmods.common.blocks.mechanical.tile.TileEntityPulley;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiPulley extends GuiContainer {
    private final TileEntityPulley tile;

    public GuiPulley(EntityPlayer player, TileEntityPulley tile) {
        super(new ContainerPulley(player, tile));
        this.ySize = 193;
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        String s = I18n.format(tile.getName());
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation(BWMod.MODID, "textures/gui/pulley.png"));

        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);

        if (this.tile.isMechanicallyPowered()) {
            drawTexturedModalRect(xPos + 81, yPos + 30, 176, 0, 14, 14);
        }

    }
}
