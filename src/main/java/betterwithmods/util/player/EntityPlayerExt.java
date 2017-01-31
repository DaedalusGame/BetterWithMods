package betterwithmods.util.player;

import betterwithmods.BWMBlocks;
import betterwithmods.util.item.ItemExt;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.UUID;

/**
 * Set of methods dealing with EntityPlayer
 *
 * @author Koward
 */
public final class EntityPlayerExt {
    private EntityPlayerExt() {
    }

    public static boolean isSurvival(EntityPlayer player) {
        boolean isAdventure = player.getEntityWorld().getWorldInfo().getGameType() == GameType.ADVENTURE;
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
        if (maximumOrdinal == healthPenalty.ordinal()) return healthPenalty;
        else if (maximumOrdinal == hungerPenalty.ordinal()) return hungerPenalty;
        else if (maximumOrdinal == fatPenalty.ordinal()) return fatPenalty;
        else return healthPenalty;
    }

    public static HungerPenalty getHungerPenalty(EntityPlayer player) {
        int level = player.getFoodStats().getFoodLevel();
        if (level > 24) return HungerPenalty.NO_PENALTY;
        else if (level > 18) return HungerPenalty.PECKISH;
        else if (level > 12) return HungerPenalty.HUNGRY;
        else if (level > 6) return HungerPenalty.FAMISHED;
        else if (level > 0 || player.getFoodStats().getSaturationLevel() > 0) return HungerPenalty.STARVING;
        else return HungerPenalty.DYING;
    }

    private static FatPenalty getFatPenalty(EntityPlayer player) {
        int level = (int) player.getFoodStats().getSaturationLevel();
        if (level < 12) return FatPenalty.NO_PENALTY;
        else if (level < 14) return FatPenalty.PLUMP;
        else if (level < 16) return FatPenalty.CHUBBY;
        else if (level < 18) return FatPenalty.FAT;
        else return FatPenalty.OBESE;
    }

    private static HealthPenalty getHealthPenalty(EntityPlayer player) {
        int level = (int) player.getHealth();
        if (level > 10) return HealthPenalty.NO_PENALTY;
        else if (level > 8) return HealthPenalty.HURT;
        else if (level > 6) return HealthPenalty.INJURED;
        else if (level > 4) return HealthPenalty.WOUNDED;
        else if (level > 2) return HealthPenalty.CRIPPLED;
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

    /**
     * Edit the speed of an entity.
     *
     * @param entity            The entity whose speed will be changed.
     * @param speedModifierUUID Unique UUID for modification
     * @param name              Unique name for easier debugging
     * @param modifier          The speed will be multiplied by this number
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

        for (ItemStack stack : player.inventory.armorInventory) {
            if (stack != null)
                weight += ItemExt.getWeight(stack);
        }

        return weight;
    }

    private static boolean isWeighted(EntityPlayer player) {
        return getWornArmorWeight(player) >= 10 || hasHeadCrab(player);
    }

    private static boolean hasHeadCrab(EntityPlayer player) {
        return player.isRiding() && player.getRidingEntity() instanceof EntitySquid;
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

        for (String type : stack.getItem().getToolClasses(stack)) {
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
}