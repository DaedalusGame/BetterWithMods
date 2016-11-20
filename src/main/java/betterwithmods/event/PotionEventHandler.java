package betterwithmods.event;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMod;
import betterwithmods.BWRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionEventHandler {

    public static boolean canMobsSpawnHere(World world, BlockPos pos) {
        if (!world.isSideSolid(pos.down(), EnumFacing.UP)) {
            return false;
        } else if (!world.isBlockNormalCube(pos, false) && !world.isBlockNormalCube(pos.up(), false)
                && !world.getBlockState(pos).getMaterial().isLiquid()) {
            IBlockState state = world.getBlockState(pos);
            if (state == Blocks.BEDROCK.getDefaultState()) {
                return false;
            } else if (world.getWorldTime() < 11615 && world.getLightFor(EnumSkyBlock.SKY, pos) >= 15) {
                return false;
            } else {
                int lightLevel = world.getLightFor(EnumSkyBlock.BLOCK, pos);
                return lightLevel < 8 && (world.isAirBlock(pos) || state.getCollisionBoundingBox(world, pos) == null);
            }
        } else {
            return false;
        }

    }

    @SubscribeEvent
    public void onRenderFireOverlay(RenderBlockOverlayEvent e) {
        if (e.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) {
            if (e.getPlayer().getEntityWorld().getBlockState(e.getBlockPos()).getBlock() == BWMBlocks.STOKED_FLAME) {
                renderFireInFirstPerson();
                e.setCanceled(true);
            }
        }
    }

    private void renderFireInFirstPerson() {
        Minecraft mc = Minecraft.getMinecraft();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);

        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            TextureAtlasSprite textureatlassprite = mc.getTextureMapBlocks()
                    .getTextureExtry(BWMod.MODID + ":blocks/stoked_fire_layer_0");
            if (textureatlassprite == null)
                textureatlassprite = mc.getTextureMapBlocks().getMissingSprite();
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float f1 = textureatlassprite.getMinU();
            float f2 = textureatlassprite.getMaxU();
            float f3 = textureatlassprite.getMinV();
            float f4 = textureatlassprite.getMaxV();
            GlStateManager.translate((float) (-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            GlStateManager.rotate((float) (i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            vertexbuffer.pos(-0.5D, -0.5D, -0.5D).tex((double) f2, (double) f4).endVertex();
            vertexbuffer.pos(0.5D, -0.5D, -0.5D).tex((double) f1, (double) f4).endVertex();
            vertexbuffer.pos(0.5D, 0.5D, -0.5D).tex((double) f1, (double) f3).endVertex();
            vertexbuffer.pos(-0.5D, 0.5D, -0.5D).tex((double) f2, (double) f3).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }

    @SubscribeEvent
    public void onPotionUpdate(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntity() instanceof EntityPlayer) {

            EntityPlayer player = (EntityPlayer) e.getEntity();
            World world = player.getEntityWorld();

            if (player.isPotionActive(BWRegistry.POTION_TRUESIGHT)) {

                if (world.isRemote) {
                    Minecraft mc = Minecraft.getMinecraft();
                    int var3 = mc.gameSettings.particleSetting;
                    if (!mc.isGamePaused()
                            && (world.provider.getDimension() == 0 || world.provider.getDimension() == 1)) {

                        int var4 = MathHelper.floor(player.posX);
                        int var5 = MathHelper.floor(player.posY);
                        int var6 = MathHelper.floor(player.posZ);
                        int radius = 10;
                        for (int x = var4 - radius; x <= var4 + radius; ++x) {
                            for (int y = var5 - radius; y <= var5 + radius; ++y) {
                                for (int z = var6 - radius; z <= var6 + radius; ++z) {
                                    if (canMobsSpawnHere(world, new BlockPos(x, y, z))
                                            && (var3 == 0 || world.rand.nextInt(12) <= 2 - var3 << 1)) {

                                        double i = (double) x + world.rand.nextDouble();
                                        double j = (double) y + world.rand.nextDouble() * 0.25D;
                                        double k = (double) z + world.rand.nextDouble();
                                        world.spawnParticle(EnumParticleTypes.SPELL_MOB, i, j, k, 0, 0, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
