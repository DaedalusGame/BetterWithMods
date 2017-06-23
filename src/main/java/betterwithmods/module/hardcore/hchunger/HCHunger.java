package betterwithmods.module.hardcore.hchunger;

import betterwithmods.client.gui.GuiHunger;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.module.CompatFeature;
import betterwithmods.module.gameplay.CauldronRecipes;
import betterwithmods.module.gameplay.KilnRecipes;
import betterwithmods.module.hardcore.hchunger.FoodHelper;
import betterwithmods.util.RecipeUtils;
import betterwithmods.util.player.HungerPenalty;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api.hunger.ExhaustionEvent;
import squeek.applecore.api.hunger.HealthRegenEvent;
import squeek.applecore.api.hunger.HungerEvent;

/**
 * Created by primetoxinz on 6/20/17.
 */
public class HCHunger extends CompatFeature {
    public HCHunger() {
        super("applecore");
    }

    @Override
    public void init(FMLInitializationEvent event) {

        FoodHelper.registerFood(new ItemStack(Items.PORKCHOP), 12);
        FoodHelper.registerFood(new ItemStack(Items.RABBIT), 12);
        FoodHelper.registerFood(new ItemStack(Items.CHICKEN), 9);
        FoodHelper.registerFood(new ItemStack(Items.MUTTON), 9);
        FoodHelper.registerFood(new ItemStack(Items.FISH), 9);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_BEEF), 15);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_PORKCHOP), 15);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_RABBIT), 15);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_CHICKEN), 12);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_MUTTON), 12);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_FISH), 12);
        FoodHelper.registerFood(new ItemStack(Items.SPIDER_EYE), 6);
        FoodHelper.registerFood(new ItemStack(Items.ROTTEN_FLESH), 9);
        FoodHelper.registerFood(new ItemStack(Items.MUSHROOM_STEW), 9);
        FoodHelper.registerFood(new ItemStack(Items.BEETROOT_SOUP), 9);
        FoodHelper.registerFood(new ItemStack(Items.RABBIT_STEW), 30);
        FoodHelper.registerFood(new ItemStack(Items.MELON), 2);
        FoodHelper.registerFood(new ItemStack(Items.APPLE), 3);
        FoodHelper.registerFood(new ItemStack(Items.POTATO), 3);
        FoodHelper.registerFood(new ItemStack(Items.CARROT), 3);
        FoodHelper.registerFood(new ItemStack(Items.BEETROOT), 3);
        FoodHelper.registerFood(new ItemStack(Items.BAKED_POTATO), 6);
        FoodHelper.registerFood(new ItemStack(Items.BREAD), 12);

        FoodHelper.registerFood(new ItemStack(Items.GOLDEN_APPLE), 3);
        FoodHelper.registerFood(new ItemStack(Items.GOLDEN_CARROT), 3);
        FoodHelper.registerFood(new ItemStack(BWMItems.BEEF_DINNER), 24);
        FoodHelper.registerFood(new ItemStack(BWMItems.BEEF_POTATOES), 18);
        FoodHelper.registerFood(new ItemStack(BWMItems.RAW_KEBAB), 18);
        FoodHelper.registerFood(new ItemStack(BWMItems.COOKED_KEBAB), 24);
        FoodHelper.registerFood(new ItemStack(BWMItems.CHICKEN_SOUP), 24);
        FoodHelper.registerFood(new ItemStack(BWMItems.CHOWDER), 15);
        FoodHelper.registerFood(new ItemStack(BWMItems.HEARTY_STEW), 30);
        FoodHelper.registerFood(new ItemStack(BWMItems.PORK_DINNER), 24);
        FoodHelper.registerFood(new ItemStack(BWMItems.RAW_EGG), 6);
        FoodHelper.registerFood(new ItemStack(BWMItems.COOKED_EGG), 9);
        FoodHelper.registerFood(new ItemStack(BWMItems.RAW_SCRAMBLED_EGG), 12);
        FoodHelper.registerFood(new ItemStack(BWMItems.COOKED_SCRAMBLED_EGG), 15);
        FoodHelper.registerFood(new ItemStack(BWMItems.RAW_OMELET), 9);
        FoodHelper.registerFood(new ItemStack(BWMItems.COOKED_OMELET), 12);
        FoodHelper.registerFood(new ItemStack(BWMItems.HAM_AND_EGGS), 18);
        FoodHelper.registerFood(new ItemStack(BWMItems.TASTY_SANDWICH), 18);
        FoodHelper.registerFood(new ItemStack(BWMItems.CREEPER_OYSTER), 6);
        FoodHelper.registerFood(new ItemStack(BWMItems.KIBBLE), 9);
        FoodHelper.registerFood(new ItemStack(BWMItems.WOLF_CHOP), 12);
        FoodHelper.registerFood(new ItemStack(BWMItems.COOKED_WOLF_CHOP), 15);

        FoodHelper.registerFood(new ItemStack(BWMItems.DONUT), 3, 3, true);
        FoodHelper.registerFood(new ItemStack(BWMItems.APPLE_PIE), 12, 15, true);
        FoodHelper.registerFood(new ItemStack(Items.COOKIE), 3, 1, true);
        FoodHelper.registerFood(new ItemStack(Items.PUMPKIN_PIE), 12, 15, true);
        FoodHelper.registerFood(new ItemStack(BWMItems.CHOCOLATE), 6, 3, true);

        RecipeUtils.removeRecipes(Items.MUSHROOM_STEW, 0);
        RecipeUtils.removeRecipes(Items.CAKE, 0);
        RecipeUtils.removeRecipes(Items.COOKIE, 0);
        RecipeUtils.removeRecipes(Items.PUMPKIN_PIE, 0);
        RecipeUtils.removeRecipes(Items.RABBIT_STEW, 0);
        RecipeUtils.removeRecipes(Items.BEETROOT_SOUP, 0);

        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 8), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, 1), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.APPLE), new ItemStack(BWMItems.APPLE_PIE, 1), 0.1F);

        KilnRecipes.addKilnRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 8));
        KilnRecipes.addKilnRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, 1));
        KilnRecipes.addKilnRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.APPLE), new ItemStack(BWMItems.APPLE_PIE, 1));

        CauldronRecipes.addCauldronRecipe(new ItemStack(Items.MUSHROOM_STEW), new ItemStack(Items.BUCKET), new Object[]{new ItemStack(Blocks.BROWN_MUSHROOM, 3), new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.BOWL)});
        CauldronRecipes.addCauldronRecipe(new ItemStack(Items.BEETROOT_SOUP), new Object[]{new ItemStack(Items.BEETROOT, 6), new ItemStack(Items.BOWL)});
        CauldronRecipes.addCauldronRecipe(new ItemStack(Items.RABBIT_STEW, 5), new Object[]{Items.COOKED_RABBIT, Items.CARROT, Items.BAKED_POTATO, new ItemStack(Items.BOWL, 5), new ItemStack(Blocks.RED_MUSHROOM, 3), "foodFlour"});
    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 16), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, 2), 0.1F);
        KilnRecipes.addKilnRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 16));
        KilnRecipes.addKilnRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, 2));
    }

    @Override
    public void preInitClient(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ClientSide.class);
        super.preInitClient(event);
    }


    @SubscribeEvent
    public void respawn(PlayerEvent.PlayerRespawnEvent event) {
        event.player.getFoodStats().setFoodSaturationLevel(0);
    }

    //Changes food to correct value.
    @SubscribeEvent
    public void modifyFoodValues(FoodEvent.GetFoodValues event) {
        FoodHelper.getFoodValue(event.food).ifPresent(v -> event.foodValues = v);
    }


    //Stops Eating if Hunger Effect is active
    @SubscribeEvent
    public void onFood(LivingEntityUseItemEvent.Start event) {
        if (event.getItem().getItem() instanceof ItemFood && event.getEntityLiving() instanceof EntityPlayer && PlayerHelper.isSurvival((EntityPlayer) event.getEntityLiving())) {
            if (event.getEntityLiving().isPotionActive(MobEffects.HUNGER)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void getPlayerFoodValue(FoodEvent.GetPlayerFoodValues event) {
        FoodStats stats = event.player.getFoodStats();
        int playerFoodLevel = stats.getFoodLevel();
        int foodLevel = event.foodValues.hunger;
        float fat = (foodLevel + playerFoodLevel) - AppleCoreAPI.accessor.getMaxHunger(event.player);
        if (!FoodHelper.isDessert(event.food)) {
            if (fat < 0) {
                event.foodValues = new FoodValues(foodLevel, 0);
            } else {
                event.foodValues = new FoodValues(foodLevel, fat / 4);
            }
        }
    }

    //Changes exhaustion to reduce food first, then fat.
    @SubscribeEvent
    public void exhaust(ExhaustionEvent.Exhausted event) {
        FoodStats stats = event.player.getFoodStats();
        float saturation = stats.getSaturationLevel();
        int hunger = stats.getFoodLevel();
        if (hunger > saturation) {
            event.deltaSaturation = 0;
            event.deltaHunger = -1;
        } else {
            event.deltaSaturation = -1;
            event.deltaHunger = 0;
        }
    }

    //Adds Exhaustion when Jumping and cancels Jump if too exhausted
    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (!PlayerHelper.canJump(player)) {
                event.getEntityLiving().motionX = 0;
                event.getEntityLiving().motionY = 0;
                event.getEntityLiving().motionZ = 0;
            }
            player.addExhaustion(0.09f);
        }
    }

    @SubscribeEvent
    public void setMaxFood(HungerEvent.GetMaxHunger event) {
        event.maxHunger = 60;
    }


    //Chaneg speed based on Hunger
    @SubscribeEvent
    public void givePenalties(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            PlayerHelper.changeSpeed(player, "Hunger Speed Modifier", PlayerHelper.getSpeedModifier(player));
        }
    }

    //Max Health only regen when above Peckish
    @SubscribeEvent
    public void allowHealthRegen(HealthRegenEvent.AllowRegen event) {
        event.setResult(PlayerHelper.getHungerPenalty(event.player) == HungerPenalty.NO_PENALTY ? Event.Result.ALLOW : Event.Result.DENY);
    }

    //Change Health Regen speed to take 30 seconds
    @SubscribeEvent
    public void healthRegenSpeed(HealthRegenEvent.GetRegenTickPeriod event) {
        event.regenTickPeriod = 600;
    }

    //Stop regen from Fat value.
    @SubscribeEvent
    public void denyFatRegen(HealthRegenEvent.AllowSaturatedRegen event) {
        event.setResult(Event.Result.DENY);
    }

    public String getFeatureDescription() {
        return "Completely revamps the hunger system of Minecraft. \n" +
                "The Saturation value is replaced with Fat. \n" +
                "Fat will accumulate if too much food is consumed then need to fill the bar.\n" +
                "Fat will only be burned once the entire hunger bar is emptied \n" +
                "The more fat the slower you will walk.\n" +
                "Food Items values are also changed, while a ton of new foods are add.";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    public static class ClientSide {

        //Replaces Hunger Gui with HCHunger
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public static void replaceHungerGui(RenderGameOverlayEvent.Pre event) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
                event.setCanceled(true);
                GuiHunger.INSTANCE.draw();
            }
        }

        //Shake Hunger bar whenever any exhaustion is given?
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public static void onExhaust(ExhaustionEvent.ExhaustionAddition event) {
            GuiHunger.INSTANCE.shake();
        }
    }
}


