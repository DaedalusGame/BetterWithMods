package betterwithmods.common.blocks.mechanical;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BWMBlock;
import betterwithmods.util.DirUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockMillGenerator extends BWMBlock {
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
    static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
    static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);

    public BlockMillGenerator(Material material) {
        super(material);
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Z));
    }


    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS);
    }


    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AXIS).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AXIS,DirUtils.getAxis(meta));
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(AXIS)) {
            case X:
                return X_AABB;
            case Y:
                return Y_AABB;
            case Z:
            default:
                return Z_AABB;
        }
    }

    @Override
    public int tickRate(World world) {
        return 20;
    }

    @Override
    public abstract ItemStack getItem(World worldIn, BlockPos pos, IBlockState state);

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(BWMBlocks.WOODEN_AXLE));
        drops.add(getItem((World) world,pos,state));
        return drops;
    }

    public IBlockState getAxisState(EnumFacing.Axis axis) {
        return this.getDefaultState().withProperty(AXIS, axis);
    }

    public EnumFacing getAxleDirectionFromState(IBlockState state) {
        EnumFacing.Axis axis = state.getValue(AXIS);
        switch (axis) {
            case Y:
                return EnumFacing.UP;
            case Z:
                return EnumFacing.SOUTH;
            case X:
            default:
                return EnumFacing.EAST;
        }
    }

}
