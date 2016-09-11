package betterwithmods.items;

import betterwithmods.entity.EntityDynamite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemDynamite extends BWMItem
{
	public ItemDynamite()
	{
		super("dynamite");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		int flintIndex = -1;
		
		for(int i = 0; i < player.inventory.mainInventory.length; i++)
		{
			if(player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].getItem() == Items.FLINT_AND_STEEL)
			{
				flintIndex = i;
				break;
			}
		}
		
		if(!world.isRemote)
		{
			boolean lit = false;
			
			if(flintIndex > -1)
			{
				lit = true;
				ItemStack steelStack = player.inventory.getStackInSlot(flintIndex);
				steelStack.damageItem(1, player);
				if(steelStack.stackSize < 1)
					player.inventory.mainInventory[flintIndex] = null;
			}
			
			stack.stackSize--;
			EntityDynamite dynamite = new EntityDynamite(world, player, new ItemStack(this, 1), lit);
			world.spawnEntityInWorld(dynamite);
			
			if(lit)
				world.playSound(null, new BlockPos(dynamite.posX, dynamite.posY, dynamite.posZ), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.AMBIENT, 1.0F, 1.0F);
			else
				world.playSound(null, new BlockPos(dynamite.posX, dynamite.posY, dynamite.posZ), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.AMBIENT, 0.5F, 0.4F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		list.add(I18n.translateToLocal("lore.bwm:dynamite"));
		super.addInformation(stack, player, list, bool);
	}
	
	@Override
	public int getEntityLifespan(ItemStack stack, World world)
	{
		return 6000;
	}
}
