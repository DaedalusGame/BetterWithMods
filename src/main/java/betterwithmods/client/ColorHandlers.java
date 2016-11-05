package betterwithmods.client;

import betterwithmods.blocks.BlockPlanter;
import betterwithmods.blocks.ItemBlockPlanter;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;

/**
 * Created by Christian on 21.10.2016.
 */
public class ColorHandlers {
    public static final IBlockColor BlockPlanterColor = (state, worldIn, pos, tintIndex) ->
            state.getBlock() instanceof BlockPlanter ? ((BlockPlanter) state.getBlock()).colorMultiplier(state, worldIn, pos, tintIndex) : -1;
    public static final IBlockColor BlockFoliageColor = (state, worldIn, pos, tintIndex) ->
            worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColor(0.5D, 1.0D);

    public static final IItemColor ItemPlanterColor = (stack, tintIndex) ->
            (stack.getItem() instanceof ItemBlock && stack.getItem() instanceof ItemBlockPlanter) ? ((ItemBlockPlanter) stack.getItem()).getColorFromItemStack(stack, tintIndex) : -1;
    public static final IItemColor ItemFoliageColor = (stack, tintIndex) ->
            BlockFoliageColor.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex);

}
