package betterwithmods.blocks.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public abstract class TileEntityVisibleInventory extends TileEntityDirectional implements IInventory
{
	public short occupiedSlots;
	public ItemStack[] contents;

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation(getName());
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
	{
		return true;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound tag = getUpdateTag();
		return new SPacketUpdateTileEntity(this.getPos(), getBlockMetadata(), tag);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onDataPacket(NetworkManager mgr, SPacketUpdateTileEntity pkt)
	{
		NBTTagCompound tag = pkt.getNbtCompound();
		readFromNBT(tag);
		IBlockState state = worldObj.getBlockState(this.pos);
		worldObj.notifyBlockUpdate(this.pos, state, state, 3);
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}
	
	public int filledSlots()
	{
		int fill = 0;
		int third = this.getSizeInventory() / 3;
		for(int i = 0; i < this.getSizeInventory(); i++)
		{
			if(contents[i] != null)
				fill++;
		}
		int percent = 0;
		if(fill > 0 && fill < third)
			percent = 1;
		else if(fill >= third && fill < (third * 2))
			percent = 2;
		else if(fill != 0 && fill <= this.getSizeInventory())
			percent = 3;
		return percent;
	}
}
