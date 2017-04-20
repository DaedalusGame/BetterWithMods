package betterwithmods.module.hardcore;

import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class HCGunpowder extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Makes a raw resource drop that must be crafted to make useful gunpowder";
    }

    @SubscribeEvent
    public void mobDrops(LivingDropsEvent evt) {
        if (evt.getEntity() instanceof EntityCreeper || evt.getEntity() instanceof EntityGhast) {
            for (EntityItem item : evt.getDrops()) {
                ItemStack stack = item.getEntityItem();
                if (stack.getItem() == Items.GUNPOWDER) {
                    item.setEntityItemStack(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NITER, stack.getCount()));
                }
            }
        }
    }
    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
