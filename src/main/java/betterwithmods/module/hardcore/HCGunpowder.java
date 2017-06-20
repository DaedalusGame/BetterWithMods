package betterwithmods.module.hardcore;

import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import com.google.common.collect.Lists;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tyler on 4/20/17.
 */
public class HCGunpowder extends Feature {
    public static List<Class> disableGunpowder = Lists.newArrayList();
    @Override
    public void setupConfig() {

        String[] array = loadPropStringList("Disable Gunpowder Drop", "List of entity classes which gunpowder will be replaced with niter", new String[]{"net.minecraft.entity.monster.EntityCreeper","net.minecraft.entity.monster.EntityGhast", "net.minecraft.entity.monster.EntityWitch"});
        disableGunpowder = Arrays.stream(array).map(clazz -> {
            try {
                return Class.forName(clazz);
            } catch (ClassNotFoundException ignore) {
            }
            return null;
        }).collect(Collectors.toList());
    }

    @Override
    public String getFeatureDescription() {
        return "Makes a raw resource drop that must be crafted to make useful gunpowder";
    }

    @SubscribeEvent
    public void mobDrops(LivingDropsEvent evt) {
        boolean contained = false;
        for(Class clazz: disableGunpowder) {
            if (evt.getEntity().getClass().isAssignableFrom(clazz)) {
                contained = true;
                break;
            }
        }
        if (contained) {
            for (EntityItem item : evt.getDrops()) {
                ItemStack stack = item.getItem();
                if (stack.getItem() == Items.GUNPOWDER) {
                    item.setItem(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NITER, stack.getCount()));
                }
            }
        }
    }
    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
