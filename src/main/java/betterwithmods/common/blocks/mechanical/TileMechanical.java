package betterwithmods.common.blocks.mechanical;

import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.blocks.tile.TileBasic;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by primetoxinz on 7/18/17.
 */
public abstract class TileMechanical extends TileBasic implements IMechanicalPower {

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }
}
