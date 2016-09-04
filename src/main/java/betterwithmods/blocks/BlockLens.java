package betterwithmods.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.util.math.AxisAlignedBB;
import betterwithmods.BWRegistry;
import betterwithmods.util.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

public class BlockLens extends BTWBlock
{
	public static final PropertyBool LIT = PropertyBool.create("lit");
	public BlockLens()
	{
		super(Material.IRON, "lens");
		this.setHardness(3.5F);
		this.setTickRandomly(true);
		this.setDefaultState(this.getDefaultState().withProperty(DirUtils.FACING, EnumFacing.NORTH));
	}
	
	@Override
	public int tickRate(World world)
	{
		return 5;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		world.scheduleBlockUpdate(pos, this, 3, 5);
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ, int meta, EntityLivingBase entity)
	{
		IBlockState state = super.onBlockPlaced(world, pos, side, flX, flY, flZ, meta, entity);
		EnumFacing face = DirUtils.convertEntityOrientationToFacing(entity, side);
		return setFacingInBlock(state, face);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		cleanupLightToFacing(world, pos, state.getValue(DirUtils.FACING));
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack)
	{
		EnumFacing facing = DirUtils.convertEntityOrientationToFacing(entity, EnumFacing.NORTH);
		setFacingInBlock(state, facing);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		world.scheduleBlockUpdate(pos, this, 3, 5);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		cleanupLight(world, pos);
		EnumFacing dir = getFacing(world, pos);
		
		boolean isLightDetector = isFacingBlockDetector(world, pos);
		
		if(!isLightDetector)
		{
			boolean sunlight = isLightFromSun(world, pos);
			boolean lightOn = isInputLit(world, pos);
			if(isLit(world, pos) != lightOn)
			{
				setLit(world, pos, lightOn);
			}
			EnumFacing expectedFacing = DirUtils.getOpposite(getFacing(world, pos));
			
			BlockPos offset = pos.offset(dir);
			if(isLit(world, pos) && (world.isAirBlock(offset) || world.getBlockState(offset).getBlock() == BWRegistry.lightSource))
			{
				for(int i = 1; i < 32; i++)
				{
					BlockPos bPos = pos.offset(dir, i);
					IBlockState lightState = BWRegistry.lightSource.getDefaultState();
					if(world.isAirBlock(bPos))
					{
						List<Entity> entity = world.getEntitiesWithinAABB(Entity.class, FULL_BLOCK_AABB.offset(bPos));
						if(entity.size() > 0)
						{
							world.setBlockState(bPos, lightState.withProperty(DirUtils.FACING, expectedFacing).withProperty(BlockInvisibleLight.SUNLIGHT, sunlight));
							break;
						}
						else if(world.getBlockState(bPos).getBlock() == BWRegistry.lightSource && world.getBlockState(bPos).getValue(DirUtils.FACING).ordinal() < expectedFacing.ordinal())
						{
							if(world.getBlockState(bPos).getValue(BlockInvisibleLight.SUNLIGHT))
								lightState = lightState.withProperty(BlockInvisibleLight.SUNLIGHT, true);
							world.setBlockState(bPos, lightState.withProperty(DirUtils.FACING, expectedFacing));
						}
						else if(!world.isAirBlock(bPos))
						{
							bPos = bPos.offset(dir.getOpposite());
							if(world.getBlockState(bPos).getBlock() != BWRegistry.lightSource || (world.getBlockState(bPos).getBlock() == BWRegistry.lightSource && world.getBlockState(bPos).getValue(DirUtils.FACING).ordinal() < expectedFacing.ordinal()))
								world.setBlockState(bPos, lightState.withProperty(DirUtils.FACING, expectedFacing).withProperty(BlockInvisibleLight.SUNLIGHT, sunlight));
							break;
						}
						
					}
					else if(!world.isAirBlock(bPos))
					{
						BlockPos dPos = bPos.offset(dir.getOpposite());
						if(world.getBlockState(dPos).getBlock() != BWRegistry.lightSource || (world.getBlockState(dPos).getBlock() == BWRegistry.lightSource && world.getBlockState(dPos).getValue(DirUtils.FACING).ordinal() <= expectedFacing.ordinal()))
						{
							if(world.getBlockState(dPos).getBlock() == BWRegistry.lightSource && world.getBlockState(dPos).getValue(BlockInvisibleLight.SUNLIGHT))
								lightState = lightState.withProperty(BlockInvisibleLight.SUNLIGHT, sunlight);
							world.setBlockState(dPos, lightState.withProperty(DirUtils.FACING, expectedFacing));
						}
						break;
					}
				}
			}
			else if(!isLit(world, pos))
			{
				for(int i = 1; i < 32; i++)
				{
					BlockPos bPos = pos.offset(dir, i);
					
					if(world.getBlockState(bPos).getBlock() == BWRegistry.lightSource)
					{
						world.setBlockToAir(bPos);
					}
					else if(world.isAirBlock(bPos))
						continue;
					else
						break;
				}
			}
		}
		else
		{
			int lightValue = world.getLight(pos.offset(dir.getOpposite()));
			
			boolean shouldBeOn = lightValue > 7;
			
			if(isLit(world, pos) != shouldBeOn)
				setLit(world, pos, shouldBeOn);
		}
		world.scheduleBlockUpdate(pos, this, 5, 5);
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return EnumPushReaction.BLOCK;
	}
	
