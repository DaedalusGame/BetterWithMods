package betterwithmods.util;

import betterwithmods.util.player.EntityPlayerExt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Food mechanics.
 *
 * @author Koward
 */
public class BWMFoodStats extends FoodStats {
    private static final int EXHAUSTION_WITH_TIME_PERIOD = 600;
    private static final float EXHAUSTION_WITH_TIME_AMOUNT = 0.5F;
    /**
     * Reference to the player to edit exhaustion
     */
    private final EntityPlayer player;
    /**
     * The natural exhaustion rate.
     */
    private int exhaustionTimer = 0;

    public BWMFoodStats(EntityPlayer playerIn) {
        super();
        player = playerIn;
    }

    /**
     * Adds inputs to foodExhaustionLevel to a max of 40
     */
    @Override
    public void addExhaustion(float exhaustion) {
        super.addExhaustion(exhaustion * EntityPlayerExt.getArmorExhaustionModifier(player) * EntityPlayerExt.getGloomExhaustionModifier(player));
    }

    /**
     * Passing time also exhausts the player.
     */
    private void updateExhaustionWithTime(EntityPlayer player) {
        ++this.exhaustionTimer;

        if (this.exhaustionTimer >= EXHAUSTION_WITH_TIME_PERIOD) {
            if (!player.capabilities.disableDamage) {
                this.addExhaustion(EXHAUSTION_WITH_TIME_AMOUNT);
            }

            this.exhaustionTimer = 0;
        }
    }

    /**
     * Should the player burn fat (saturation) instead of hunger (level) ?
     *
     * @return true if food level lower than fat level
     */
    private boolean shouldBurnFat() {
        return this.getSaturationLevel() > (float) ((this.getFoodLevel() + 5) / 6) * 2.0F;
    }

    /**
     * Add food stats.
     */
    @Override
    public void addStats(int foodLevelIn, float foodSaturationModifier) {
        int currentFoodLevel = this.getFoodLevel();
        setFoodLevel(Math.min(foodLevelIn + currentFoodLevel, 60));

        int overWeight = foodLevelIn - (this.getFoodLevel() - currentFoodLevel);
        if (overWeight > 0) {
            setSaturation(
                    Math.min(this.getSaturationLevel() + (float) overWeight * foodSaturationModifier / 3.0F, 20.0F));
        }

    }

    /**
     * Handles the food game logic.
     */
    @Override
    public void onUpdate(EntityPlayer player) {
        EnumDifficulty enumdifficulty = player.getEntityWorld().getDifficulty();
        setPrevFoodLevel(getFoodLevel());

        if (enumdifficulty != EnumDifficulty.PEACEFUL) {
            updateExhaustionWithTime(player);
            while (this.getFoodLevel() > 0 && getExhaustion() >= 1.33F && !this.shouldBurnFat()) {
                setExhaustion(getExhaustion() - 1);
                setFoodLevel(Math.max(this.getFoodLevel() - 1, 0));
            }

            while (getExhaustion() >= 0.5F && this.shouldBurnFat()) {
                setExhaustion(getExhaustion() - 0.5F);
                setSaturation(Math.max(this.getSaturationLevel() - 0.125F, 0.0F));
            }
        } else
            setExhaustion(0.0F);

        if (player.getEntityWorld().getGameRules().getBoolean("naturalRegeneration") && this.getFoodLevel() >= 24
                && player.shouldHeal()) {
            setFoodTimer(getFoodTimer() + 1);

            if (this.getFoodTimer() >= 600) {
                player.heal(1.0F);
                // Healing doesn't add exhaustion anymore
                // this.addExhaustion(3.0F);
                setFoodTimer(0);
            }
        } else if (this.getFoodLevel() <= 0 && this.getSaturationLevel() <= 0.01F) {
            setFoodTimer(getFoodTimer() + 1);

            if (this.getFoodTimer() >= 80) {
                if (enumdifficulty != EnumDifficulty.PEACEFUL) {
                    player.attackEntityFrom(DamageSource.STARVE, 1.0F);
                }

                setFoodTimer(0);
            }
        } else {
            setFoodTimer(0);
        }
    }

    /**
     * Reads the food data for the player.
     */
    @Override
    public void readNBT(NBTTagCompound compound) {
        super.readNBT(compound);
        if (compound.hasKey("foodExhaustionTimer")) {
            this.exhaustionTimer = compound.getInteger("foodExhaustionTimer");
        }
        if (!compound.hasKey("bwmAdjustedFoodStats")) {
            setFoodLevel(getFoodLevel() * 3);
            setSaturation(0);
        }
        if (getFoodLevel() > 60)
            setFoodLevel(60);
        if (getSaturationLevel() > 20)
            setSaturation(20);
    }

    /**
     * Writes the food data for the player.
     */
    @Override
    public void writeNBT(NBTTagCompound compound) {
        super.writeNBT(compound);
        compound.setInteger("foodExhaustionTimer", this.exhaustionTimer);
        compound.setBoolean("bwmAdjustedFoodStats", true);
    }

    /**
     * Get whether the player must eat food.
     */
    @Override
    public boolean needFood() {
        return this.getFoodLevel() < 60.0F;
    }

    public float getExhaustion() {
        return ReflectionHelper.getPrivateValue(FoodStats.class, this, "field_75126_c", "foodExhaustionLevel");
    }

    public void setExhaustion(float exhaustionIn) {
        ReflectionHelper.setPrivateValue(FoodStats.class, this, exhaustionIn, "field_75126_c", "foodExhaustionLevel");
    }

    public int getFoodTimer() {
        return ReflectionHelper.getPrivateValue(FoodStats.class, this, "field_75123_d", "foodTimer");
    }

    public void setFoodTimer(int foodTimerIn) {
        ReflectionHelper.setPrivateValue(FoodStats.class, this, foodTimerIn, "field_75123_d", "foodTimer");
    }

    public void setPrevFoodLevel(int prevFoodLevel) {
        ReflectionHelper.setPrivateValue(FoodStats.class, this, prevFoodLevel, "field_75124_e", "prevFoodLevel");
    }

    private void setSaturation(float saturation) {
        ReflectionHelper.setPrivateValue(FoodStats.class, this, saturation, "field_75125_b", "foodSaturationLevel");
    }
}