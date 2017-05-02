package betterwithmods.module.tweaks;

import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class Dung extends Feature {
    private static final int[] fearLevel = {1600, 1500, 1400, 1300, 1200, 1100, 1000, 900, 800, 700, 600, 500, 400, 300, 200, 100};
    private boolean wolvesOnly;

    @Override
    public String getFeatureDescription() {
        return "Animals will launch dung depending on their conditions, a useful material";
    }

    @Override
    public void setupConfig() {
        wolvesOnly = loadPropBool("Only Wolves", "Only Wolves will produce dung", false);
    }

    @SubscribeEvent
    public void mobDungProduction(LivingEvent.LivingUpdateEvent evt) {
        if (evt.getEntityLiving().getEntityWorld().isRemote)
            return;
        if (evt.getEntityLiving() instanceof EntityAnimal) {
            EntityAnimal animal = (EntityAnimal) evt.getEntityLiving();
            if (animal instanceof EntityWolf) {
                if (!animal.getEntityWorld().canSeeSky(animal.getPosition())) {
                    if (animal.getGrowingAge() > 99) {
                        int light = animal.getEntityWorld().getLight(animal.getPosition());
                        if (animal.getGrowingAge() == fearLevel[light]) {
                            evt.getEntityLiving().entityDropItem(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG), 0.0F);
                            animal.setGrowingAge(99);
                        }
                    }
                }
            } else if(!wolvesOnly){
                if (animal.world.rand.nextInt(1200) == 0) {
                    evt.getEntityLiving().entityDropItem(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG), 0.0F);
                }
            }
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
