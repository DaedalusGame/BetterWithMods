package betterwithmods.common.registry.bulk;

public class CraftingManagerCauldronStoked extends CraftingManagerBulk {
    private static final CraftingManagerCauldronStoked instance = new CraftingManagerCauldronStoked();

    public CraftingManagerCauldronStoked() {
        super("cauldronStoked");
    }

    public static CraftingManagerCauldronStoked getInstance() {
        return instance;
    }
}
