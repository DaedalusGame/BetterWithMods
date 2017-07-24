package betterwithmods.common.blocks.mechanical.tile;

import betterwithmods.BWMod;
import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockAnchor;
import betterwithmods.common.blocks.BlockRope;
import betterwithmods.common.blocks.mechanical.BlockMechMachines;
import betterwithmods.common.blocks.tile.SimpleStackHandler;
import betterwithmods.common.blocks.tile.TileEntityVisibleInventory;
import betterwithmods.common.entity.EntityExtendingRope;
import betterwithmods.common.registry.PulleyStructureManager;
import betterwithmods.module.GlobalConfig;
import betterwithmods.util.InvUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailBase.EnumRailDirection;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import java.util.*;

public class TileEntityPulley extends TileEntityVisibleInventory implements IMechanicalPower {

    private EntityExtendingRope rope;
    private NBTTagCompound ropeTag = null;
    private int power;


    public boolean isMechanicallyPowered() {
        return power > 0;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    public boolean isRaising() {
        return !MechanicalUtil.isRedstonePowered(world, pos) && isMechanicallyPowered();
    }

    public boolean isLowering() {
        return !MechanicalUtil.isRedstonePowered(world, pos) && !isMechanicallyPowered();
    }

    @Override
    public int getInventorySize() {
        return 4;
    }

    @Override
    public SimpleStackHandler createItemStackHandler() {
        return super.createItemStackHandler();
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
        if (this.getWorld().isRemote)
            return;
        tryNextOperation();
    }

    private void tryNextOperation() {
        this.power = calculateInput();

        if (!activeOperation() && this.getWorld().getBlockState(this.pos).getBlock() instanceof BlockMechMachines) {
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
                BlockPos lowest = BlockRope.getLowestRopeBlock(getWorld(), pos);
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
                BlockPos newPos = BlockRope.getLowestRopeBlock(getWorld(), pos).down();
                IBlockState state = getWorld().getBlockState(newPos);
                boolean flag = !isMoving && state.getBlock() == BWMBlocks.ANCHOR
                        && ((BlockAnchor) BWMBlocks.ANCHOR).getFacingFromBlockState(state) == EnumFacing.UP;
                if (newPos.getY() > 0 && (getWorld().isAirBlock(newPos) || state.getBlock().isReplaceable(getWorld(), newPos) || flag)
                        && newPos.up().getY() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void goUp() {
        BlockPos lowest = BlockRope.getLowestRopeBlock(getWorld(), pos);
        IBlockState state = getWorld().getBlockState(lowest.down());
        boolean flag = state.getBlock() == BWMBlocks.ANCHOR
                && ((BlockAnchor) BWMBlocks.ANCHOR).getFacingFromBlockState(state) == EnumFacing.UP;
        rope = new EntityExtendingRope(getWorld(), pos, lowest, lowest.up().getY());
        if (!flag || movePlatform(lowest.down(), true)) {
            getWorld().playSound(null, pos.down(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS,
                    0.4F + (getWorld().rand.nextFloat() * 0.1F), 1.0F);
            getWorld().spawnEntity(rope);
            getWorld().setBlockToAir(lowest);
            putRope(true);
        } else {
            rope = null;
        }
    }

    private void goDown() {
        BlockPos newPos = BlockRope.getLowestRopeBlock(getWorld(), pos).down();
        IBlockState state = getWorld().getBlockState(newPos);
        boolean flag = state.getBlock() == BWMBlocks.ANCHOR
                && ((BlockAnchor) BWMBlocks.ANCHOR).getFacingFromBlockState(state) == EnumFacing.UP;
        rope = new EntityExtendingRope(getWorld(), pos, newPos.up(), newPos.getY());
        if (!flag || movePlatform(newPos, false)) {
            getWorld().spawnEntity(rope);
        } else {
            rope = null;
        }
    }

    /**
     * Turns the platform into entities and moves them with the rope
     */

    private boolean movePlatform(BlockPos anchor, boolean up) {
        IBlockState state = getWorld().getBlockState(anchor);
        if (state.getBlock() != BWMBlocks.ANCHOR)
            return false;

        HashSet<BlockPos> platformBlocks = new HashSet<>();
        platformBlocks.add(anchor);
        boolean success;
        BlockPos below = anchor.down();
        if (isPlatform(below)) {
            success = addToList(platformBlocks, below, up);
        } else {
            success = up || isIgnoreable(below);
        }
        if (!success) {
            return false;
        }

        for (BlockPos blockPos : platformBlocks) {
            Arrays.asList(new BlockPos[]{blockPos.north(), blockPos.south()}).forEach(p -> {
                if (!platformBlocks.contains(p)) {
                    fixRail(p, EnumRailDirection.ASCENDING_NORTH, EnumRailDirection.ASCENDING_SOUTH);
                }
            });
            Arrays.asList(new BlockPos[]{blockPos.east(), blockPos.west()}).forEach(p -> {
                if (!platformBlocks.contains(p)) {
                    fixRail(p, EnumRailDirection.ASCENDING_EAST, EnumRailDirection.ASCENDING_WEST);
                }
            });
        }

        if (!getWorld().isRemote) {
            for (BlockPos blockPos : platformBlocks) {
                IBlockState blockState = getWorld().getBlockState(blockPos.up());
                Vec3i offset = blockPos.subtract(anchor.up());
                rope.addBlock(offset, getWorld().getBlockState(blockPos));
                if (isMoveableBlock(blockPos.up())) {
                    rope.addBlock(new Vec3i(offset.getX(), offset.getY() + 1, offset.getZ()), blockState);
                    getWorld().setBlockToAir(blockPos.up());
                }
                getWorld().setBlockToAir(blockPos);
            }
        }

        return true;
    }

    public boolean isIgnoreable(BlockPos pos) {
        return world.isAirBlock(pos) || world.getBlockState(pos).getMaterial().isReplaceable();
    }

    public boolean isMoveableBlock(BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == Blocks.REDSTONE_WIRE || state.getBlock() instanceof BlockRailBase;
    }

    public boolean isPlatform(BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return PulleyStructureManager.isPulleyBlock(state);
    }

    @SuppressWarnings("unchecked")
    private void fixRail(BlockPos rail, EnumRailDirection... directions) {
        List<EnumRailDirection> list = Arrays.asList(directions);
        IBlockState state = getWorld().getBlockState(rail);
        if (getWorld().getBlockState(rail).getBlock() instanceof BlockRailBase) {
            PropertyEnum<EnumRailDirection> shape = null;
            for (IProperty<?> p : state.getPropertyKeys()) {
                if ("shape".equals(p.getName()) && p instanceof PropertyEnum<?>) {
                    shape = (PropertyEnum<EnumRailDirection>) p;
                    break;
                }
            }

            if (shape != null) {
                EnumRailDirection currentShape = state.getValue(shape);
                if (list.contains(currentShape)) {
                    getWorld().setBlockState(rail, state.withProperty(shape, flatten(currentShape)), 6);
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
        if (set.size() > GlobalConfig.maxPlatformBlocks)
            return false;
        if (!PulleyStructureManager.isPulleyBlock(getWorld().getBlockState(p))) {
            return true;
        }

        BlockPos blockCheck = up ? p.up() : p.down();

        if (!isMoveableBlock(blockCheck) || !isIgnoreable(blockCheck) || PulleyStructureManager.isPulleyBlock(getWorld().getBlockState(blockCheck)))
            return false;
        if (!set.contains(blockCheck)) {
            return false;
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
        return InvUtils.consumeItemsInInventory(inventory, new ItemStack(BWMBlocks.ROPE), 1, !flag);
    }

    private boolean putRope(boolean flag) {
        return InvUtils.insert(inventory, new ItemStack(BWMBlocks.ROPE, 1), !flag).isEmpty();
    }

    public boolean onJobCompleted(boolean up, int targetY, EntityExtendingRope theRope) {
        BlockPos ropePos = new BlockPos(pos.getX(), targetY - (up ? 1 : 0), pos.getZ());
        IBlockState state = getWorld().getBlockState(ropePos);
        if (!up) {
            if ((getWorld().isAirBlock(ropePos) || state.getBlock().isReplaceable(getWorld(), ropePos)) && BWMBlocks.ROPE.canPlaceBlockAt(getWorld(), ropePos) && takeRope(true)) {
                getWorld().playSound(null, pos.down(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 0.4F, 1.0F);
                getWorld().setBlockState(ropePos, BWMBlocks.ROPE.getDefaultState());
            } else {
                tryNextOperation();
                theRope.setDead();
                return false;
            }
        }
        if ((theRope.getUp() ? canGoUp() : canGoDown(true)) && !theRope.isPathBlocked()) {
            theRope.setTargetY(targetY + (theRope.getUp() ? 1 : -1));
            if (up) {
                if (!getWorld().isAirBlock(ropePos.up())) {
                    getWorld().playSound(null, pos.down(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS,
                            0.4F + (getWorld().rand.nextFloat() * 0.1F), 1.0F);
                    getWorld().setBlockToAir(ropePos.up());
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

        tag.setInteger("power", power);
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.ropeTag = (NBTTagCompound) tag.getTag("Rope");
        this.power = tag.getInteger("power");
    }

    @Override
    public void setWorld(World worldIn) {
        super.setWorld(worldIn);
        if (rope == null && !worldIn.isRemote && ropeTag != null && !ropeTag.hasNoTags()) {
            NBTTagList pos = (NBTTagList) ropeTag.getTag("Pos");
            if (pos != null) {
                rope = (EntityExtendingRope) AnvilChunkLoader.readWorldEntityPos(ropeTag, getWorld(), pos.getDoubleAt(0),
                        pos.getDoubleAt(1), pos.getDoubleAt(2), true);
            }
        }
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        return -1;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        if (facing.getAxis().isHorizontal())
            return MechanicalUtil.getPowerOutput(world, pos.offset(facing), facing.getOpposite());
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 1;
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityMechanicalPower.MECHANICAL_POWER)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nonnull
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityMechanicalPower.MECHANICAL_POWER)
            return CapabilityMechanicalPower.MECHANICAL_POWER.cast(this);
        return super.getCapability(capability, facing);
    }

}
