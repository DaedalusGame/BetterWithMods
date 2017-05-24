package betterwithmods.client.model;

import betterwithmods.common.blocks.tile.TileStake;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 * Created by tyler on 5/22/17.
 */
public class TESRStake extends TileEntitySpecialRenderer<TileStake> {
    @Override
    public void renderTileEntityAt(TileStake stake, double x, double y, double z, float partialTicks, int destroyStage) {
        if (stake != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            render(TileEntityRendererDispatcher.instance.world, Minecraft.getMinecraft().getTextureManager(), new BlockPos(1,1,1), stake.getPos());
//            if (stake.connections != null) {
//                for (int i = 0; i < stake.connections.length; i++) {
//                    BlockPos connection = stake.connections[i];
//                    if (connection != null) {
//                        GL11.glPushMatrix();
//
//                        GL11.glPopMatrix();
//                    }
//                }
//            }
            GlStateManager.popMatrix();
        }
    }


    public static void render(World world, TextureManager manager, BlockPos start, BlockPos end) {

        // Render a constant width line to stop "aliasing" with lasers that are very far away
        // Deprecated GL but its only a single line per laser so it shouldn't be too bad
        GlStateManager.glLineWidth(10);
        GlStateManager.glBegin(GL11.GL_LINES);
        // The texture point at (1, 1) is always the light (not black) colour that we want.
        GlStateManager.glTexCoord2f(0.9999f, 0.9999f);
        GlStateManager.glVertex3f(start.getX(), start.getY(), start.getZ());
        GlStateManager.glVertex3f(start.getX(), start.getY() + 10, start.getZ());
//        GL11.glVertex3d(end.getX(),end.getY(),end.getZ());
        GlStateManager.glEnd();
//        GL11.glTranslated(start.getX(), start.getY(), start.getZ());

    }
}
