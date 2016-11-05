package betterwithmods.blocks;

import betterwithmods.BWMBlocks;
import betterwithmods.api.block.ITurnable;
import betterwithmods.blocks.BlockMechMachines.EnumType;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAnchor extends BlockDirectional implements ITurnable {
    public static final PropertyBool LINKED = PropertyBool.create("linked");
    private static final float HEIGHT = 0.375F;
    private static final AxisAlignedBB D_AABB = new AxisAlignedBB(0.0F, 1.0F - HEIGHT, 0.0F, 1.0F, 1.0F, 1.0F);
    private static final AxisAlignedBB U_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, HEIGHT, 1.0F);
    private static final AxisAlignedBB N_AABB = new AxisAlignedBB(0.0F, 0.0F, 1.0F - HEIGHT, 1.0F, 1.0F, 1.0F);
    private static final AxisAlignedBB S_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, HEIGHT);
    private static final AxisAlignedBB W_AABB = new AxisAlignedBB(1.0F - HEIGHT, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    private static final AxisAlignedBB E_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, HEIGHT, 1.0F, 1.0F);

    public BlockAnchor() {
        super(Material.ROCK);
        setCreativeTab(BWCreativeTabs.BWTAB);
        this.setHardness(2.0F);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);
        switch (facing) {
            case DOWN:
                return D_AABB;
            case UP:
                return U_AABB;
            case NORTH:
                return N_AABB;
            case SOUTH:
                return S_AABB;
            case WEST:
                return W_AABB;
            case EAST:
            default:
                return E_AABB;
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ, int meta, EntityLivingBase entity, ItemStack stack) {
        IBlockState state = super.getStateForPlacement(world, pos, side, flX, flY, flZ, meta, entity, stack);
        return this.setFacingInBlock(state, side);
    }

    @Override
    public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing) {
        return state.withProperty(FACING, facing);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        BlockPos down = pos.down();

        if (heldItem != null) {
            if (heldItem.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) heldItem.getItem()).getBlock();
                if (block == BWMBlocks.ROPE) {
                    if (!world.isRemote) {
                        if (world.getBlockState(down).getBlock() == BWMBlocks.ROPE) {
                            BlockRope.placeRopeUnder(heldItem, world, down, player);
                        } else if (world.getBlockState(down).getBlock().isReplaceable(world, down) || world.isAirBlock(down)) {
                            world.setBlockState(down, BWMBlocks.ROPE.getDefaultState());
                            if (!player.capabilities.isCreativeMode)
                                heldItem.stackSize--;
                        } else
                            return false;
                    }
                    return true;
                }
            }
            return false;
        } else if (!world.isRemote)
            retractRope(world, pos, player);
        return true;
    }

    @Override
    public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos) {
        EnumFacing facing = getFacingFromBlockState(world.getBlockState(pos));
        return facing != EnumFacing.UP && facing != EnumFacing.DOWN;
    }

    @Override
    public boolean canRotateHorizontally(IBlockAccess world, BlockPos pos) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canRotateVertically(IBlockAccess world, BlockPos pos) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void rotateAroundYAxis(World world, BlockPos pos, boolean reverse) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        EnumFacing facing = getFacingFromBlockState(world.getBlockState(pos));
        return side == facing.getOpposite();
    }

    @Override
    public EnumFacing getFacingFromBlockState(IBlockState state) {
        return state.getValue(FACING);
    }

    private void retractRope(World world, BlockPos pos, EntityPlayer player) {
        for (int i = pos.getY() - 1; i >= 0; i--) {
            BlockPos pos2 = new BlockPos(pos.getX(), i, pos.getZ());
            if (world.getBlockState(pos2).getBlock() != BWMBlocks.ROPE && world.getBlockState(pos2.up()).getBlock() == BWMBlocks.ROPE) {
                if (!player.capabilities.isCreativeMode)
                    addRopeToInv(world, pos, player);
                world.setBlockToAir(pos2.up());
                break;
            } else if (world.getBlockState(pos2).getBlock() != BWMBlocks.ROPE)
                break;
        }
    }

    private void addRopeToInv(World world, BlockPos pos, EntityPlayer player) {
        ItemStack rope = new ItemStack(BWMBlocks.ROPE);

        if (player.inventory.addItemStackToInventory(rope))
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        else
            InvUtils.ejectStackWithOffset(world, pos, rope);
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        return entity instanceof EntityPlayer && world.getBlockState(pos).getBlock() == this && world.getBlockState(pos).getValue(FACING) != EnumFacing.DOWN;
    }

    private boolean isRope(IBlockAccess world, BlockPos origin, EnumFacing facing) {
        BlockPos pos = origin.offset(facing);
        return world.getBlockState(pos).getBlock() == BWMBlocks.ROPE;
    }

    private boolean isAnchor(IBlockAccess world, BlockPos origin, EnumFacing facing) {
        BlockPos pos = origin.offset(facing);
        return world.getBlockState(pos).getBlock() == this && world.getBlockState(pos).getValue(FACING) != facing;
    }

    private boolean isPulley(IBlockAccess world, BlockPos origin, EnumFacing facing) {
        BlockPos pos = origin.offset(facing);
        return world.getBlockState(pos).getBlock() == BWMBlocks.SINGLE_MACHINES && world.getBlockState(pos).getValue(BlockMechMachines.MACHINETYPE) == EnumType.PULLEY;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean isConnected;
        switch (state.getValue(FACING)) {
            case UP:
                isConnected = isRope(world, pos, EnumFacing.UP) || isAnchor(world, pos, EnumFacing.UP) || isPulley(world, pos, EnumFacing.UP);
                break;
            case DOWN:
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
            default:
                isConnected = isRope(world, pos, EnumFacing.DOWN) || isAnchor(world, pos, EnumFacing.DOWN);
        }
        return state.withProperty(LINKED, isConnected);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, LINKED);
    }
}
