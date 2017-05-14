package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by tyler on 5/13/17.
 */
public class PigBreeding extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Pigs can breed many differet items, including Wheat, Carrot,Beetroot, Potato (raw & cooked), Chocolate.";
    }

    @Override
    public void init(FMLInitializationEvent event) {
//        Field tempationItems = ReflectionHelper.findField(EntityPig.class, "TEMPTATION_ITEMS");
//        System.out.println(tempationItems);
////        try {

//            tempationItems.setAccessible(true);
////            tempationItems.set(null, Sets.newHashSet(new Item[]{Items.CARROT, Items.POTATO, Items.BEETROOT, Items.WHEAT, BWMItems.CHOCOLATE}));
////        } catch (IllegalAccessException e) {
////            e.printStackTrace();
////        }
    }
}
