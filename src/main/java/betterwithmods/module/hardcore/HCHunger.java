package betterwithmods.module.hardcore;

import betterwithmods.BWMod;
import betterwithmods.client.gui.GuiHunger;
import betterwithmods.common.BWCrafting;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.module.Feature;
import betterwithmods.util.BWMFoodStats;
import betterwithmods.util.RecipeUtils;
import betterwithmods.util.player.EntityPlayerExt;
import betterwithmods.util.player.FatPenalty;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created by tyler on 4/20/17.
 */
public class HCHunger extends Feature {

    private double jumpExhaustion;
    @Override
    public void setupConfig() {
        jumpExhaustion = loadPropDouble("Jump Exhaustion", "Exhaustion penalty from jumping", 0.09);
        if(loadPropBool("Kill Autojump", "Automatically Disable Autojump, because it's stupid",true))
            Minecraft.getMinecraft().gameSettings.autoJump = false;
    }

    @Override
    public void init(FMLInitializationEvent event) {


        //MEATS
        modifyFoodValue((ItemFood) Items.SPIDER_EYE, 6,0);

        modifyFoodValue((ItemFood) Items.ROTTEN_FLESH, 9,0);
        modifyFoodValue((ItemFood) Items.CHICKEN, 9,0);
        modifyFoodValue((ItemFood) Items.MUTTON, 9,0);

        modifyFoodValue((ItemFood) Items.COOKED_CHICKEN, 12,0);
        modifyFoodValue((ItemFood) Items.COOKED_MUTTON, 12,0);
        modifyFoodValue((ItemFood) Items.BEEF, 12,0);
        modifyFoodValue((ItemFood) Items.PORKCHOP, 12,0);
        modifyFoodValue((ItemFood) BWMItems.WOLF_CHOP, 12,0);

        modifyFoodValue((ItemFood) Items.COOKED_BEEF, 15,0);
        modifyFoodValue((ItemFood) Items.COOKED_PORKCHOP, 15,0);

        modifyFoodValue((ItemFood) Items.FISH, 9,0);
        modifyFoodValue((ItemFood) Items.COOKED_FISH, 12,0);
        //OTHER
        modifyFoodValue((ItemFood) Items.MELON, 2,0);
        modifyFoodValue((ItemFood) Items.MUSHROOM_STEW, 15,0);
        modifyFoodValue((ItemFood) Items.BREAD, 12,0);
        modifyFoodValue((ItemFood) Items.COOKIE, 3,3);
        modifyFoodValue((ItemFood) Items.PUMPKIN_PIE, 6,15);
        //TODO CAKE????
        modifyFoodValue((ItemFood) Items.POTATO, 3,0);
        modifyFoodValue((ItemFood) Items.BAKED_POTATO, 6,0);
        modifyFoodValue((ItemFood) Items.CARROT, 3,0);
        modifyFoodValue((ItemFood) Items.APPLE, 3,0);
        modifyFoodValue((ItemFood) Items.GOLDEN_APPLE, 3,0);
        modifyFoodValue((ItemFood) Items.GOLDEN_CARROT, 3,0);

        RecipeUtils.removeRecipes(Items.BREAD, 0);
        RecipeUtils.removeRecipes(Items.MUSHROOM_STEW, 0);
        RecipeUtils.removeRecipes(Items.CAKE, 0);
        RecipeUtils.removeRecipes(Items.COOKIE, 0);
        RecipeUtils.removeRecipes(Items.PUMPKIN_PIE, 0);
        RecipeUtils.removeRecipes(Items.RABBIT_STEW, 0);
        RecipeUtils.removeRecipes(Items.BEETROOT_SOUP, 0);

        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 8), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, 1), 0.1F);

        BWCrafting.addKilnRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 8));
        BWCrafting.addKilnRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, 1));

        BWCrafting.addCauldronRecipe(new ItemStack(Items.MUSHROOM_STEW), new ItemStack(Items.BUCKET), new ItemStack[]{new ItemStack(Blocks.BROWN_MUSHROOM, 3), new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.BOWL)});
        BWCrafting.addCauldronRecipe(new ItemStack(Items.BEETROOT_SOUP), new ItemStack[]{new ItemStack(Items.BEETROOT, 6), new ItemStack(Items.BOWL)});

        initDesserts();
    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 16), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, 2), 0.1F);
        BWCrafting.addKilnRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 16));
        BWCrafting.addKilnRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, 2));
    }

    @Override
    public String getFeatureDescription() {
        return "Completely revamps the hunger system of Minecraft. \n" +
                "The Saturation value is replaced with Fat. \n" +
                "Fat will accumulate if too much food is consumed then need to fill the bar.\n" +
                "Fat will only be burned once the entire hunger bar is emptied \n" +
                "The more fat the slower you will walk.\n" +
                "Food Items values are also changed, while a ton of new foods are add.";
    }

    @Override
    public String[] getIncompatibleMods() {
        return new String[]{"applecore"};
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    private static GuiHunger guiHunger = null;

    @SubscribeEvent
    public void replaceHungerGui(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
            if (!(Minecraft.getMinecraft().player.getFoodStats() instanceof BWMFoodStats))
                return;// Can happen for a moment when changing config
            event.setCanceled(true);
            if (guiHunger == null)
                guiHunger = new GuiHunger();
            guiHunger.draw();
        }
    }

    @SubscribeEvent
    public void replaceFoodSystem(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            applyFoodSystem(player);
        }
    }

    private void setFoodStats(EntityPlayer player, FoodStats foodStats) {
        ReflectionHelper.setPrivateValue(EntityPlayer.class, player, foodStats, "field_71100_bB", "foodStats");
    }

    private void applyFoodSystem(EntityPlayer player) {
        if (player.getFoodStats() instanceof BWMFoodStats)
            return;
        BWMFoodStats newFS = new BWMFoodStats(player);
        NBTTagCompound compound = player.getEntityData();
        newFS.readNBT(compound);
        setFoodStats(player, newFS);
        BWMod.logger.debug("Custom food system " + newFS + " applied on " + player.getName() + ".");
    }

    /**
     * Revert player's food system to vanilla {@link FoodStats}.
     */
    private void revertFoodSystem(EntityPlayer player) {
        if (!(player.getFoodStats() instanceof BWMFoodStats))
            return;
        BWMFoodStats originalFS = (BWMFoodStats) player.getFoodStats();
        FoodStats newFS = new FoodStats();
        NBTTagCompound compound = player.getEntityData();
        newFS.readNBT(compound);
        if (compound.hasKey("bwmAdjustedFoodStats")) {
            newFS.setFoodLevel(originalFS.getFoodLevel() / 3);
            compound.setBoolean("bwmAdjustedFoodStats", false);
            newFS.writeNBT(compound);
        }
        setFoodStats(player, newFS);
        BWMod.logger.debug("Vanilla food system " + newFS + " applied on " + player.getName() + ".");
    }

    /**
     * The FoodStats must be manually saved with event. Why is not known.
     */
    @SubscribeEvent
    public void saveFoodSystem(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.player.getFoodStats() instanceof BWMFoodStats))
            return;
        event.player.getFoodStats().writeNBT(event.player.getEntityData());
    }

    /**
     * Eating is not allowed when food poisoned.
     */
    @SubscribeEvent
    public void onFood(LivingEntityUseItemEvent.Start event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer))
            return;
        if (!(event.getItem().getItem() instanceof ItemFood))
            return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (!(player.getFoodStats() instanceof BWMFoodStats))
            return;
        if (player.isPotionActive(MobEffects.HUNGER)) {
            event.setCanceled(true);
        }
    }

    /**
     * Mining speed changed according to health/exhaustion/fat. Complete rework
     * of EntityPlayer.getDigSpeed() to also check if using right item to
     * harvest.
     */
    @SubscribeEvent
    public void breakSpeedPenalty(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        if (!(player.getFoodStats() instanceof BWMFoodStats))
            return;
        IBlockState state = event.getState();
        float f = player.inventory.getStrVsBlock(state);

        if (f > 1.0F) {
            int i = EnchantmentHelper.getEfficiencyModifier(player);
            ItemStack itemstack = player.getHeldItemMainhand();

            if (i > 0 && itemstack != ItemStack.EMPTY) {
                float intermediate = (float) (i * i + 1);

                if (!itemstack.canHarvestBlock(state) && f <= 1.0F) {
                    f += intermediate * 0.08F;
                } else {
                    f += intermediate;
                }
            }
        }

        if (player.isPotionActive(MobEffects.HASTE)) {
            f *= 1.0F + (float) (player.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F;
        }

        if (player.isPotionActive(MobEffects.MINING_FATIGUE)) {
            float f1;

            switch (player.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
                case 0:
                    f1 = 0.3F;
                    break;
                case 1:
                    f1 = 0.09F;
                    break;
                case 2:
                    f1 = 0.0027F;
                    break;
                case 3:
                default:
                    f1 = 8.1E-4F;
            }

            f *= f1;
        }

        if (player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(player)) {
            f /= 5.0F;
        }

        if (!player.onGround) {
            f /= 5.0F;
        }

        f *= EntityPlayerExt.getHealthAndExhaustionModifier(player);

        if (f < 0)
            f = 0;
        event.setNewSpeed(f);
    }

    /**
     * Walking speed changed according to health/exhaustion/fat
     */
    @SubscribeEvent
    public void walkingPenalty(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (!EntityPlayerExt.isSurvival(player))
            return;
        if (!(player.getFoodStats() instanceof BWMFoodStats))
            return;
        final UUID penaltySpeedUUID = UUID.fromString("c5595a67-9410-4fb2-826a-bcaf432c6a6f");
        EntityPlayerExt.changeSpeed(player, penaltySpeedUUID, "Health speed penalty",
                EntityPlayerExt.getHealthAndExhaustionModifier(player));
        if(player.isSprinting())
            guiHunger.triggerShake();
    }

    /**
     * Disable swimming if needed. FIXME Not able to jump at the bottom.
     * New hook may be required. (Probable workaround implemented)
     */
    @SubscribeEvent
    public void swimmingPenalty(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (!EntityPlayerExt.isSurvival(player))
            return;
        if (!(player.getFoodStats() instanceof BWMFoodStats))
            return;
    }

    @SubscribeEvent
    public void jumpingPenalty(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (!EntityPlayerExt.isSurvival(player))
            return;
        if (!(player.getFoodStats() instanceof BWMFoodStats))
            return;
        if (!EntityPlayerExt.canJump(player)) {
            event.getEntityLiving().motionX = 0;
            event.getEntityLiving().motionY = 0;
            event.getEntityLiving().motionZ = 0;
        }
        guiHunger.triggerShake();
        player.addExhaustion((float) jumpExhaustion);
    }

    /**
     * Cancel the FOV decrease caused by the decreasing speed due to player
     * penalties. Original FOV value given by the event is never used, we start
     * from scratch 1.0F value. Edited from
     * AbstractClientPlayer.getFovModifier()
     */
    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent event) {
        EntityPlayer player = event.getEntity();
        if (!(player.getFoodStats() instanceof BWMFoodStats))
            return;
        float modifier = EntityPlayerExt.getHealthAndExhaustionModifier(player);

        float f = 1.0F;

        if (player.capabilities.isFlying) {
            f *= 1.1F;
        }

        IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        double oldAttributeValue = iattributeinstance.getAttributeValue() / modifier;
        f = (float) ((double) f * ((oldAttributeValue / (double) player.capabilities.getWalkSpeed() + 1.0D) / 2.0D));

        if (player.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
            f = 1.0F;
        }

        if (player.isHandActive() && player.getActiveItemStack() != ItemStack.EMPTY
                && player.getActiveItemStack().getItem() == Items.BOW) {
            int i = player.getItemInUseMaxCount();
            float f1 = (float) i / 20.0F;

            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 = f1 * f1;
            }

            f *= 1.0F - f1 * 0.15F;
        }

        event.setNewfov(f);
    }

    @SubscribeEvent
    public void saveSoup(LivingEntityUseItemEvent.Finish event) {
        if (event.getItem() != ItemStack.EMPTY) {
            if (event.getItem().getItem() instanceof ItemSoup) {
                if (event.getItem().getCount() > 0) {
                    ItemStack result = event.getResultStack();
                    event.setResultStack(event.getItem());
                    if (event.getEntityLiving() instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                        if (!player.inventory.addItemStackToInventory(result)) {
                            player.dropItem(result, false);
                        }
                    }
                }
            }
        }
    }


    public static void initDesserts() {
        setDessert((ItemFood) Items.COOKIE);
        setDessert((ItemFood) Items.PUMPKIN_PIE);
        setDessert((ItemFood) BWMItems.CHOCOLATE);
    }

    public static void setDessert(ItemFood food) {
        food.setAlwaysEdible();
    }


    @Override
    public boolean hasSubscriptions() {
        return true;
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderPlayer(RenderPlayerEvent.Pre e) {
        EntityPlayer player = e.getEntityPlayer();
        if(!(player.getFoodStats() instanceof  BWMFoodStats))
            return;
        FatPenalty penalty = EntityPlayerExt.getFatPenalty(player);
        RenderPlayer render = e.getRenderer();
    }

    private static Field foodField = ReflectionHelper.findField(ItemFood.class,"healAmount","field_77853_b");
    private static Field satField = ReflectionHelper.findField(ItemFood.class,"saturationModifier","field_77854_c");
    static {
        foodField.setAccessible(true);
        satField.setAccessible(true);
    }
    public static void modifyFoodValue(ItemFood item, int food, float saturation) {
        try {
            foodField.set(item,food);
            satField.set(item,saturation);
            BWMod.logger.info("{},{}",food,saturation);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}

