package betterwithmods.client.container;

import betterwithmods.blocks.tile.TileEntityMill;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.opengl.GL11;

public class GuiMill extends GuiContainer
{
	private TileEntityMill mill;
	private IItemHandler playerInv;
	
	public GuiMill(EntityPlayer player, TileEntityMill mill)
	{
		super(new ContainerMill(player, mill));
		this.playerInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null);
		this.ySize = 158;
		this.mill = mill;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		String s = I18n.format(mill.getName());
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
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
