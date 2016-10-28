package betterwithmods.util.player;

/**
 * Penalty linked to fat (saturation).
 *
 * @author Koward
 */
public enum FatPenalty implements IPlayerPenalty {
    NO_PENALTY(1, ""),
    PLUMP(1, "Plump"),
    CHUBBY(0.75F, "Chubby"),
    FAT(0.5F, "Fat"),
    OBESE(0.25F, "Obese");

    private final float modifier;
    private final String description;

    FatPenalty(float modifier, String description) {
        this.modifier = modifier;
        this.description = description;
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
