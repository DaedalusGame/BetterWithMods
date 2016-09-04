package betterwithmods.blocks;

import java.util.Random;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import betterwithmods.BWRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

public class BlockHibachi extends BTWBlock
{
	public static final PropertyBool LIT = PropertyBool.create("lit");
	
	public BlockHibachi()
	{
		super(Material.ROCK, "hibachi");
		this.setTickRandomly(true);
		this.setHardness(3.5F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(LIT, false));
	}
	
	@Override
	public int tickRate(World world)
	{
		return 4;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		boolean powered = world.isBlockIndirectlyGettingPowered(pos) > 0;
		
		if(powered)
		{
			if(!isLit(world, pos))
				ignite(world, pos);
			else
			{
				Block above = world.getBlockState(pos.up()).getBlock();
				if(above != Blocks.FIRE && above != BWRegistry.stokedFlame)
				{
					if(shouldIgnite(world, pos.up()))
					{
						world.playSound(null, pos, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
						world.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
					}
				}
			}
		}
		else if(isLit(world, pos))
			extinguish(world, pos);
		else
		{
			Block above = world.getBlockState(pos.up()).getBlock();
			
			if(above == Blocks.FIRE || above == BWRegistry.stokedFlame)
				world.setBlockToAir(pos);
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		if(!isCurrentlyValid(world, pos))
			world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
	}
	
	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing side)
	{
		return world.getBlockState(pos).getValue(LIT).booleanValue() && side == EnumFacing.UP;
	}
	
	public boolean isCurrentlyValid(World world, BlockPos pos)
	{
		boolean powered = world.isBlockIndirectlyGettingPowered(pos) > 0;
		if(isLit(world, pos) != powered)
			return false;
		if(isLit(world, pos))
		{
			Block block = world.getBlockState(pos).getBlock();
			
			if(block != Blocks.FIRE && block != BWRegistry.stokedFlame)
			{
				if(shouldIgnite(world, pos.up()))
					return false;
			}
		}
		return true;
	}
	
	public boolean isLit(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(LIT).booleanValue();
	}
	
	private boolean shouldIgnite(World world, BlockPos pos)
	{
		if(world.isAirBlock(pos))
			return true;
		if(world.getBlockState(pos).getBlock().isFlammable(world, pos, EnumFacing.DOWN))
			return true;
		Block block = world.getBlockState(pos).getBlock();
		Material material = block.getMaterial(world.getBlockState(pos));
		
		if(!material.blocksMovement() && material != Material.LAVA && material != Material.WATER)
		{
			if(!(block instanceof BlockDoor))
				return true;
		}
		return false;
	}
	
	private void ignite(World world, BlockPos pos)
	{
		setLit(world, pos);
		world.playSound(null, pos, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.4F + 1.0F);
		
		if(shouldIgnite(world, pos.up()))
			world.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
	}
	
	private void extinguish(World world, BlockPos pos)
	{
		clearLit(world, pos);
		
		world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		
		boolean isFire = world.getBlockState(pos.up()).getBlock() == Blocks.FIRE || world.getBlockState(pos.up()).getBlock() == BWRegistry.stokedFlame;
		
		if(isFire)
			world.setBlockToAir(pos.up());
	}
	
	private void setLit(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.withProperty(LIT, true));
	}
	
	private void clearLit(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.withProperty(LIT, false));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		boolean isLit = meta == 1;
		return this.getDefaultState().withProperty(LIT, isLit);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(LIT) ? 1 : 0;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, LIT);
	}
}
