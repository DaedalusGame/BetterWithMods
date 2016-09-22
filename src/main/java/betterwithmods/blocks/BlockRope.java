package betterwithmods.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.util.math.BlockPos;
import betterwithmods.BWMBlocks;
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

public class BlockRope extends BWMBlock
{
	public static final float width = 0.125F;
	
	public BlockRope()
	{
		super(Material.CIRCUITS);
		setSoundType(SoundType.PLANT);
		this.setHardness(0.5F);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighbor) {
		if (!canBlockStay(world, pos)) {
			dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
		}
	}
	
	public boolean canBlockStay(World world, BlockPos pos) {
		Block blockAbove = world.getBlockState(pos.up()).getBlock();
		
		boolean supported = false;
		
		if (blockAbove == BWMBlocks.ANCHOR) {
			EnumFacing facing = ((BlockAnchor)BWMBlocks.ANCHOR).getFacing(world, pos.up());
			supported = facing != EnumFacing.UP;
		}
		if (blockAbove == this) {
			supported = true;
		}
		if (blockAbove == BWMBlocks.SINGLE_MACHINES) {
			if (world.getBlockState(pos.up()).getValue(BlockMechMachines.MACHINETYPE) == BlockMechMachines.EnumType.PULLEY) {
				supported = true;
			}
		}
		
		return supported;
	}
	
	public static boolean placeRopeUnder(ItemStack stack, World world, BlockPos pos, EntityPlayer player) {
		if(stack != null || player == null)
		{
			BlockPos bp = getLowestRopeBlock(world, pos).down();
			Block block = world.getBlockState(bp).getBlock();
			if ((world.isAirBlock(bp) || block.isReplaceable(world, bp)) && ((BlockRope) BWMBlocks.ROPE.getDefaultState().getBlock()).canBlockStay(world, bp)) {
				world.setBlockState(bp, BWMBlocks.ROPE.getDefaultState());
				if(player != null && !player.capabilities.isCreativeMode) // if this is placed by a pulley, let the pulley manage the stack size
					stack.stackSize--;
				return true;
			}
		}
		return false;
	}
	
	public static BlockPos getLowestRopeBlock(World world, BlockPos pos) {
		if (world != null && pos != null) {
			BlockPos down = pos.down();
			Block below = world.getBlockState(down).getBlock();
			if (below == BWMBlocks.ROPE) {
				return getLowestRopeBlock(world, down);
			} else {
				return pos;
			}
		} else {
			return null;
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
