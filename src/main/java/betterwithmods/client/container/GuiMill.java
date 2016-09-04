package betterwithmods.client.container;

import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import betterwithmods.blocks.tile.TileEntityMill;

public class GuiMill extends GuiContainer
{
	private TileEntityMill mill;
	private IInventory playerInv;
	
	public GuiMill(IInventory inv, TileEntityMill mill)
	{
		super(new ContainerMill(inv, mill));
		this.playerInv = inv;
		this.ySize = 158;
		this.mill = mill;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		String s = I18n.format(mill.getName());
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f,
			int x, int y) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation("betterwithmods", "textures/gui/mill.png"));
		int xPos = (this.width - this.xSize) / 2;
		int yPos = (this.height - this.ySize) / 2;
		drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
		
		if(this.mill.isGrinding())
		{
			int scaledIcon = this.mill.getGrindProgressScaled(12);
			drawTexturedModalRect(xPos + 80, yPos + 18 + 12 - scaledIcon, 176, 12 - scaledIcon, 14, scaledIcon + 2);
		}
	}
}
