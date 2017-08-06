package betterwithmods.module.tweaks;

import betterwithmods.common.BWMItems;
import betterwithmods.common.damagesource.BWDamageSource;
import betterwithmods.module.Feature;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 5/13/17.
 */
public class HeadDrops extends Feature {
    private int sawHeadDropChance, battleAxeHeadDropChance;

    @Override
    public void setupConfig() {
        sawHeadDropChance = loadPropInt("Saw Drop Chance", "Chance for extra drops from Mobs dying on a Saw. 0 disables it entirely", 3);
        battleAxeHeadDropChance = loadPropInt("BattleAxe Drop Chance", "Chance for extra drops from Mobs dying from a BattleAxe. 0 disables it entirely", 3);
    }

    @Override
    public String getFeatureDescription() {
        return "Heads and Skulls can drop from death by Saw or BattleAxe";
    }

    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event) {
        if (isChoppingBlock(event.getSource()))
            addHead(event, sawHeadDropChance);
        if (isBattleAxe(event.getEntityLiving()))
            addHead(event, battleAxeHeadDropChance);
    }

    private boolean isChoppingBlock(DamageSource source) {
        return source.equals(BWDamageSource.getChoppingBlockDamage());
    }

    private boolean isBattleAxe(EntityLivingBase entity) {
        DamageSource source = entity.getLastDamageSource();
        if (source != null && source.getImmediateSource() != null) {
            Entity e = source.getImmediateSource();
            if (e instanceof EntityLivingBase) {
                ItemStack held = ((EntityLivingBase) e).getHeldItemMainhand();
                if (!held.isEmpty() && held.isItemEqual(new ItemStack(BWMItems.STEEL_BATTLEAXE))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addDrop(LivingDropsEvent evt, ItemStack drop) {
        EntityItem item = new EntityItem(evt.getEntityLiving().getEntityWorld(), evt.getEntityLiving().posX, evt.getEntityLiving().posY, evt.getEntityLiving().posZ, drop);
        item.setDefaultPickupDelay();
        evt.getDrops().add(item);
    }

    public void addHead(LivingDropsEvent evt, int chance) {
        if (chance > 0 && evt.getEntity().getEntityWorld().rand.nextInt(chance) != 0)
            return;
        if (evt.getEntityLiving() instanceof EntitySkeleton)
            addDrop(evt, new ItemStack(Items.SKULL, 1, 0));
        else if (evt.getEntityLiving() instanceof EntityWitherSkeleton)
            addDrop(evt, new ItemStack(Items.SKULL, 1, 1));
        else if (evt.getEntityLiving() instanceof EntityZombie)
            addDrop(evt, new ItemStack(Items.SKULL, 1, 2));
        else if (evt.getEntityLiving() instanceof EntityCreeper)
            addDrop(evt, new ItemStack(Items.SKULL, 1, 4));
        else if (evt.getEntityLiving() instanceof EntityPlayer) {
            addDrop(evt, PlayerHelper.getPlayerHead((EntityPlayer) evt.getEntityLiving()));
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
