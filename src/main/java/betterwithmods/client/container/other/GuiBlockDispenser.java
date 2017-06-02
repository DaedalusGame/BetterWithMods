package betterwithmods.client.container.other;

import betterwithmods.BWMod;
import betterwithmods.common.blocks.tile.TileEntityBlockDispenser;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.opengl.GL11;

public class GuiBlockDispenser extends GuiContainer {
    private static final int guiHeight = 182;
    private static final String NAME = "inv.bwm.dispenser.name";
    final IItemHandler playerInv;
    private final TileEntityBlockDispenser tile;

    public GuiBlockDispenser(EntityPlayer player, TileEntityBlockDispenser tile) {

        super(new ContainerBlockDispenser(player, tile));
        playerInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        this.tile = tile;
        this.ySize = guiHeight;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        String s = I18n.format(NAME);
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation(BWMod.MODID, "textures/gui/dispenser.png"));

        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);

        int xOff = this.tile.nextIndex % 4 * 18;
        int yOff = this.tile.nextIndex / 4 * 18;
        drawTexturedModalRect(xPos + 51 + xOff, yPos + 15 + yOff, 176, 0, 20, 20);
    }
}
