package betterwithmods.client.render;

import betterwithmods.blocks.tile.TileEntityPulley;
import betterwithmods.entity.EntityMovingPlatform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author mrebhan
 */

public class RenderMovingPlatform extends Render<EntityMovingPlatform> {

	public RenderMovingPlatform(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMovingPlatform entity) {
		return null;
	}

	@Override
	public void doRender(EntityMovingPlatform entity, double x, double y, double z, float entityYaw,
			float partialTicks) {

		World world = entity.getEntityWorld();
		IBlockState iblockstate = entity.getBlockState();
		IBlockState onTop = entity.getOnTop();
		if (iblockstate == null) {
			iblockstate = TileEntityPulley.PLATFORM.getDefaultState();
		}
		if (onTop != null && onTop.getBlock() == Blocks.AIR) {
			onTop = null;
		}
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
		BlockPos blockpos = new BlockPos(0, entity.getEntityBoundingBox().maxY, 0);
		GlStateManager.translate(x - 0.5, (float) (y - (double) blockpos.getY()), z - 0.5);
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		blockrendererdispatcher.getBlockModelRenderer().renderModel(world,
				blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos, vertexbuffer, false, 0);
		if (onTop != null) {
			blockrendererdispatcher.getBlockModelRenderer().renderModel(world,
					blockrendererdispatcher.getModelForState(onTop), onTop, blockpos.up(), vertexbuffer, false, 0);
		}
		tessellator.draw();

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

}
