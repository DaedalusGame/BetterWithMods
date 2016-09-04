package betterwithmods.client.container;

import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;

import betterwithmods.blocks.tile.TileEntityBlockDispenser;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiBlockDispenser extends GuiContainer
{
	private static final int guiHeight = 182;
	private TileEntityBlockDispenser tile;
	private IInventory playerInv;
	
	public GuiBlockDispenser(IInventory inv, TileEntityBlockDispenser tile)
	{
		super(new ContainerBlockDispenser(inv, tile));
		this.playerInv = inv;
		this.tile = tile;
		this.ySize = guiHeight;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		String s = I18n.format(tile.getName());
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation("betterwithmods", "textures/gui/dispenser.png"));
		
		int xPos = (this.width - this.xSize) / 2;
		int yPos = (this.height - this.ySize) / 2;
		drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
		
		int xOff = this.tile.getField(0) % 4 * 18;
		int yOff = this.tile.getField(0) / 4 * 18;
		drawTexturedModalRect(xPos + 51 + xOff, yPos + 15 + yOff, 176, 0, 20, 20);
	}
}
