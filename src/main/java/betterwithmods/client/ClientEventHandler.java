package betterwithmods.client;

import betterwithmods.api.block.IAdvancedRotationPlacement;
import betterwithmods.common.registry.HopperFilters;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by primetoxinz on 6/7/17.
 */
public class ClientEventHandler {
    Minecraft mc = Minecraft.getMinecraft();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void putTooltip(ItemTooltipEvent e) {
        int type = HopperFilters.getFilterType(e.getItemStack());
        if(type > 0) {
            e.getToolTip().addAll(HopperFilters.tooltips.get(type));
        }

    }


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
        Block block = Block.getBlockFromItem(stack.getItem());
        if (event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK && block instanceof IAdvancedRotationPlacement) {
            World world = player.getEntityWorld();
            EnumFacing side = event.getTarget().sideHit;

            BlockPos pos = event.getTarget().getBlockPos();
            IBlockState iblockstate = world.getBlockState(pos);

            if (world.getBlockState(pos.offset(side)).getMaterial().isReplaceable() && iblockstate.getMaterial() != Material.AIR && world.getWorldBorder().contains(pos)) {
                double dx = (player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.getPartialTicks());
                double dy = (player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.getPartialTicks());
                double dz = (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.getPartialTicks());
                float x = (float) event.getTarget().hitVec.x - pos.getX();
                float y = (float) event.getTarget().hitVec.y - pos.getY();
                float z = (float) event.getTarget().hitVec.z - pos.getZ();
                BlockPos renderPos = pos.offset(side);

                IBlockState placeState = ((IAdvancedRotationPlacement) block).getRenderState(world,renderPos,side,x,y,z,stack.getMetadata(),player);
                GlStateManager.pushMatrix();

                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

                Tessellator tessellator = Tessellator.getInstance();
                VertexBuffer buffer = tessellator.getBuffer();
                buffer.setTranslation(-dx,-dy,-dz);
                renderBlock(placeState, renderPos, world);
                buffer.setTranslation(0,0,0);

                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    public void renderBlock(IBlockState state, BlockPos pos, World world) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();

        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        buffer.begin(GL11.GL_QUADS,DefaultVertexFormats.BLOCK);
        mc.getBlockRendererDispatcher().renderBlock(state,pos,world,buffer);
        tessellator.draw();
    }

}
