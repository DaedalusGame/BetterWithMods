package betterwithmods.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class InvUtils 
{
	public static List<ItemStack> dustNames = new ArrayList<ItemStack>();
	public static List<ItemStack> ingotNames = new ArrayList<ItemStack>();
	public static List<ItemStack> cropNames = new ArrayList<ItemStack>();

	public static void initOreDictGathering()
	{
		dustNames = getOreNames("dust");
		ingotNames = getOreNames("ingot");
		cropNames = getOreNames("crop");
	}
	
	private static ArrayList<ItemStack> getOreNames(String prefix)
	{
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for(String name : OreDictionary.getOreNames())
		{
			if(!name.startsWith(prefix))
				continue;
			
			if(OreDictionary.doesOreNameExist(name) && OreDictionary.getOres(name).size() > 0)
			{
				list.addAll(OreDictionary.getOres(name));
			}
		}
		return list;
	}

	public static boolean listContains(ItemStack check, List<ItemStack> list)
	{
		if(list != null) {
			if(list.isEmpty()) return false;
			for (ItemStack item : list) {
				if (ItemStack.areItemsEqual(check, item))
					return true;
			}
		}
		return false;
	}
	
	public static void ejectInventoryContents(World world, BlockPos pos, IInventory inv)
	{
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			
			if(stack != null)
			{
				float fX = world.rand.nextFloat() * 0.7F + 0.15F;
				float fY = world.rand.nextFloat() * 0.7F + 0.15F;
				float fZ = world.rand.nextFloat() * 0.7F + 0.15F;
				
				while(stack.stackSize > 0)
				{
					int j = world.rand.nextInt(21) + 10;
					
					if(j > stack.stackSize)
						j = stack.stackSize;
					
					stack.stackSize -= j;
					EntityItem item = new EntityItem(world, pos.getX() + fX, pos.getY() + fY, pos.getZ() + fZ, new ItemStack(stack.getItem(), j, stack.getItemDamage()));
					float f1 = 0.05F;
					
					item.motionX = (float)world.rand.nextGaussian() * f1;
					item.motionY = (float)world.rand.nextGaussian() * f1 + 0.2F;
					item.motionZ = (float)world.rand.nextGaussian() * f1;
					
					copyTags(item.getEntityItem(), stack);
					
					world.spawnEntityInWorld(item);
				}
			}
		}
	}
	
	public static void clearInventory(IInventory inv)
	{
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(stack != null)
				inv.setInventorySlotContents(i, null);
		}
	}
	
	public static void copyTags(ItemStack destStack, ItemStack sourceStack)
	{
		if(sourceStack.hasTagCompound())
			destStack.setTagCompound(((NBTTagCompound)sourceStack.getTagCompound().copy()));
	}
	
	public static ItemStack decrStackSize(IInventory inv, int slot, int amount)
	{
		if(inv.getStackInSlot(slot) != null)
		{
			if(inv.getStackInSlot(slot).stackSize <= amount)
			{
				ItemStack stack = inv.getStackInSlot(slot);
				inv.setInventorySlotContents(slot, null);
				return stack;
			}
			
			ItemStack splitStack = inv.getStackInSlot(slot).splitStack(amount);
			
			if(inv.getStackInSlot(slot).stackSize < 1)
				inv.setInventorySlotContents(slot, null);
			else
				inv.markDirty();
			return splitStack;
		}
		return null;
	}
	
	public static boolean addSingleItemToInv(IInventory inv, Item item, int meta)
	{
		ItemStack stack = new ItemStack(item, 1, meta);
		return addItemStackToInv(inv, stack);
	}
	
	public static boolean addItemStackToInv(IInventory inv, ItemStack stack)
	{
		return addItemStackToInvInSlotRange(inv, stack, 0, inv.getSizeInventory() - 1);
	}
	
	public static boolean addItemStackToInvInSlotRange(IInventory inv, ItemStack stack, int minSlot, int maxSlot)
	{
		if(!stack.isItemDamaged())
		{
			if(attemptToMergeWithExistingStacksInRange(inv, stack, minSlot, maxSlot))
				return true;
		}
		return attemptToPlaceInEmptySlotInRange(inv, stack, minSlot, maxSlot);
	}
	
	private static boolean canStacksMerge(ItemStack source, ItemStack dest, int sizeLimit)
	{
		return dest != null && dest.getItem() == source.getItem() && dest.isStackable() && dest.stackSize < dest.getMaxStackSize() && dest.stackSize < sizeLimit && (!dest.getHasSubtypes() || dest.getItemDamage() == source.getItemDamage()) && ItemStack.areItemStackTagsEqual(dest, source);
	}
	
	private static int findSlotToMergeStackInRange(IInventory inv, ItemStack stack, int minSlot, int maxSlot)
	{
		int stackLimit = inv.getInventoryStackLimit();
		
		for(int i = minSlot; i <= maxSlot; i++)
		{
			ItemStack tempStack = inv.getStackInSlot(i);
			
			if(canStacksMerge(stack, tempStack, stackLimit))
				return i;
		}
		return -1;
	}
	
	private static boolean attemptToMergeWithExistingStacksInRange(IInventory inv, ItemStack stack, int minSlot, int maxSlot)
	{
		int mergeSlot = findSlotToMergeStackInRange(inv, stack, minSlot, maxSlot);
		
		while(mergeSlot >= 0)
		{
			int numItemsToStore = stack.stackSize;
			ItemStack tempStack = inv.getStackInSlot(mergeSlot);
			
			if(numItemsToStore > tempStack.getMaxStackSize() - tempStack.stackSize)
				numItemsToStore = tempStack.getMaxStackSize() - tempStack.stackSize;
			if(numItemsToStore > inv.getInventoryStackLimit() - tempStack.stackSize)
				numItemsToStore = inv.getInventoryStackLimit() - tempStack.stackSize;
			if(numItemsToStore == 0)
				return false;
			
			stack.stackSize -= numItemsToStore;
			tempStack.stackSize += numItemsToStore;
			inv.setInventorySlotContents(mergeSlot, tempStack);
			
			if(stack.stackSize < 1)
				return true;
			
			mergeSlot = findSlotToMergeStackInRange(inv, stack, minSlot, maxSlot);
		}
		return false;
	}
	
	private static boolean attemptToPlaceInEmptySlotInRange(IInventory inv, ItemStack stack, int minSlot, int maxSlot)
	{
		Item item = stack.getItem();
		int meta = stack.getItemDamage();
		int slot = getFirstEmptyStackInRange(inv, minSlot, maxSlot);
		
		while(slot >= 0)
		{
			int numItemsToStore = stack.stackSize;
			
			if(numItemsToStore > inv.getInventoryStackLimit())
				numItemsToStore = inv.getInventoryStackLimit();
			ItemStack newStack = new ItemStack(item, numItemsToStore, meta);
			copyTags(newStack, stack);
			inv.setInventorySlotContents(slot, newStack);
			stack.stackSize -= numItemsToStore;
			if(stack.stackSize <= 0)
				return true;
			
			slot = getFirstEmptyStackInRange(inv, minSlot, maxSlot);
		}
		return false;
	}

	public static int getFirstOccupiedStack(IInventory inv)
	{
		return getFirstOccupiedStackInRange(inv, 0, inv.getSizeInventory() - 1);
	}
	
	public static int getFirstOccupiedStackInRange(IInventory inv, int minSlot, int maxSlot)
	{
		for(int slot = minSlot; slot <= maxSlot; slot++)
		{
			if(inv.getStackInSlot(slot) != null)
				return slot;
		}
		return -1;
	}
	
	public static int getFirstEmptyStackInRange(IInventory inv, int minSlot, int maxSlot)
	{
		for(int slot = minSlot; slot <= maxSlot; slot++)
		{
			if(inv.getStackInSlot(slot) == null)
				return slot;
		}
		return -1;
	}
	
	public static int getOccupiedStacks(IInventory inv)
	{
		return getOccupiedStacks(inv, 0, inv.getSizeInventory() - 1);
	}
	
	public static int getOccupiedStacks(IInventory inv, int min, int max)
	{
		int count = 0;
		for(int i = min; i <= max; i++)
		{
			if(inv.getStackInSlot(i) != null)
				count++;
		}
		return count;
	}

	public static int countItemsInInventory(IInventory inv, Item item)
	{
		return countItemsInInventory(inv, item, 32767);
	}
	
	public static int countItemsInInventory(IInventory inv, Item item, int meta)
	{
		int itemCount = 0;
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			
			if(stack != null)
			{
				if(stack.getItem() == item)
				{
					if(meta == 32767 || stack.getItemDamage() == meta)
					{
						itemCount += inv.getStackInSlot(i).stackSize;
					}
				}
			}
		}
		return itemCount;
	}
	
	public static int countOresInInv(IInventory inv, List<ItemStack> list)
	{
		int ret = 0;
		if(list != null && !list.isEmpty() && list.size() > 0)
			for(int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				if(stack != null)
				{
					for(int o = 0; o < list.size(); o++)
					{
						Item oreItem = list.get(o).getItem();
						int oreMeta = list.get(o).getItemDamage();
						if(OreDictionary.itemMatches(new ItemStack(stack.getItem(), 1, stack.getItemDamage()), new ItemStack(oreItem, 1, oreMeta), false))
						{
							ret += stack.stackSize;
							break;
						}
					}
				}
			}
		return ret;
	}

	public static boolean consumeItemsInInventory(IInventory inv, Item item, int stackSize)
	{
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(stack != null)
			{
				if(stack.getItem() == item)
				{
					if(stack.stackSize >= stackSize)
					{
						decrStackSize(inv, i, stackSize);
						return true;
					}
					stackSize -= stack.stackSize;
					inv.setInventorySlotContents(i, null);
				}
			}
		}
		return false;
	}
	
	public static boolean consumeItemsInInventory(IInventory inv, Item item, int meta, int stackSize)
	{
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			
			if(stack != null)
			{
				if(stack.getItem() == item)
				{
					if(meta == 32767 || stack.getItemDamage() == meta)
					{
						if(stack.stackSize >= stackSize)
						{
							decrStackSize(inv, i, stackSize);
							
							return true;
						}
						
						stackSize -= stack.stackSize;
						
						inv.setInventorySlotContents(i, null);
					}
				}
			}
		}
		return false;
	}
	
	public static boolean consumeOresInInventory(IInventory inv, List<ItemStack> list, int stackSize)
	{
		if(list.size() > 0)
		{
			for(int i = 0; i < list.size(); i++)
			{
				ItemStack tempStack = list.get(i);
				Item item = tempStack.getItem();
				int meta = tempStack.getItemDamage();
				for(int j = 0; j < inv.getSizeInventory(); j++)
				{
					ItemStack stack = inv.getStackInSlot(j);
					if(stack != null)
					{
						if(stack.getItem() == item)
						{
							if(stack.getItemDamage() == meta || meta == 32767)
							{
								if(stack.stackSize >= stackSize)
								{
									decrStackSize(inv, j, stackSize);
									return true;
								}
								stackSize -= stack.stackSize;
								inv.setInventorySlotContents(j, null);
							}
						}
					}
				}
			}
		}
		return false;
	}

	public static int getFirstOccupiedStackOfOre(IInventory inv, List<ItemStack> ores)
	{
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			if(inv.getStackInSlot(i) != null)
			{
				if(listContains(inv.getStackInSlot(i), ores))
					return i;
			}
		}
		return -1;
	}
	
	public static int getFirstOccupiedStackNotOfItem(IInventory inv, Item item)
	{
		return getFirstOccupiedStackNotOfItem(inv, item, 32767);
	}
	
	public static int getFirstOccupiedStackNotOfItem(IInventory inv, Item item, int meta)
	{
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			if(inv.getStackInSlot(i) != null)
			{
				int tempMeta = inv.getStackInSlot(i).getItemDamage();
				if(inv.getStackInSlot(i).getItem() != item && (meta == 32767 || tempMeta != meta))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	public static int getFirstOccupiedStackOfItem(IInventory inv, Item item)
	{
		return getFirstOccupiedStackOfItem(inv, item, 32767);
	}
	
	public static int getFirstOccupiedStackOfItem(IInventory inv, Item item, int meta)
	{
		{
			for(int i = 0; i < inv.getSizeInventory(); i++)
			{
				if(inv.getStackInSlot(i) != null)
				{
					int tempMeta = inv.getStackInSlot(i).getItemDamage();
					if(inv.getStackInSlot(i).getItem() == item && (meta == 32767 || tempMeta == meta))
					{
						return i;
					}
				}
			}
			return -1;
		}
	}
	
	public static void ejectStackWithOffset(World world, BlockPos pos, ItemStack stack)
	{
		float yOff = world.rand.nextFloat() * 0.2F + 0.1F;
		ejectStack(world, pos.getX() + 0.5F, pos.getY() + yOff, pos.getZ() + 0.5F, stack);
	}
	
	public static void ejectStack(World world, double x, double y, double z, ItemStack stack)
	{
		EntityItem item = new EntityItem(world, x, y, z, stack);
		
		float velocity = 0.005F;
		item.motionX = (float)world.rand.nextGaussian() * velocity;
		item.motionY = (float)world.rand.nextGaussian() * velocity + 0.2F;
		item.motionZ = (float)world.rand.nextGaussian() * velocity;
		item.setDefaultPickupDelay();
		world.spawnEntityInWorld(item);
	}

	public static Predicate<EntityLivingBase> GET_VALID_BDISP_ENTITIES = new Predicate<EntityLivingBase>() {
		@Override
		public boolean apply(EntityLivingBase entity)
		{
			return !entity.isDead && (entity instanceof EntityWolf || entity instanceof EntitySheep || entity instanceof EntityChicken);
		}
	};
}
