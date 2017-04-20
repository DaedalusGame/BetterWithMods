package betterwithmods.module.hardcore;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.module.Feature;
import betterwithmods.util.player.EntityPlayerExt;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tyler on 4/20/17.
 */
public class HCPiles extends Feature {

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    public static Map<Block, ItemStack> blockToPile = new HashMap<>();
    public static void registerPile(Block block, ItemStack stack) {
        blockToPile.put(block,stack);
    }
    @Override
    public void init(FMLInitializationEvent event) {
        registerPile(Blocks.DIRT, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(Blocks.FARMLAND, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(BWMBlocks.FERTILE_FARMLAND, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(Blocks.GRASS, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(Blocks.MYCELIUM, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(Blocks.GRASS_PATH, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(Blocks.GRAVEL, new ItemStack(BWMItems.GRAVEL_PILE, 3));
        registerPile(Blocks.SAND, new ItemStack(BWMItems.SAND_PILE, 3));
        registerPile(BWMBlocks.DIRT_SLAB, new ItemStack(BWMItems.DIRT_PILE, 2));
    }

    @SubscribeEvent
    public void onHarvest(BlockEvent.HarvestDropsEvent event) {
        Block block = event.getState().getBlock();
        boolean shouldDropInferior = true;
        EntityPlayer player = event.getHarvester();
        if (player != null) {
            ItemStack stack = event.getHarvester().getHeldItemMainhand();
            shouldDropInferior = !EntityPlayerExt.isCurrentToolEffectiveOnBlock(stack, event.getState());
        }
        if (!shouldDropInferior) {
            return;
        }

        if (blockToPile.containsKey(block)) {
            ItemStack pile = blockToPile.get(block);
            event.getDrops().clear();
            if (event.getWorld().rand.nextFloat() <= event.getDropChance()) {
                event.getDrops().add(pile);
            }
        }

    }

    @Override
    public String getFeatureDescription() {
        return "Makes soils drop 75% of their content if not broken with a shovel to incentivize the use of shovels";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
