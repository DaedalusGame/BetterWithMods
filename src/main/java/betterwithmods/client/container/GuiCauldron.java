package betterwithmods.client.container;

import betterwithmods.blocks.tile.TileEntityCauldron;
import net.minecraft.inventory.IInventory;

public class GuiCauldron extends GuiCookingPot
{
    public GuiCauldron(IInventory inv, TileEntityCauldron cauldron)
    {
        super(inv, cauldron);
    }
}
