package betterwithmods.common.entity.ai;

import betterwithmods.module.hardcore.HCChickens;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class AIFoodEggLayer extends EntityAIBase {
    private final EntityAnimal entity;
    private final HCChickens.IEggLayer layer;
    private EntityItem targetItem;
    private int timeoutCounter;

    public AIFoodEggLayer(EntityAnimal creature) {
        this.entity = creature;
        this.layer = creature.getCapability(HCChickens.EGG_LAYER_CAP, EnumFacing.DOWN);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (entity.getGrowingAge() < 1 && !layer.isFeed()) {
            BlockPos entityPos = entity.getPosition();
            if (targetItem == null) {
                List<EntityItem> entityItems = entity.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(entityPos, entityPos.add(1, 1, 1)).expand(5, 5, 5));
                if (!entityItems.isEmpty()) {
                    for (EntityItem item : entityItems) {
                        if (layer.isBreedingItem(item.getItem())) {
                            targetItem = item;
                            break;
                        }
                    }
                }
            }
            if (targetItem != null) {
                BlockPos targetPos = targetItem.getPosition();
                if (entityPos.getDistance(targetPos.getX(), targetPos.getY(), targetPos.getZ()) <= 2D && targetItem.getItem().getCount() > 0) {
                    processItemEating();
                    return false;
                } else {
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
    public boolean shouldContinueExecuting() {
        if (targetItem.isDead || targetItem.getItem().getCount() < 1) {
            BlockPos entityPos = entity.getPosition();
            List<EntityItem> entityItems = entity.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(entityPos, entityPos.add(1, 1, 1)).expand(5, 5, 5));
            if (!entityItems.isEmpty()) {
                for (EntityItem item : entityItems) {
                    if (layer.isBreedingItem(item.getItem())) {
                        targetItem = item;
                        break;
                    }
                }
            }
        }
        if (targetItem == null || targetItem.isDead)
            return false;
        if (entity.getGrowingAge() < 1 && !layer.isFeed()) {
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
        if (entity.getDistanceSq(targetItem.posX, targetItem.posY, targetItem.posZ) <= 2.0D && targetItem.getItem().getCount() > 0) {
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
            ItemStack foodStack = targetItem.getItem().splitStack(1);
            boolean bred = false;
            if (layer.isBreedingItem(foodStack)) {
                if (entity.getGrowingAge() == 0 && !layer.isFeed()) {
                    bred = true;
                    layer.feed(entity, foodStack);
                }
            }
            if (!bred) {
                targetItem.getItem().grow(1);
            } else if (targetItem.getItem().getCount() < 1) {
                targetItem.setDead();
            }
        }
    }
}
