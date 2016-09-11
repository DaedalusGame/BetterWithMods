package betterwithmods.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import betterwithmods.BWRegistry;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

public class BlockBellows extends BTWBlock implements IMechanicalBlock
{
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	public static final PropertyBool TRIGGER = PropertyBool.create("trigger");
	
	public BlockBellows()
	{
		super(Material.WOOD, "bellows");
		this.setTickRandomly(true);
		this.setHardness(2.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(DirUtils.HORIZONTAL, EnumFacing.SOUTH).withProperty(ACTIVE, false).withProperty(TRIGGER, false));
		this.setSoundType(SoundType.WOOD);
	}
	
	@Override
	public int tickRate(World world)
	{
		return 37;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return state.getBlock() == this && !state.getValue(ACTIVE);
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
    {
        return state.getBlock() == this && !state.getValue(ACTIVE);
    }
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ, int meta, EntityLivingBase living)
	{
		IBlockState state = super.onBlockPlaced(world, pos, side, flX, flY, flZ, meta, living);
		if(side.ordinal() < 2)
			side = DirUtils.convertEntityOrientationToFlatFacing(living, side);
		return setFacingInBlock(state, side);
	}
	
	@Override
	public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing)
	{
		return state.withProperty(DirUtils.HORIZONTAL, facing);
	}
	
	@Override
	public void setFacing(World world, BlockPos pos, EnumFacing facing)
	{
		world.getBlockState(pos).withProperty(DirUtils.HORIZONTAL, facing);
	}
	
	@Override
	public EnumFacing getFacing(IBlockAccess world, BlockPos pos)
	{
		return getFacingFromBlockState(world.getBlockState(pos));
	}
	
	@Override
	public EnumFacing getFacingFromBlockState(IBlockState state)
	{
		return state.getValue(DirUtils.HORIZONTAL);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack)
	{
		if(entity == null)
			setFacingInBlock(state, EnumFacing.NORTH);
		EnumFacing facing = DirUtils.convertEntityOrientationToFlatFacing(entity);
		setFacingInBlock(state, facing);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if(state.getValue(ACTIVE))
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.6875F, 1.0F);
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		if(!isCurrentStateValid(world, pos))
		{
				world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
				setTriggerMechanicalStateChange(world, pos, true);
		}
		else
		{
			boolean continuous = isTriggerMechanicalStateChange(world, pos);
			
			if(continuous)
			{
				if(isCurrentStateValid(world, pos))
				{
					setTriggerMechanicalStateChange(world, pos, false);
				}
			}
		}
	}
	
	public boolean isCurrentStateValid(World world, BlockPos pos)
	{
		boolean gettingPower = isInputtingMechPower(world, pos);
		boolean mechanicalOn = isMechanicalOn(world, pos);
		
		return gettingPower == mechanicalOn;
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		boolean gettingPower = isInputtingMechPower(world, pos);
		boolean isMechOn = isMechanicalOn(world, pos);
		boolean continuous = isTriggerMechanicalStateChange(world, pos);
		
		if(isMechOn != gettingPower)
		{
			if(continuous)
			{
				setTriggerMechanicalStateChange(world, pos, false);
				setMechanicalOn(world, pos, gettingPower);
				world.scheduleBlockUpdate(pos, this, tickRate(world), 5);//world.markBlockForUpdate(pos);
				
				if(gettingPower)
				{
					blow(world, pos);
				}
				else
					liftCollidingEntities(world, pos);
			}
			else
			{
				world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
				setTriggerMechanicalStateChange(world, pos, true);
			}
		}
		else if(continuous)
		{
			setTriggerMechanicalStateChange(world, pos, false);
		}
	}
	
	public boolean isTriggerMechanicalState(IBlockState state)
	{
		return state.getValue(TRIGGER);
	}
	
	public boolean isTriggerMechanicalStateChange(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(TRIGGER);
	}
	
	public void setTriggerMechanicalStateChange(World world, BlockPos pos, boolean continuous)
	{
		world.setBlockState(pos, world.getBlockState(pos).withProperty(TRIGGER, continuous));
	}
	
	@Override
	public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public void rotateAroundYAxis(World world, BlockPos pos, boolean reverse)
	{
		if(DirUtils.rotateAroundY(this, world, pos, reverse))
		{
			world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
			MechanicalUtil.destoryHorizontalAxles(world, pos);
			world.scheduleBlockUpdate(pos, this, tickRate(world), 5);//world.markBlockForUpdate(pos);
		}
	}

	@Override
	public boolean canOutputMechanicalPower() 
	{
		return false;
	}

	@Override
	public boolean canInputMechanicalPower() 
	{
		return true;
	}

	@Override
	public boolean isInputtingMechPower(World world, BlockPos pos) 
	{
		return MechanicalUtil.isBlockPoweredByAxle(world, pos, this) || MechanicalUtil.isPoweredByCrank(world, pos);
	}

	@Override
	public boolean isOutputtingMechPower(World world, BlockPos pos) 
	{
		return false;
	}

	@Override
	public boolean canInputPowerToSide(IBlockAccess world, BlockPos pos,
			EnumFacing dir) 
	{
		EnumFacing facing = getFacing(world, pos);
		return dir != facing && dir != EnumFacing.UP;
	}

	@Override
	public void overpower(World world, BlockPos pos) 
	{
		breakBellows(world, pos);
	}

	@Override
	public boolean isMechanicalOn(IBlockAccess world, BlockPos pos) 
	{
		return isMechanicalOnFromState(world.getBlockState(pos));
	}

	@Override
	public void setMechanicalOn(World world, BlockPos pos, boolean isOn) 
	{
		world.setBlockState(pos, world.getBlockState(pos).withProperty(ACTIVE, isOn));
	}

	@Override
	public boolean isMechanicalOnFromState(IBlockState state) 
	{
		return state.getValue(ACTIVE);
	}
	
	private void blow(World world, BlockPos pos)
	{
		stokeFlames(world, pos);
	}
	
	private void stokeFlames(World world, BlockPos pos)
	{
		EnumFacing dir = getFacing(world, pos);
		EnumFacing dirLeft = DirUtils.rotateFacingAroundY(getFacing(world, pos), false);
		EnumFacing dirRight = DirUtils.rotateFacingAroundY(getFacing(world, pos), true);
		
		for(int i = 0; i < 3; i++)
		{
			BlockPos dirPos = pos.offset(dir, 1 + i);
			
			Block target = world.getBlockState(dirPos).getBlock();
			
			if(target == Blocks.FIRE || target == BWRegistry.stokedFlame)
				stokeFire(world, dirPos);
			else if(!world.isAirBlock(dirPos))
				break;
			
			BlockPos posLeft = dirPos.offset(dirLeft);
			
			Block targetLeft = world.getBlockState(posLeft).getBlock();
			if(targetLeft == Blocks.FIRE || targetLeft == BWRegistry.stokedFlame)
				stokeFire(world, posLeft);
			
			BlockPos posRight = dirPos.offset(dirRight);
			
			Block targetRight = world.getBlockState(posRight).getBlock();
			if(targetRight == Blocks.FIRE || targetRight == BWRegistry.stokedFlame)
				stokeFire(world, posRight);
		}
	}
	
	private void stokeFire(World world, BlockPos pos)
	{
		BlockPos down = pos.down();
		BlockPos up = pos.up();
		if(world.getBlockState(down).getBlock() == BWRegistry.hibachi)
		{
			if(world.getBlockState(pos).getBlock() == BWRegistry.stokedFlame)
				world.setBlockState(pos, BWRegistry.stokedFlame.getDefaultState());
			else
				world.setBlockState(pos, BWRegistry.stokedFlame.getDefaultState());
			
			if(world.isAirBlock(up))
				world.setBlockState(pos, Blocks.FIRE.getDefaultState());
		}
		else
			world.setBlockToAir(pos);
	}
	
	private void liftCollidingEntities(World world, BlockPos pos)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		List<Entity> list = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(x, y + 0.6875F, z, x + 1, y + 1, z + 1));
		
		float extendedY = y + 1;
		
		if(list != null && list.size() > 0)
		{
			for(int i = 0; i < list.size(); i++)
			{
				Entity entity = (Entity)list.get(i);
				
				if(!entity.isDead && (entity.canBePushed() || entity instanceof EntityItem))
				{
					double tempY = entity.getEntityBoundingBox().minY;
					
					if(tempY < extendedY)
					{
						double entityOffset = extendedY - tempY;
						entity.setPosition(entity.posX, entity.posY + entityOffset, entity.posZ);
					}
				}
			}
		}
	}
	
	public void breakBellows(World world, BlockPos pos)
	{
		InvUtils.ejectStackWithOffset(world, pos, new ItemStack(Blocks.WOODEN_SLAB, 2, 0));
		InvUtils.ejectStackWithOffset(world, pos, new ItemStack(BWRegistry.material, 1, 0));
		InvUtils.ejectStackWithOffset(world, pos, new ItemStack(BWRegistry.material, 2, 6));
		world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.3F, world.rand.nextFloat() * 0.1F + 0.45F);
		world.setBlockToAir(pos);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int meta = isMechanicalOnFromState(state) ? 8 : 0;
		meta += isTriggerMechanicalState(state) ? 4 : 0;
		return meta + state.getValue(DirUtils.HORIZONTAL).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		boolean isActive = false;
		if(meta > 7)
		{
			isActive = true;
			meta -= 8;
		}
		boolean isTrigger = meta > 3;
		if(isTrigger)
			meta -= 4;
		return this.getDefaultState().withProperty(ACTIVE, isActive).withProperty(TRIGGER, isTrigger).withProperty(DirUtils.HORIZONTAL, EnumFacing.getHorizontal(meta));
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DirUtils.HORIZONTAL, ACTIVE, TRIGGER);
	}
}
