package betterwithmods.module.hardcore;

import betterwithmods.BWMod;
import betterwithmods.client.gui.GuiHunger;
import betterwithmods.common.BWMItems;
import betterwithmods.module.Feature;
import betterwithmods.util.BWMFoodStats;
import betterwithmods.util.InvUtils;
import betterwithmods.util.player.EntityPlayerExt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ThrowableImpactEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Random;
import java.util.UUID;

/**
 * Created by tyler on 4/20/17.
 */
public class HCHunger extends Feature {
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
        if (player.isInWater() && !EntityPlayerExt.canSwim(player) && !EntityPlayerExt.isNearBottom(player)) {
            player.motionY -= 0.02;
        }
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
    /*
        @SubscribeEvent
        public void onFoodConfigChanged(OnConfigChangedEvent event) {
            if (!event.getModID().equals(BWMod.MODID))
                return;
            if (!event.isWorldRunning())
                return;
            if (!Minecraft.getMinecraft().isSingleplayer())
                return;
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (BWConfig.hardcoreHunger)
                applyFoodSystem(player);
            else
                revertFoodSystem(player);
            // TODO find solution for issue #71
        }*/

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

    @SubscribeEvent
    public void getRawEgg(ThrowableImpactEvent event) {
        if (event.getEntityThrowable() instanceof EntityEgg) {
            event.setCanceled(true);
            RayTraceResult result = event.getRayTraceResult();
            EntityThrowable egg = event.getEntityThrowable();
            Random rand = egg.getEntityWorld().rand;
            if (result.entityHit != null) {
                result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(egg, egg.getThrower()), 0.0F);
            }

            if (!egg.getEntityWorld().isRemote) {
                if (rand.nextInt(8) == 0) {
                    int i = 1;

                    if (rand.nextInt(32) == 0) {
                        i = 4;
                    }

                    for (int j = 0; j < i; ++j) {
                        EntityChicken entitychicken = new EntityChicken(egg.getEntityWorld());
                        entitychicken.setGrowingAge(-24000);
                        entitychicken.setLocationAndAngles(egg.posX, egg.posY, egg.posZ, egg.rotationYaw, 0.0F);
                        egg.getEntityWorld().spawnEntity(entitychicken);
                    }
                } else {
                    InvUtils.ejectStack(egg.getEntityWorld(), egg.posX, egg.posY, egg.posZ, new ItemStack(BWMItems.RAW_EGG));
                }
            }

            for (int k = 0; k < 8; ++k) {
                egg.getEntityWorld().spawnParticle(EnumParticleTypes.ITEM_CRACK, egg.posX, egg.posY, egg.posZ, ((double) rand.nextFloat() - 0.5D) * 0.08D, ((double) rand.nextFloat() - 0.5D) * 0.08D, ((double) rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(Items.EGG));
            }

            if (!egg.getEntityWorld().isRemote) {
                egg.setDead();
            }
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return super.hasSubscriptions();
    }
}
