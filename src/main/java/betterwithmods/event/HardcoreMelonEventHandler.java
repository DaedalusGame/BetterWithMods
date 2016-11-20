package betterwithmods.event;

import betterwithmods.config.BWConfig;
import betterwithmods.entity.EntityFallingGourd;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HardcoreMelonEventHandler {
    @SubscribeEvent
    public void onHarvest(BlockEvent.HarvestDropsEvent event) {
        if (!BWConfig.hardcoreMelons) return;
        if (event.getState().getBlock().equals(Blocks.MELON_BLOCK)) {
            event.getDrops().removeIf(x -> x.getItem().equals(Items.MELON));
            event.getDrops().add(new ItemStack(Blocks.MELON_BLOCK, 1));
        }
    }

    @SubscribeEvent
    public void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
        if (!BWConfig.hardcoreMelons) return;
        World world = event.getWorld();
        makeGourdFall(world, event.getPos());
        makeGourdFall(world, event.getPos().up());
    }

    private void makeGourdFall(World world, BlockPos pos) {
        IBlockState blockstate = world.getBlockState(pos);
        Block block = blockstate.getBlock();
        if (block instanceof BlockMelon || block instanceof BlockPumpkin) {
            BlockPos bottompos = pos.down();
            IBlockState bottomstate = world.getBlockState(bottompos);
            if (world.isAirBlock(bottompos) || BlockFalling.canFallThrough(bottomstate)) {
                if (!world.isRemote && world.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
                    EntityFallingGourd entitygourd = new EntityFallingGourd(world, (double) pos.getX() + 0.5, pos.getY(), (double) pos.getZ() + 0.5, blockstate);
                    if (block instanceof BlockPumpkin) entitygourd.setSeedStack(new ItemStack(Items.PUMPKIN_SEEDS));
                    else entitygourd.setSeedStack(new ItemStack(Items.MELON_SEEDS));
                    world.spawnEntityInWorld(entitygourd);
                }
            }
        }
    }
}
