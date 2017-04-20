package betterwithmods.common.blocks.tile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/20/17
 */
public class TileEntityDragonVessel extends TileEntity implements ITickable {
    private final int MAX_EXPERIENCE = 800;
    private int experience;
    private int maxDist = 5;
    public TileEntityDragonVessel() {
    }

    private int addExperience(int xp)  {
        if(this.experience >= MAX_EXPERIENCE)
            return xp;
        if(this.experience < MAX_EXPERIENCE-xp) {
            this.experience += xp;
            return 0;
        } else if( this.experience >= MAX_EXPERIENCE-xp) {
            int newXp = xp - (MAX_EXPERIENCE-experience);
            this.experience += newXp;
            return xp - newXp;
        }
        return xp;
    }
    private void hooverXP(EntityXPOrb entity) {
        if (!world.isRemote && !entity.isDead) {
            int xpValue = entity.getXpValue();
            addExperience(xpValue);
            markDirty();
            if (xpValue > 0) {
                entity.xpValue = xpValue;
                release(entity);
            } else {
                entity.setDead();
            }
        }
    }

    @Override
    public void update() {
        AxisAlignedBB box = world.getBlockState(pos).getBoundingBox(world,pos).expand(5,5,5);
        List<EntityXPOrb> xp = world.getEntitiesWithinAABB(EntityXPOrb.class, box);
        for (EntityXPOrb entity : xp) {
            double xDist = (getPos().getX() + 0.5D - entity.posX);
            double yDist = (getPos().getY() + 0.5D - entity.posY);
            double zDist = (getPos().getZ() + 0.5D - entity.posZ);

            double totalDistance = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);

            if (totalDistance < 1.5) {
                hooverXP(entity);
            } else if (shouldAttract(getPos(), entity)) {
                double d = 1 - (Math.max(0.1, totalDistance) / maxDist);
                double speed = 0.01 + (d * 0.02);

                entity.motionX += xDist / totalDistance * speed;
                entity.motionZ += zDist / totalDistance * speed;
                entity.motionY += yDist / totalDistance * speed;
                if (yDist > 0.5) {
                    entity.motionY = 0.12;
                }

                // force client sync because this movement is server-side only
                boolean silent = entity.isSilent();
                entity.setSilent(!silent);
                entity.setSilent(silent);
            }
        }
    }

    private static final String PREVENT_REMOTE_MOVEMENT = "PreventRemoteMovement";
    public static final String BWM_PULLER_TAG = "BWMpuller";

    public static boolean shouldAttract(@Nullable BlockPos pullerPos, @Nullable Entity entity) {

        if (entity == null || entity.isDead) {
            return false;
        }
        if (entity instanceof IProjectile && entity.motionY > 0.01) {
            return false;
        }

        NBTTagCompound data = entity.getEntityData();

        if (isReservedByOthers(data)) {
            return false;
        }

        if (!isReservedByBWM(data)) {
            // if it is not being pulled already, pull it
            if (pullerPos != null) {
                data.setLong(BWM_PULLER_TAG, pullerPos.toLong());
            }
            return true;
        }

        if (pullerPos == null) {
            // it is already being pulled, so with no further info we are done
            return false;
        }

        long posL = data.getLong(BWM_PULLER_TAG);
        if (posL == pullerPos.toLong()) {
            // item already pulled from pullerPos so done
            return true;
        }

        // it is being pulled by something else, so check to see if we are closer
        BlockPos curOwner = BlockPos.fromLong(posL);
        double distToCur = curOwner.distanceSqToCenter(entity.posX, entity.posY, entity.posZ);
        double distToMe = pullerPos.distanceSqToCenter(entity.posX, entity.posY, entity.posZ);
        if (distToMe + 1 < distToCur) {
            // only take over if it is clearly nearer to us
            data.setLong(BWM_PULLER_TAG, pullerPos.toLong());
            return true;
        }
        return false;
    }

    public static void release(@Nullable Entity entity) {
        if (entity != null && !entity.isDead) {
            NBTTagCompound data = entity.getEntityData();
            data.removeTag(BWM_PULLER_TAG);
        }
    }

    public static boolean isReserved(Entity entity) {
        return isReservedByBWM(entity.getEntityData()) || isReservedByOthers(entity.getEntityData());
    }

    public static boolean isReservedByBWM(NBTTagCompound data) {
        return data.hasKey(BWM_PULLER_TAG);
    }


    public static boolean isReservedByOthers(NBTTagCompound data) {
        return data.hasKey(PREVENT_REMOTE_MOVEMENT);
    }
}
