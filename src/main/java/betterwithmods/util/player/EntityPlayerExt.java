package betterwithmods.util.player;

import java.util.UUID;

import betterwithmods.util.item.ItemExt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;

/**
 * Set of methods dealing with EntityPlayer
 * @author Koward
 *
 */
public final class EntityPlayerExt {
	private EntityPlayerExt() {}

	public static boolean isSurvival(EntityPlayer player) {
		boolean isAdventure = player.worldObj.getWorldInfo().getGameType() == GameType.ADVENTURE;
		boolean isCreative = player.capabilities.isCreativeMode;
		return !(isAdventure || isCreative);
	}

	public static float getHealthAndExhaustionModifier(EntityPlayer player) {
		return getWorstPenalty(player).getModifier();
	}
	
	public static IPlayerPenalty getWorstPenalty(EntityPlayer player) {
		HealthPenalty healthPenalty = getHealthPenalty(player);
		HungerPenalty hungerPenalty = getHungerPenalty(player);
		FatPenalty fatPenalty = getFatPenalty(player);
		int maximumOrdinal = Math.max(healthPenalty.ordinal(), Math.max(hungerPenalty.ordinal(), fatPenalty.ordinal()));
		if(maximumOrdinal == healthPenalty.ordinal()) return healthPenalty;
		else if(maximumOrdinal == hungerPenalty.ordinal()) return hungerPenalty;
		else if(maximumOrdinal == fatPenalty.ordinal()) return fatPenalty;
		else return healthPenalty;
	}

	public static HungerPenalty getHungerPenalty(EntityPlayer player) {
		int level = player.getFoodStats().getFoodLevel();
		if(level > 24) return HungerPenalty.NO_PENALTY;
		else if(level > 18) return HungerPenalty.PECKISH;
		else if(level > 12) return HungerPenalty.HUNGRY;
		else if(level > 6) return HungerPenalty.FAMISHED;
		else if(level > 0 || player.getFoodStats().getSaturationLevel() > 0) return HungerPenalty.STARVING;
		else return HungerPenalty.DYING;
	}

	public static FatPenalty getFatPenalty(EntityPlayer player) {
		int level = (int) player.getFoodStats().getSaturationLevel();
		if(level < 12) return FatPenalty.NO_PENALTY;
		else if(level < 14) return FatPenalty.PLUMP;
		else if(level < 16) return FatPenalty.CHUBBY;
		else if(level < 18) return FatPenalty.FAT;
		else return FatPenalty.OBESE;
	}

	public static HealthPenalty getHealthPenalty(EntityPlayer player) {
		int level = (int) player.getHealth();
		if(level > 10) return HealthPenalty.NO_PENALTY;
		else if(level > 8) return HealthPenalty.HURT;
		else if(level > 6) return HealthPenalty.INJURED;
		else if(level > 4) return HealthPenalty.WOUNDED;
		else if(level > 2) return HealthPenalty.CRIPPLED;
		else return HealthPenalty.DYING;
	}

	public static boolean canJump(EntityPlayer player) {
		return player.getHealth() > 4
				&& player.getFoodStats().getFoodLevel() > 12
				&& player.getFoodStats().getSaturationLevel() < 18.0;
	}

	public static boolean canSwim(EntityPlayer player) {
		return (!isWeighted(player)) && player.getHealth() > 4;
	}
	
	/**
	 * Edit the speed of an entity.
	 * 
	 * @param entity
	 * @param speedModifierUUID
	 *            Unique UUID for modification
	 * @param name
	 *            Unique name for easier debugging
	 * @param modifier
	 *            The speed will be multiplied by this number
	 */
	public static void changeSpeed(EntityLivingBase entity,
			UUID speedModifierUUID, String name, double modifier) {
		AttributeModifier speedModifier = (new AttributeModifier(
				speedModifierUUID, name, modifier - 1, 2));
		IAttributeInstance iattributeinstance = entity
				.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

		if (iattributeinstance.getModifier(speedModifierUUID) != null) {
			iattributeinstance.removeModifier(speedModifier);
		}
		iattributeinstance.applyModifier(speedModifier);
	}
	
	    private static int getWornArmorWeight(EntityPlayer player) {
        int weight = 0;

        for (ItemStack stack : player.inventory.armorInventory){
            if (stack != null)
                weight += ItemExt.getWeight(stack);
        }

        return weight;
    }
    
    public static boolean isWeighted(EntityPlayer player) {
        return getWornArmorWeight(player) >= 10 || hasHeadCrab(player);
    }
    
    public static boolean hasHeadCrab(EntityPlayer player) {
    	return player.isRiding() && player.getRidingEntity() instanceof EntitySquid;
    }
    
    public static float getArmorExhaustionModifier(EntityPlayer player)
    {
        float modifier = 1.0F;
        int weight = getWornArmorWeight(player);

        if (weight > 0)
        {
            modifier += (float) weight / 44.0F;
        }

        return modifier;
    }

	public static boolean isWearingSoulforgedArmor(Entity entity)
    {
    	boolean helmet = false;
    	boolean chestplate = false;
    	boolean boots = false;
    	boolean leggings = false;
   		/*TODO add Items
    	for(ItemStack stack : entity.getEquipmentAndArmor()) {
    		if(stack.getItem() == ModItems.) helmet = true;
    		else if(stack.getItem() == ModItems.) chestplate = true;
    		else if(stack.getItem() == ModItems.) boots = true;
    		else if(stack.getItem() == ModItems.) leggings = true;
    	}
    	*/
    	return helmet&&chestplate&&boots&&leggings;
    }

    public static boolean isWearingSoulforgedHelm(Entity entity)
    {
  		/*TODO add Item
    	for(ItemStack stack : entity.getEquipmentAndArmor()) {
    		if(stack.getItem() == ModItems.) return true;
    	}
    	*/
    	return false;
    }

    public static boolean isWearingSoulforgedBoots(Entity entity)
    {
  		/*TODO add Item
    	for(ItemStack stack : entity.getEquipmentAndArmor()) {
    		if(stack.getItem() == ModItems.) return true;
    	}
    	*/
    	return false;
    }	
}