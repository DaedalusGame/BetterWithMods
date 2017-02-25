package betterwithmods.common.registry.bulk;

public class CraftingManagerCauldron extends CraftingManagerBulk {
    private static final CraftingManagerCauldron instance = new CraftingManagerCauldron();

    public CraftingManagerCauldron() {
        super("cauldron");
    }

    public static CraftingManagerCauldron getInstance() {
        return instance;
    }
}
