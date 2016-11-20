package betterwithmods.blocks;

import betterwithmods.BWMBlocks;
import betterwithmods.api.block.IAxle;
import betterwithmods.api.block.IMechanical;
import betterwithmods.util.DirUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
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

public abstract class BlockMillGenerator extends BWMBlock implements IMechanical, IAxle {
    public static final PropertyBool ISACTIVE = PropertyBool.create("ison");
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
    static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
    static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);

    public BlockMillGenerator(Material material) {
        super(material);
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Z).withProperty(ISACTIVE, false));
    }

    @Override
    public int getPowerLevel(IBlockAccess world, BlockPos pos) {
        return getPowerLevelFromState(world.getBlockState(pos));
    }

    public int getPowerLevelFromState(IBlockState state) {
        return state.getValue(ISACTIVE) ? 4 : 0;
    }

    @Override
    public int getMechPowerLevelToFacing(World world, BlockPos pos,
                                         EnumFacing dir) {
        if (world.getBlockState(pos).getValue(ISACTIVE)) {
            EnumFacing.Axis axis = world.getBlockState(pos).getValue(AXIS);
            if (dir.getAxis() == axis) return 4;
        }
        return 0;
    }

    @Override
    public boolean isAxleOrientedToFacing(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        EnumFacing.Axis axis = world.getBlockState(pos).getValue(AXIS);
        return dir.getAxis() == axis;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, ISACTIVE);
    }


    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = DirUtils.getLegacyAxis(state.getValue(AXIS));
        if (state.getValue(ISACTIVE))
            meta += 8;
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean active = meta > 7;
        if (active)
            meta -= 8;
        return this.getAxisState(DirUtils.getAxisFromLegacy(meta)).withProperty(ISACTIVE, active);
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

    public abstract ItemStack getGenStack(IBlockState state);

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(BWMBlocks.AXLE));
        drops.add(getGenStack(state));
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

    public EnumFacing getAxleDirection(World world, BlockPos pos) {
        EnumFacing.Axis axis = world.getBlockState(pos).getValue(AXIS);
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

    @Override
    public int getAxisAlignment(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this)
            return DirUtils.getLegacyAxis(state.getValue(AXIS));
        return DirUtils.getLegacyAxis(EnumFacing.Axis.Y);
    }

    @Override
    public boolean isMechanicalJunction() {
        return true;
    }
}
