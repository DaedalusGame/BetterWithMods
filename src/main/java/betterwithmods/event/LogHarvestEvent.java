package betterwithmods.event;

import betterwithmods.api.block.IDebarkable;
import betterwithmods.config.BWConfig;
import betterwithmods.craft.SawInteraction;
import betterwithmods.util.InvUtils;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import betterwithmods.BWRegistry;
import betterwithmods.craft.HardcoreWoodInteraction;
import betterwithmods.items.ItemKnife;
import betterwithmods.util.FakeCrafter;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class LogHarvestEvent 
{
	public static FakeCrafter craft = new FakeCrafter(3, 3);

	@SubscribeEvent
	public void debarkLog(PlayerInteractEvent.RightClickBlock evt)
	{
		if(!evt.getWorld().isRemote)
		{
			Block block = evt.getWorld().getBlockState(evt.getPos()).getBlock();
			ItemStack playerStack = evt.getEntityPlayer().getHeldItem(evt.getHand());
			BlockPos playerPos = evt.getPos().offset(evt.getFace());
			if(playerStack != null && playerStack.getItem().getHarvestLevel(playerStack, "axe") >= 0)
			{
				if (block == Blocks.LOG)
				{
					IBlockState state = evt.getWorld().getBlockState(evt.getPos());
					IBlockState dbl = BWRegistry.debarkedOld.getDefaultState().withProperty(BlockLog.LOG_AXIS, state.getValue(BlockLog.LOG_AXIS)).withProperty(BlockOldLog.VARIANT, state.getValue(BlockOldLog.VARIANT));
					InvUtils.ejectStackWithOffset(evt.getWorld(), playerPos, ((IDebarkable)dbl.getBlock()).getBark(dbl));
					evt.getWorld().setBlockState(evt.getPos(), dbl);
					evt.getWorld().playSound(null, evt.getPos(), dbl.getBlock().getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
					playerStack.damageItem(1, evt.getEntityPlayer());
				}
				else if(block == Blocks.LOG2)
				{
					IBlockState state = evt.getWorld().getBlockState(evt.getPos());
					IBlockState dbl = BWRegistry.debarkedOld.getDefaultState().withProperty(BlockLog.LOG_AXIS, state.getValue(BlockLog.LOG_AXIS)).withProperty(BlockNewLog.VARIANT, state.getValue(BlockNewLog.VARIANT));
					InvUtils.ejectStackWithOffset(evt.getWorld(), playerPos, ((IDebarkable)dbl.getBlock()).getBark(dbl));
					evt.getWorld().setBlockState(evt.getPos(), dbl);
					evt.getWorld().playSound(null, evt.getPos(), dbl.getBlock().getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
					playerStack.damageItem(1, evt.getEntityPlayer());
				}
			}
		}
	}
	
	@SubscribeEvent
	public void harvestLog(HarvestDropsEvent evt)
	{
		if(!BWConfig.hardcoreLumber || evt.getHarvester() == null)
			return;
		else if(evt.getHarvester() != null) { //Checking invulnerability because checking for instances of FakePlayer doesn't work.
			if(evt.getHarvester().isEntityInvulnerable(DamageSource.cactus))
				return;
		}
		Block block = evt.getState().getBlock();
		int harvestMeta = evt.getState().getBlock().damageDropped(evt.getState());
		if(!evt.getWorld().isRemote && (!evt.isSilkTouching() || evt.getHarvester() != null))
		{
			boolean harvest = false;
			if(evt.getHarvester() != null && evt.getHarvester().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != null)
			{
				Item item = evt.getHarvester().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem();

				if(item != null)
				{
					if((item.getHarvestLevel(evt.getHarvester().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND), "axe") >= 0) && !(item instanceof ItemKnife))
					{
						harvest = true;
					}
				}
			}
			
			if(!harvest)
			{
				int fortune = evt.getHarvester() != null && evt.getHarvester().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != null && evt.getHarvester().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() instanceof ItemKnife ? evt.getFortuneLevel() : 0;
				boolean fort = fortune > 0;
				if(HardcoreWoodInteraction.contains(block, harvestMeta) && !evt.isSilkTouching())
				{
					for(ItemStack logStack : evt.getDrops())
					{
						if(logStack.getItem() instanceof ItemBlock)
						{
							ItemBlock iBlock = (ItemBlock)logStack.getItem();
							if(iBlock.getBlock() == block)
							{
								ItemStack planks = null;
								if(SawInteraction.contains(block, harvestMeta))
								{
									planks = SawInteraction.getProduct(block, harvestMeta).copy();
									planks.stackSize /= 2;
									int plankStack = fort ? planks.stackSize + evt.getWorld().rand.nextInt(2) : planks.stackSize;
									planks.stackSize = plankStack;
									int barkStack = fort ? 1 + evt.getWorld().rand.nextInt(fortune) : 1;
									ItemStack bark = new ItemStack(BWRegistry.bark, barkStack, 0);
									if(HardcoreWoodInteraction.getBarkOverride(block, harvestMeta) != null)
									{
										barkStack = fort ? HardcoreWoodInteraction.getBarkOverride(block, harvestMeta).stackSize + evt.getWorld().rand.nextInt(fortune) : HardcoreWoodInteraction.getBarkOverride(block, harvestMeta).stackSize;
										bark = new ItemStack(HardcoreWoodInteraction.getBarkOverride(block, harvestMeta).getItem(), barkStack, HardcoreWoodInteraction.getBarkOverride(block, harvestMeta).getItemDamage());
									}
									int sawdustStack = fort ? 1 + evt.getWorld().rand.nextInt(fortune) : 1;
									ItemStack sawdust = new ItemStack(BWRegistry.material, sawdustStack, 22);
									evt.getDrops().add(planks);
									evt.getDrops().add(sawdust);
									evt.getDrops().add(bark);
									evt.getDrops().remove(logStack);
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static IRecipe findMatchingRecipe(InventoryCrafting inv, World world)
	{
		for(int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); i++)
		{
			IRecipe recipe = CraftingManager.getInstance().getRecipeList().get(i);
			
			if(recipe.matches(inv, world))
			{
				return recipe;
			}
		}
		return null;
	}
}
