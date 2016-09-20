package betterwithmods.items;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMod;
import betterwithmods.blocks.BlockAxle;
import betterwithmods.blocks.BlockWaterwheel;
import betterwithmods.blocks.BlockWindmill;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMechanical extends Item implements IBWMItem
{
	public String[] names = {"Windmill", "Waterwheel", "Windmill_Vertical"};
	public ItemMechanical()
	{
		super();
    	this.setCreativeTab(BWCreativeTabs.BWTAB);
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
	}

    @Override
    public int getMaxMeta() {
        return names.length;
    }

    @Override
    public String getLocation(int meta) {
        return BWMod.MODID + ":"+names[meta].toLowerCase();
    }

    @Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		Block block = world.getBlockState(pos).getBlock();
		
		if(block == BWMBlocks.axle)
		{
			int axis = ((BlockAxle)block).getAxisAlignment(world, pos);
			
			if(axis == 0 && stack.getItemDamage() == 2)
			{
				if(isVerticalWindmillValid(player, world, pos, hitY))
					stack.stackSize -= 1;
			}
			else if(axis != 0 && stack.getItemDamage() != 2)
			{
				if(isHorizontalDeviceValid(player, world, pos, stack.getItemDamage(), axis))
					stack.stackSize -= 1;
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
	
	private boolean isHorizontalDeviceValid(EntityPlayer player, World world, BlockPos pos, int meta, int axis)
	{
		boolean valid = false;
		if(meta == 1 && validateWaterwheel(world, pos, axis))
		{
			if(axis > 0)
			{
				world.setBlockState(pos, ((BlockWaterwheel)BWMBlocks.waterwheel).getWaterwheelState(axis));
				valid = true;
			}
		}
		else if(meta == 1)
		{
			if(world.isRemote)
				player.addChatMessage(new TextComponentString("Not enough room to place the waterwheel. Need a 5x5 area to work."));
		}
		if(meta == 0 && validateWindmill(world, pos, axis))
		{
			if(axis > 0)
			{
				world.setBlockState(pos, ((BlockWindmill)BWMBlocks.windmillBlock).getWindmillState(axis));
				valid = true;
			}
		}
		else if(meta == 0)
		{
			if(world.isRemote)
				player.addChatMessage(new TextComponentString("Not enough room to place the windmill. Need a 13x13 area to work."));
		}
		return valid;
	}
	
	public boolean validateWaterwheel(World world, BlockPos pos, int axis)
	{
		return validateHorizontal(world, pos, 2, axis, true);
	}
	
	public boolean validateWindmill(World world, BlockPos pos, int axis)
	{
		return validateHorizontal(world, pos, 6, axis, false);
	}
	
	public boolean validateHorizontal(World world, BlockPos pos, int radius, int axis, boolean isWheel)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		boolean valid = true;
		//if(meta == 0)
		{
			for(int vert = -radius; vert <= radius; vert++)
			{
				for(int i = -radius; i <= radius; i++)
				{
					int xP = axis == 1 ? i : 0;
					int zP = axis == 2 ? i : 0;
					int xPos = x + xP;
					int yPos = y + vert;
					int zPos = z + zP;
					BlockPos checkPos = new BlockPos(xPos, yPos, zPos);
					
					if(yPos == y - radius && isWheel)
					{
						valid = world.isAirBlock(checkPos) || world.isMaterialInBB(new AxisAlignedBB(xPos, yPos, zPos, xPos + 1, yPos + 1, zPos + 1), Material.WATER);
					}
					else if(xP == 0 && yPos == y && zP == 0)
						continue;
					else
					{
						if(isWheel && (xP == -radius || xP == radius || zP == -radius || zP == radius))
							valid = world.isAirBlock(checkPos) || world.isMaterialInBB(new AxisAlignedBB(xPos, yPos, zPos, xPos + 1, yPos + 1, zPos + 1), Material.WATER);
						else
							valid = world.isAirBlock(checkPos);
					}
					if(!valid)
						break;
				}
				if(!valid)
					break;
			}
		}
		return valid;
	}
	
	private boolean isVerticalWindmillValid(EntityPlayer player, World world, BlockPos pos, float flY)
	{
		boolean valid = false;
		int yPos = 0;
		if(flY > 0.5F)
			yPos += 3;
		else
			yPos -= 3;
		BlockPos target = new BlockPos(pos.getX(), pos.getY() + yPos, pos.getZ());
		if(checkForSupportingAxles(world, target))
		{
			if(validateArea(player, world, target))
			{
				world.setBlockState(target, BWMBlocks.windmillBlock.getDefaultState());//BlockPowerSource.setProxies(world, x, yPos, z);
				valid = true;
			}
			else if(world.isRemote)
				player.addChatMessage(new TextComponentString("Not enough room to place the windmill. Need a 9x9 area with a height of seven to work."));
		}
		else
		{
			if(world.isRemote)
			{
				player.addChatMessage(new TextComponentString("Too few vertical axles in column to place here. (Need seven)"));
			}
		}
		return valid;
	}
	
	public boolean validateArea(EntityPlayer player, World world, BlockPos pos)
	{
		boolean clear = true;
		for(int yP = -4; yP < 5; yP++)
		{
			for(int xP = -4; xP < 5; xP++)
			{
				for(int zP = -4; zP < 5; zP++)
				{
					int x = pos.getX(); int y = pos.getY(); int z = pos.getZ();
					int xPos = xP;
					int yPos = yP;
					int zPos = zP;
					BlockPos target = new BlockPos(x + xPos, y + yPos, z + zPos);
					if(x + xPos != x && z + zPos != z && yP != -4 && yP != 4)
						clear = world.isAirBlock(target);
					if((yP == -4 || yP == 4) && (xP == 0 && zP == 0))
					{
						clear = !(world.getBlockState(target).getBlock() instanceof BlockWindmill);
					}
					if(!clear)
					{
						if(world.isRemote)
							player.addChatMessage(new TextComponentString("Blockage at x:" + (x + xPos) + " y:" + (y + yPos) + " z:" + (z + zPos)));
						break;
					}
				}
				if(!clear)
					break;
			}
			if(!clear)
				break;
		}
		for(int i = -8; i < 8; i++)
		{
			for(int j = -8; j < 7; j++)
			{
				for(int k = -8; k < 8; k++)
				{
					if(!clear)
						break;
					int xPos = i;
					int yPos = j;
					int zPos = k;
					BlockPos target = pos.add(xPos, yPos, zPos);
					if(i > -5 && i < 5 && k > -5 && k < 5)
					{
						continue;
					}
					else if(world.getBlockState(target).getBlock() instanceof BlockWindmill || world.getBlockState(target).getBlock() instanceof BlockWaterwheel)
						clear = false;
					if(!clear)
						break;
				}
				if(!clear)
					break;
			}
			if(!clear)
				break;
		}
		for(int i = 5; i < 8; i++)
		{
			int yMin = -i;
			int yMax = i;
			BlockPos minPos = pos.add(0, yMin, 0);
			BlockPos maxPos = pos.add(0, yMax, 0);
			if(!clear)
				break;
			if(world.getBlockState(minPos).getBlock() instanceof BlockWindmill)
				clear = false;
			if(!clear)
				break;
			if(world.getBlockState(maxPos).getBlock() instanceof BlockWindmill)
				clear = false;
			if(!clear)
				break;
		}
		return clear;
	}
	
	private boolean checkForSupportingAxles(World world, BlockPos pos)
	{
		for(int i = -3; i <= 3; i++)
		{
			int yPos = i;
			BlockPos target = pos.add(0, yPos, 0);
			Block targetBlock = world.getBlockState(target).getBlock();
			if(targetBlock == BWMBlocks.axle)
			{
				int axis = ((BlockAxle)targetBlock).getAxisAlignment(world, target);
				if(axis != 0)
					return false;
			}
			else
				return false;
		}
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for(int i = 0; i < 3; i++)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + names[stack.getItemDamage()];
	}
}
