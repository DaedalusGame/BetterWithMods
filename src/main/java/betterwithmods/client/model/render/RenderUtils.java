package betterwithmods.client.model.render;

import betterwithmods.BWMod;
import betterwithmods.client.model.filters.*;
import betterwithmods.common.BWMBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class RenderUtils {
    protected static final Minecraft minecraft = Minecraft.getMinecraft();

    private static HashMap<String, ModelWithResource> filterLocations = new HashMap<>();
    private static RenderItem renderItem;

    public static boolean filterContains(ItemStack stack) {
        return !stack.isEmpty() && filterLocations.containsKey(stack.getItem().toString() + stack.getMetadata());
    }

    public static ModelWithResource getModelFromStack(ItemStack stack) {
        if (filterContains(stack))
            return filterLocations.get(stack.getItem().toString() + stack.getMetadata());
        return null;
    }

    public static void addFilter(ItemStack stack, ModelWithResource resource) {
        String stackString = stack.getItem().toString() + stack.getMetadata();
        filterLocations.put(stackString, resource);
    }

    public static void registerFilters() {
        String[] woodTypes = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak"};
        for (int i = 0; i < 6; i++) {
            addFilter(new ItemStack(BWMBlocks.SLATS, 1, i), new ModelSlats(new ResourceLocation(BWMod.MODID, "textures/blocks/wood_side_" + woodTypes[i] + ".png")));
            addFilter(new ItemStack(BWMBlocks.GRATE, 1, i), new ModelGrate(new ResourceLocation(BWMod.MODID, "textures/blocks/wood_side_" + woodTypes[i] + ".png")));
        }
        addFilter(new ItemStack(BWMBlocks.PANE, 1, 2), new ModelOpaque(new ResourceLocation(BWMod.MODID, "textures/blocks/wicker.png")));
        addFilter(new ItemStack(Blocks.SOUL_SAND), new ModelOpaque(new ResourceLocation("minecraft", "textures/blocks/soul_sand.png")));
        addFilter(new ItemStack(Blocks.IRON_BARS), new ModelTransparent(new ResourceLocation("minecraft", "textures/blocks/iron_bars.png")));
        addFilter(new ItemStack(Blocks.LADDER), new ModelTransparent(new ResourceLocation("minecraft", "textures/blocks/ladder.png")));
        addFilter(new ItemStack(Blocks.TRAPDOOR), new ModelTransparent(new ResourceLocation("minecraft", "textures/blocks/trapdoor.png")));
        addFilter(new ItemStack(Blocks.IRON_TRAPDOOR), new ModelTransparent(new ResourceLocation("minecraft", "textures/blocks/iron_trapdoor.png")));
    }

    public static void renderFill(ResourceLocation textureLocation, BlockPos pos, double x, double y, double z, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        Tessellator t = Tessellator.getInstance();
        VertexBuffer renderer = t.getBuffer();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        minecraft.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        int brightness = minecraft.world.getCombinedLight(pos, minecraft.world.getLight(pos));
        preRender(x, y, z);

        TextureAtlasSprite sprite = minecraft.getTextureMapBlocks().getTextureExtry(textureLocation.toString());
        drawTexturedQuad(renderer, sprite, minX, minY, minZ, maxX - minX, maxY - minY, maxZ - minZ, brightness, EnumFacing.UP);
        drawTexturedQuad(renderer, sprite, minX, minY, minZ, maxX - minX, maxY - minY, maxZ - minZ, brightness, EnumFacing.WEST);
        drawTexturedQuad(renderer, sprite, minX, minY, minZ, maxX - minX, maxY - minY, maxZ - minZ, brightness, EnumFacing.EAST);
        drawTexturedQuad(renderer, sprite, minX, minY, minZ, maxX - minX, maxY - minY, maxZ - minZ, brightness, EnumFacing.NORTH);
        drawTexturedQuad(renderer, sprite, minX, minY, minZ, maxX - minX, maxY - minY, maxZ - minZ, brightness, EnumFacing.SOUTH);

        t.draw();
        postRender();
    }

    /*
    Everything from this point onward was shamelessly taken from Tinkers Construct. I'm sorry, but at some point, models are just too limited.
     */
    private static void preRender(double x, double y, double z) {
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        } else
            GL11.glShadeModel(GL11.GL_FLAT);
        GlStateManager.translate(x, y, z);
    }

    private static void postRender() {
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
    }

    private static void drawTexturedQuad(VertexBuffer renderer, TextureAtlasSprite sprite, double x, double y, double z, double w, double h, double d, int brightness, EnumFacing facing) {
        if (sprite == null) {
            sprite = minecraft.getTextureMapBlocks().getMissingSprite();
        }
        int light1 = brightness >> 0x10 & 0xFFFF;
        int light2 = brightness & 0xFFFF;
        int r = 255;
        int g = 255;
        int b = 255;
        int a = 255;
        double minU;
        double maxU;
        double minV;
        double maxV;

        double size = 16F;

        double x2 = x + w;
        double y2 = y + h;
        double z2 = z + d;

        double xt1 = x % 1D;
        double xt2 = xt1 + w;
        while (xt2 > 1D) xt2 -= 1D;
        double yt1 = y % 1D;
        double yt2 = yt1 + h;
        while (yt2 > 1D) yt2 -= 1D;
        double zt1 = z % 1D;
        double zt2 = zt1 + d;
        while (zt2 > 1D) zt2 -= 1D;

        switch (facing) {
            case DOWN:
            case UP:
                minU = sprite.getInterpolatedU(xt1 * size);
                maxU = sprite.getInterpolatedU(xt2 * size);
                minV = sprite.getInterpolatedV(zt1 * size);
                maxV = sprite.getInterpolatedV(zt2 * size);
                break;
            case NORTH:
            case SOUTH:
                minU = sprite.getInterpolatedU(xt2 * size);
                maxU = sprite.getInterpolatedU(xt1 * size);
                minV = sprite.getInterpolatedV(yt1 * size);
                maxV = sprite.getInterpolatedV(yt2 * size);
                break;
            case WEST:
            case EAST:
                minU = sprite.getInterpolatedU(zt2 * size);
                maxU = sprite.getInterpolatedU(zt1 * size);
                minV = sprite.getInterpolatedV(yt1 * size);
                maxV = sprite.getInterpolatedV(yt2 * size);
                break;
            default:
                minU = sprite.getMinU();
                maxU = sprite.getMaxU();
                minV = sprite.getMinV();
                maxV = sprite.getMaxV();
        }

        switch (facing) {
            case DOWN:
                renderer.pos(x, y, z).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y, z).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x, y, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                break;
            case UP:
                renderer.pos(x, y2, z).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x, y2, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y2, z).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case NORTH:
                renderer.pos(x, y, z).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x, y2, z).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y2, z).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y, z).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;
            case SOUTH:
                renderer.pos(x, y, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case WEST:
                renderer.pos(x, y, z).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x, y, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x, y2, z).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case EAST:
                renderer.pos(x2, y, z).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y2, z).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;
        }
    }

    public static TextureAtlasSprite getSprite(ItemStack stack) {
        if (renderItem == null) {
            renderItem = Minecraft.getMinecraft().getRenderItem();
        }

        return renderItem.getItemModelWithOverrides(stack, null, null).getParticleTexture();
    }

    public static ResourceLocation getResourceLocation(ItemStack stack) {
        TextureAtlasSprite sprite = getSprite(stack);
        String iconLoc = sprite.getIconName();
        String domain = iconLoc.substring(0, iconLoc.indexOf(':')), resource = iconLoc.substring(iconLoc.indexOf(':') + 1, iconLoc.length());
        return new ResourceLocation(domain, "textures/" + resource + ".png");
    }
}
