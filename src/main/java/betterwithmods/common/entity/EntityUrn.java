package betterwithmods.common.entity;

import betterwithmods.util.WorldUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by primetoxinz on 6/13/17.
 */
public class EntityUrn extends EntitySnowball {

    public EntityUrn(World worldIn) {
        super(worldIn);
    }

    public EntityUrn(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntityUrn(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {

    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.typeOfHit != RayTraceResult.Type.MISS) {
                BlockPos pos = result.typeOfHit == RayTraceResult.Type.ENTITY ? result.entityHit.getPosition() : result.getBlockPos();
                WorldUtils.spawnGhast(world, pos);
            }
            this.world.setEntityState(this, (byte) 3);
            this.setDead();
        }
    }
}
