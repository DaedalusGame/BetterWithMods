package betterwithmods.craft.bulk;

public class CraftingManagerCrucible extends CraftingManagerBulk
{
    private static final CraftingManagerCrucible instance = new CraftingManagerCrucible();

    public CraftingManagerCrucible()
    {
        super("crucible");
    }

    public static final CraftingManagerCrucible getInstance()
    {
        return instance;
    }
}
