package betterwithmods.client.container;

import betterwithmods.client.container.anvil.ContainerSteelAnvil;
import betterwithmods.client.container.anvil.GuiSteelAnvil;
import betterwithmods.client.container.bulk.ContainerCookingPot;
import betterwithmods.client.container.bulk.ContainerFilteredHopper;
import betterwithmods.client.container.bulk.ContainerMill;
import betterwithmods.client.container.bulk.GuiCauldron;
import betterwithmods.client.container.bulk.GuiCrucible;
import betterwithmods.client.container.bulk.GuiFilteredHopper;
import betterwithmods.client.container.bulk.GuiMill;
import betterwithmods.client.container.other.ContainerBlockDispenser;
import betterwithmods.client.container.other.ContainerPulley;
import betterwithmods.client.container.other.GuiBlockDispenser;
import betterwithmods.client.container.other.GuiPulley;
import betterwithmods.common.blocks.tile.TileEntityBlockDispenser;
import betterwithmods.common.blocks.tile.TileEntityCauldron;
import betterwithmods.common.blocks.tile.TileEntityCrucible;
import betterwithmods.common.blocks.tile.TileEntityFilteredHopper;
import betterwithmods.common.blocks.tile.TileEntityMill;
import betterwithmods.common.blocks.tile.TileEntityPulley;
import betterwithmods.common.blocks.tile.TileEntitySteelAnvil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class BWGuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == 0) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityPulley)
                return new ContainerPulley(player, (TileEntityPulley) tile);
            if (tile instanceof TileEntityBlockDispenser)
                return new ContainerBlockDispenser(player, (TileEntityBlockDispenser) tile);
            if (tile instanceof TileEntityCrucible)
                return new ContainerCookingPot(player, (TileEntityCrucible) tile);
            if (tile instanceof TileEntityCauldron)
                return new ContainerCookingPot(player, (TileEntityCauldron) tile);
            if (tile instanceof TileEntityMill)
                return new ContainerMill(player, (TileEntityMill) tile);
            if (tile instanceof TileEntityFilteredHopper)
                return new ContainerFilteredHopper(player, (TileEntityFilteredHopper) tile);
            if (tile instanceof TileEntitySteelAnvil)
                return new ContainerSteelAnvil(player.inventory, (TileEntitySteelAnvil) tile);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == 0) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityPulley)
                return new GuiPulley(player, (TileEntityPulley) tile);
            if (tile instanceof TileEntityBlockDispenser)
                return new GuiBlockDispenser(player, (TileEntityBlockDispenser) tile);
            if (tile instanceof TileEntityCrucible)
                return new GuiCrucible(player, (TileEntityCrucible) tile);
            if (tile instanceof TileEntityCauldron)
                return new GuiCauldron(player, (TileEntityCauldron) tile);
            if (tile instanceof TileEntityMill)
                return new GuiMill(player, (TileEntityMill) tile);
            if (tile instanceof TileEntityFilteredHopper)
                return new GuiFilteredHopper(player, (TileEntityFilteredHopper) tile);
            if (tile instanceof TileEntitySteelAnvil)
                return new GuiSteelAnvil((TileEntitySteelAnvil) tile,new ContainerSteelAnvil(player.inventory, (TileEntitySteelAnvil) tile));
        }
        return null;
    }

}
