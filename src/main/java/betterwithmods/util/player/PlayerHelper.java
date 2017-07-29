package betterwithmods.util.player;

import betterwithmods.common.BWMBlocks;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.HCArmor;
import betterwithmods.module.hardcore.HCGloom;
import betterwithmods.module.hardcore.HCInjury;
import betterwithmods.module.hardcore.hchunger.HCHunger;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.UUID;

/**
 * Set of methods dealing with EntityPlayer
 *
 * @author Koward
 */
public final class PlayerHelper {
    protected final static UUID penaltySpeedUUID = UUID.fromString("c5595a67-9410-4fb2-826a-bcaf432c6a6f");

    private PlayerHelper() {
    }

    public static boolean isSurvival(EntityPlayer player) {
        return !player.isCreative() && !player.isSpectator() && !player.isSpectator();
    }

    public static float getSpeedModifier(EntityPlayer player) {
        return Math.min(getHealthPenalty(player).getModifier(), Math.min(getFatPenalty(player).getModifier(), getHungerPenalty(player).getModifier()));
    }

    public static GloomPenalty getGloomPenalty(EntityPlayer player) {
        if (!ModuleLoader.isFeatureEnabled(HCGloom.class))
            return GloomPenalty.NO_PENALTY;
        int gloom = HCGloom.getGloomTime(player);
        GloomPenalty penalty = GloomPenalty.NO_PENALTY;
        for (GloomPenalty p : GloomPenalty.VALUES) {
            if (p.isInRange(gloom))
                penalty = p;
        }
        return penalty;
    }

    public static IPlayerPenalty getWorstPenalty(EntityPlayer player) {
        HungerPenalty hungerPenalty = getHungerPenalty(player);
        FatPenalty fatPenalty = getFatPenalty(player);
        int maximumOrdinal = Math.max(hungerPenalty.ordinal(), fatPenalty.ordinal());
        if (maximumOrdinal == hungerPenalty.ordinal()) return hungerPenalty;
        else if (maximumOrdinal == fatPenalty.ordinal()) return fatPenalty;
        else return hungerPenalty;
    }

    public static HungerPenalty getHungerPenalty(EntityPlayer player) {
        if (!ModuleLoader.isFeatureEnabled(HCHunger.class))
            return HungerPenalty.NO_PENALTY;
        int level = player.getFoodStats().getFoodLevel();
        if (level > 24) return HungerPenalty.NO_PENALTY;
        else if (level > 18) return HungerPenalty.PECKISH;
        else if (level > 12) return HungerPenalty.HUNGRY;
        else if (level > 6) return HungerPenalty.FAMISHED;
        else if (level > 0 || player.getFoodStats().getSaturationLevel() > 0) return HungerPenalty.STARVING;
        else return HungerPenalty.DYING;
    }

    public static FatPenalty getFatPenalty(EntityPlayer player) {
        if (!ModuleLoader.isFeatureEnabled(HCHunger.class))
            return FatPenalty.NO_PENALTY;
        int level = (int) player.getFoodStats().getSaturationLevel();
        if (level < 36) return FatPenalty.NO_PENALTY;
        else if (level < 42) return FatPenalty.PLUMP;
        else if (level < 48) return FatPenalty.CHUBBY;
        else if (level < 52) return FatPenalty.FAT;
        else return FatPenalty.OBESE;
    }

    public static HealthPenalty getHealthPenalty(EntityPlayer player) {
        if (!ModuleLoader.isFeatureEnabled(HCInjury.class))
            return HealthPenalty.NO_PENALTY;
        double max = player.getMaxHealth();
        double level = player.getHealth();
        double frac = level / max;

        if (frac > 0.5d) return HealthPenalty.NO_PENALTY;
        else if (frac > 0.4d) return HealthPenalty.HURT;
        else if (frac > 0.3d) return HealthPenalty.INJURED;
        else if (frac > 0.2d) return HealthPenalty.WOUNDED;
        else if (frac > 0.1d) return HealthPenalty.CRIPPLED;
        else return HealthPenalty.DYING;
    }

    public static boolean canJump(EntityPlayer player) {
        return getHungerPenalty(player).canJump() && getHealthPenalty(player).canJump() && getFatPenalty(player).canJump() && getGloomPenalty(player).canJump();
    }

    public static boolean canSwim(EntityPlayer player) {
        return (!isWeighted(player)) && player.getHealth() > 4;
    }

    /**
     * This will at least keep players from sticking to the bottom of a pool.
     *
     * @param player The swimming player.
     * @return Whether the player is near the bottom of the pool or not.
     */
    public static boolean isNearBottom(EntityPlayer player) {
        Block toCheck = player.getEntityWorld().getBlockState(player.getPosition().down()).getBlock();
        Block toCheck2 = player.getEntityWorld().getBlockState(player.getPosition().down(2)).getBlock();
        return !toCheck.isReplaceable(player.getEntityWorld(), player.getPosition().down()) || !toCheck2.isReplaceable(player.getEntityWorld(), player.getPosition().down(2));
    }

