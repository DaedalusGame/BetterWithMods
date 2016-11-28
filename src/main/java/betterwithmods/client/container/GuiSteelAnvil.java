package betterwithmods.client.container;

import betterwithmods.BWMod;
import betterwithmods.blocks.tile.TileEntitySteelAnvil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 * Created by blueyu2 on 11/22/16.
 */
public class GuiSteelAnvil extends GuiContainer {
    private final TileEntitySteelAnvil anvil;

    public GuiSteelAnvil(InventoryPlayer playerInventory, World worldIn, BlockPos pos, TileEntitySteelAnvil anvilIn) {
        super(new ContainerSteelAnvil(playerInventory, worldIn, pos, anvilIn));
        this.anvil = anvilIn;
        ySize = 183;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        this.fontRendererObj.drawString(I18n.format(anvil.getName()), 10, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 3, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation(BWMod.MODID, "textures/gui/steel_anvil.png"));

        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
    }
}
