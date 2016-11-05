package betterwithmods.integration;

import betterwithmods.client.model.filters.ModelTransparent;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.util.NetherSpawnWhitelist;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
public class Quark implements ICompatModule {

    public static final String MODID = "quark";

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation(MODID, "basalt")), 0);
    }

    @Override
    public void postInit() {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        registerClientRendering();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void postInitClient() {
    }

    @SideOnly(Side.CLIENT)
    private void registerClientRendering() {
        String[] trapdoors = {"spruce", "birch", "jungle", "acacia", "dark_oak"};
        for (int i = 0; i < 5; i++)
            RenderUtils.addFilter(new ItemStack(Block.REGISTRY.getObject(new ResourceLocation(MODID, trapdoors[i] + "_trapdoor"))), new ModelTransparent(new ResourceLocation(MODID, "textures/blocks/trapdoor_" + trapdoors[i] + ".png")));
    }
}
