package betterwithmods.event;

import betterwithmods.config.BWConfig;
import betterwithmods.entity.ai.EntityAIFlee;
import betterwithmods.entity.ai.EntityAISearchFood;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.passive.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.stream.Collectors;

public class MobAIEvent {

    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent evt)
    {
        if(evt.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase)evt.getEntity();
            if (entity instanceof EntityAnimal) {
                ((EntityAnimal) entity).tasks.addTask(3, new EntityAISearchFood((EntityAnimal) entity));
                if(!(entity instanceof EntityTameable)) {
                    float speed = 1.25F;
                    if(entity instanceof EntityCow)
                        speed = 2.0F;
                    else if(entity instanceof EntityChicken)
                        speed = 1.4F;
                    ((EntityAnimal) entity).tasks.addTask(0, new EntityAIFlee((EntityCreature) entity, speed));
                }
            } else if(entity instanceof EntityVillager) {
                if(BWConfig.hardcoreVillagers) {
                    EntityVillager villager = (EntityVillager) entity;
                    villager.tasks.removeTask(new EntityAIVillagerMate(villager));
                    villager.tasks.addTask(0, new betterwithmods.entity.ai.EntityAIVillagerMate(villager, 1));
                    villager.tasks.addTask(0,new EntityAITempt(villager,1d,false, new HashSet<Item>(OreDictionary.getOres("gemDiamond").stream().map(ItemStack::getItem).collect(Collectors.toList()))));
                }
            }
        }
    }

    @SubscribeEvent
    public void mateVillagers(PlayerInteractEvent.EntityInteractSpecific e) {
        //TODO fix trading menu opening when breeding
        if(BWConfig.hardcoreVillagers) {
            if (e.getTarget() instanceof EntityVillager) {
                EntityVillager villager = (EntityVillager) e.getTarget();
                ItemStack stack = e.getItemStack();
                boolean isDiamond = stack == null ? false : OreDictionary.getOres("gemDiamond").stream().anyMatch(gem -> stack.isItemEqual(gem));
                if (isDiamond && !villager.isChild() && !isWillingToMate(villager) && villager.getGrowingAge() == 0) {
                    if(e.getEntityPlayer().capabilities.isCreativeMode)
                        stack.stackSize--;
                    e.getWorld().setEntityState(villager, (byte) 12);
                    ((EntityVillager) e.getTarget()).setIsWillingToMate(true);
                    if(stack.stackSize < 1)
                        e.getEntityPlayer().setHeldItem(e.getHand(), null);
                }
            }
        }
    }

    public static boolean isWillingToMate(EntityVillager villager) {
        if(villager != null) {
            boolean field = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, "isWillingToMate", "field_175565_bs");
            return field;
        }
        return false;
    }
}
