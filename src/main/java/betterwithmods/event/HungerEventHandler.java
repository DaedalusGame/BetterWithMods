package betterwithmods.event;

import java.util.UUID;

import betterwithmods.client.gui.GuiHunger;
import betterwithmods.config.BWConfig;
import betterwithmods.util.BWMFoodStats;
import betterwithmods.util.player.EntityPlayerExt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Events handling food systems.
 * @author Koward
 *
 */
public class HungerEventHandler {
	private static GuiHunger guiHunger = null;
	@SubscribeEvent
	public void replaceHungerGui(RenderGameOverlayEvent.Pre event) {
		if (!BWConfig.hardcoreHunger) return;
		if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD){
			event.setCanceled(true);
			if(guiHunger == null) guiHunger = new GuiHunger();
			guiHunger.draw();
		}
	}

	@SubscribeEvent
	public void replaceFoodSystem(EntityJoinWorldEvent event) {
		if (!BWConfig.hardcoreHunger) return;
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			if (player.getFoodStats() instanceof BWMFoodStats)
				return;
			ReflectionHelper.setPrivateValue(EntityPlayer.class, player, new BWMFoodStats(player), "field_71100_bB", "foodStats");
			player.getFoodStats().readNBT(player.getEntityData());
		}
	}

	/**
	 * The FoodStats must be manually saved with event. Why is not known.
	 * @param event
	 */
	@SubscribeEvent
	public void saveFoodSystem(PlayerLoggedOutEvent event) {
		if (!BWConfig.hardcoreHunger) return;
		event.player.getFoodStats().writeNBT(event.player.getEntityData());
	}

	/**
	 * Eating is not allowed when food poisoned.
	 * @param event
	 */
	@SubscribeEvent
	public void onFood(LivingEntityUseItemEvent.Start event) {
		if (!BWConfig.hardcoreHunger) return;
		if (!(event.getEntityLiving() instanceof EntityPlayer))
			return;
		if (!(event.getItem().getItem() instanceof ItemFood))
			return;
		EntityPlayer entityPlayer = (EntityPlayer) event.getEntityLiving();
		if (entityPlayer.isPotionActive(MobEffects.HUNGER)) {
			event.setCanceled(true);
		}
	}
	

	/**
	 * Mining speed changed according to health/exhaustion/fat.
	 * Complete rework of EntityPlayer.getDigSpeed() to also check if using right item to harvest.
	 * @param event
	 */
	@SubscribeEvent
	public void breakSpeedPenalty(PlayerEvent.BreakSpeed event) {
		if (!BWConfig.hardcoreHunger) return;
		EntityPlayer player = event.getEntityPlayer();
		IBlockState state = event.getState();
        float f = player.inventory.getStrVsBlock(state);

        if (f > 1.0F)
        {
            int i = EnchantmentHelper.getEfficiencyModifier(player);
            ItemStack itemstack = player.getHeldItemMainhand();

            if (i > 0 && itemstack != null)
            {
                float intermediate = (float)(i * i + 1);
                
                if (!itemstack.canHarvestBlock(state) && f <= 1.0F)
                {
                    f += intermediate * 0.08F;
                }
                else
                {
                    f += intermediate;
                }
            }
        }

        if (player.isPotionActive(MobEffects.HASTE))
        {
            f *= 1.0F + (float)(player.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F;
        }

        if (player.isPotionActive(MobEffects.MINING_FATIGUE))
        {
            float f1;

            switch (player.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier())
            {
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

        if (player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(player))
        {
            f /= 5.0F;
        }

        if (!player.onGround)
        {
            f /= 5.0F;
        }
        
        f *= EntityPlayerExt.getHealthAndExhaustionModifier(player);

        if(f < 0) f = 0;
        event.setNewSpeed(f);
	}

	/**
	 * Walking speed changed according to health/exhaustion/fat
	 * @param event
	 */
	@SubscribeEvent
	public void walkingPenalty(LivingUpdateEvent event) {
		if (!BWConfig.hardcoreHunger) return;
		if (!(event.getEntityLiving() instanceof EntityPlayer)) return; 
		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		if(!EntityPlayerExt.isSurvival(player)) return;
		final UUID penaltySpeedUUID = UUID
				.fromString("c5595a67-9410-4fb2-826a-bcaf432c6a6f");
		EntityPlayerExt.changeSpeed(player, penaltySpeedUUID,
				"Health speed penalty",
				EntityPlayerExt.getHealthAndExhaustionModifier(player));
	}

	/**
	 * Disable swimming if needed.
	 * TODO FIXME Not able to jump at the bottom. New hook may be required.
	 * @param event
	 */
	@SubscribeEvent
	public void swimmingPenalty(LivingUpdateEvent event) {
		if (!BWConfig.hardcoreHunger) return;
		if (!(event.getEntityLiving() instanceof EntityPlayer)) return; 
		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		if(!EntityPlayerExt.isSurvival(player)) return;
		if (player.isInWater() && !EntityPlayerExt.canSwim(player)) {
			player.motionY -= 0.02;
		}
	}
	
	@SubscribeEvent
	public void jumpingPenalty(LivingJumpEvent event) {
		if (!BWConfig.hardcoreHunger) return;
		if (!(event.getEntityLiving() instanceof EntityPlayer)) return; 
		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		if(!EntityPlayerExt.isSurvival(player)) return;
		if(!EntityPlayerExt.canJump(player)) {
			event.getEntityLiving().motionX = 0;
			event.getEntityLiving().motionY = 0;
			event.getEntityLiving().motionZ = 0;
		}
	}
	
	/**
	 * Cancel the FOV decrease caused by the decreasing speed due to player penalties.
	 * Original FOV value given by the event is never used, we start from scratch 1.0F value.
	 * Edited from AbstractClientPlayer.getFovModifier()
	 * @param event
	 */
	@SubscribeEvent
	public void onFOVUpdate(FOVUpdateEvent event) {
		if (!BWConfig.hardcoreHunger) return;
		EntityPlayer player = event.getEntity();
		float modifier = EntityPlayerExt.getHealthAndExhaustionModifier(player);

        float f = 1.0F;

        if (player.capabilities.isFlying)
        {
            f *= 1.1F;
        }

        IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        double oldAttributeValue = iattributeinstance.getAttributeValue() / modifier;
        f = (float)((double)f * ((oldAttributeValue / (double)player.capabilities.getWalkSpeed() + 1.0D) / 2.0D));

        if (player.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f))
        {
            f = 1.0F;
        }

        if (player.isHandActive() && player.getActiveItemStack() != null && player.getActiveItemStack().getItem() == Items.BOW)
        {
            int i = player.getItemInUseMaxCount();
            float f1 = (float)i / 20.0F;

            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }
            else
            {
                f1 = f1 * f1;
            }

            f *= 1.0F - f1 * 0.15F;
        }

		event.setNewfov(f);
	}
}
