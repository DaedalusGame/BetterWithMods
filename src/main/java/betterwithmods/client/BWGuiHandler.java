package betterwithmods.client;

import betterwithmods.client.container.anvil.ContainerSteelAnvil;
import betterwithmods.client.container.anvil.GuiSteelAnvil;
import betterwithmods.client.container.bulk.*;
import betterwithmods.client.container.other.ContainerBlockDispenser;
import betterwithmods.client.container.other.ContainerPulley;
import betterwithmods.client.container.other.GuiBlockDispenser;
import betterwithmods.client.container.other.GuiPulley;
import betterwithmods.client.gui.GuiManual;
import betterwithmods.common.blocks.tile.*;
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
        switch (Gui.VALUES[ID]) {
            case TILE:
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
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        switch (Gui.VALUES[ID]) {
            case TILE:
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
                    return new GuiSteelAnvil((TileEntitySteelAnvil) tile, new ContainerSteelAnvil(player.inventory, (TileEntitySteelAnvil) tile));
            case MANUAL:
                return new GuiManual();
            default:
                return null;
        }
    }

    public enum Gui {
        TILE,
        MANUAL;

        public static Gui[] VALUES = values();
    }

}
