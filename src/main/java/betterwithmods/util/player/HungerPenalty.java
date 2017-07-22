package betterwithmods.util.player;

/**
 * Penalty linked to hunger (food level).
 *
 * @author Koward
 */
public enum HungerPenalty implements IPlayerPenalty {
    NO_PENALTY(1, "", true, true),
    PECKISH(0.75F, "Peckish", true, false),
    HUNGRY(0.75F, "Hungry", true, false),
    FAMISHED(0.5F, "Famished", false, false),
    STARVING(0.25F, "Starving", false, false),
    DYING(0.25F, "Starving", false, false);

    private final float modifier;
    private final String description;
    private final boolean canJump;
    private final boolean canSprint;
    HungerPenalty(float modifier, String description, boolean canJump, boolean canSprint) {
        this.modifier = modifier;
        this.description = description;
        this.canJump = canJump;
        this.canSprint = canSprint;
    }


    @Override
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

    public boolean canSprint() {
        return canSprint;
    }
}
