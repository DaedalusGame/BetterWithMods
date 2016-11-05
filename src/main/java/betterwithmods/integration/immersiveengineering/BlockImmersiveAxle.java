package betterwithmods.integration.immersiveengineering;

import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.blocks.BWMBlock;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockImmersiveAxle extends BWMBlock implements IMechanicalBlock, ITileEntityProvider, IMultiVariants {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyInteger AXLEDIR = PropertyInteger.create("dir", 0, 2);

    private static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
    private static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
    private static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);

    public BlockImmersiveAxle() {
        super(Material.WOOD);
        this.setHardness(3.5F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, false).withProperty(AXLEDIR, 0));
    }

    @Override
    public String[] getVariants() {
        return new String[]{"active=false,dir=0"};
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
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int ori = state.getValue(AXLEDIR);
        switch (ori) {
            case 0:
                return Y_AABB;
            case 1:
                return Z_AABB;
            case 2:
                return X_AABB;
        }
        return null;
    }

    @Deprecated
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ, int meta, EntityLivingBase placer, ItemStack stack) {
        IBlockState state = super.getStateForPlacement(world, pos, side, flX, flY, flZ, meta, placer, stack);
        return setAxisAlignment(state, side);
    }

    private IBlockState setAxisAlignment(IBlockState state, EnumFacing facing) {
        int axis;
        switch (facing) {
            case DOWN:
            case UP:
                axis = 0;
                break;
            case NORTH:
            case SOUTH:
                axis = 1;
                break;
            default:
                axis = 2;
        }
        return state.withProperty(AXLEDIR, axis).withProperty(ACTIVE, false);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, 1, 5);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        validatePower(state, world, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        validatePower(state, world, pos);
    }

    private void validatePower(IBlockState state, World world, BlockPos pos) {
        boolean powered = state.getValue(ACTIVE);
        boolean gettingPower = isInputtingMechPower(world, pos);
        if (powered != gettingPower)
            world.setBlockState(pos, state.withProperty(ACTIVE, gettingPower));
    }

    @Override
    public boolean isMechanicalOnFromState(IBlockState state) {
        return state.getValue(ACTIVE);
    }

    @Override
    public void setMechanicalOn(World world, BlockPos pos, boolean active) {
        IBlockState state = world.getBlockState(pos);
        if (state.getValue(ACTIVE) != active)
            world.setBlockState(pos, state.withProperty(ACTIVE, active));
    }

    @Override
    public boolean isMechanicalOn(IBlockAccess world, BlockPos pos) {
        return isMechanicalOnFromState(world.getBlockState(pos));
    }

    @Override
    public boolean isOutputtingMechPower(World world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    public EnumFacing getFacing(IBlockState state) {
        int axle = state.getValue(AXLEDIR);
        return EnumFacing.getFront(axle * 2);
    }

    @Override
    public boolean canInputPowerToSide(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        EnumFacing dir = EnumFacing.getFront(world.getBlockState(pos).getValue(AXLEDIR) * 2);
        return facing == dir || facing == dir.getOpposite();
    }

    @Override
    public boolean isInputtingMechPower(World world, BlockPos pos) {
        EnumFacing facing = getFacing(world.getBlockState(pos));
        return MechanicalUtil.isBlockPoweredOnSide(world, pos, facing) || MechanicalUtil.isBlockPoweredOnSide(world, pos, facing.getOpposite());
    }

    @Override
    public void overpower(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            if (te instanceof TileEntityImmersiveAxle)
                ((TileEntityImmersiveAxle) te).setOverpowered();
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityImmersiveAxle();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AXLEDIR) + (state.getValue(ACTIVE) ? 8 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean active = meta > 7;
        if (active)
            meta -= 8;
        return this.getDefaultState().withProperty(ACTIVE, active).withProperty(AXLEDIR, meta);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE, AXLEDIR);
    }
}
