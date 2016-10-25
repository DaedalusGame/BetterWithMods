package betterwithmods.util.player;

/**
 * Penalty linked to hunger (food level).
 *
 * @author Koward
 */
public enum HungerPenalty implements IPlayerPenalty {
    NO_PENALTY(1, ""),
    PECKISH(1, "Peckish"),
    HUNGRY(0.75F, "Hungry"),
    FAMISHED(0.5F, "Famished"),
    STARVING(0.25F, "Starving"),
    DYING(0.25F, "Dying");

    private final float modifier;
    private final String description;

    HungerPenalty(float modifier, String description) {
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
