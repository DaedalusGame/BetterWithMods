package betterwithmods.common.blocks;

import betterwithmods.api.block.PropertyObject;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.tile.TileCamo;
import betterwithmods.common.registry.KilnStructureManger;
import betterwithmods.common.registry.blockmeta.managers.KilnManager;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockKiln extends BWMBlock {
    public static final PropertyInteger COOK = PropertyInteger.create("cook", 0, 8);
    public static final PropertyObject<IBlockState> HELD_STATE = new PropertyObject<>("held_state", IBlockState.class);
    public static final PropertyObject<IBlockAccess> HELD_WORLD = new PropertyObject<>("held_world", IBlockAccess.class);
    public static final PropertyObject<BlockPos> HELD_POS = new PropertyObject<>("held_pos", BlockPos.class);

    public BlockKiln() {
        super(Material.ROCK);
        this.setTickRandomly(true);
        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public int tickRate(World world) {
        return 20;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, 20, 5);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int oldCookTime = getCookCounter(world, pos);
        BlockPos down = pos.down();
        BlockPos up = pos.up();
        if (world.getBlockState(down).getBlock() != BWMBlocks.STOKED_FLAME) {
            Block block = world.getBlockState(down).getBlock();
            int meta = block.damageDropped(world.getBlockState(down));

            boolean intenseFlame = BWMHeatRegistry.contains(block, meta) && BWMHeatRegistry.get(block, meta).value > 4;

            if (!intenseFlame) {
                world.setBlockState(pos, world.getBlockState(pos).withProperty(COOK, 0));

                if (oldCookTime > 0)
                    world.sendBlockBreakProgress(0, up, -1);
                return;
            }
        }

        int currentTickRate = 20;

        boolean canCook = false;
        Block above = world.getBlockState(up).getBlock();
        int aboveMeta = world.getBlockState(up).getBlock().damageDropped(world.getBlockState(up));
        if (!world.isAirBlock(up) && KilnManager.INSTANCE.contains(above, aboveMeta)) {
            if (KilnStructureManger.isValidKiln(world, pos))
                canCook = true;
        }

        if (canCook) {
            int newCookTime = oldCookTime + 1;

            if (newCookTime > 7) {
                newCookTime = 0;
                cookBlock(world, pos.up());
                setCookCounter(world, pos, 0);
            } else {
                if (newCookTime > 0) {
                    world.sendBlockBreakProgress(0, up, newCookTime + 2);
                }

                currentTickRate = calculateTickRate(world, pos);
            }

            setCookCounter(world, pos, newCookTime);

            if (newCookTime == 0) {
                world.sendBlockBreakProgress(0, up, -1);
                setCookCounter(world, pos, 0);
                world.scheduleBlockUpdate(pos, this, tickRate(world), 5);// world.markBlockForUpdate(pos);
            }
        } else if (oldCookTime != 0) {
            world.sendBlockBreakProgress(0, up, -1);
            setCookCounter(world, pos, 0);
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
        }

        world.scheduleBlockUpdate(pos, this, currentTickRate, 5);
    }

    private int calculateTickRate(IBlockAccess world, BlockPos pos) {
        int secondaryFire = 0;
        for (int xP = -1; xP < 2; xP++) {
            for (int zP = -1; zP < 2; zP++) {
                if (xP != 0 || zP != 0) {
                    BlockPos bPos = pos.add(xP, -1, zP);
                    if (BWMHeatRegistry.contains(world.getBlockState(bPos).getBlock(),
                            world.getBlockState(bPos).getBlock().damageDropped(world.getBlockState(bPos)))) {
                        if (BWMHeatRegistry.get(world.getBlockState(bPos).getBlock(), world.getBlockState(bPos)
                                .getBlock().damageDropped(world.getBlockState(bPos))).value > 4)
                            secondaryFire++;
                    }
                }
            }
        }
        return 60 * (8 - secondaryFire) / 8 + 20;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        int cookTime = getCookCounter(world, pos);
        BlockPos above = pos.up();
        BlockPos below = pos.down();
        IBlockState aboveBlock = world.getBlockState(above);
        IBlockState belowBlock = world.getBlockState(below);
        if (cookTime > 0) {
            if (!KilnManager.INSTANCE.contains(aboveBlock.getBlock(), aboveBlock.getBlock().damageDropped(aboveBlock))) {
                if (!BWMHeatRegistry.contains(belowBlock.getBlock(), belowBlock.getBlock().damageDropped(belowBlock))
                        || (BWMHeatRegistry.contains(belowBlock.getBlock(), belowBlock.getBlock().damageDropped(state))
                        && BWMHeatRegistry.get(belowBlock.getBlock(),
                        belowBlock.getBlock().damageDropped(belowBlock)).value < 5)) {
                    if (getCookCounter(world, pos) > 0) {
                        world.sendBlockBreakProgress(0, above, -1);
                        setCookCounter(world, pos, 0);
                    }
                }
            }
        }
        world.scheduleBlockUpdate(pos, this, 20, 5);
    }

    @Override
    public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canRotateHorizontally(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canRotateVertically(IBlockAccess world, BlockPos pos) {
        return true;
    }

    public int getCookCounterFromMeta(int meta) {
        return meta & 0x7;
    }

    public int getCookCounter(IBlockAccess world, BlockPos pos) {
        return getCookCounterFromMeta(world.getBlockState(pos).getValue(COOK));
    }

    public void setCookCounter(World world, BlockPos pos, int cookCounter) {
        world.setBlockState(pos, world.getBlockState(pos).withProperty(COOK, cookCounter));
    }

    private void cookBlock(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        int meta = world.getBlockState(pos).getBlock().damageDropped(world.getBlockState(pos));
        if (block != null) {
            if (KilnManager.INSTANCE.contains(block, meta)) {
                List<ItemStack> result = KilnManager.INSTANCE.getProducts(block, meta);
                InvUtils.ejectStackWithOffset(world, pos, result);
                world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(COOK, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(COOK);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{COOK}, new IUnlistedProperty[]{HELD_WORLD, HELD_POS, HELD_STATE});
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        state = ((IExtendedBlockState) state).withProperty(HELD_WORLD, world)
                .withProperty(HELD_POS, pos);
        TileEntity te = world instanceof ChunkCache ? ((ChunkCache) world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (te instanceof TileCamo) {
            TileCamo tile = (TileCamo) te;
            IExtendedBlockState camo = ((IExtendedBlockState) state).withProperty(HELD_STATE, tile.camoState);
            return camo;
        } else {
            return state;
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCamo();
    }

}
