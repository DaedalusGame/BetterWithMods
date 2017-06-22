package betterwithmods.util.player;

/**
 * Penalty linked to health.
 *
 * @author Koward
 */
public enum HealthPenalty implements IPlayerPenalty {
    NO_PENALTY(1, ""),
    HURT(1, "Hurt"),
    INJURED(0.75F, "Injured"),
    WOUNDED(0.5F, "Wounded"),
    CRIPPLED(0.25F, "Crippled"),
    DYING(0.20F, "Dying");

    private final float modifier;
    private final String description;

    HealthPenalty(float modifier, String description) {
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
