package betterwithmods.client.container;

import betterwithmods.blocks.tile.TileEntityBlockDispenser;
import betterwithmods.blocks.tile.TileEntityCauldron;
import betterwithmods.blocks.tile.TileEntityCrucible;
import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import betterwithmods.blocks.tile.TileEntityMill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class BWGuiHandler implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
		BlockPos pos = new BlockPos(x, y, z);
		if(ID == 0)
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileEntityBlockDispenser)
				return new ContainerBlockDispenser(player.inventory, (TileEntityBlockDispenser)tile);
			if(tile instanceof TileEntityCrucible)
				return new ContainerCookingPot(player.inventory, (TileEntityCrucible)tile);
			if(tile instanceof TileEntityCauldron)
				return new ContainerCookingPot(player.inventory, (TileEntityCauldron)tile);
			if(tile instanceof TileEntityMill)
				return new ContainerMill(player.inventory, (TileEntityMill)tile);
			if(tile instanceof TileEntityFilteredHopper)
				return new ContainerFilteredHopper(player.inventory, (TileEntityFilteredHopper)tile);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
		BlockPos pos = new BlockPos(x, y, z);
		if(ID == 0)
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileEntityBlockDispenser)
				return new GuiBlockDispenser(player.inventory, (TileEntityBlockDispenser)tile);
			if(tile instanceof TileEntityCrucible)
				return new GuiCrucible(player.inventory, (TileEntityCrucible)tile);
			if(tile instanceof TileEntityCauldron)
				return new GuiCauldron(player.inventory, (TileEntityCauldron)tile);
			if(tile instanceof TileEntityMill)
				return new GuiMill(player.inventory, (TileEntityMill)tile);
			if(tile instanceof TileEntityFilteredHopper)
				return new GuiFilteredHopper(player.inventory, (TileEntityFilteredHopper)tile);
		}
		return null;
	}

}
