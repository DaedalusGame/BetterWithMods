package betterwithmods.integration;

import betterwithmods.BWMod;
import betterwithmods.client.model.filters.ModelTransparent;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.util.NetherSpawnWhitelist;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Quark extends ModIntegration {

    @Override
    public void init() {
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("quark", "basalt")), 0);
        if (BWMod.proxy.isClientside()) {
            registerClientRendering();
        }
    }

    @SideOnly(Side.CLIENT)
    private void registerClientRendering() {
        String[] trapdoors = {"spruce", "birch", "jungle", "acacia", "dark_oak"};
        for (int i = 0; i < 5; i++)
            RenderUtils.addFilter(new ItemStack(Block.REGISTRY.getObject(new ResourceLocation("quark", trapdoors[i] + "_trapdoor"))), new ModelTransparent(new ResourceLocation("quark", "textures/blocks/trapdoor_" + trapdoors[i] + ".png")));
    }
}