    public static void changeAttack(EntityLivingBase entity,
                                    UUID attackModifierUUID, String name, double multiplier) {
        AttributeModifier attackModifier = (new AttributeModifier(
                attackModifierUUID, name, multiplier - 1, 2));
        IAttributeInstance iattributeinstance = entity
                .getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

        if (iattributeinstance.getModifier(attackModifierUUID) != null) {
            iattributeinstance.removeModifier(attackModifier);
        }
        iattributeinstance.applyModifier(attackModifier);
    }

    /**
     * Edit the speed of an entity.
     *
     * @param entity   The entity whose speed will be changed.
     * @param name     Unique name for easier debugging
     * @param modifier The speed will be multiplied by this number
     */
    public static void changeSpeed(EntityLivingBase entity,
                                   String name, double modifier) {
        AttributeModifier speedModifier = (new AttributeModifier(
                penaltySpeedUUID, name, modifier - 1, 2));
        IAttributeInstance iattributeinstance = entity
                .getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (iattributeinstance.getModifier(penaltySpeedUUID) != null) {
            iattributeinstance.removeModifier(speedModifier);
        }
        iattributeinstance.applyModifier(speedModifier);
    }

    private static int getWornArmorWeight(EntityPlayer player) {
        int weight = 0;
        for (ItemStack stack : player.inventory.armorInventory) {
            if (stack != null)
                weight += HCArmor.getWeight(stack);
        }
        return weight;
    }

    private static boolean isWeighted(EntityPlayer player) {
        return getWornArmorWeight(player) >= 10 || hasHeadCrab(player);
    }

    private static boolean hasHeadCrab(EntityPlayer player) {
        return player.isRiding() && player.getRidingEntity() instanceof EntitySquid;
    }

    public static float getGloomExhaustionModifier(EntityPlayer player) {
        if (!ModuleLoader.isFeatureEnabled(HCGloom.class))
            return 1.0f;
        GloomPenalty gloom = getGloomPenalty(player);
        if (gloom != null)
            return gloom.getModifier();
        return 1.0f;
    }

    public static float getArmorExhaustionModifier(EntityPlayer player) {
        float modifier = 1.0F;
        int weight = getWornArmorWeight(player);

        if (weight > 0) {
            modifier += (float) weight / 44.0F;
        }
        return modifier;
    }

    /**
     * This pos-sensitive version should be used when it's available, as it uses {@link IBlockState#getActualState(IBlockAccess, BlockPos)}.
     *
     * @param player
     * @param pos
     * @return
     */
    public static boolean isCurrentToolEffectiveOnBlock(EntityPlayer player, BlockPos pos) {
        ItemStack stack = player.getHeldItemMainhand();
        World world = player.getEntityWorld();
        IBlockState state = world.getBlockState(pos).getActualState(world, pos);
        return isCurrentToolEffectiveOnBlock(stack, state) && ForgeHooks.isToolEffective(player.getEntityWorld(), pos, stack);
    }

    /**
     * Partial copy of {@link ForgeHooks#isToolEffective(IBlockAccess, BlockPos, ItemStack)} build 2185
     *
     * @param stack The tool.
     * @param state The block.
     * @return Whether the tool can harvest well the block.
     */
    public static boolean isCurrentToolEffectiveOnBlock(ItemStack stack, IBlockState state) {
        if (stack == null) return false;
        //Hardcore Stumping
        if (state.getBlock() == BWMBlocks.STUMP) {
            return false;
        }
        if (stack.hasTagCompound()) {
            NBTTagCompound stats = stack.getSubCompound("Stats");
            if (stats != null) {
                return stats.getByte("Broken") != 1;
            }
        }
        for (String type : stack.getItem().getToolClasses(stack)) {
            if (type == "mattock")
                return state.getBlock().isToolEffective("shovel", state) || state.getBlock().isToolEffective("axe", state);
            if (state.getBlock().isToolEffective(type, state))
                return true;
        }
        return false;
    }

    public static ItemStack getPlayerHead(EntityPlayer player) {
        ItemStack head = new ItemStack(Items.SKULL, 1, 3);
        NBTTagCompound name = new NBTTagCompound();
        name.setString("SkullOwner", player.getDisplayNameString());
        head.setTagCompound(name);
        return head;
    }

    public static boolean hasFullSet(EntityPlayer player, Class<? extends ItemArmor> armor) {
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (!armor.isAssignableFrom(stack.getItem().getClass()))
                return false;
        }
        return true;
    }

    public static boolean hasPart(EntityLivingBase living, EntityEquipmentSlot type, Class<? extends ItemArmor> armor) {
        return armor.isAssignableFrom(living.getItemStackFromSlot(type).getItem().getClass());
    }


    public static UUID getUUID(EntityPlayer player) {
        GameProfile profile = player.getGameProfile();
        if (profile != null)
            return profile.getId();
        return player.getUniqueID();
    }
}