	@Override
	public EnumFacing getFacing(IBlockAccess world, BlockPos pos)
	{
		return getFacingFromBlockState(world.getBlockState(pos));
	}
	
	@Override
	public void setFacing(World world, BlockPos pos, EnumFacing facing)
	{
		world.setBlockState(pos, world.getBlockState(pos));
	}
	
	@Override
	public EnumFacing getFacingFromBlockState(IBlockState state)
	{
		return state.getValue(DirUtils.FACING);
	}
	
	@Override
	public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing)
	{
		return state.withProperty(DirUtils.FACING, facing);
	}
	
	public boolean isLit(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(LIT);
	}
	
	public void setLit(World world, BlockPos pos, boolean isOn)
	{
		boolean lit = isOn;
		boolean oldLit = world.getBlockState(pos).getValue(LIT);
		
		if(lit != oldLit)
		{
			world.setBlockState(pos, world.getBlockState(pos).withProperty(LIT, lit));
			world.notifyBlockOfStateChange(pos, this);
		}
	}
	
	private boolean isInputLit(World world, BlockPos pos)
	{
		EnumFacing facing = getFacing(world, pos);
		EnumFacing dir = DirUtils.getOpposite(facing);
		BlockPos oppOff = pos.offset(dir);
		Block block = world.getBlockState(oppOff).getBlock();
		
		if(world.isAirBlock(oppOff) && world.canBlockSeeSky(oppOff))
		{
			return world.getLightFor(EnumSkyBlock.SKY, oppOff) > 12 && world.isDaytime() && (!world.isRaining() || !world.isThundering());
		}
		else if(block.getLightValue(world.getBlockState(oppOff), world, oppOff) > 12)
		{
			return true;
		}
		return false;
	}
	
	private boolean isFacingBlockDetector(World world, BlockPos pos)
	{
		EnumFacing facing = getFacing(world, pos);
		BlockPos offset = pos.offset(facing);
		Block block = world.getBlockState(offset).getBlock();
		
		if(block == BWRegistry.detector)
		{
			EnumFacing detFacing = ((BlockDetector)block).getFacing(world, offset);
			
			if(detFacing == DirUtils.getOpposite(facing))
				return true;
		}
		return false;
	}
	
	private boolean isLightFromSun(World world, BlockPos pos)
	{
		EnumFacing facing = DirUtils.getOpposite(getFacing(world, pos));
		BlockPos offset = pos.offset(facing);
		if(world.isAirBlock(offset) && world.canBlockSeeSky(offset))
			return true;
		else if(world.getBlockState(offset).getBlock() == BWRegistry.lightSource && world.getBlockState(offset).getValue(BlockInvisibleLight.SUNLIGHT))
			return true;
		return false;
	}
	
	private void cleanupLightToFacing(World world, BlockPos pos, EnumFacing facing)
	{
		EnumFacing opp = DirUtils.getOpposite(facing);
		for(int i = 1; i < 32; i++)
		{
			BlockPos offset = pos.offset(facing, i);
			if(world.getBlockState(offset).getBlock() == BWRegistry.lightSource)
			{
				EnumFacing lightFace = ((BlockInvisibleLight)world.getBlockState(offset).getBlock()).getFacing(world, offset);
				if(lightFace == opp)
				{
					world.setBlockToAir(offset);
					break;
				}
			}
			else if(!world.isAirBlock(offset))
				break;
		}
	}
	
	private void cleanupLight(World world, BlockPos pos)
	{
		EnumFacing facing = getFacing(world, pos);
		EnumFacing oppFacing = DirUtils.getOpposite(facing);
		
		for(int i = 1; i < 32; i++)
		{
			BlockPos offset = pos.offset(facing, i);
			
			if(world.getBlockState(offset).getBlock() == BWRegistry.lightSource)
			{
				EnumFacing lightFace = ((BlockInvisibleLight)world.getBlockState(offset).getBlock()).getFacing(world, offset);
				if(lightFace == oppFacing)
				{
					world.setBlockToAir(offset);
				}
				continue;
			}
			else if(world.isAirBlock(offset))
				continue;
			else if(world.getBlockState(offset).getBlock() == this)
			{
				BlockLens lens = (BlockLens)world.getBlockState(pos).getBlock();
				if(lens.getFacing(world, offset) == facing)
					break;
			}
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		boolean lit = false;
		if(meta > 7)
		{
			lit = true;
			meta -= 8;
		}
		return this.getDefaultState().withProperty(LIT, lit).withProperty(DirUtils.FACING, EnumFacing.getFront(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int meta = state.getValue(LIT) ? 8 : 0;
		return meta + state.getValue(DirUtils.FACING).getIndex();
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DirUtils.FACING, LIT);
	}
}
