package betterwithmods.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.util.math.BlockPos;
import betterwithmods.BWRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

public class BlockRope extends BTWBlock
{
	public static final float width = 0.125F;
	
	public BlockRope()
	{
		super(Material.CIRCUITS, "rope");
		setSoundType(SoundType.PLANT);
		this.setHardness(0.5F);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighbor)
	{
		Block blockAbove = world.getBlockState(pos.up()).getBlock();
		
		boolean supported = true;
		
		if(blockAbove == BWRegistry.anchor)
		{
			EnumFacing facing = ((BlockAnchor)BWRegistry.anchor).getFacing(world, pos.up());
			
			if(facing == EnumFacing.UP)
				supported = false;
		}
		else if(blockAbove != this && ((blockAbove == BWRegistry.singleMachines && world.getBlockState(pos.up()).getValue(BlockMechMachines.MACHINETYPE) == BlockMechMachines.EnumType.PULLEY) || blockAbove != BWRegistry.singleMachines))
			supported = false;
		
		if(!supported)
		{
			dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
		}
	}
	
	public boolean canBlockStay(World world, BlockPos pos)
	{
		Block blockAbove = world.getBlockState(pos.up()).getBlock();
		
		boolean supported = true;
		
		if(blockAbove == BWRegistry.anchor)
		{
			EnumFacing facing = ((BlockAnchor)BWRegistry.anchor).getFacing(world, pos.up());
			
			if(facing == EnumFacing.UP)
				supported = false;
		}
		else if(blockAbove != this && ((blockAbove == BWRegistry.singleMachines && world.getBlockState(pos.up()).getValue(BlockMechMachines.MACHINETYPE) == BlockMechMachines.EnumType.PULLEY) || blockAbove != BWRegistry.singleMachines))
			supported = false;
		
		return supported;
	}
	
	public void placeRopeUnder(ItemStack stack, World world, BlockPos pos, EntityPlayer player)
	{
		if(stack != null)
		{
			BlockPos down = pos.down();
			Block below = world.getBlockState(down).getBlock();
			if(below == BWRegistry.rope)
			{
				((BlockRope)below).placeRopeUnder(stack, world, down, player);
			}
			else if(world.isAirBlock(down) || below.isReplaceable(world, down))
			{
				world.setBlockState(down, BWRegistry.rope.getDefaultState());
				if(!player.capabilities.isCreativeMode)
					stack.stackSize--;
			}
			else
				return;
		}
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return canBlockStay(world, pos);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return new AxisAlignedBB(0.4375F, 0.0F, 0.4375F, 0.5625F, 1.0F, 0.5625F);
	}
	
	@Override
	public EnumFacing getFacing(IBlockAccess world, BlockPos pos)
	{
		return null;
	}
	
	@Override
	public void setFacing(World world, BlockPos pos, EnumFacing facing)
	{
		
	}
	
	@Override
	public EnumFacing getFacingFromBlockState(IBlockState state)
	{
		return null;
	}
	
	@Override
	public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing)
	{
		return state;
	}
	
	@Override
	public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
	{
		return entity instanceof EntityPlayer;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		if(side.ordinal() < 2)
		{
			EnumFacing dir = EnumFacing.getFront(side.ordinal());
			BlockPos offPos = side.ordinal() == 0 ? pos.down() : pos.up();
			return !world.isSideSolid(offPos, dir.getOpposite(), true);
		}
		return true;
	}
}
