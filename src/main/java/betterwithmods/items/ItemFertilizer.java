package betterwithmods.items;

public class ItemFertilizer extends BWMItem
{
    public ItemFertilizer()
    {
        super("fertilizer");
    }

    @Override
    public String getLocation(int meta) {
        return "betterwithmods:fertilizer";
    }
}
