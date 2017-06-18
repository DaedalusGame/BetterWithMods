package betterwithmods.module.tweaks;

import betterwithmods.common.BWMItems;
import betterwithmods.common.entity.EntityShearedCreeper;
import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class CreeperShearing extends Feature {
    @SubscribeEvent
    public void shearCreeper(PlayerInteractEvent.EntityInteractSpecific e) {
        if (e.getTarget() instanceof EntityLivingBase) {
            EntityLivingBase creeper = (EntityLivingBase) e.getTarget();
            if (creeper instanceof EntityCreeper) {
                if (e.getSide().isServer() && creeper.isEntityAlive() && !e.getItemStack().isEmpty()) {
                    if (e.getItemStack().getItem() instanceof ItemShears) {
                        InvUtils.ejectStack(e.getWorld(), creeper.posX, creeper.posY, creeper.posZ, new ItemStack(BWMItems.CREEPER_OYSTER));
                        EntityShearedCreeper shearedCreeper = new EntityShearedCreeper(e.getWorld());
                        creeper.attackEntityFrom(new DamageSource(""), 0);
                        copyEntityInfo(creeper, shearedCreeper);
                        e.getWorld().playSound(null, shearedCreeper.posX, shearedCreeper.posY, shearedCreeper.posZ, SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.HOSTILE, 1, 0.3F);
                        e.getWorld().playSound(null, shearedCreeper.posX, shearedCreeper.posY, shearedCreeper.posZ, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.HOSTILE, 1, 1F);
                        creeper.setDead();
                        e.getWorld().spawnEntity(shearedCreeper);
                    }
                }
            }
        }
    }

    private void copyEntityInfo(EntityLivingBase copyFrom, EntityLivingBase copyTo) {
        copyTo.setHealth(copyFrom.getHealth());
        copyTo.setPositionAndRotation(copyFrom.posX, copyFrom.posY, copyFrom.posZ, copyFrom.rotationYaw, copyFrom.rotationPitch);
        copyTo.setRotationYawHead(copyFrom.getRotationYawHead());
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public String getFeatureDescription() {
        return "Shearing a Creeper will removes it's ability to explode, making him very sad";
    }
}
