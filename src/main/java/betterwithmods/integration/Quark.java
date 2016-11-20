package betterwithmods.integration;

import betterwithmods.BWMBlocks;
import betterwithmods.client.model.filters.ModelTransparent;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.util.NetherSpawnWhitelist;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
public class Quark implements ICompatModule {

    public static final String MODID = "quark";
    public String[] wood = {"spruce", "birch", "jungle", "acacia", "dark_oak"};

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation(MODID, "basalt")), 0);

        for (int i = 0; i < 5; i++)
            GameRegistry.addShapedRecipe(new ItemStack(Block.REGISTRY.getObject(new ResourceLocation(MODID, "custom_chest")), 1, i), "SSS", "S S", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, i + 1));

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
        for (int i = 0; i < 5; i++)
            RenderUtils.addFilter(new ItemStack(Block.REGISTRY.getObject(new ResourceLocation(MODID, wood[i] + "_trapdoor"))), new ModelTransparent(new ResourceLocation(MODID, "textures/blocks/trapdoor_" + wood[i] + ".png")));
    }
}
