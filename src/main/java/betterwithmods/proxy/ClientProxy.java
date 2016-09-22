package betterwithmods.proxy;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMItems;
import betterwithmods.blocks.BlockPlanter;
import betterwithmods.blocks.ItemBlockPlanter;
import betterwithmods.blocks.tile.gen.TileEntityWaterwheel;
import betterwithmods.blocks.tile.gen.TileEntityWindmillHorizontal;
import betterwithmods.blocks.tile.gen.TileEntityWindmillVertical;
import betterwithmods.client.BWStateMapper;
import betterwithmods.client.model.TESRVerticalWindmill;
import betterwithmods.client.model.TESRWaterwheel;
import betterwithmods.client.model.TESRWindmill;
import betterwithmods.client.render.RenderExtendingRope;
import betterwithmods.client.render.RenderMiningCharge;
import betterwithmods.client.render.RenderShearedCreeper;
import betterwithmods.entity.EntityDynamite;
import betterwithmods.entity.EntityExtendingRope;
import betterwithmods.entity.EntityMiningCharge;
import betterwithmods.entity.EntityShearedCreeper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.ArrayList;

public class ClientProxy extends CommonProxy {

    public static ArrayList<Item> itemModelRegistry = new ArrayList<>();

    @Override
    public Item addItemBlockModel(Item item) {
        if (isClientside()) {
            itemModelRegistry.add(item);
        }
        return item;
    }

    @Override
    public void registerRenderInformation() {
    	BWMBlocks.linkBlockModels();
    	BWMItems.linkItemModels();
        ModelLoader.setCustomStateMapper(BWMBlocks.STOKED_FLAME, new BWStateMapper(BWMBlocks.STOKED_FLAME.getRegistryName().toString()));
        ModelLoader.setCustomStateMapper(BWMBlocks.WINDMILL_BLOCK, new BWStateMapper(BWMBlocks.WINDMILL_BLOCK.getRegistryName().toString()));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmillHorizontal.class, new TESRWindmill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmillVertical.class, new TESRVerticalWindmill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWaterwheel.class, new TESRWaterwheel());

    }

    @Override
    public void registerColors() {
        final BlockColors col = Minecraft.getMinecraft().getBlockColors();
        col.registerBlockColorHandler((state, worldIn, pos, tintIndex) ->
                state.getBlock() instanceof BlockPlanter ? ((BlockPlanter) state.getBlock()).colorMultiplier(state, worldIn, pos, tintIndex) : -1, new Block[]{BWMBlocks.PLANTER});
        final ItemColors itCol = Minecraft.getMinecraft().getItemColors();
        itCol.registerItemColorHandler((stack, tintIndex) ->
                (stack.getItem() instanceof ItemBlock && stack.getItem() instanceof ItemBlockPlanter) ? ((ItemBlockPlanter) stack.getItem()).getColorFromItemstack(stack, tintIndex) : -1, new Block[]{BWMBlocks.PLANTER});
    }

    @Override
    public void initRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, manager -> new RenderSnowball<EntityDynamite>(manager, BWMItems.DYNAMITE, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityMiningCharge.class, RenderMiningCharge::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExtendingRope.class, RenderExtendingRope::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityShearedCreeper.class, RenderShearedCreeper::new);
    }

    @Override
    public boolean isClientside() {
        return true;
    }
}
