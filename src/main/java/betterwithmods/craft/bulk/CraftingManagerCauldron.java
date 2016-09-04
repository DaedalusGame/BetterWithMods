package betterwithmods.craft.bulk;

public class CraftingManagerCauldron extends CraftingManagerBulk
{
    private static final CraftingManagerCauldron instance = new CraftingManagerCauldron();

    public CraftingManagerCauldron()
    {
        super("cauldron");
    }

    public static final CraftingManagerCauldron getInstance()
    {
        return instance;
    }
}
