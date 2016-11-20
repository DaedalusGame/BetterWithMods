package betterwithmods.blocks;

import betterwithmods.BWMBlocks;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.util.DirUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author mrebhan
 */

public class BlockPump extends BWMBlock implements IMechanicalBlock, IMultiVariants {

    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockPump() {
        super(Material.WOOD);
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setTickRandomly(true);
        this.setHardness(3.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DirUtils.HORIZONTAL, EnumFacing.NORTH)
                .withProperty(ACTIVE, false));
        setSoundType(SoundType.WOOD);
    }

    public static boolean hasWaterToPump(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        EnumFacing direction = state.getValue(DirUtils.HORIZONTAL);
        BlockPos source = DirUtils.movePos(pos, direction);
        IBlockState sourceState = world.getBlockState(source);
        Block block = sourceState.getBlock();
        return block == Blocks.WATER || block == Blocks.FLOWING_WATER;
    }

    @Override
    public String[] getVariants() {
        return new String[]{"active=false,facing=north"};
    }

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public boolean isInputtingMechPower(World world, BlockPos pos) {
        return MechanicalUtil.isBlockPoweredByAxle(world, pos, this) || MechanicalUtil.isPoweredByCrank(world, pos);
    }

    @Override
    public boolean isOutputtingMechPower(World world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canInputPowerToSide(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        return dir == EnumFacing.DOWN;
    }

    @Override
    public void overpower(World world, BlockPos pos) {

    }

    @Override
    public boolean isMechanicalOn(IBlockAccess world, BlockPos pos) {
        return isMechanicalOnFromState(world.getBlockState(pos));
    }

    @Override
    public void setMechanicalOn(World world, BlockPos pos, boolean isOn) {
        if (isOn != world.getBlockState(pos).getValue(ACTIVE)) {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(ACTIVE, isOn));
        }
    }

    @Override
    public boolean isMechanicalOnFromState(IBlockState state) {
        return state.getValue(ACTIVE);
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
        return false;
    }

    @Override
    public void rotateAroundYAxis(World world, BlockPos pos, boolean reverse) {
        DirUtils.rotateAroundY(this, world, pos, reverse);
    }

    @Override
    public int tickRate(World world) {
        return 5;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ,
                                            int meta, EntityLivingBase entity) {
        IBlockState state = super.getStateForPlacement(world, pos, side, flX, flY, flZ, meta, entity);
        return setFacingInBlock(state, DirUtils.convertEntityOrientationToFlatFacing(entity, side));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
                                ItemStack stack) {
        EnumFacing facing = DirUtils.convertEntityOrientationToFlatFacing(entity, EnumFacing.NORTH);
        setFacingInBlock(state, facing);
    }

    @Override
    public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing) {
        return state.withProperty(DirUtils.HORIZONTAL, facing);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean isActive = false;
        if (meta > 7) {
            isActive = true;
            meta -= 8;
        }
        return this.getDefaultState().withProperty(ACTIVE, isActive).withProperty(DirUtils.HORIZONTAL,
                EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(ACTIVE) ? 8 : 0;
        return meta + state.getValue(DirUtils.HORIZONTAL).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.HORIZONTAL, ACTIVE);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (isMechanicalOnFromState(state)) {
            if (world.isAirBlock(pos.up()) && hasWaterToPump(world, pos)) {
                world.setBlockState(pos.up(), BWMBlocks.TEMP_LIQUID_SOURCE.getDefaultState());
            }
        }

        setMechanicalOn(world, pos, isInputtingMechPower(world, pos));
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

}
