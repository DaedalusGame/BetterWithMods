package betterwithmods.util.player;

/**
 * Penalty linked to hunger (food level).
 *
 * @author Koward
 */
public enum HungerPenalty implements IPlayerPenalty {
    NO_PENALTY(1, "", true),
    PECKISH(1, "Peckish", true),
    HUNGRY(0.75F, "Hungry", true),
    FAMISHED(0.5F, "Famished", false),
    STARVING(0.25F, "Starving", false),
    DYING(0.25F, "Dying", false);

    private final float modifier;
    private final String description;
    private final boolean canJump;
    HungerPenalty(float modifier, String description, boolean canJump) {
        this.modifier = modifier;
        this.description = description;
        this.canJump = canJump;
    }

    @Override
    public float getModifier() {
        return modifier;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean canJump() {
        return canJump;
    }
}
