package betterwithmods.blocks;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import betterwithmods.BWRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockTemporaryWater extends BlockLiquid {

	public BlockTemporaryWater() {
		super(Material.WATER);
		this.setUnlocalizedName("bwm:temporary_water");
		this.setRegistryName("temporary_water");
		this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 4));
		this.setTickRandomly(true);
		GameRegistry.register(this);
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		int i = ((Integer) state.getValue(LEVEL)).intValue();

		IBlockState iblockstate1 = worldIn.getBlockState(pos.down());

		if (!(iblockstate1.getBlock() == BWRegistry.pump && iblockstate1.getValue(BlockPump.ACTIVE))) {
			worldIn.setBlockToAir(pos);
			return;
		}

		if (this.canFlowInto(worldIn, pos.down(), iblockstate1)) {
			if (i >= 8) {
				this.tryFlowInto(worldIn, pos.down(), iblockstate1, i);
			} else {
				this.tryFlowInto(worldIn, pos.down(), iblockstate1, i + 8);
			}
		} else if (i >= 0 && (i == 0 || this.isBlocked(worldIn, pos.down(), iblockstate1))) {
			Set<EnumFacing> set = this.getPossibleFlowDirections(worldIn, pos);
			int k1 = i + 1;

			if (i >= 8) {
				k1 = 1;
			}

			if (k1 >= 8) {
				return;
			}

			for (EnumFacing enumfacing1 : set) {
				this.tryFlowInto(worldIn, pos.offset(enumfacing1), worldIn.getBlockState(pos.offset(enumfacing1)), k1);
			}
		}

		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
	}

	private boolean canFlowInto(World worldIn, BlockPos pos, IBlockState state) {
		Material material = state.getMaterial();
		return material != this.blockMaterial && material != Material.LAVA && !this.isBlocked(worldIn, pos, state);
	}

	private void tryFlowInto(World worldIn, BlockPos pos, IBlockState state, int level) {
		if (this.canFlowInto(worldIn, pos, state)) {
			if (state.getMaterial() != Material.AIR) {
				state.getBlock().dropBlockAsItem(worldIn, pos, state, 0);
			}

			worldIn.setBlockState(pos,
					Blocks.FLOWING_WATER.getDefaultState().withProperty(LEVEL, Integer.valueOf(level)), 3);
		}
	}

	private boolean isBlocked(World worldIn, BlockPos pos, IBlockState state) {
		Block block = worldIn.getBlockState(pos).getBlock();
		return !(block instanceof BlockDoor) && block != Blocks.STANDING_SIGN && block != Blocks.LADDER
				&& block != Blocks.REEDS
						? (block.getMaterial(state) != Material.PORTAL
								&& block.getMaterial(state) != Material.field_189963_J
										? block.getMaterial(state).blocksMovement() : true)
						: true;
	}

	private Set<EnumFacing> getPossibleFlowDirections(World worldIn, BlockPos pos) {
		int i = 1000;
		Set<EnumFacing> set = EnumSet.<EnumFacing>noneOf(EnumFacing.class);

		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
			BlockPos blockpos = pos.offset(enumfacing);
			IBlockState iblockstate = worldIn.getBlockState(blockpos);

			if (!this.isBlocked(worldIn, blockpos, iblockstate) && (iblockstate.getMaterial() != this.blockMaterial
					|| ((Integer) iblockstate.getValue(LEVEL)).intValue() > 0)) {
				int j;

				if (this.isBlocked(worldIn, blockpos.down(), worldIn.getBlockState(blockpos.down()))) {
					j = this.getSlopeDistance(worldIn, blockpos, 1, enumfacing.getOpposite());
				} else {
					j = 0;
				}

				if (j < i) {
					set.clear();
				}

				if (j <= i) {
					set.add(enumfacing);
					i = j;
				}
			}
		}

		return set;
	}

	private int getSlopeDistance(World worldIn, BlockPos pos, int distance, EnumFacing calculateFlowCost) {
		int i = 1000;

		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
			if (enumfacing != calculateFlowCost) {
				BlockPos blockpos = pos.offset(enumfacing);
				IBlockState iblockstate = worldIn.getBlockState(blockpos);

				if (!this.isBlocked(worldIn, blockpos, iblockstate) && (iblockstate.getMaterial() != this.blockMaterial
						|| ((Integer) iblockstate.getValue(LEVEL)).intValue() > 0)) {
					if (!this.isBlocked(worldIn, blockpos.down(), iblockstate)) {
						return distance;
					}

					if (distance < 4) {
						int j = this.getSlopeDistance(worldIn, blockpos, distance + 1, enumfacing.getOpposite());

						if (j < i) {
							i = j;
						}
					}
				}
			}
		}

		return i;
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.checkForMixing(worldIn, pos, state)) {
			worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
		}
	}

}
