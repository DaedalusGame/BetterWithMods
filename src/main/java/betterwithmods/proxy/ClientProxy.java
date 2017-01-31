package betterwithmods.proxy;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMItems;
import betterwithmods.BWMod;
import betterwithmods.blocks.tile.TileEntityCauldron;
import betterwithmods.blocks.tile.TileEntityCrucible;
import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import betterwithmods.blocks.tile.TileEntityTurntable;
import betterwithmods.blocks.tile.gen.TileEntityWaterwheel;
import betterwithmods.blocks.tile.gen.TileEntityWindmillHorizontal;
import betterwithmods.blocks.tile.gen.TileEntityWindmillVertical;
import betterwithmods.client.BWStateMapper;
import betterwithmods.client.ColorHandlers;
import betterwithmods.client.model.*;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.client.render.*;
import betterwithmods.entity.*;
import betterwithmods.integration.ICompatModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@SuppressWarnings("unused")
public class ClientProxy implements IProxy {

    @Override
    public void preInit() {
        registerRenderInformation();
        initRenderers();
        BWMod.getLoadedModules().forEach(ICompatModule::preInitClient);
    }

    @Override
    public void init() {
        registerColors();
        BWMod.getLoadedModules().forEach(ICompatModule::initClient);
    }

    @Override
    public void postInit() {
        BWMod.getLoadedModules().forEach(ICompatModule::postInitClient);
    }

    private void registerRenderInformation() {
        BWMBlocks.linkBlockModels();
        BWMItems.linkItemModels();
        RenderUtils.registerFilters();
        ModelLoader.setCustomStateMapper(BWMBlocks.STOKED_FLAME, new BWStateMapper(BWMBlocks.STOKED_FLAME.getRegistryName().toString()));
        ModelLoader.setCustomStateMapper(BWMBlocks.WINDMILL_BLOCK, new BWStateMapper(BWMBlocks.WINDMILL_BLOCK.getRegistryName().toString()));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmillHorizontal.class, new TESRWindmill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmillVertical.class, new TESRVerticalWindmill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWaterwheel.class, new TESRWaterwheel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFilteredHopper.class, new TESRFilteredHopper());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurntable.class, new TESRTurntable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, new TESRCrucible());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCauldron.class, new TESRCauldron());

    }

    private void registerColors() {
        final BlockColors col = Minecraft.getMinecraft().getBlockColors();
        col.registerBlockColorHandler(ColorHandlers.BlockPlanterColor, BWMBlocks.PLANTER);
        col.registerBlockColorHandler(ColorHandlers.BlockFoliageColor, BWMBlocks.VINE_TRAP);
        final ItemColors itCol = Minecraft.getMinecraft().getItemColors();
        itCol.registerItemColorHandler(ColorHandlers.ItemPlanterColor, BWMBlocks.PLANTER);
        itCol.registerItemColorHandler(ColorHandlers.ItemFoliageColor, BWMBlocks.VINE_TRAP);
        col.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D), BWMBlocks.DIRT_SLAB);
        itCol.registerItemColorHandler((stack, tintIndex) -> {
            IBlockState iblockstate = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
            return col.colorMultiplier(iblockstate, null, null, tintIndex);
        }, BWMBlocks.DIRT_SLAB);
    }

    private void initRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, manager -> new RenderSnowball<>(manager, BWMItems.DYNAMITE, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityMiningCharge.class, RenderMiningCharge::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExtendingRope.class, RenderExtendingRope::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityShearedCreeper.class, RenderShearedCreeper::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCow.class, RenderCowHarness::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPig.class, RenderPigHarness::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySheep.class, RenderSheepHarness::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBroadheadArrow.class, RenderBroadheadArrow::new);
    }
}
