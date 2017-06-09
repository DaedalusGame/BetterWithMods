package betterwithmods.common.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by tyler on 4/22/17.
 */
public class EntityAIEatFood extends EntityAIBase {
    private EntityCreature entity;
    private Predicate<ItemStack> validItem;
    private EntityItem targetItem;
    private int timeoutCounter;

    public EntityAIEatFood(EntityCreature creature, Predicate<ItemStack> validItem) {
        this.entity = creature;
        this.validItem = validItem;
    }

    public EntityItem getTargetItem(List<EntityItem> items) {
        if (items.isEmpty())
            return null;
        EntityItem target = null;
        for (EntityItem item : items) {
            if(validItem.test(item.getEntityItem())) {
                target = item;
                break;
            }
        }
        return target;
    }

    @Override
    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(targetItem.posX, targetItem.posY, targetItem.posZ, 1.0F);
    }

    @Override
    public boolean shouldExecute() {
        BlockPos entityPos = entity.getPosition();
        if (targetItem == null) {
            List<EntityItem> entityItems = entity.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(entityPos, entityPos.add(1, 1, 1)).expand(5, 5, 5));
            targetItem = getTargetItem(entityItems);
        }
        if (targetItem != null) {
            BlockPos targetPos = targetItem.getPosition();
            if (entityPos.getDistance(targetPos.getX(), targetPos.getY(), targetPos.getZ()) <= 2D && targetItem.getEntityItem().getCount() > 0) {
                processItemEating();
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (targetItem.isDead || targetItem.getEntityItem().getCount() < 1) {
            BlockPos entityPos = entity.getPosition();
            List<EntityItem> entityItems = entity.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(entityPos, entityPos.add(1, 1, 1)).expand(5, 5, 5));
            targetItem = getTargetItem(entityItems);
        }
        if (targetItem == null || targetItem.isDead)
            return false;
        if (timeoutCounter > 1200)
            return false;
        if (!this.entity.getNavigator().noPath()) {
            double sqDistToPos = this.entity.getDistanceSq(targetItem.posX, targetItem.posY, targetItem.posZ);
            if (sqDistToPos > 2.0D)
                return true;
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
        ItemStack foodStack = targetItem.getEntityItem().splitStack(1);
        entity.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1.0F, (entity.world.rand.nextFloat() - entity.world.rand.nextFloat()) * 0.2F + 1.0F);
        foodStack.shrink(1);
    }
}
