package betterwithmods.util.player;

/**
 * Penalty linked to health.
 *
 * @author Koward
 */
public enum HealthPenalty implements IPlayerPenalty {
    NO_PENALTY(1, "", true),
    HURT(1, "Hurt", true),
    INJURED(0.75F, "Injured", true),
    WOUNDED(0.5F, "Wounded", true),
    CRIPPLED(0.25F, "Crippled", false),
    DYING(0.20F, "Dying", false);

    private final float modifier;
    private final String description;
    private final boolean canJump;
    HealthPenalty(float modifier, String description, boolean canJump) {
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
