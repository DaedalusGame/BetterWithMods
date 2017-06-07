package betterwithmods.client;

import betterwithmods.api.block.IAdvancedRotationPlacement;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by primetoxinz on 6/7/17.
 */
public class ClientEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        EntityPlayer player = event.getPlayer();
        ItemStack stack = ItemStack.EMPTY;
        if ((player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() || !(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock)) && !player.getHeldItem(EnumHand.OFF_HAND).isEmpty()) {
            stack = player.getHeldItem(EnumHand.OFF_HAND);
        } else if (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            stack = player.getHeldItem(EnumHand.MAIN_HAND);
        }
        if (event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK && !stack.isEmpty() && Block.getBlockFromItem(stack.getItem()) instanceof IAdvancedRotationPlacement) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            BlockPos pos = event.getTarget().getBlockPos();
            IBlockState iblockstate = player.getEntityWorld().getBlockState(pos);
            AxisAlignedBB AABB = iblockstate.getBoundingBox(player.getEntityWorld(), pos);
            boolean isAABBCenteredXZ = (((AABB.minX + ((AABB.maxX - AABB.minX) / 2D)) == 0.5D) || ((AABB.maxX - ((AABB.maxX - AABB.minX) / 2D)) == 0.5D)) && (((AABB.minZ + ((AABB.maxZ - AABB.minZ) / 2D)) == 0.5D) || ((AABB.maxZ - ((AABB.maxZ - AABB.minZ) / 2D)) == 0.5D));

            if (iblockstate.getMaterial() != Material.AIR && player.getEntityWorld().getWorldBorder().contains(pos) && isAABBCenteredXZ) {
                double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.getPartialTicks();
                double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.getPartialTicks();
                double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.getPartialTicks();
                Tessellator tessellator = Tessellator.getInstance();
                VertexBuffer vertexbuffer = tessellator.getBuffer();

                vertexbuffer.setTranslation(-d0, -d1, -d2);
                AABB = AABB.expandXyz(0.0020000000949949026D);
                drawRotablePlacementBlockHighlight(tessellator, vertexbuffer, AABB, event.getTarget().sideHit, pos);
                vertexbuffer.setTranslation(0D, 0D, 0D);
            }

            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    @SideOnly(Side.CLIENT)
    private void drawRotablePlacementBlockHighlight(Tessellator tessellator, VertexBuffer buffer, AxisAlignedBB AABB, EnumFacing side, BlockPos pos) {
        double min = 0, max = 0, u = 0, v = 0, w = 0;
        setAxis(side.getAxis());
        switch (axis) {
            case Y:
                min = Math.max(AABB.minX, AABB.minZ);
                max = Math.min(AABB.maxX, AABB.maxZ);
                w = ((side == EnumFacing.UP) ? AABB.maxY : AABB.minY) + pos.getY();
                u = pos.getX();
                v = pos.getZ();
                break;
            case X:
                min = Math.max(AABB.minY, AABB.minZ);
                max = Math.max(AABB.maxY, AABB.maxZ);
                w = ((side == EnumFacing.EAST) ? AABB.maxX : AABB.minX) + pos.getX();
                u = pos.getY();
                v = pos.getZ();
                break;
            case Z:
                min = Math.max(AABB.minY, AABB.minX);
                max = Math.max(AABB.maxY, AABB.maxX);
                w = ((side == EnumFacing.SOUTH) ? AABB.maxZ : AABB.minZ) + pos.getZ();
                u = pos.getX();
                v = pos.getY();
                break;
        }
        drawX(tessellator, buffer, u, v, w, min, max);

    }

    public void drawX(Tessellator tessellator, VertexBuffer buffer, double u, double v, double w, double min, double max) {
        double m = 0.25d, n = 0.75d;
        drawQuad(tessellator, buffer, vec(u + m, w, v + m), vec(u + n, w, v + n));
        drawLine(tessellator, buffer, vec(min + u, w, min + v), vec(u + m, w, v + m));
        drawLine(tessellator, buffer, vec(u + n, w, v + n), vec(u + max, w, v + max));
        drawLine(tessellator, buffer, vec(max + u, w, min + v), vec(u + n, w, v + m));
        drawLine(tessellator, buffer, vec(min + u, w, max + v), vec(u + m, w, v + n));
    }

    public void drawLine(Tessellator tessellator, VertexBuffer buffer, AxisVector pos1, AxisVector pos2) {
        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(pos1.getX(), pos1.getY(), pos1.getZ()).color(0F, 0F, 0f, 0.2F).endVertex();
        buffer.pos(pos2.getX(), pos2.getY(), pos2.getZ()).color(0F, 0F, 0f, 0.2F).endVertex();
        tessellator.draw();
    }

    public void drawQuad(Tessellator tessellator, VertexBuffer buffer, AxisVector pos1, AxisVector pos2) {
        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(pos1.getX(), pos1.getY(), pos1.getZ()).color(0F, 0F, 0f, 0.4F).endVertex();
        buffer.pos(pos1.getX(), pos1.getY(), pos2.getZ()).color(0F, 0F, 0f, 0.4F).endVertex();
        buffer.pos(pos2.getX(), pos2.getY(), pos2.getZ()).color(0F, 0F, 0f, 0.4F).endVertex();
        buffer.pos(pos2.getX(), pos1.getY(), pos1.getZ()).color(0F, 0F, 0f, 0.4F).endVertex();
        buffer.pos(pos1.getX(), pos1.getY(), pos1.getZ()).color(0F, 0F, 0f, 0.4F).endVertex();
        tessellator.draw();
    }

    public AxisVector vec(double x, double y, double z) {
        return new AxisVector(x, y, z);
    }

    private static EnumFacing.Axis axis;

    public static void setAxis(EnumFacing.Axis axis) {
        ClientEventHandler.axis = axis;
    }

    public class AxisVector extends Vector3d {


        public AxisVector(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            if (axis == EnumFacing.Axis.X)
                return this.y;
            return this.x;
        }

        public double getY() {
            switch (axis) {
                case X:
                    return this.x;
                case Z:
                    return this.z;
                default:
                    return this.y;
            }
        }

        public double getZ() {
            if (axis == EnumFacing.Axis.Z)
                return this.y;
            return this.z;
        }

    }
}
