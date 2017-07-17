package betterwithmods.module.hardcore.hcbeacons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by primetoxinz on 7/17/17.
 */
public interface IBeaconEffect {

    int[] radii = new int[]{20, 40, 80, 160};

    void effect(World world, BlockPos pos, int level);

    static void forEachPlayersAround(World world, BlockPos pos, int level, Consumer<? super EntityPlayer> player) {
        int r = radii[Math.min(level-1,3)];
        AxisAlignedBB box = new AxisAlignedBB(pos, pos.add(1, 1, 1)).grow(r);
        List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, box);
        players.forEach(player);
    }

    static void forEachPlayersSpecial(World world, BlockPos pos, int level, Consumer<? super EntityPlayer> player) {

    }
}
