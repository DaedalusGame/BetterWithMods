package betterwithmods.client.render;

import betterwithmods.BWMBlocks;
import betterwithmods.entity.EntityExtendingRope;
import betterwithmods.util.AABBArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author mrebhan
 */

public class RenderExtendingRope extends Render<EntityExtendingRope> {

	public RenderExtendingRope(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityExtendingRope entity) {
		return null;
	}

	@Override
	public void doRender(EntityExtendingRope entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		World world = entity.getEntityWorld();
		IBlockState iblockstate = BWMBlocks.rope.getDefaultState();
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();

		vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
		BlockPos blockpos = new BlockPos(0, entity.getEntityBoundingBox().maxY, 0);
		GlStateManager.translate(x - 0.5, (float) (y - (double) blockpos.getY()), z - 0.5);
		GlStateManager.translate(-0.005, 0, -0.005);
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

		int i = 0;
		while (entity.getPulleyPosition().getY() - entity.posY > i && i < 2) {
			blockrendererdispatcher.getBlockModelRenderer().renderModel(world,
					blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos.up(i), vertexbuffer,
					false, 0);
			i++;
		}

		entity.getBlocks().forEach((vec, state) -> blockrendererdispatcher.getBlockModelRenderer().renderModel(world,
				blockrendererdispatcher.getModelForState(state), state, blockpos.add(vec), vertexbuffer, false, 0));

		tessellator.draw();

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

		if (renderManager.isDebugBoundingBox()) {
			renderDebugBoundingBox(entity, x, y, z, entityYaw, partialTicks);
		}

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	private void renderDebugBoundingBox(Entity entityIn, double x, double y, double z, float entityYaw,
			float partialTicks) {
		GlStateManager.depthMask(false);
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
		for (AxisAlignedBB aabb : AABBArray.getParts(axisalignedbb)) {
			RenderGlobal.func_189694_a(aabb.minX - entityIn.posX + x, aabb.minY - entityIn.posY + y,
					aabb.minZ - entityIn.posZ + z, aabb.maxX - entityIn.posX + x, aabb.maxY - entityIn.posY + y,
					aabb.maxZ - entityIn.posZ + z, 0.5F, 0.5F, 1.0F, 1.0F);
		}

		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
	}

}
