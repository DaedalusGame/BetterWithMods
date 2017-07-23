package betterwithmods.common.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.world.World;

/**
 * Created by primetoxinz on 7/23/17.
 */
public class EntityHCFishHook extends EntityFishHook {

    public EntityHCFishHook(World world) {
        super(world, null);
    }

    public EntityHCFishHook(EntityFishHook orig) {
        this(orig.getEntityWorld(),orig.getAngler());
    }

    public EntityHCFishHook(World worldIn, EntityPlayer p_i47290_2_, double x, double y, double z) {
        super(worldIn, p_i47290_2_, x, y, z);
    }

    public EntityHCFishHook(World worldIn, EntityPlayer fishingPlayer) {
        super(worldIn, fishingPlayer);
    }
}
