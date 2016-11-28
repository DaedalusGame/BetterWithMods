package betterwithmods.event;

import betterwithmods.entity.ai.EntityAIFlee;
import betterwithmods.entity.ai.EntityAISearchFood;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobAIEvent {
    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent evt) {
        if (evt.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) evt.getEntity();
            if (entity instanceof EntityAnimal) {
                ((EntityAnimal) entity).tasks.addTask(3, new EntityAISearchFood((EntityAnimal) entity));
                if (!(entity instanceof EntityTameable)) {
                    float speed = 1.25F;
                    if (entity instanceof EntityCow)
                        speed = 2.0F;
                    else if (entity instanceof EntityChicken)
                        speed = 1.4F;
                    ((EntityAnimal) entity).tasks.addTask(0, new EntityAIFlee((EntityCreature) entity, speed));
                }
            } /*else if (entity instanceof EntityVillager) {
                if (BWConfig.hardcoreVillagers) {
                    EntityVillager villager = (EntityVillager) entity;
                    villager.tasks.removeTask(new EntityAIVillagerMate(villager));
                    villager.tasks.addTask(0, new betterwithmods.entity.ai.EntityAIVillagerMate(villager, 1));
                    villager.tasks.addTask(0, new EntityAITempt(villager, 1d, false, new HashSet<>(OreDictionary.getOres("gemDiamond").stream().map(ItemStack::getItem).collect(Collectors.toList()))));
                }
            }*/
        }
    }

    @SubscribeEvent
    public void mateVillagers(PlayerInteractEvent.EntityInteractSpecific e) {
        //TODO doesn't work
       /*
        if (BWConfig.hardcoreVillagers) {
            if (e.getTarget() instanceof EntityVillager) {
                EntityVillager villager = (EntityVillager) e.getTarget();
                ItemStack stack = e.getItemStack();
                boolean isDiamond = stack != ItemStack.EMPTY && OreDictionary.getOres("gemDiamond").stream().anyMatch(stack::isItemEqual);
                if (isDiamond && !villager.isChild() && !isWillingToMate(villager) && villager.getGrowingAge() == 0) {
                    if (e.getEntityPlayer().capabilities.isCreativeMode)
                        stack.shrink(1);
                    e.getWorld().setEntityState(villager, (byte) 12);
                    ((EntityVillager) e.getTarget()).setIsWillingToMate(true);
                    if (stack.getCount() < 1)
                        e.getEntityPlayer().setHeldItem(e.getHand(), ItemStack.EMPTY);
                }
            }
        }
        */
    }
}
