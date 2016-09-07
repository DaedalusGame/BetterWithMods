package betterwithmods.blocks;

import betterwithmods.BWRegistry;
import betterwithmods.blocks.BlockMechMachines.EnumType;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

public class BlockAnchor extends BTWBlock
{
	public static final PropertyBool TOPCONNECT = PropertyBool.create("topconnect");
	public static final PropertyBool BOTTOMCONNECT = PropertyBool.create("bottomconnect");
	public static float height = 0.375F;
	
	public BlockAnchor()
	{
		super(Material.ROCK, "anchor");
		this.setHardness(2.0F);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		int facing = state.getValue(DirUtils.FACING).getIndex();
		switch(facing)
		{
		case 0:
			return new AxisAlignedBB(0.0F, 1.0F - height, 0.0F, 1.0F, 1.0F, 1.0F);
		case 1:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, height, 1.0F);
		case 2:
			return new AxisAlignedBB(0.0F, 0.0F, 1.0F - height, 1.0F, 1.0F, 1.0F);
		case 3:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, height);
		case 4:
			return new AxisAlignedBB(1.0F - height, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
		return new AxisAlignedBB(0.0F, 0.0F, 0.0F, height, 1.0F, 1.0F);
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
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ, int meta, EntityLivingBase entity)
	{
		IBlockState state = super.onBlockPlaced(world, pos, side, flX, flY, flZ, meta, entity);
		return this.setFacingInBlock(state, side);
	}
	
	@Override
	public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing)
	{
		return state.withProperty(DirUtils.FACING, facing);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = heldItem;
		BlockPos down = pos.down();
		
		if(stack != null)
		{
			if(stack.getItem() instanceof ItemBlock)
			{
				Block block = ((ItemBlock)stack.getItem()).getBlock();
				if(block == BWRegistry.rope)
				{
					if(!world.isRemote)
					{
						if(world.getBlockState(down).getBlock() == BWRegistry.rope)
						{
							((BlockRope)world.getBlockState(down).getBlock()).placeRopeUnder(stack, world, down, player);
						}
						else if(world.getBlockState(down).getBlock().isReplaceable(world, down) || world.isAirBlock(down))
						{
							world.setBlockState(down, BWRegistry.rope.getDefaultState());
							if(!player.capabilities.isCreativeMode)
								stack.stackSize--;
						}
						else
							return false;
					}
					return true;
				}
			}
			return false;
		}
		else if(!world.isRemote)
			retractRope(world, pos, player);
		return true;
	}
	
	@Override
	public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos)
	{
		EnumFacing facing = getFacing(world, pos);
		return facing != EnumFacing.UP && facing != EnumFacing.DOWN;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		EnumFacing facing = getFacing(world, pos);
		return side == facing.getOpposite();
	}
	
	@Override
	public EnumFacing getFacing(IBlockAccess world, BlockPos pos)
	{
		return getFacingFromBlockState(world.getBlockState(pos));
	}
	
	@Override
	public EnumFacing getFacingFromBlockState(IBlockState state)
	{
		return state.getValue(DirUtils.FACING);
	}
	
	private void retractRope(World world, BlockPos pos, EntityPlayer player)
	{
		for(int i = pos.getY() - 1; i >= 0; i--)
		{
			BlockPos pos2 = new BlockPos(pos.getX(), i, pos.getZ());
			Block block = world.getBlockState(pos2).getBlock();
			
			if(world.getBlockState(pos2).getBlock() != BWRegistry.rope && world.getBlockState(pos2.up()).getBlock() == BWRegistry.rope)
			{
				if(!player.capabilities.isCreativeMode)
					addRopeToInv(world, pos, player);
				world.setBlockToAir(pos2.up());
				break;
			}

			else if(world.getBlockState(pos2).getBlock() != BWRegistry.rope)
				break;
		}
	}
	
	private void addRopeToInv(World world, BlockPos pos, EntityPlayer player)
	{
		ItemStack rope = new ItemStack(BWRegistry.rope);
		
		if(player.inventory.addItemStackToInventory(rope))
			world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		else
			InvUtils.ejectStackWithOffset(world, pos, rope);
	}
	
	@Override
	public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
	{
		return entity instanceof EntityPlayer && world.getBlockState(pos).getBlock() == this && world.getBlockState(pos).getValue(DirUtils.FACING) != EnumFacing.DOWN;
	}
	
	private boolean isRope(IBlockAccess world, BlockPos origin, EnumFacing facing)
	{
		BlockPos pos = origin.offset(facing);
		return world.getBlockState(pos).getBlock() == BWRegistry.rope;
	}
	
	private boolean isAnchor(IBlockAccess world, BlockPos origin, EnumFacing facing)
	{
		BlockPos pos = origin.offset(facing);
		return world.getBlockState(pos).getBlock() == this && world.getBlockState(pos).getValue(DirUtils.FACING) != facing;
	}
	
	private boolean isPulley(IBlockAccess world, BlockPos origin, EnumFacing facing)
	{
		BlockPos pos = origin.offset(facing);
		return world.getBlockState(pos).getBlock() == BWRegistry.singleMachines && world.getBlockState(pos).getValue(BlockMechMachines.MACHINETYPE) == EnumType.PULLEY;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		boolean topRope = isRope(world, pos, EnumFacing.UP) || isAnchor(world, pos, EnumFacing.UP) || isPulley(world, pos, EnumFacing.UP);
		boolean bottomRope = isRope(world, pos, EnumFacing.DOWN) || isAnchor(world, pos, EnumFacing.DOWN);
		
		return state.withProperty(TOPCONNECT, topRope).withProperty(BOTTOMCONNECT, bottomRope);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(DirUtils.FACING, EnumFacing.getFront(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(DirUtils.FACING).getIndex();
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DirUtils.FACING, TOPCONNECT, BOTTOMCONNECT);
	}
}
