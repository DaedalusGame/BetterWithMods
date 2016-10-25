package betterwithmods.blocks;

import java.util.Random;

import betterwithmods.BWSounds;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import betterwithmods.api.block.IAxle;
import betterwithmods.api.block.IMechanical;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.config.BWConfig;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGearbox extends BWMBlock implements IMechanicalBlock, IMechanical
{
	public static final PropertyBool ISACTIVE = PropertyBool.create("ison");
	public BlockGearbox()
	{
		super(Material.WOOD);
		this.setHardness(2.0F);
		this.setTickRandomly(true);
		this.setDefaultState(getDefaultState().withProperty(DirUtils.FACING, EnumFacing.UP).withProperty(ISACTIVE, false));
		this.setSoundType(SoundType.WOOD);
	}
	
	@Override
	public int tickRate(World world)
	{
		return 10;
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = super.onBlockPlaced(world, pos, side, flX, flY, flZ, meta, placer);
		return setFacingInBlock(state, side.getOpposite());
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack)
	{
		EnumFacing facing = DirUtils.convertEntityOrientationToFacing(entity, EnumFacing.NORTH);
		
		setFacing(world, pos, facing);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{//TODO: Maybe make this try to work with items that don't have a use action?
		boolean emptyHands = player.getHeldItem(EnumHand.MAIN_HAND) == null && player.getHeldItem(EnumHand.OFF_HAND) == null && player.isSneaking();

		if(world.isRemote && emptyHands)
			return true;
		else if(!world.isRemote && emptyHands)
		{
			EnumFacing facing = DirUtils.cycleFacing(state.getValue(DirUtils.FACING), false);
			world.playSound(null, pos, this.getSoundType(state, world, pos, player).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
			world.setBlockState(pos, state.withProperty(ISACTIVE, false).withProperty(DirUtils.FACING, facing));
			world.notifyNeighborsOfStateChange(pos, this);
			world.scheduleBlockUpdate(pos, this, 10, 5);
			return true;
		}
		return false;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);
		world.scheduleBlockUpdate(pos, this, 10, 5);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		boolean gettingPower = isInputtingMechPower(world, pos);
		boolean isOn = isGearboxOn(world, pos);
		boolean isRedstonePowered = world.isBlockIndirectlyGettingPowered(pos) > 0;
		
		if(isRedstonePowered)
			gettingPower = false;
		
		if(isOn != gettingPower)
		{
			if(isOn)
				setGearboxState(world, pos, false);
			else
				setGearboxState(world, pos, true);
			world.playSound(null, pos, BWSounds.WOODCREAK, SoundCategory.BLOCKS, 0.25F, world.rand.nextFloat() * 0.25F + 0.25F);
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		boolean isOn = isGearboxOn(world, pos);
		
		if(isOn)
		{
			boolean gettingPower = isInputtingMechPower(world, pos);
			boolean isRedstonePowered = world.isBlockIndirectlyGettingPowered(pos) > 0;
			
			if(isRedstonePowered)
				gettingPower = false;
			if(!gettingPower)
			{
				world.scheduleBlockUpdate(pos, this, 9, 5);
				return;
			}
		}
		world.scheduleBlockUpdate(pos, this, 10, 5);
	}
	
	public int getMechPowerProvidedToAxle(World world, BlockPos pos, EnumFacing facing)
	{
		if(isGearboxOn(world, pos))
		{
			if(getFacing(world, pos) != facing)
				return 4;
		}
		return 0;
	}
	
	@Override
	public EnumFacing getFacing(IBlockAccess world, BlockPos pos)
	{
		return getFacingFromState(world.getBlockState(pos));
	}
	
	@Override
	public void setFacing(World world, BlockPos pos, EnumFacing facing)
	{
		world.setBlockState(pos, world.getBlockState(pos).withProperty(DirUtils.FACING, facing));
	}
	
	public EnumFacing getFacingFromState(IBlockState state)
	{
		return state.getValue(DirUtils.FACING);
	}
	
	@Override
	public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing)
	{
		return state.withProperty(DirUtils.FACING, facing);
	}
	
	@Override
	public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public boolean canRotateHorizontally(IBlockAccess world, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public boolean canRotateVertically(IBlockAccess world, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public void rotateAroundYAxis(World world, BlockPos pos, boolean reverse)
	{
		EnumFacing facing = getFacing(world, pos);
		EnumFacing newFacing = DirUtils.rotateFacingAroundY(facing, reverse);
		if(newFacing != facing)
		{
			if(isGearboxOn(world, pos))
			{
				setGearboxState(world, pos, false);
			}
			
			setFacing(world, pos, newFacing);
			
			world.markBlockRangeForRenderUpdate(pos, pos);
			
			world.scheduleBlockUpdate(pos, this, 10, 5);
			
			MechanicalUtil.destoryHorizontalAxles(world, pos);
		}
	}

	@Override
	public boolean canOutputMechanicalPower() 
	{
		return true;
	}

	@Override
	public boolean canInputMechanicalPower() 
	{
		return true;
	}

	@Override
	public boolean isInputtingMechPower(World world, BlockPos pos) 
	{
		EnumFacing dir = getFacing(world, pos);
		return MechanicalUtil.isBlockPoweredByAxleOnSide(world, pos, dir) || MechanicalUtil.isPoweredByCrankOnSide(world, pos, dir);
	}

	@Override
	public boolean isOutputtingMechPower(World world, BlockPos pos) 
	{
		return isGearboxOn(world, pos);
	}

	@Override
	public boolean canInputPowerToSide(IBlockAccess world, BlockPos pos, EnumFacing dir) 
	{
		EnumFacing facing = getFacing(world, pos);
		return dir == facing;
	}

	@Override
	public void overpower(World world, BlockPos pos) 
	{
		if(isGearboxOn(world, pos))
			breakGearbox(world, pos);
	}
	
	public boolean isGearboxOn(IBlockAccess world, BlockPos pos)
	{
		return isGearboxOnFromState(world.getBlockState(pos));
	}
	
	public void setGearboxState(World world, BlockPos pos, boolean on)
	{
		boolean isActive = world.getBlockState(pos).getValue(ISACTIVE);
		if(on != isActive)
			world.setBlockState(pos, world.getBlockState(pos).withProperty(ISACTIVE, on));
	}
	
	public boolean isGearboxOnFromState(IBlockState state)
	{
		return state.getValue(ISACTIVE);
	}
	
	private void emitGearboxParticles(World world, BlockPos pos, Random rand)
	{
		for(int i = 0; i < 5; i++)
		{
			float flX = pos.getX() + rand.nextFloat();
			float flY = pos.getY() + rand.nextFloat() * 0.5F + 1.0F;
			float flZ = pos.getZ() + rand.nextFloat();
			
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, flX, flY, flZ, 0.0D, 0.0D, 0.0D);
		}
	}
	
	public void breakGearbox(World world, BlockPos pos)
	{
		if (BWConfig.dropsGearbox)
			InvUtils.ejectBrokenItems(world, pos, new ResourceLocation("betterwithmods", "block/gearbox"));
		/*
		InvUtils.ejectStackWithOffset(world, pos, new ItemStack(Blocks.PLANKS));
		InvUtils.ejectStackWithOffset(world, pos, new ItemStack(BWMItems.MATERIAL, 3, 22));
		InvUtils.ejectStackWithOffset(world, pos, new ItemStack(BWMItems.MATERIAL, 2, 0));
		InvUtils.ejectStackWithOffset(world, pos, new ItemStack(Items.GOLD_NUGGET, 2, 0));
		*/
		world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.3F, world.rand.nextFloat() * 0.1F + 0.45F);
		world.setBlockToAir(pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		if(state.getValue(ISACTIVE))
		{
			emitGearboxParticles(world, pos, rand);
			if(rand.nextInt(50) == 0)
				world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, BWSounds.WOODCREAK, SoundCategory.BLOCKS, 0.75F, world.rand.nextFloat() * 0.25F + 0.25F, false);
		}
	}

	@Override
	public boolean isMechanicalOn(IBlockAccess world, BlockPos pos) 
	{
		IBlockState state = world.getBlockState(pos);
		return isMechanicalOnFromState(state);
	}

	@Override
	public void setMechanicalOn(World world, BlockPos pos, boolean isOn) 
	{
		world.setBlockState(pos, world.getBlockState(pos).withProperty(ISACTIVE, isOn));
	}

	@Override
	public boolean isMechanicalOnFromState(IBlockState state) 
	{
		return state.getValue(ISACTIVE);
	}

	@Override
	public int getMechPowerLevelToFacing(World world, BlockPos pos, EnumFacing dir) 
	{
		return getMechPowerProvidedToAxle(world, pos, dir);
	}

	@Override
	public boolean isMechanicalJunction() {
		return true;
	}
	
	private boolean isAxle(IBlockAccess world, BlockPos origin, EnumFacing facing)
	{
		BlockPos pos = origin.offset(facing);
		Block block = world.getBlockState(pos).getBlock();
		return block instanceof IAxle && ((IAxle)block).isAxleOrientedToFacing(world, pos, facing.getOpposite());
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		boolean[] dirs = new boolean[6];
		for(int i = 0; i < 6; i++)
		{
			EnumFacing facing = EnumFacing.getFront(i);
			dirs[i] = isAxle(world, pos, facing) && this.getFacing(world, pos) != facing;
		}
		return state.withProperty(DirUtils.DOWN, dirs[0])
				.withProperty(DirUtils.UP, dirs[1])
				.withProperty(DirUtils.NORTH, dirs[2])
				.withProperty(DirUtils.SOUTH, dirs[3])
				.withProperty(DirUtils.WEST, dirs[4])
				.withProperty(DirUtils.EAST, dirs[5]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int meta = isMechanicalOnFromState(state) ? 8 : 0;
		meta += state.getValue(DirUtils.FACING).getIndex();
		return meta;
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
		return this.getDefaultState().withProperty(ISACTIVE, isActive).withProperty(DirUtils.FACING, EnumFacing.getFront(meta));
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DirUtils.FACING, ISACTIVE, DirUtils.UP, DirUtils.DOWN, DirUtils.NORTH, DirUtils.SOUTH, DirUtils.WEST, DirUtils.EAST);
	}
}
