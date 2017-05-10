package betterwithmods.module.hardcore;

import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.util.item.StackMap;
import betterwithmods.util.player.EntityPlayerExt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by tyler on 5/10/17.
 */
public class HCArmor extends Feature {
    public static final StackMap<Integer> weights = new StackMap<>(0);

    @Override
    public String getFeatureDescription() {
        return "Gives Armor weight values that effect movement.";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        initWeights();
    }

    public static void initWeights() {
        weights.put(Items.CHAINMAIL_HELMET, OreDictionary.WILDCARD_VALUE, 3);
        weights.put(Items.CHAINMAIL_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 4);
        weights.put(Items.CHAINMAIL_LEGGINGS, OreDictionary.WILDCARD_VALUE, 4);
        weights.put(Items.CHAINMAIL_BOOTS, OreDictionary.WILDCARD_VALUE, 2);

        weights.put(Items.IRON_HELMET, OreDictionary.WILDCARD_VALUE, 5);
        weights.put(Items.IRON_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 8);
        weights.put(Items.IRON_LEGGINGS, OreDictionary.WILDCARD_VALUE, 7);
        weights.put(Items.IRON_BOOTS, OreDictionary.WILDCARD_VALUE, 4);

        weights.put(Items.DIAMOND_HELMET, OreDictionary.WILDCARD_VALUE, 5);
        weights.put(Items.DIAMOND_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 8);
        weights.put(Items.DIAMOND_LEGGINGS, OreDictionary.WILDCARD_VALUE, 7);
        weights.put(Items.DIAMOND_BOOTS, OreDictionary.WILDCARD_VALUE, 4);

        weights.put(Items.GOLDEN_HELMET, OreDictionary.WILDCARD_VALUE, 5);
        weights.put(Items.GOLDEN_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 8);
        weights.put(Items.GOLDEN_LEGGINGS, OreDictionary.WILDCARD_VALUE, 7);
        weights.put(Items.GOLDEN_BOOTS, OreDictionary.WILDCARD_VALUE, 4);
    }

    public static float getWeight(ItemStack stack) {
        if(!ModuleLoader.isFeatureEnabled(HCArmor.class))
            return 0;
        return weights.get(stack);
    }

    @SubscribeEvent
    public void swimmingPenalty(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (EntityPlayerExt.isSurvival(player) && player.isInWater() && !EntityPlayerExt.canSwim(player) && !EntityPlayerExt.isNearBottom(player)) {
            player.motionY -= 0.02;
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
