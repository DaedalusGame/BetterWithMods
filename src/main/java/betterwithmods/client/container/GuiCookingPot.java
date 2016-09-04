package betterwithmods.client.container;

import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;

import betterwithmods.blocks.tile.TileEntityCookingPot;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiCookingPot extends GuiContainer
{
	private static final int fireHeight = 12;
	private static final int stokedHeight = 28;
	private TileEntityCookingPot tile;
	private IInventory playerInv;
	
	public GuiCookingPot(IInventory inv, TileEntityCookingPot tile)
	{
		super(new ContainerCookingPot(inv, tile));
		this.playerInv = inv;
		this.ySize = 193;
		this.tile = tile;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		String s = I18n.format(tile.getName());
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f,
			int x, int y) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.renderEngine.bindTexture(new ResourceLocation("betterwithmods", "textures/gui/cauldron.png"));
		int xPos = (this.width - this.xSize) / 2;
		int yPos = (this.height - this.ySize) / 2;
		drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
		
		if(this.tile.isCooking())
		{
			int iconHeight = this.tile.getFireIntensity() > 5 ? this.stokedHeight : this.fireHeight;
			int scaledIconHeight = this.tile.getCookingProgressScaled(12);
			
			drawTexturedModalRect(xPos + 81, yPos + 19 + 12 - scaledIconHeight, 176, iconHeight - scaledIconHeight, 14, scaledIconHeight + 2);
		}
	}
}
