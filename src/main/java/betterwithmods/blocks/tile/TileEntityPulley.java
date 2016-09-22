package betterwithmods.blocks.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMod;
import betterwithmods.blocks.BlockAnchor;
import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.blocks.BlockRope;
import betterwithmods.config.BWConfig;
import betterwithmods.entity.EntityExtendingRope;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailBase.EnumRailDirection;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public class TileEntityPulley extends TileEntityVisibleInventory {

	public static final Block PLATFORM = BWMBlocks.PLATFORM;

	private EntityExtendingRope rope;
	private NBTTagCompound ropeTag = null;

	private boolean isRedstonePowered() {
		return worldObj.getBlockState(pos).getBlock() != null && worldObj.isBlockPowered(pos);
	}

	public boolean isMechanicallyPowered() {
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

		tryNextOperation();
	}

	private void tryNextOperation() {
		if (!activeOperation() && this.worldObj.getBlockState(this.pos).getBlock() instanceof BlockMechMachines) {
			if (canGoDown(false)) {
				goDown();
			} else if (canGoUp()) {
				goUp();
			}
		}
	}

	private boolean canGoUp() {
		if (isRaising()) {
			if (putRope(false)) {
				BlockPos lowest = BlockRope.getLowestRopeBlock(worldObj, pos);
				if (!lowest.equals(pos)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean canGoDown(boolean isMoving) {
		if (isLowering()) {
			if (takeRope(false)) {
				BlockPos newPos = BlockRope.getLowestRopeBlock(worldObj, pos).down();
				IBlockState state = worldObj.getBlockState(newPos);
				boolean flag = !isMoving && state.getBlock() == BWMBlocks.ANCHOR
						&& ((BlockAnchor) BWMBlocks.ANCHOR).getFacingFromBlockState(state) == EnumFacing.UP;
				if ((worldObj.isAirBlock(newPos) || state.getBlock().isReplaceable(worldObj, newPos) || flag)
						&& newPos.up().getY() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	private void goUp() {
		BlockPos lowest = BlockRope.getLowestRopeBlock(worldObj, pos);
		IBlockState state = worldObj.getBlockState(lowest.down());
		boolean flag = state.getBlock() == BWMBlocks.ANCHOR
				&& ((BlockAnchor) BWMBlocks.ANCHOR).getFacingFromBlockState(state) == EnumFacing.UP;
		rope = new EntityExtendingRope(worldObj, pos, lowest, lowest.up().getY());
		if (!flag || movePlatform(lowest.down(), true)) {
			worldObj.playSound(null, pos.down(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS,
					0.4F + (worldObj.rand.nextFloat() * 0.1F), 1.0F);
			worldObj.spawnEntityInWorld(rope);
			worldObj.setBlockToAir(lowest);
			putRope(true);
		} else {
			rope = null;
		}
	}

	private void goDown() {
		BlockPos newPos = BlockRope.getLowestRopeBlock(worldObj, pos).down();
		IBlockState state = worldObj.getBlockState(newPos);
		boolean flag = state.getBlock() == BWMBlocks.ANCHOR
				&& ((BlockAnchor) BWMBlocks.ANCHOR).getFacingFromBlockState(state) == EnumFacing.UP;
		rope = new EntityExtendingRope(worldObj, pos, newPos.up(), newPos.getY());
		if (!flag || movePlatform(newPos, false)) {
			worldObj.spawnEntityInWorld(rope);
		} else {
			rope = null;
		}
	}

	/**
	 * Turns the platform into entities and moves them with the rope
	 */

	private boolean movePlatform(BlockPos anchor, boolean up) {
		IBlockState state = worldObj.getBlockState(anchor);
		if (state.getBlock() != BWMBlocks.ANCHOR)
			return false;

		HashSet<BlockPos> platformBlocks = new HashSet<>();
		platformBlocks.add(anchor);
		Block b = worldObj.getBlockState(anchor.down()).getBlock();
		boolean success = worldObj.getBlockState(anchor.down()).getBlock() == PLATFORM
				? addToList(platformBlocks, anchor.down(), up) : up || isValidBlock(b, anchor.down());
		if (!success) {
			return false;
		}

		for (BlockPos blockPos : platformBlocks) {
			Arrays.asList(new BlockPos[] { blockPos.north(), blockPos.south() }).forEach(p -> {
				if (!platformBlocks.contains(p)) {
					fixRail((BlockPos) p, EnumRailDirection.ASCENDING_NORTH, EnumRailDirection.ASCENDING_SOUTH);
				}
			});
			Arrays.asList(new BlockPos[] { blockPos.east(), blockPos.west() }).forEach(p -> {
				if (!platformBlocks.contains(p)) {
					fixRail((BlockPos) p, EnumRailDirection.ASCENDING_EAST, EnumRailDirection.ASCENDING_WEST);
				}
			});
		}

		if (!worldObj.isRemote) {
			for (BlockPos blockPos : platformBlocks) {
				IBlockState blockState = worldObj.getBlockState(blockPos.up());
				b = blockState.getBlock();
				blockState = (b == Blocks.REDSTONE_WIRE || b instanceof BlockRailBase ? blockState : null);
				Vec3i offset = blockPos.subtract(anchor.up());
				rope.addBlock(offset, worldObj.getBlockState(blockPos));
				if (blockState != null) {
					rope.addBlock(new Vec3i(offset.getX(), offset.getY() + 1, offset.getZ()), blockState);
					worldObj.setBlockToAir(blockPos.up());
				}
				worldObj.setBlockToAir(blockPos);
			}
		}

		return true;
	}

	public boolean isValidBlock(Block b, BlockPos pos) {
		return b == Blocks.AIR || b.isReplaceable(worldObj, pos) || b == PLATFORM;
	}

	@SuppressWarnings("unchecked")
	private void fixRail(BlockPos rail, EnumRailDirection... directions) {
		List<EnumRailDirection> list = Arrays.asList(directions);
		IBlockState state = worldObj.getBlockState(rail);
		if (worldObj.getBlockState(rail).getBlock() instanceof BlockRailBase) {
			PropertyEnum<EnumRailDirection> shape = null;
			for (IProperty<?> p : state.getPropertyNames()) {
				if ("shape".equals(p.getName()) && p instanceof PropertyEnum<?>) {
					shape = (PropertyEnum<EnumRailDirection>) p;
					break;
				}
			}

			if (shape != null) {
				EnumRailDirection currentShape = state.getValue(shape);
				if (list.contains(currentShape)) {
					worldObj.setBlockState(rail, state.withProperty(shape, flatten(currentShape)), 6);
				}
			} else {
				Formatter f = new Formatter();
				BWMod.logger.warn(f.format("Rail at %s has no shape?", rail));
				f.close();
			}
		}
	}

	private EnumRailDirection flatten(EnumRailDirection old) {
		switch (old) {
		case ASCENDING_EAST:
		case ASCENDING_WEST:
			return EnumRailDirection.EAST_WEST;
		case ASCENDING_NORTH:
		case ASCENDING_SOUTH:
			return EnumRailDirection.NORTH_SOUTH;
		default:
			return old;
		}
	}

	private boolean addToList(HashSet<BlockPos> set, BlockPos p, boolean up) {
		if (set.size() > BWConfig.maxPlatformBlocks)
			return false;

		BlockPos blockCheck = up ? p.up() : p.down();

		Block b = worldObj.getBlockState(p).getBlock();
		if (worldObj.getBlockState(p).getBlock() != PLATFORM) {
			return true;
		}

		b = worldObj.getBlockState(blockCheck).getBlock();

		if (b == Blocks.REDSTONE_WIRE || b instanceof BlockRailBase) {

		} else {
			if (!(worldObj.isAirBlock(blockCheck) || b.isReplaceable(worldObj, blockCheck) || b == PLATFORM)
					&& !set.contains(blockCheck)) {
				return false;
			}
		}

		set.add(p);

		List<BlockPos> fails = new ArrayList<>();

		Arrays.asList(p.up(), p.down(), p.north(), p.south(), p.east(), p.west()).forEach(q -> {
			if (fails.isEmpty() && !set.contains(q)) {
				if (!addToList(set, q, up))
					fails.add(q);
			}
		});

		return fails.isEmpty();
	}

	private boolean activeOperation() {
		return rope != null && rope.isEntityAlive();
	}

	private boolean takeRope(boolean flag) {
		for (int i = 0; i < 4; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null && stack.getItem() == Item.getItemFromBlock(BWMBlocks.ROPE) && stack.stackSize > 0) {
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
			if (stack == null || stack.getItem() == Item.getItemFromBlock(BWMBlocks.ROPE) && stack.stackSize < 64) {
				if (flag) {
					if (stack == null) {
						inventory.setStackInSlot(i, stack = new ItemStack(BWMBlocks.ROPE, 1));
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

	public boolean onJobCompleted(boolean up, int targetY, EntityExtendingRope theRope) {
		BlockPos ropePos = new BlockPos(pos.getX(), targetY - (up ? 1 : 0), pos.getZ());
		IBlockState state = worldObj.getBlockState(ropePos);
		if (!up) {
			if ((worldObj.isAirBlock(ropePos) || state.getBlock().isReplaceable(worldObj, ropePos)) && takeRope(true)) {
				worldObj.playSound(null, pos.down(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 0.4F, 1.0F);
				worldObj.setBlockState(ropePos, BWMBlocks.ROPE.getDefaultState());
			}
		}
		if ((theRope.getUp() ? canGoUp() : canGoDown(true)) && !theRope.isPathBlocked()) {
			theRope.setTargetY(targetY + (theRope.getUp() ? 1 : -1));
			if (up) {
				if (!worldObj.isAirBlock(ropePos.up())) {
					worldObj.playSound(null, pos.down(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS,
							0.4F + (worldObj.rand.nextFloat() * 0.1F), 1.0F);
					worldObj.setBlockToAir(ropePos.up());
					putRope(true);
				}
			}
			return true;
		} else {
			tryNextOperation();
			theRope.setDead();
			return false;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		NBTTagCompound ropetag = new NBTTagCompound();
		if (rope != null)
			rope.writeToNBTAtomically(ropetag);
		tag.setTag("Rope", ropetag);
		return super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagCompound ropetag = (NBTTagCompound) tag.getTag("Rope");
		this.ropeTag = ropetag;
	}

	@Override
	public void setWorldObj(World worldIn) {
		super.setWorldObj(worldIn);
		if (rope == null && !worldIn.isRemote && ropeTag != null && !ropeTag.hasNoTags()) {
			NBTTagList pos = (NBTTagList) ropeTag.getTag("Pos");
			if (pos != null) {
				rope = (EntityExtendingRope) AnvilChunkLoader.readWorldEntityPos(ropeTag, worldObj, pos.getDoubleAt(0),
						pos.getDoubleAt(1), pos.getDoubleAt(2), true);
			}
		}
	}

}
