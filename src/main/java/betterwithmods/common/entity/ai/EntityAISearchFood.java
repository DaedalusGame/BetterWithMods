package betterwithmods.common.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class EntityAISearchFood extends EntityAIBase {
    private final EntityAnimal entity;
    private EntityItem targetItem;
    private int timeoutCounter;

    public EntityAISearchFood(EntityAnimal creature) {
        this.entity = creature;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (entity.getGrowingAge() < 1 && !entity.isInLove()) {
            if (entity instanceof EntityWolf) {
                if (!((EntityWolf) entity).isTamed())
                    return false;
            }
            BlockPos entityPos = entity.getPosition();
            if (targetItem == null) {
                List<EntityItem> entityItems = entity.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(entityPos, entityPos.add(1, 1, 1)).expand(5, 5, 5));
                if (!entityItems.isEmpty()) {
                    for (EntityItem item : entityItems) {
                        if (entity.isBreedingItem(item.getEntityItem())) {
                            targetItem = item;
                            break;
                        }
                    }
                }
            }
            if (targetItem != null) {
                BlockPos targetPos = targetItem.getPosition();
                if (entityPos.getDistance(targetPos.getX(), targetPos.getY(), targetPos.getZ()) <= 2D && targetItem.getEntityItem().getCount() > 0) {
                    processItemEating();
                    return false;
                } else {
                    //targetX = targetItem.posX; targetY = targetItem.posY; targetZ = targetItem.posZ;
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
        if (entity.getGrowingAge() < 1 && !entity.isInLove()) {
            if (entity instanceof EntityWolf) {
                if (((EntityWolf) entity).isSitting())
                    return;
            }
            this.entity.getNavigator().tryMoveToXYZ(targetItem.posX, targetItem.posY, targetItem.posZ, 1.0F);
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        if (targetItem.isDead || targetItem.getEntityItem().getCount() < 1) {
            BlockPos entityPos = entity.getPosition();
            List<EntityItem> entityItems = entity.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(entityPos, entityPos.add(1, 1, 1)).expand(5, 5, 5));
            if (!entityItems.isEmpty()) {
                for (EntityItem item : entityItems) {
                    if (entity.isBreedingItem(item.getEntityItem())) {
                        targetItem = item;
                        break;
                    }
                }
            }
        }
        if (targetItem == null || targetItem.isDead)
            return false;
        if (entity.getGrowingAge() < 1 && !entity.isInLove()) {
            if (entity instanceof EntityWolf) {
                if (((EntityWolf) entity).isSitting())
                    return false;
            }
            if (timeoutCounter > 1200)
                return false;
            if (!this.entity.getNavigator().noPath()) {
                double sqDistToPos = this.entity.getDistanceSq(targetItem.posX, targetItem.posY, targetItem.posZ);
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
        if (entity.getDistanceSq(targetItem.posX, targetItem.posY, targetItem.posZ) <= 2.0D && targetItem.getEntityItem().getCount() > 0) {
            processItemEating();
        } else {
            ++timeoutCounter;
            if (timeoutCounter % 40 == 0) {
                this.entity.getNavigator().tryMoveToXYZ(targetItem.posX, targetItem.posY, targetItem.posZ, 1.0F);
            }
        }
    }

    private void processItemEating() {
        if (!entity.getEntityWorld().isRemote) {
            ItemStack foodStack = targetItem.getEntityItem().splitStack(1);
            boolean bred = false;
            if (entity.isBreedingItem(foodStack)) {
                if (entity.getGrowingAge() == 0 && !entity.isInLove()) {
                    bred = true;
                    entity.setInLove(null);
                } else if (entity.isChild()) {
                    bred = true;
                    entity.ageUp((int) ((float) (-entity.getGrowingAge() / 20) * 0.1F), true);
                }
            }
            if (!bred) {
                targetItem.getEntityItem().grow(1);
            } else if (targetItem.getEntityItem().getCount() < 1) {
                targetItem.setDead();
            }
        }
    }
}
