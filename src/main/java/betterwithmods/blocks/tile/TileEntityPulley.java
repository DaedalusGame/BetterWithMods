package betterwithmods.blocks.tile;

import java.util.HashSet;

import betterwithmods.BWRegistry;
import betterwithmods.blocks.BlockAnchor;
import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.blocks.BlockRope;
import betterwithmods.config.BWConfig;
import betterwithmods.entity.EntityExtendingRope;
import betterwithmods.entity.EntityMovingPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPulley extends TileEntityVisibleInventory {

	public static final Block PLATFORM = BWRegistry.platform;

	private EntityExtendingRope rope;
	private int timer = 20 * 5; // wait 5 sec after block is loaded for any
								// action

	private boolean isRedstonePowered() {
		return worldObj.getBlockState(pos).getBlock() != null && worldObj.isBlockPowered(pos);
	}

	private boolean isMechanicallyPowered() {
		return worldObj.getBlockState(pos).getBlock() != null
				&& worldObj.getBlockState(pos).getBlock() instanceof BlockMechMachines
				&& ((BlockMechMachines) worldObj.getBlockState(pos).getBlock()).isMechanicalOn(worldObj, pos);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	public boolean isRaising() {
		return !isRedstonePowered() && isMechanicallyPowered();
	}

	public boolean isLowering() {
		return !isRedstonePowered() && !isMechanicallyPowered();
	}

	@Override
	public SimpleItemStackHandler createItemStackHandler() {
		return new SimpleItemStackHandler(this, true, 4);
	}

	@Override
	public String getName() {
		return "inv.pulley.name";
	}

	@Override
	public int getMaxVisibleSlots() {
		return 4;
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		return player.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) <= 64.0D;
	}

	@Override
	public void update() {
		if (this.worldObj.isRemote)
			return;

		if (timer > 0) {
			timer--;
			return;
		}

		tryNextOperation();
	}

	private void tryNextOperation() {
		if (!activeOperation() && this.worldObj.getBlockState(this.pos).getBlock() instanceof BlockMechMachines) {
			if (isLowering()) {
				if (takeRope(false)) {
					BlockPos newPos = BlockRope.getLowestRopeBlock(worldObj, pos).down();
					IBlockState state = worldObj.getBlockState(newPos);
					boolean flag = state.getBlock() == BWRegistry.anchor
							&& ((BlockAnchor) BWRegistry.anchor).getFacingFromBlockState(state) == EnumFacing.UP;
					if ((worldObj.isAirBlock(newPos) || state.getBlock().isReplaceable(worldObj, newPos) || flag)
							&& newPos.up().getY() > 0) {
						rope = new EntityExtendingRope(worldObj, pos, newPos.up(), newPos.getY());
						if (!flag || movePlatform(newPos, false)) {
							worldObj.spawnEntityInWorld(rope);
						} else {
							rope = null;
						}
					}
				}
			} else if (isRaising()) {
				if (putRope(false)) {
					BlockPos lowest = BlockRope.getLowestRopeBlock(worldObj, pos);
					IBlockState state = worldObj.getBlockState(lowest.down());
					boolean flag = state.getBlock() == BWRegistry.anchor
							&& ((BlockAnchor) BWRegistry.anchor).getFacingFromBlockState(state) == EnumFacing.UP;
					if (!lowest.equals(pos)) {
						rope = new EntityExtendingRope(worldObj, pos, lowest, lowest.up().getY());
						if (!flag || movePlatform(lowest.down(), true)) {
							worldObj.spawnEntityInWorld(rope);
							worldObj.setBlockToAir(lowest);
							putRope(true);
						} else {
							rope = null;
						}
					}
				}
			}
		}
	}

	/**
	 * Turns the platform into entities and moves them with the rope
	 */

	private boolean movePlatform(BlockPos anchor, boolean up) {
		IBlockState state = worldObj.getBlockState(anchor);
		if (state.getBlock() != BWRegistry.anchor)
			return false;

		HashSet<BlockPos> platformBlocks = new HashSet<>();
		platformBlocks.add(anchor);
		boolean success = worldObj.getBlockState(anchor.down()).getBlock() == PLATFORM
				&& addToList(platformBlocks, anchor.down(), up);
		if (!success) {
			return false;
		}

		for (BlockPos blockPos : platformBlocks) {
			IBlockState blockState = worldObj.getBlockState(blockPos.up());
			Block b = blockState.getBlock();
			blockState = (b == Blocks.REDSTONE_WIRE || b == Blocks.RAIL || b == Blocks.ACTIVATOR_RAIL
					|| b == Blocks.DETECTOR_RAIL || b == Blocks.GOLDEN_RAIL ? blockState : null);
			EntityMovingPlatform platform = new EntityMovingPlatform(worldObj, blockPos,
					(up ? blockPos.up() : blockPos.down()).getY(), worldObj.getBlockState(blockPos), blockState);
			worldObj.spawnEntityInWorld(platform);
			if (blockState != null)
				worldObj.setBlockToAir(blockPos.up());
			worldObj.setBlockToAir(blockPos);
		}

		return true;
	}

	private boolean addToList(HashSet<BlockPos> set, BlockPos p, boolean up) {

		if (set.size() > BWConfig.maxPlatformBlocks)
			return false;

		BlockPos blockCheck = up ? p.up() : p.down();

		Block b = worldObj.getBlockState(p).getBlock();
		if (worldObj.getBlockState(p).getBlock() != PLATFORM) {
			if (b == Blocks.REDSTONE_WIRE || b == Blocks.RAIL || b == Blocks.ACTIVATOR_RAIL || b == Blocks.DETECTOR_RAIL
					|| b == Blocks.GOLDEN_RAIL) {

			}
			return true;
		}

		b = worldObj.getBlockState(blockCheck).getBlock();

		if (b == Blocks.REDSTONE_WIRE || b == Blocks.RAIL || b == Blocks.ACTIVATOR_RAIL || b == Blocks.DETECTOR_RAIL
				|| b == Blocks.GOLDEN_RAIL) {

		} else {
			if (!(worldObj.isAirBlock(blockCheck) || b.isReplaceable(worldObj, blockCheck) || b == PLATFORM)
					&& !set.contains(blockCheck)) {
				return false;
			}
		}

		set.add(p);

		if (!set.contains(p.up())) {
			if (!addToList(set, p.up(), up))
				return false;
		}
		if (!set.contains(p.down())) {
			if (!addToList(set, p.down(), up))
				return false;
		}
		if (!set.contains(p.north())) {
			if (!addToList(set, p.north(), up))
				return false;
		}
		if (!set.contains(p.south())) {
			if (!addToList(set, p.south(), up))
				return false;
		}
		if (!set.contains(p.east())) {
			if (!addToList(set, p.east(), up))
				return false;
		}
		if (!set.contains(p.west())) {
			if (!addToList(set, p.west(), up))
				return false;
		}

		return true;
	}

	private boolean activeOperation() {
		return rope != null && rope.isEntityAlive();
	}

	private boolean takeRope(boolean flag) {
		for (int i = 0; i < 4; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null && stack.getItem() == Item.getItemFromBlock(BWRegistry.rope) && stack.stackSize > 0) {
				if (flag) {
					stack.stackSize--;
					if (stack.stackSize < 1) {
						inventory.setStackInSlot(i, null);
					}
					inventory.onContentsChanged(i);
				}
				return true;
			}
		}
		return false;
	}

	private boolean putRope(boolean flag) {
		for (int i = 0; i < 4; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack == null || stack.getItem() == Item.getItemFromBlock(BWRegistry.rope) && stack.stackSize < 64) {
				if (flag) {
					if (stack == null) {
						inventory.setStackInSlot(i, stack = new ItemStack(BWRegistry.rope, 1));
					} else {
						stack.stackSize++;
					}
					inventory.onContentsChanged(i);
				}
				return true;
			}
		}
		return false;
	}

	public void onJobCompleted(boolean up, int targetY) {
		BlockPos ropePos = new BlockPos(pos.getX(), targetY - (up ? 1 : 0), pos.getZ());
		IBlockState state = worldObj.getBlockState(ropePos);
		if (!up) {
			if ((worldObj.isAirBlock(ropePos) || state.getBlock().isReplaceable(worldObj, ropePos)) && takeRope(true)) {
				worldObj.setBlockState(ropePos, BWRegistry.rope.getDefaultState());
			}
		}
		tryNextOperation();
	}

}
