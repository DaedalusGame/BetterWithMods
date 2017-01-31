package betterwithmods.event;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMItems;
import betterwithmods.config.BWConfig;
import betterwithmods.util.player.EntityPlayerExt;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Koward
 */
public class InferiorDropsEventHandler {
    private static final int PILES_PER_BLOCK = 3;
    private static final int PILES_PER_SLAB = 2;

    @SubscribeEvent
    public void onHarvest(BlockEvent.HarvestDropsEvent event) {
        if(!BWConfig.inferiorDrops)
            return;
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
        if (block == Blocks.DIRT ||
                block == Blocks.FARMLAND ||
                block == BWMBlocks.FERTILE_FARMLAND ||
                block == Blocks.GRASS ||
                block == Blocks.MYCELIUM ||
                block == Blocks.GRASS_PATH
                ) {
            event.getDrops().clear();
            for (int i = 0; i < PILES_PER_BLOCK; ++i) {
                if (event.getWorld().rand.nextFloat() <= event.getDropChance()) {
                    event.getDrops().add(new ItemStack(BWMItems.DIRT_PILE));
                }
            }
        } else if (block == Blocks.GRAVEL) {
            event.getDrops().clear();
            for (int i = 0; i < PILES_PER_BLOCK; ++i) {
                if (event.getWorld().rand.nextFloat() <= event.getDropChance()) {
                    event.getDrops().add(new ItemStack(BWMItems.GRAVEL_PILE));
                }
            }
        } else if (block == Blocks.SAND) {
            event.getDrops().clear();
            for (int i = 0; i < PILES_PER_BLOCK; ++i) {
                if (event.getWorld().rand.nextFloat() <= event.getDropChance()) {
                    event.getDrops().add(new ItemStack(BWMItems.SAND_PILE));
                }
            }
        } else if (block == BWMBlocks.DIRT_SLAB) {
            event.getDrops().clear();
            for (int i = 0; i < PILES_PER_SLAB; ++i) {
                if (event.getWorld().rand.nextFloat() <= event.getDropChance()) {
                    event.getDrops().add(new ItemStack(BWMItems.DIRT_PILE));
                }
            }
        }
    }
}
