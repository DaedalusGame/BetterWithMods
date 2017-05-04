package betterwithmods.common.blocks.mini;

import betterwithmods.common.blocks.tile.TileBasic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMultiType extends TileBasic {
    private int type = 0;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        type = tag.getInteger("BlockType");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        t.setInteger("BlockType", type);
        return t;
    }

    public int getCosmeticType() {
        return type;
    }

    public void setCosmeticType(int type) {
        this.type = type;
    }

}
