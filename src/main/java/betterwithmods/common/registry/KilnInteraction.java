package betterwithmods.common.registry;

public class KilnInteraction extends BlockMetaHandler {
    public static final KilnInteraction INSTANCE = new KilnInteraction();

    private KilnInteraction() {
        super("kiln");
    }
}
