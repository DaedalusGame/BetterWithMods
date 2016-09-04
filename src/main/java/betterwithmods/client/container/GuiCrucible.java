package betterwithmods.client.container;

import betterwithmods.blocks.tile.TileEntityCrucible;
import net.minecraft.inventory.IInventory;

public class GuiCrucible extends GuiCookingPot
{
    public GuiCrucible(IInventory inv, TileEntityCrucible crucible)
    {
        super(inv, crucible);
    }
}
