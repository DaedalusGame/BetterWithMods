package betterwithmods.client.container.anvil;

import betterwithmods.BWMod;
import betterwithmods.common.blocks.tile.TileEntitySteelAnvil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiSteelAnvil extends GuiContainer {

    private static final ResourceLocation tex = new ResourceLocation(BWMod.MODID, "textures/gui/steel_anvil.png");
    private TileEntitySteelAnvil tile;

    public GuiSteelAnvil(TileEntitySteelAnvil tileEntity, ContainerSteelAnvil container) {
        super(container);
        this.ySize = 183;
        tile = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        this.fontRenderer.drawString(I18n.format(tile.getName()), 10, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 3, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(tex);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
    }
}