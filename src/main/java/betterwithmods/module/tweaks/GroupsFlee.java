package betterwithmods.module.tweaks;

import betterwithmods.common.entity.ai.EntityAIFlee;
import betterwithmods.module.Feature;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class GroupsFlee extends Feature {
    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent evt) {
        if (evt.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) evt.getEntity();
            if (entity instanceof EntityAnimal && !(entity instanceof EntityTameable)) {
                float speed = 1.25F;
                if (entity instanceof EntityCow)
                    speed = 2.0F;
                else if (entity instanceof EntityChicken)
                    speed = 1.4F;
                ((EntityAnimal) entity).tasks.addTask(0, new EntityAIFlee((EntityCreature) entity, speed));
            }
        }
    }

    @Override
    public String getFeatureDescription() {
        return "When attacking an animal, all surrounding animals will flee";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
