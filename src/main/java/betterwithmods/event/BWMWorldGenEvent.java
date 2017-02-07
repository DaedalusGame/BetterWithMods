package betterwithmods.event;

import betterwithmods.config.BWConfig;
import betterwithmods.util.HardcoreFunctions;
import betterwithmods.world.BWMapGenScatteredFeature;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by blueyu2 on 11/27/16.
 */
public class BWMWorldGenEvent {

    public static boolean isInRadius(World world, BlockPos pos) {
        return isInRadius(world, pos.getX(), pos.getZ());
    }

    public static boolean isInRadius(World world, int x, int z) {
        BlockPos center = world.getSpawnPoint();
        return Math.sqrt(Math.pow(x - center.getX(), 2) + Math.pow(z - center.getZ(), 2)) < RespawnEventHandler.HARDCORE_SPAWN_RADIUS;
    }

    @SubscribeEvent
    public void overrideScatteredFeature(InitMapGenEvent event) {
        if (!BWConfig.hardcoreStructures)
            return;

        if (event.getType().equals(InitMapGenEvent.EventType.SCATTERED_FEATURE))
            event.setNewGen(new BWMapGenScatteredFeature());
    }

    @SubscribeEvent
    public void addStumpToTree(SaplingGrowTreeEvent event) {
        if (!BWConfig.hardcoreStumping)
            return;
        IBlockState state = event.getWorld().getBlockState(event.getPos());
        if (state.getBlock() != Blocks.SAPLING)/*(!(state.getBlock() instanceof BlockSapling))*/ return;
        event.setResult(Event.Result.DENY);
        HardcoreFunctions.generateTreeWithStump(event.getWorld(), event.getPos(), state, event.getRand());
    }
}
