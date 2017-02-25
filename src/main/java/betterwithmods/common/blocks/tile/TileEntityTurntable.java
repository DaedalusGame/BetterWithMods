package betterwithmods.common.blocks.tile;

import betterwithmods.common.BWMBlocks;
import betterwithmods.api.block.ITurnable;
import betterwithmods.common.blocks.BlockMechMachines;
import betterwithmods.common.registry.TurntableInteraction;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import betterwithmods.util.RecipeUtils;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityTurntable extends TileEntity implements IMechSubtype, ITickable {
    private static final int[] ticksToRotate = {10, 20, 40, 80};
    public byte timerPos = 0;
    private int potteryRotation = 0;
    private boolean potteryRotated = false;
    private double[] offsets = {0.25D, 0.375D, 0.5D, 0.625D};
    private boolean asynchronous = false;
    private int rotationTime = 0;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("SwitchSetting")) {
            this.timerPos = tag.getByte("SwitchSetting");
            if (this.timerPos > 3)
                this.timerPos = 3;
        }
        if (tag.hasKey("PotteryRotation"))
            this.potteryRotation = tag.getInteger("PotteryRotation");
        if (tag.hasKey("Asynchronous"))
            this.asynchronous = tag.getBoolean("Asynchronous");
        if (tag.hasKey("RotationTime"))
            this.rotationTime = tag.getInteger("RotationTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("PotteryRotation", this.potteryRotation);
        tag.setByte("SwitchSetting", this.timerPos);
        tag.setBoolean("Asynchronous", this.asynchronous);
        if (this.asynchronous || this.rotationTime != 0)
            tag.setInteger("RotationTime", this.rotationTime);
        return tag;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void update() {
        if (!this.getWorld().isRemote) {
            if (getWorld().getBlockState(pos).getBlock() != null && getWorld().getBlockState(pos).getBlock() instanceof BlockMechMachines && ((BlockMechMachines) getWorld().getBlockState(pos).getBlock()).isMechanicalOn(getWorld(), pos)) {
                if (!asynchronous && getWorld().getTotalWorldTime() % (long) ticksToRotate[timerPos] == 0) {
                    this.getWorld().playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.05F, 1.0F);
                    rotateTurntable();
                } else if (asynchronous) {
                    rotationTime++;
                    if (rotationTime >= ticksToRotate[timerPos]) {
                        rotationTime = 0;
                        this.getWorld().playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.05F, 1.0F);
                        rotateTurntable();
                    }
                }
            }
        }
    }

    public boolean processRightClick(EntityPlayer player) {
        if (player.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY) {
            if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.CLOCK) {
                toggleAsynchronous(player);
                return true;
            }
        } else if (player.getHeldItemMainhand() == ItemStack.EMPTY && player.getHeldItemOffhand() == ItemStack.EMPTY) {
            advanceTimerPos();
            getWorld().scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(getWorld()), 5);
            getWorld().playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
            return true;
        }
        return false;
    }

    public void toggleAsynchronous(EntityPlayer player) {
        if (!this.getWorld().getGameRules().getBoolean("doDaylightCycle")) {
            if (!asynchronous) {
                this.asynchronous = true;
            } else if (player != null) {
                player.sendStatusMessage(new TextComponentTranslation("message.bwm:async.unavailable"), false);
            }
        } else {
            boolean isSneaking = player.isSneaking();
            String isOn = "enabled";
            boolean async = !this.asynchronous;
            if ((!async && !isSneaking) || (async && isSneaking))
                isOn = "disabled";
            player.sendStatusMessage(new TextComponentTranslation("message.bwm:async." + isOn), false);
            if (!isSneaking) {
                this.getWorld().playSound(null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.05F, 1.0F);
                this.asynchronous = async;
            }
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = this.getUpdateTag();
        return new SPacketUpdateTileEntity(pos, this.getBlockMetadata(), tag);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager mgr, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.getNbtCompound();
        this.readFromNBT(tag);
        IBlockState state = getWorld().getBlockState(this.pos);
        this.getWorld().notifyBlockUpdate(this.pos, state, state, 3);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    public void rotateTurntable() {
        boolean reverse = ((BlockMechMachines) getWorld().getBlockState(pos).getBlock()).isRedstonePowered(getWorld(), pos);

        this.potteryRotated = false;

        for (int tempY = 1; tempY < 3; tempY++) {
            BlockPos searchPos = pos.add(0, tempY, 0);
            boolean canTransmitHorizontally = canBlockTransmitRotationHorizontally(searchPos);
            boolean canTransmitVertically = canBlockTransmitRotationVertically(searchPos);
            rotateBlock(searchPos, reverse);

            if (canTransmitHorizontally)
                rotateBlocksAttached(searchPos, reverse);

            if (!canTransmitVertically)
                break;
        }

        if (!potteryRotated)
            potteryRotation = 0;

        getWorld().neighborChanged(pos, BWMBlocks.SINGLE_MACHINES, pos);
    }

    public byte getTimerPos() {
        return timerPos;
    }

    public double getOffset() {
        return offsets[this.timerPos];
    }

    public void advanceTimerPos() {
        timerPos++;
        if (timerPos > 3)
            timerPos = 0;
        IBlockState state = getWorld().getBlockState(pos);
        getWorld().notifyBlockUpdate(pos, state, state, 3);
    }

    private boolean canBlockTransmitRotationHorizontally(BlockPos pos) {
        Block target = getWorld().getBlockState(pos).getBlock();

        if (target instanceof ITurnable) {
            return ((ITurnable) target).canRotateHorizontally(getWorld(), pos);
        }
        if (target == Blocks.GLASS || target == Blocks.STAINED_GLASS)
            return true;
        if (target instanceof BlockPistonBase) {
            IBlockState state = getWorld().getBlockState(pos);

            return !state.getValue(BlockPistonBase.EXTENDED);
        }
        if (target == Blocks.PISTON_EXTENSION || target == Blocks.PISTON_HEAD)
            return false;

        return this.getWorld().isBlockNormalCube(pos, false);
    }

    private boolean canBlockTransmitRotationVertically(BlockPos pos) {
        Block target = getWorld().getBlockState(pos).getBlock();

        if (target instanceof ITurnable) {
            return ((ITurnable) target).canRotateVertically(getWorld(), pos);
        }
        if (target == Blocks.GLASS)
            return true;
        if (target == Blocks.CLAY)
            return false;
        if (target instanceof BlockPistonBase) {
            IBlockState state = getWorld().getBlockState(pos);

            return !state.getValue(BlockPistonBase.EXTENDED);
        }
        if (target == Blocks.PISTON_EXTENSION || target == Blocks.PISTON_HEAD)
            return false;

        return this.getWorld().isBlockNormalCube(pos, false);
    }

    private void rotateBlocksAttached(BlockPos pos, boolean reverse) {
        Block[] newBlocks = new Block[4];
        for (int i = 0; i < 4; i++)
            newBlocks[i] = null;

        for (int i = 2; i < 6; i++) {
            BlockPos offset = pos.offset(EnumFacing.getFront(i));

            Block block = getWorld().getBlockState(offset).getBlock();
            IBlockState state = getWorld().getBlockState(offset);
            boolean attached = false;

            if (block instanceof BlockTorch) {
                EnumFacing facing = state.getValue(BlockTorch.FACING);
                if ((facing == EnumFacing.getFront(i))) {
                    attached = true;

                    if (block == Blocks.UNLIT_REDSTONE_TORCH)
                        block = Blocks.REDSTONE_TORCH;
                }
            } else if (block instanceof BlockLadder) {
                int meta = state.getValue(BlockLadder.FACING).getIndex();
                if (meta == i)
                    attached = true;
            } else if (block == Blocks.WALL_SIGN) {
                int meta = state.getValue(BlockWallSign.FACING).getIndex();
                if (meta == i) {
                    block.dropBlockAsItem(getWorld(), offset, state, 0);
                    this.getWorld().setBlockToAir(offset);
                }
            } else if (block instanceof BlockButton) {
                EnumFacing facing = state.getValue(BlockButton.FACING);
                if ((facing == EnumFacing.getFront(i))) {
                    block.dropBlockAsItem(getWorld(), offset, state, 0);
                    this.getWorld().setBlockToAir(offset);
                }
            } else if (block == Blocks.LEVER) {
                BlockLever.EnumOrientation facing = state.getValue(BlockLever.FACING);
                if (facing.getFacing() == EnumFacing.getFront(i)) {
                    block.dropBlockAsItem(getWorld(), offset, state, 0);
                    this.getWorld().setBlockToAir(offset);
                }
            }

            if (attached) {
                EnumFacing destFacing = DirUtils.rotateFacingAroundY(EnumFacing.getFront(i), reverse);
                newBlocks[destFacing.ordinal() - 2] = block;
                getWorld().setBlockToAir(offset);
            }
        }

        for (int i = 0; i < 4; i++) {
            Block block = newBlocks[i];

            if (block != null) {
                int facing = i + 2;
                int meta;

                BlockPos offset = pos.offset(EnumFacing.getFront(facing));
                IBlockState state = getWorld().getBlockState(offset);
                if (block instanceof BlockTorch) {
                    int targetFacing = 0;

                    if (facing == 2)
                        targetFacing = 4;
                    else if (facing == 3)
                        targetFacing = 3;
                    else if (facing == 4)
                        targetFacing = 2;
                    else if (facing == 5)
                        targetFacing = 1;
                    meta = targetFacing;
                    state = ((BlockTorch) block).getStateFromMeta(meta);
                } else if (block instanceof BlockLadder) {
                    meta = facing;
                    state = ((BlockLadder) block).getStateFromMeta(meta);
                }

                if (getWorld().getBlockState(offset).getBlock().isReplaceable(getWorld(), offset))
                    getWorld().setBlockState(offset, state);
                else {
                    EnumFacing oldFacing = DirUtils.rotateFacingAroundY(EnumFacing.getFront(facing), !reverse);
                    BlockPos oldPos = pos.offset(oldFacing);
                    block.dropBlockAsItem(getWorld(), oldPos, state, 0);
                }
            }
        }
    }

    private void rotateBlock(BlockPos pos, boolean reverse) {
        if (getWorld().isAirBlock(pos))
            return;

        IBlockState state = getWorld().getBlockState(pos);
        ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
        Block target = state.getBlock();
        Rotation rot = reverse ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;

        if (TurntableInteraction.INSTANCE.contains(stack) && TurntableInteraction.INSTANCE.getRecipe(stack) != null) {
            if (target instanceof ITurnable) {
                ITurnable block = (ITurnable) target;
                if (block.canRotateOnTurntable(getWorld(), pos))
                    block.rotateAroundYAxis(getWorld(), pos, reverse);
            } else if (state != state.withRotation(rot))
                getWorld().setBlockState(pos, state.withRotation(rot));
            rotateCraftable(state, (TurntableInteraction.TurntableRecipe) TurntableInteraction.INSTANCE.getRecipe(stack), pos, reverse);
            this.potteryRotated = true;
        } else if (target instanceof ITurnable) {
            ITurnable block = (ITurnable) target;

            if (block.canRotateOnTurntable(getWorld(), pos))
                block.rotateAroundYAxis(getWorld(), pos, reverse);
        } else {
            IBlockState newState = state.withRotation(rot);
            if (!(target instanceof BlockRailBase) && !(target instanceof BlockPistonExtension) && !(target instanceof BlockPistonMoving) && state != newState) {
                if (target instanceof BlockPistonBase) {
                    if (!state.getValue(BlockPistonBase.EXTENDED))
                        getWorld().setBlockState(pos, newState);
                } else
                    getWorld().setBlockState(pos, newState);
            }
            else if (target instanceof BlockRailBase) {
                BlockRailBase rail = (BlockRailBase) target;
                BlockRailBase.EnumRailDirection dir = state.getValue(rail.getShapeProperty());
                if (dir != BlockRailBase.EnumRailDirection.ASCENDING_NORTH && dir != BlockRailBase.EnumRailDirection.ASCENDING_EAST && dir != BlockRailBase.EnumRailDirection.ASCENDING_SOUTH && dir != BlockRailBase.EnumRailDirection.ASCENDING_WEST) {
                    if (state != newState)
                        getWorld().setBlockState(pos, newState);
                }
            }
        }
    }

    private void rotateCraftable(IBlockState input, TurntableInteraction.TurntableRecipe craft, BlockPos pos, boolean reverse) {
        Block block = input.getBlock();
        this.potteryRotation++;
        if (this.potteryRotation > 7) {
            if (!craft.getOutputs().isEmpty() && craft.getOutputs().size() > 0) {
                for (ItemStack scrap : craft.getOutputs()) {
                    InvUtils.ejectStackWithOffset(getWorld(), pos.up(), scrap.copy());
                }
            }
            getWorld().setBlockState(pos, RecipeUtils.getStateFromStack(craft.getResult()));
            this.potteryRotation = 0;
        }
        this.getWorld().spawnParticle(EnumParticleTypes.BLOCK_DUST, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0, Block.getStateId(input));
        this.getWorld().playSound(null, pos, block.getSoundType(input, this.getWorld(), pos, null).getPlaceSound(), SoundCategory.BLOCKS, 0.5F, getWorld().rand.nextFloat() * 0.1F + 0.8F);
    }

    @Override
    public int getSubtype() {
        return this.timerPos + 8;
    }

    @Override
    public void setSubtype(int type) {
        this.timerPos = (byte) Math.min(type, 3);
    }
}
