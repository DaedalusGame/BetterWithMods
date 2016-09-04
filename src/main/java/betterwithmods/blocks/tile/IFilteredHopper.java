package betterwithmods.blocks.tile;

import net.minecraft.tileentity.IHopper;

public interface IFilteredHopper extends IHopper
{
	public int filterType();
}
