package betterwithmods.blocks.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEntityDirectional extends TileEntity implements ITickable
{
	private int facing;
	
	public void update()
	{
		
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}
	
	public void setFacing(int facing)
	{
		this.facing = facing;
	}
	
	public int getFacing()
	{
		return this.facing;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		this.facing = tag.getByte("facing");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		NBTTagCompound t = super.writeToNBT(tag);
		t.setByte("facing", (byte)this.facing);
		return t;
	}
}
