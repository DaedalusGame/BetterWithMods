package betterwithmods.client.render;

import betterwithmods.BWRegistry;
import betterwithmods.entity.EntityExtendingRope;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author mrebhan
 */

public class RenderExtendingRope extends Render<EntityExtendingRope> {

	public static final ResourceLocation ROPE_TEXTURE = new ResourceLocation("betterwithmods:textures/blocks/rope");

	public RenderExtendingRope(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityExtendingRope entity) {
		return ROPE_TEXTURE;
	}

	@Override
	public void doRender(EntityExtendingRope entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		World world = entity.getEntityWorld();
		IBlockState iblockstate = BWRegistry.rope.getDefaultState();
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
		GlStateManager.translate(-0.005, 0, -0.005);
		GlStateManager.scale(1.01, 1, 1.01); // prevent z-fighting
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		if (entity.getPulleyPosition().getY() - entity.posY > 0.0) {
			blockrendererdispatcher.getBlockModelRenderer().renderModel(world,
					blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos, vertexbuffer, false,
					0);
		}
		if (entity.getPulleyPosition().getY() - entity.posY > 1) {
			blockrendererdispatcher.getBlockModelRenderer().renderModel(world,
					blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos.up(), vertexbuffer,
					false, 0);
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
