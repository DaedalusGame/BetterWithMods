package betterwithmods.blocks.tile;

import betterwithmods.craft.bulk.CraftingManagerBulk;
import betterwithmods.craft.bulk.CraftingManagerCrucible;
import betterwithmods.craft.bulk.CraftingManagerCrucibleStoked;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

public class TileEntityCrucible extends TileEntityCookingPot
{
	public TileEntityCrucible()
	{
		super();
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		
		this.cookCounter = tag.getInteger("CrucibleCookTime");
		
		if(tag.hasKey("StokedCooldown"))
			this.stokedCooldownCounter = tag.getInteger("StokedCooldown");
		
		if(tag.hasKey("ContainsValidIngredients"))
			this.containsValidIngredients = tag.getBoolean("ContainsValidIngredients");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		NBTTagCompound t = super.writeToNBT(tag);
		t.setInteger("CrucibleCookTime", this.cookCounter);
		t.setInteger("StokedCooldown", this.stokedCooldownCounter);
		t.setBoolean("ContainsValidIngredients", this.containsValidIngredients);
		return t;
	}

	@Override
	public void validateContents() 
	{
		this.containsValidIngredients = false;
		
		if(this.fireIntensity > 0 && this.fireIntensity < 5)
		{
			if(CraftingManagerCrucible.getInstance().getCraftingResult(this) != null)
				this.containsValidIngredients = true;
		}
		else
		{
			if(containsExplosives())
				this.containsValidIngredients = false;//true;
			else if(CraftingManagerCrucibleStoked.getInstance().getCraftingResult(this) != null)
				this.containsValidIngredients = true;
		}
	}

	@Override
	protected CraftingManagerBulk getCraftingManager(boolean stoked)
	{
		if(stoked)
			return CraftingManagerCrucibleStoked.getInstance();
		else
			return CraftingManagerCrucible.getInstance();
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
		return "inv.crucible.name";
	}

	@Override
	public boolean hasCustomName() 
	{
		return true;
	}
	
}
