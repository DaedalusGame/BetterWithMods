package betterwithmods.module.hardcore.hcbeacons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by primetoxinz on 7/25/17.
 */
public class WeatherBeaconEffect implements IBeaconEffect {
    @Override
    public void effect(World world, BlockPos pos, int level) {

    }

    @Override
    public boolean processInteractions(World world, BlockPos pos, int level, EntityPlayer player) {
        return false;
    }
}
