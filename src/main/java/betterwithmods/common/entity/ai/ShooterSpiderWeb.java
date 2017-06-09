package betterwithmods.common.entity.ai;

import betterwithmods.common.entity.EntitySpiderWeb;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;

/**
 * Created by tyler on 4/22/17.
 */
public class ShooterSpiderWeb extends EntityAIBase {
    private EntitySpider spider;
    private EntityLivingBase target;
    private int seeTime;
    private int attackTime, maxAttackTime;
    private float radius, maxRadius;

    public ShooterSpiderWeb(EntitySpider spider, int maxAttackTime, float radius) {
        this.spider = spider;
        this.maxAttackTime = maxAttackTime;
        this.maxRadius = this.radius = radius;
        setMutexBits(3);
        this.attackTime = -1;
    }

    @Override
    public boolean shouldExecute() {

        EntityLivingBase target = spider.getAttackTarget();
        if (target != null) {
            this.target = target;
            double d = spider.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            return !(Math.sqrt(d) < 3 || target.isInWater() || target.isInWeb);
        }
        return false;
    }

    @Override
    public void updateTask() {
        double d = spider.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        boolean canSee = spider.getEntitySenses().canSee(target);
        seeTime = canSee ? seeTime + 1 : 0;
        if (d <= (maxRadius * maxRadius) && seeTime >= 20) {

            spider.getNavigator().clearPathEntity();
        } else {
            spider.getNavigator().tryMoveToEntityLiving(target, 1.0F);
        }
        spider.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        if (attackTime-- == 0) {
            if (!canSee)
                return;
            float f = MathHelper.sqrt(d) / (radius);
//            float limit = MathHelper.clamp(f, 0.1F, 1.0F);
            shootWeb();
            attackTime = MathHelper.floor(f * maxAttackTime);
        } else if (attackTime < 0) {
            float f2 = MathHelper.sqrt(d) / (radius);
            attackTime = MathHelper.floor(f2 * maxAttackTime);
        }
        super.updateTask();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute() || !spider.getNavigator().noPath();
    }

    public void resetTask() {
        target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    private void shootWeb() {
        EntitySpiderWeb web = new EntitySpiderWeb(spider.getEntityWorld(), spider);
        double d0 = target.posX - spider.posX;
        double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - web.posY;
        double d2 = target.posZ - spider.posZ;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        web.setThrowableHeading(d0, d1 + d3 * 0.5, d2, 1.6F, (float) (14 - spider.world.getDifficulty().getDifficultyId() * 4));
        spider.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.0F / (spider.getRNG().nextFloat() * 0.4F + 0.8F));
        spider.world.spawnEntity(web);
    }
}
