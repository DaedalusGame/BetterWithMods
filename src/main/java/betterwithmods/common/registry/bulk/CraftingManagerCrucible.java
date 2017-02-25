package betterwithmods.common.registry.bulk;

public class CraftingManagerCrucible extends CraftingManagerBulk {
    private static final CraftingManagerCrucible instance = new CraftingManagerCrucible();

    public CraftingManagerCrucible() {
        super("crucible");
    }

    public static CraftingManagerCrucible getInstance() {
        return instance;
    }
}
