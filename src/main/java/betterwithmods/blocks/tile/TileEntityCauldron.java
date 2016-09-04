package betterwithmods.blocks.tile;

import betterwithmods.BWRegistry;
import betterwithmods.craft.bulk.CraftingManagerBulk;
import betterwithmods.craft.bulk.CraftingManagerCauldron;
import betterwithmods.craft.bulk.CraftingManagerCauldronStoked;
import betterwithmods.util.InvUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

public class TileEntityCauldron extends TileEntityCookingPot
{
	public TileEntityCauldron()
	{
		super();
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		
		if(tag.hasKey("CauldronCookTime"))
			this.cookCounter = tag.getInteger("CauldronCookTime");
		if(tag.hasKey("RenderCooldown"))
			this.stokedCooldownCounter = tag.getInteger("RenderCooldown");
		if(tag.hasKey("ContainsValidIngredients"))
			this.containsValidIngredients = tag.getBoolean("ContainsValidIngredients");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		NBTTagCompound t = super.writeToNBT(tag);
		t.setInteger("CauldronCookTime", this.cookCounter);
		t.setInteger("RenderCooldown", this.stokedCooldownCounter);
		t.setBoolean("ContainsValidIngredients", this.containsValidIngredients);
		return t;
	}

	@Override
	public void validateContents() 
	{
		this.containsValidIngredients = false;
		
		if(this.fireIntensity > 0 && this.fireIntensity < 5)
		{
			if(InvUtils.getFirstOccupiedStackOfItem(this, BWRegistry.material, 5) > -1 && hasNonFoulFood())
			{
				this.containsValidIngredients = true;
			}
			else if(CraftingManagerCauldron.getInstance().getCraftingResult(this) != null)
				this.containsValidIngredients = true;
		}
		else if(this.fireIntensity > 5)
		{
			if(containsExplosives())
				this.containsValidIngredients = true;
			else if(CraftingManagerCauldronStoked.getInstance().getCraftingResult(this) != null)
				this.containsValidIngredients = true;
		}
	}

	@Override
	protected CraftingManagerBulk getCraftingManager(boolean stoked)
	{
		if(stoked)
			return CraftingManagerCauldronStoked.getInstance();
		else
			return CraftingManagerCauldron.getInstance();
	}
	
	@Override
	protected boolean attemptToCookNormal()
	{
		int dung = InvUtils.getFirstOccupiedStackOfItem(this, BWRegistry.material, 5);
		if(dung > -1 && this.hasNonFoulFood())
		{
			return spoilFood();
		}
		else
			return super.attemptToCookNormal();
	}
	
	private boolean hasNonFoulFood()
	{
		for(int i = 0; i < 27; i++)
		{
			if(this.contents[i] != null)
			{
				Item item = this.contents[i].getItem();
				if(item != null)
				{
					if(item instanceof ItemFood)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean spoilFood()
	{
		boolean foodSpoiled = false;
		for(int i = 0; i < 27; i++)
		{
			if(this.contents[i] != null)
			{
				Item item = this.contents[i].getItem();
				if(item != null)
				{
					if(item != BWRegistry.fertilizer && item instanceof ItemFood)
					{
						int stackSize = this.contents[i].stackSize;
						ItemStack spoiled = new ItemStack(BWRegistry.fertilizer, stackSize);
						this.setInventorySlotContents(i, spoiled);
						foodSpoiled = true;
					}
				}
			}
		}
		return foodSpoiled;
	}

	@Override
	public void openInventory(EntityPlayer player) 
	{
		
	}

	@Override
	public void closeInventory(EntityPlayer player) 
	{
		
	}

	@Override
	public int getField(int id) 
	{
		return 0;
	}

	@Override
	public void setField(int id, int value) 
	{
		
	}

	@Override
	public int getFieldCount() 
	{
		return 0;
	}

	@Override
	public void clear() 
	{
		for(int i = 0; i < this.contents.length; i++)
		{
			this.contents[i] = null;
		}
	}

	@Override
	public String getName() 
	{
		return "inv.cauldron.name";
	}

	@Override
	public boolean hasCustomName() 
	{
		return true;
	}
	
}
