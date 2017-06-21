package betterwithmods.util.player;

/**
 * Penalty linked to fat (saturation).
 *
 * @author Koward
 */
public enum FatPenalty implements IPlayerPenalty {
    NO_PENALTY(1, "", true),
    PLUMP(0.9f, "Plump", true),
    CHUBBY(0.75F, "Chubby", true),
    FAT(0.5F, "Fat", true),
    OBESE(0.25F, "Obese", false);

    private final float modifier;
    private final String description;
    private final boolean canJump;

    FatPenalty(float modifier, String description, boolean canJump) {
        this.modifier = modifier;
        this.description = description;
        this.canJump = canJump;
    }

    public boolean canJump() {
        return canJump;
    }

    @Override
    public float getModifier() {
        return modifier;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
