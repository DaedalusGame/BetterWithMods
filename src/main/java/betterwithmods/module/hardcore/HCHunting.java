package betterwithmods.module.hardcore;

import betterwithmods.common.entity.ai.EntityAIEatFood;
import betterwithmods.common.entity.ai.ShooterSpiderWeb;
import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.function.Predicate;

/**
 * Created by tyler on 4/22/17.
 */
public class HCHunting extends Feature {

    private static final Predicate<ItemStack> isMeat = stack -> InvUtils.isOre(stack, "listAllmeat");

    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent evt) {
        if (evt.getEntity() instanceof EntityCreature) {
            EntityCreature entity = (EntityCreature) evt.getEntity();
            if (entity instanceof EntityZombie) {
                ((EntityZombie) entity).tasks.addTask(0, new EntityAIEatFood(entity, isMeat));
                ((EntityZombie) entity).targetTasks.addTask(3, new EntityAINearestAttackableTarget(entity, EntityCow.class, true));
                ((EntityZombie) entity).targetTasks.addTask(3, new EntityAINearestAttackableTarget(entity, EntitySheep.class, true));
                ((EntityZombie) entity).targetTasks.addTask(3, new EntityAINearestAttackableTarget(entity, EntityPig.class, true));
            }
            if (entity instanceof EntitySpider) {
                ((EntitySpider) entity).targetTasks.addTask(0, new EntityAINearestAttackableTarget(entity, EntityChicken.class, true));
                ((EntitySpider) entity).tasks.addTask(0, new EntityAIEatFood(entity, itemStack -> itemStack.getItem() == Items.CHICKEN || itemStack.getItem() == Items.COOKED_CHICKEN));
                ((EntitySpider) entity).tasks.addTask(3, new ShooterSpiderWeb((EntitySpider) entity, 200, 15.0F));
            }
        }
    }

    @Override
    public String getFeatureDescription() {
        return "Makes it so Mobs hunt other animals too. Zombies attack herd animals, Spiders eat Chickens, Wolves will eat anything";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
