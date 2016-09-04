package betterwithmods.blocks.tile;

import betterwithmods.util.InvUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class TileEntityBlockDispenser extends TileEntity implements IInventory
{
	private ItemStack[] contents;
	public int nextIndex;
	
	public TileEntityBlockDispenser()
	{
		this.contents = new ItemStack[16];
		this.nextIndex = 0;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public int getSizeInventory() 
	{
		return 16;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		return this.contents[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) 
	{
		return InvUtils.decrStackSize(this, slot, amount);
	}

	public ItemStack getStackInSlotOnClosing(int slot) 
	{
		if(this.contents[slot] != null)
		{
			ItemStack stack = this.contents[slot];
			this.contents[slot] = null;
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) 
	{
		super.markDirty();
		
		this.contents[slot] = stack;
		
		if(stack != null && stack.stackSize > getInventoryStackLimit())
			stack.stackSize = getInventoryStackLimit();
		markDirty();
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return this.worldObj.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) 
	{
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		NBTTagList tagList = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		this.contents = new ItemStack[this.getSizeInventory()];
		for(int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound tag1 = tagList.getCompoundTagAt(i);
			int j = tag1.getByte("Slot") & 0xFF;
			if(j >= 0 && j < this.contents.length)
				this.contents[j] = ItemStack.loadItemStackFromNBT(tag1);
		}
		
		if(tag.hasKey("NextSlot"))
			this.nextIndex = tag.getInteger("NextSlot");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		NBTTagCompound t = super.writeToNBT(tag);
		NBTTagList tagList = new NBTTagList();
		for(int i = 0; i < this.contents.length; i++)
		{
			if(this.contents[i] != null)
			{
				NBTTagCompound tag1 = new NBTTagCompound();
				tag1.setByte("Slot", (byte)i);
				this.contents[i].writeToNBT(tag1);
				tagList.appendTag(tag1);
			}
		}
		
		t.setTag("Items", tagList);
		t.setInteger("NextSlot", this.nextIndex);
		return t;
	}

	public void addStackToInventory(ItemStack stack, BlockPos pos)
	{
		if(stack == null) return;
		for(int i = 0; i < 16; i++)
		{
			ItemStack check = this.contents[i];
			if(ItemStack.areItemsEqual(stack, check) && check.stackSize < check.getMaxStackSize())
			{
				check.stackSize++;
				this.contents[i] = check;
				return;
			}
		}
		int firstSlot = findFirstNullStack();
		if(firstSlot > -1)
		{
			this.contents[firstSlot] = stack;
		}
		else
		{
			EntityItem item = new EntityItem(worldObj, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
			item.setDefaultPickupDelay();
			worldObj.spawnEntityInWorld(item);
		}
	}

	private int findFirstNullStack()
	{
		for(int i = 0; i < 16; i++)
		{
			if(this.contents[i] == null)
				return i;
		}
		return -1;
	}

	public ItemStack getNextStackFromInv()
	{
		ItemStack nextStack;
		
		if(this.nextIndex >= this.contents.length || this.contents[this.nextIndex] == null)
		{
			int slot = findNextValidSlot(this.nextIndex);
			
			if(slot < 0)
				return null;
			
			this.nextIndex = slot;
		}
		
		nextStack = this.contents[this.nextIndex];
		
		int slot = findNextValidSlot(this.nextIndex);
		
		if(slot < 0)
			this.nextIndex = 0;
		else
			this.nextIndex = slot;
		
		return nextStack;
	}
	
	private int findNextValidSlot(int currentSlot)
	{
		for(int slot = currentSlot + 1; slot < this.contents.length; slot++)
		{
			if(this.contents[slot] != null)
				return slot;
		}
		
		for(int slot = 0; slot < currentSlot; slot++)
		{
			if(this.contents[slot] != null)
				return slot;
		}
		
		if(this.contents[currentSlot] != null)
			return currentSlot;
		
		return -1;
	}

	@Override
	public String getName() 
	{
		return "inv.bwm.dispenser.name";
	}

	@Override
	public boolean hasCustomName() 
	{
		return true;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation(getName());
	}

	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return getStackInSlotOnClosing(index);
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public int getField(int id)
	{
		return this.nextIndex;
	}

	@Override
	public void setField(int id, int value) 
	{
		this.nextIndex = value;
	}

	@Override
	public int getFieldCount() 
	{
		return 1;
	}

	@Override
	public void clear() 
	{
		for(int i = 0; i < this.contents.length; i++)
		{
			this.contents[i] = null;
		}
	}
	
}
