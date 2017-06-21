package betterwithmods.common.blocks.tile;

import betterwithmods.util.InvUtils;
import betterwithmods.util.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Created by tyler on 5/17/17.
 */
public class TileStake extends TileBasicInventory {

    public static Optional<TileStake> getStake(@Nonnull World world, @Nonnull BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        return Optional.ofNullable(tile instanceof TileStake ? (TileStake) tile : null);
    }

    public BlockPos[] connections = new BlockPos[getInventorySize()];

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        connectStake(worldIn, pos, side.getOpposite(), playerIn);
        return true;
    }

    public int getInventorySize() {
        return 6;
    }

    public BlockPos findStake(World world, BlockPos first, EnumFacing direction) {
        BlockPos second = first;
        int i = 0;
        while (i < 64) {
            second = second.offset(direction);
            if (getStake(world, second).isPresent()) {
                return second;
            }
            i++;
        }
        return null;
    }

    public boolean isConnected(EnumFacing facing) {
        return connections[facing.getIndex()] != null;
    }

    public ItemStack getItemFromDistance(BlockPos pos1, BlockPos pos2) {
        return new ItemStack(Items.STRING, WorldUtils.getDistance(pos1, pos2));
    }

    public void setConnected(EnumFacing facing, BlockPos pos) {
        this.connections[facing.getIndex()] = pos;
        if (pos == null) {
            inventory.setStackInSlot(facing.getIndex(), ItemStack.EMPTY);
        } else {
            inventory.setStackInSlot(facing.getIndex(), getItemFromDistance(getPos(), pos));
        }
        markDirty();
    }

    public boolean connectStake(World world, BlockPos first, EnumFacing direction, EntityPlayer player) {
        BlockPos second = findStake(world, first, direction);
        if (first == null || second == null)
            return false;
        ItemStack string = getItemFromDistance(first, second);

        TileStake firstTile = getStake(world, first).orElse(null), secondTile = getStake(world, second).orElse(null);
        if (firstTile == null || secondTile == null)
            return false;
        if (!firstTile.isConnected(direction) && !secondTile.isConnected(direction)) {
            if (!InvUtils.usePlayerItemStrict(player, EnumFacing.UP, string, string.getCount()))
                return false;
            firstTile.setConnected(direction, second);
            secondTile.setConnected(direction.getOpposite(), first);
        }
        return false;
    }

    @Override
    public void onBreak() {
        for (int i = 0; i < connections.length; i++) {
            BlockPos second = connections[i];
            if (second != null) {
                TileStake stake = getStake(world, second).orElse(null);
                if (stake != null) {
                    stake.setConnected(EnumFacing.getFront(i).getOpposite(), null);
                }
            }
        }
        super.onBreak();
    }

    @Override
    public void validateContents() {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (connections[facing.getIndex()] != null)
                tag.setLong(getKey(facing), connections[facing.getIndex()].toLong());
        }
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (compound.hasKey(getKey(facing)))
                connections[facing.getIndex()] = BlockPos.fromLong(compound.getLong(getKey(facing)));
        }
        super.readFromNBT(compound);
    }

    public String getKey(EnumFacing facing) {
        return "connected" + facing.name();
    }
}
