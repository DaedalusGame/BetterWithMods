package betterwithmods.client.model;

import betterwithmods.blocks.BlockGen;
import betterwithmods.blocks.tile.gen.TileEntityWindmillHorizontal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TESRWindmill extends TileEntitySpecialRenderer<TileEntityWindmillHorizontal>
{
	private ModelWindmillShafts shafts = new ModelWindmillShafts();
	private ModelWindmillSail sail = new ModelWindmillSail();

	@Override
	public void renderTileEntityAt(TileEntityWindmillHorizontal te, double x, double y, double z,
			float partialTicks, int destroyStage) {
		TileEntityWindmillHorizontal windmill = (TileEntityWindmillHorizontal)te;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		
		float rotation = (windmill.getCurrentRotation() + (windmill.getRunningState() == 0 ? 0 : partialTicks * windmill.getPrevRotation()));
		
		EnumFacing dir = EnumFacing.SOUTH;

		if(te.hasWorldObj()) {
			if(te.getBlockType() instanceof BlockGen)
				dir = ((BlockGen)te.getBlockType()).getAxleDirection(te.getWorld(), te.getPos());
		}
		
		if(dir == EnumFacing.EAST)
		{
			shafts.setRotateAngle(shafts.axle, 0, 0, -(float)Math.toRadians(rotation));
			sail.setRotateAngleForSails(0, 0, -(float)Math.toRadians(rotation));
			GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
		}
		else if(dir == EnumFacing.SOUTH)
		{
			shafts.setRotateAngle(shafts.axle, 0, 0, -(float)Math.toRadians(rotation));
			sail.setRotateAngleForSails(0, 0, -(float)Math.toRadians(rotation));
		}
		else
		{
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
		}
		
		this.bindTexture(new ResourceLocation("minecraft", "textures/blocks/planks_oak.png"));
		this.shafts.render(0.0625F);
		this.bindTexture(new ResourceLocation("minecraft", "textures/blocks/wool_colored_white.png"));
		this.sail.render(0.0625F, windmill);
		GlStateManager.popMatrix();
	}

}
