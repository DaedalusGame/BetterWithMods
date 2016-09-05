package betterwithmods.blocks.tile;

import betterwithmods.BWRegistry;
import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.craft.bulk.CraftingManagerBulk;
import betterwithmods.craft.heat.BWMHeatRegistry;
import betterwithmods.craft.heat.BWMHeatSource;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;

public abstract class TileEntityCookingPot extends TileEntityVisibleInventory
{
	public int cookCounter;
	public int stokedCooldownCounter;
	public int scaledCookCounter;
	public boolean containsValidIngredients;
	private boolean forceValidation;
	public int fireIntensity;
	
	public TileEntityCookingPot()
	{
		this.cookCounter = 0;
		this.containsValidIngredients = false;
		this.forceValidation = true;
		this.scaledCookCounter = 0;
		this.fireIntensity = -1;
		this.occupiedSlots = 0;
	}


	@Override
	public SimpleItemStackHandler createItemStackHandler() {
		return new SimpleItemStackHandler(this, true, 27);
	}
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		if(tag.hasKey("fireIntensity"))
			this.fireIntensity = tag.getInteger("fireIntensity");
		else
			this.fireIntensity = -1;

		validateInventory();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		NBTTagCompound t = super.writeToNBT(tag);
		t.setInteger("fireIntensity", this.fireIntensity);
		return t;
	}
	
	@Override
	public void update()
	{
		if(this.worldObj.isRemote)
			return;
		
		if(this.worldObj.getBlockState(this.pos).getBlock() instanceof BlockMechMachines)
		{
			BlockMechMachines block = (BlockMechMachines)this.worldObj.getBlockState(this.pos).getBlock();

			if(this.fireIntensity != getFireIntensity()) {
				validateFireIntensity();
				this.forceValidation = true;
			}
			
			if(!block.isMechanicalOn(this.worldObj, this.pos))
			{
				if(this.fireIntensity > 0)
				{
					if(this.forceValidation)
					{
						validateContents();
						this.forceValidation = false;
					}
					
					if(this.fireIntensity > 4)
					{
						if(this.stokedCooldownCounter < 1)
							this.cookCounter = 0;
						this.stokedCooldownCounter = 20;
						
						performStokedFireUpdate(getCurrentFireIntensity());
					}
					else if(this.stokedCooldownCounter > 0)
					{
						this.stokedCooldownCounter -= 1;
						
						if(this.stokedCooldownCounter < 1) {
							this.cookCounter = 0;
						}
					}
					else if(this.stokedCooldownCounter == 0 && this.fireIntensity > 0 && this.fireIntensity < 5)
						performNormalFireUpdate(getCurrentFireIntensity());
				}
				else
					this.cookCounter = 0;
			}
			else
			{
				this.cookCounter = 0;
			}
		}
		validateInventory();
		this.scaledCookCounter = this.cookCounter * 1000 / 4350;
	}


	@Override
	public void markDirty()
	{
		super.markDirty();
		this.forceValidation = true;
		if(this.worldObj != null)
		{
			validateInventory();
		}
	}
	

	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	public abstract void validateContents();
	
	protected abstract CraftingManagerBulk getCraftingManager(boolean stoked);
	
	public int getCurrentFireIntensity()
	{
		int fireFactor = 0;
		
		if(this.fireIntensity > 0)
		{
			for(int x = -1; x <= 1; x++)
			{
				for(int z = -1; z <= 1; z++)
				{
					int xPos = x;
					int yPos = -1;
					int zPos = z;
					BlockPos target = pos.add(xPos, yPos, zPos);
					Block block = this.worldObj.getBlockState(target).getBlock();
					int meta = this.worldObj.getBlockState(target).getBlock().damageDropped(this.worldObj.getBlockState(target));
					if(BWMHeatRegistry.get(block, meta) != null)
						fireFactor += BWMHeatRegistry.get(block, meta).value;
				}
			}
			if(fireFactor < 5)
				fireFactor = 5;
		}
		
		return fireFactor;
	}
	
	private void performNormalFireUpdate(int fireIntensity)
	{
		if(this.containsValidIngredients)
		{
			this.cookCounter += fireIntensity;
			
			if(this.cookCounter >= 4350)
			{
				attemptToCookNormal();
				this.cookCounter = 0;
			}
		}
		else
			this.cookCounter = 0;
	}
	
	private void performStokedFireUpdate(int fireIntensity)
	{
		if(this.containsValidIngredients)
		{
			this.cookCounter += fireIntensity;
			
			if(this.cookCounter >= 4350)
			{
				if(containsExplosives())
					explode();
				else
					attemptToCookStoked();
				this.cookCounter = 0;
			}
		}
		else
			this.cookCounter = 0;
	}
	
	protected boolean containsExplosives()
	{
		return containsItem(BWRegistry.material, 16) || containsItem(Item.getItemFromBlock(Blocks.TNT)) || containsItem(Items.GUNPOWDER) || containsItem(BWRegistry.material, 29);
	}

	private boolean containsItem(Item item)
	{
		return containsItem(item, OreDictionary.WILDCARD_VALUE);
	}

	private boolean containsItem(Item item, int meta)
	{
		return InvUtils.getFirstOccupiedStackOfItem(inventory, item, meta) > -1;
	}
	
	private void explode()
	{
		int hellfire = InvUtils.countItemsInInventory(inventory, BWRegistry.material, 16);
		float expSize = hellfire * 10.0F / 64.0F;
		expSize += InvUtils.countItemsInInventory(inventory, Items.GUNPOWDER) * 10.0F / 64.0F;
		expSize += InvUtils.countItemsInInventory(inventory, BWRegistry.material, 29) * 10.0F / 64.0F;
		if(InvUtils.countItemsInInventory(inventory, Item.getItemFromBlock(Blocks.TNT)) > 0)
		{
			if(expSize < 4.0F)
				expSize = 4.0F;
			expSize += InvUtils.countItemsInInventory(inventory, Item.getItemFromBlock(Blocks.TNT));
		}

		if(expSize < 2.0F)
			expSize = 2.0F;
		else if(expSize > 10.0F)
			expSize = 10.0F;

		InvUtils.clearInventory(inventory);
		worldObj.setBlockToAir(pos);
		worldObj.createExplosion(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, expSize, true);
	}
	
	protected boolean attemptToCookNormal()
	{
		return attemptToCookWithManager(getCraftingManager(false));
	}
	
	protected boolean attemptToCookStoked()
	{
		return attemptToCookWithManager(getCraftingManager(true));
	}
	
	private boolean attemptToCookWithManager(CraftingManagerBulk man)
	{
		if(man != null)
		{
			if(man.getCraftingResult(inventory) != null)
			{
				ItemStack[] output = man.craftItem(inventory);

				if(output != null) {
					for (ItemStack out : output) {
						if (out != null) {
							ItemStack stack = out.copy();
							if (!InvUtils.addItemStackToInv(inventory, stack))
								InvUtils.ejectStackWithOffset(this.worldObj, this.pos.up(), stack);
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public int getCookingProgressScaled(int scale)
	{
		return this.scaledCookCounter * scale / 1000;
	}
	
	public boolean isCooking()
	{
		return this.scaledCookCounter > 0;
	}
	
	private boolean validateInventory()
	{
		boolean stateChanged = false;
		short currentSlots = (short)InvUtils.getOccupiedStacks(inventory);
		if(currentSlots != this.occupiedSlots)
		{
			this.occupiedSlots = currentSlots;
			stateChanged = true;
		}
		if(worldObj != null && stateChanged)
		{
			IBlockState state = worldObj.getBlockState(pos);
			worldObj.notifyBlockUpdate(pos, state, state, 3);
		}
		return stateChanged;
	}
	
	public int getFireIntensity()
	{
		BlockPos down = pos.down();
		Block block = this.worldObj.getBlockState(down).getBlock();
		int meta = block.damageDropped(this.worldObj.getBlockState(down));
		BWMHeatSource source = BWMHeatRegistry.get(block, meta);
		if(source != null)
			return source.value;
		return -1;
	}
	
	private void validateFireIntensity()
	{
		BlockPos down = pos.down();
		Block block = this.worldObj.getBlockState(down).getBlock();
		int meta = block.damageDropped(this.worldObj.getBlockState(down));
		BWMHeatSource source = BWMHeatRegistry.get(block, meta);
		if(source != null)
			fireIntensity = source.value;
		else
			fireIntensity = -1;
	}

	@Override
	public int getMaxVisibleSlots()
	{
		return 27;
	}
}
