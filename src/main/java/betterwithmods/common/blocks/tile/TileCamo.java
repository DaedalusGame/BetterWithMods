package betterwithmods.common.blocks.tile;

/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * <p>
 * File Created @ [Jun 7, 2014, 2:21:28 PM (GMT)]
 */


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileCamo extends TileBasic {

    private static final String TAG_CAMO = "camo";
    private static final String TAG_CAMO_META = "camoMeta";

    public IBlockState camoState;

    public void setCamoState(IBlockState camoState) {
        this.camoState = camoState;
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (camoState != null) {
            compound.setString(TAG_CAMO, Block.REGISTRY.getNameForObject(camoState.getBlock()).toString());
            compound.setInteger(TAG_CAMO_META, camoState.getBlock().getMetaFromState(camoState));
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        Block b = Block.getBlockFromName(compound.getString(TAG_CAMO));
        if (b != null) {
            camoState = b.getStateFromMeta(compound.getInteger(TAG_CAMO_META));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
        super.onDataPacket(manager, packet);
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}