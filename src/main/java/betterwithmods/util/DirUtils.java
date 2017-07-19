package betterwithmods.util;

import betterwithmods.api.block.ITurnable;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DirUtils {

    public static final EnumFacing[] X_AXIS = new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST};
    public static final EnumFacing[] Y_AXIS = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};
    public static final EnumFacing[] Z_AXIS = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.SOUTH};
    public static final EnumFacing[][] AXIS_DIRECTIONS = new EnumFacing[][]{X_AXIS, Y_AXIS, Z_AXIS};

    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final PropertyDirection HORIZONTAL = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyDirection TILTING = PropertyDirection.create("facing", facing -> facing != EnumFacing.DOWN);
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool[] DIR_PROP_HORIZ = new PropertyBool[]{NORTH, SOUTH, WEST, EAST};

    public static void setEntityOrientationFacing(EntityLivingBase entity, EnumFacing side) {
        float pitch = 0.0F;
        float yaw = 0.0F;
        switch (side) {
            case UP:
                pitch = 61.0F;
                break;
            case DOWN:
                pitch = -61.0F;
                break;
            case NORTH:
                yaw = 180.0F;
                break;
            case WEST:
                yaw = 90.0F;
                break;
            case SOUTH:
                yaw = 0.0F;
                break;
            case EAST:
                yaw = -90.0F;
                break;
        }
        entity.rotationYaw = yaw;
        entity.rotationPitch = pitch;
        // entity.setAngles(yaw, pitch);
    }

    public static EnumFacing convertEntityOrientationToFacing(EntityLivingBase entity, EnumFacing side) {
        if (entity == null)
            return side;
        float pitch = entity.rotationPitch;

        if (pitch > 60.0F)
            return EnumFacing.UP;
        if (pitch < -60.0F)
            return EnumFacing.DOWN;
        return convertEntityOrientationToFlatFacing(entity, side);
    }

    public static EnumFacing convertEntityOrientationToFlatFacing(EntityLivingBase entity, EnumFacing side) {
        if (entity == null)
            return side;
        return entity.getHorizontalFacing().getOpposite();
    }

    public static EnumFacing convertEntityOrientationToFlatFacing(EntityLivingBase entity) {
        return entity.getHorizontalFacing().getOpposite();
    }

    public static EnumFacing getOpposite(EnumFacing facing) {
        return facing.getOpposite();
    }

    public static EnumFacing rotateFacingAroundY(EnumFacing facing, boolean reverse) {
        if (facing.ordinal() >= 2) {
            switch (facing) {
                case NORTH:
                    facing = EnumFacing.EAST;
                    break;
                case SOUTH:
                    facing = EnumFacing.WEST;
                    break;
                case WEST:
                    facing = EnumFacing.NORTH;
                    break;
                case EAST:
                    facing = EnumFacing.SOUTH;
                    break;
                default:
                    break;
            }

            if (reverse)
                facing = getOpposite(facing);
        }

        return facing;
    }

    public static boolean rotateAroundY(ITurnable block, World world, BlockPos pos, boolean reverse) {
        IBlockState state = world.getBlockState(pos);
        IBlockState newState = rotateStateAroundY(block, state, reverse);
        if (newState != state) {
            world.setBlockState(pos, newState);
            world.markBlockRangeForRenderUpdate(pos, pos);
            return true;
        }
        return false;
    }

    public static IBlockState rotateStateAroundY(ITurnable block, IBlockState state, boolean reverse) {
        EnumFacing facing = block.getFacingFromBlockState(state);
        EnumFacing newFacing = rotateFacingAroundY(facing, reverse);
        state = block.setFacingInBlock(state, newFacing);
        return state;
    }

    public static EnumFacing.Axis getAxis(int axis) {
        return EnumFacing.Axis.values()[axis];
    }

    public static BlockPos movePos(BlockPos source, EnumFacing facing) {
        return source.add(facing.getDirectionVec());

    }

    public static EnumFacing[] getAxisDirection(EnumFacing.Axis axis) {
        return AXIS_DIRECTIONS[axis.ordinal()];
    }

}
