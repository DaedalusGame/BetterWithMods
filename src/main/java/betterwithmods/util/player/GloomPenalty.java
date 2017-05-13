package betterwithmods.util.player;

/**
 * Created by tyler on 5/13/17.
 */
public enum GloomPenalty implements IPlayerPenalty {
    NO_PENALTY(1.0f, 0, 0, ""),
    GLOOM(1.5f, 1, 1200, "Gloom"),
    Dread(2f, 1200, 2400, "Dread"),
    TERROR(3f, 2400, 10000000, "Terror");

    private final float modifier;
    private final int timeLower, timeUpper;
    private final String description;
    public static final GloomPenalty[] VALUES = values();

    GloomPenalty(float modifier, int timeLower, int timeUpper, String description) {
        this.modifier = modifier;
        this.timeLower = timeLower;
        this.timeUpper = timeUpper;
        this.description = description;
    }

    public boolean isInRange(int time) {
        return time > timeLower && time <= timeUpper;
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
