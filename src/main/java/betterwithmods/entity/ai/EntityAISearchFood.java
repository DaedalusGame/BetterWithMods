package betterwithmods.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.List;

public class EntityAISearchFood extends EntityAIBase
{
    private final EntityAnimal entity;
    private double targetX;
    private double targetY;
    private double targetZ;
    private EntityItem targetItem;

    public EntityAISearchFood(EntityAnimal creature) {
        this.entity = creature;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if(entity.getGrowingAge() < 1 && !entity.isInLove()) {
            if(entity instanceof EntityWolf) {
                if(!((EntityWolf)entity).isTamed())
                    return false;
            }
            BlockPos entityPos = entity.getPosition();
            if(targetItem == null) {
                List<EntityItem> entityItems = entity.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(entityPos, entityPos.add(1, 1, 1)).expand(5, 5, 5));
                for (EntityItem item : entityItems) {
                    if (entity.isBreedingItem(item.getEntityItem())) {
                        targetItem = item;
                        break;
                    }
                }
            }
            if(targetItem != null) {
                BlockPos targetPos = targetItem.getPosition();
                if(entityPos.getDistance(targetPos.getX(), targetPos.getY(), targetPos.getZ()) <= 2D) {
                    processItemEating();
                    return false;
                }
                else {
                    targetX = targetItem.posX; targetY = targetItem.posY; targetZ = targetItem.posZ;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        if(entity.getGrowingAge() < 1 && !entity.isInLove()) {
            if(entity instanceof EntityWolf) {
                if(((EntityWolf)entity).isSitting())
                    return;
            }
            this.entity.getNavigator().tryMoveToXYZ(this.targetX, this.targetY, this.targetZ, 1.0F);
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        if(entity.getGrowingAge() < 1 && !entity.isInLove()) {
            if(entity instanceof EntityWolf) {
                if(((EntityWolf)entity).isSitting())
                    return false;
            }
            if(!this.entity.getNavigator().noPath()) {
                double sqDistToPos = this.entity.getDistanceSq(this.targetX, this.targetY, this.targetZ);
                if (sqDistToPos > 2.0D)
                    return true;
            }
        }
        return false;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask() {
        if(entity.getDistanceSq(targetX, targetY, targetZ) <= 2.0D) {
            processItemEating();
        }
    }

    private void processItemEating() {
        if(!entity.getEntityWorld().isRemote) {
            FakePlayer player = FakePlayerFactory.getMinecraft((WorldServer) entity.getEntityWorld());
            entity.processInteract(player, EnumHand.MAIN_HAND, targetItem.getEntityItem());
            if (targetItem.getEntityItem().stackSize < 1)
                targetItem.setDead();
        }
    }
}
