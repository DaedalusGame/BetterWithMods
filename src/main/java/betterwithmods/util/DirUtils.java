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

import java.util.Objects;

import static net.minecraft.util.EnumFacing.Axis.*;

public class DirUtils {
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final PropertyDirection HORIZONTAL = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyDirection TILTING = PropertyDirection.create("facing", facing -> facing != EnumFacing.DOWN);
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool[] DIR_PROP_HORIZ = new PropertyBool[]{NORTH,SOUTH,WEST,EAST};
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

    public static EnumFacing cycleFacing(EnumFacing facing, boolean reverse) {
        if (reverse) {
            switch (facing) {
                case DOWN:
                    facing = EnumFacing.UP;
                    break;
                case UP:
                    facing = EnumFacing.NORTH;
                    break;
                case NORTH:
                    facing = EnumFacing.EAST;
                    break;
                case SOUTH:
                    facing = EnumFacing.WEST;
                    break;
                case WEST:
                    facing = EnumFacing.DOWN;
                    break;
                case EAST:
                    facing = EnumFacing.SOUTH;
            }
        } else {
            switch (facing) {
                case DOWN:
                    facing = EnumFacing.WEST;
                    break;
                case UP:
                    facing = EnumFacing.DOWN;
                    break;
                case NORTH:
                    facing = EnumFacing.UP;
                    break;
                case SOUTH:
                    facing = EnumFacing.EAST;
                    break;
                case WEST:
                    facing = EnumFacing.SOUTH;
                    break;
                case EAST:
                    facing = EnumFacing.NORTH;
            }
        }
        return facing;
    }

    public static int getPlacementMeta(String type, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (Objects.equals(type, "siding")) {
            return side.getOpposite().ordinal();
        } else if (Objects.equals(type, "moulding")) {// X: East-West; Z: North-South
            return getMoulding(side, hitX, hitY, hitZ).getMeta();
        } else if (Objects.equals(type, "corner")) {
            if (side == EnumFacing.UP || side == EnumFacing.DOWN) {
                boolean up = side == EnumFacing.UP;
                if (hitX < 0.5F && hitZ < 0.5F)
                    return up ? 1 : 5;
                else if (hitX < 0.5F && hitZ > 0.5F)
                    return up ? 0 : 4;
                else if (hitX > 0.5F && hitZ < 0.5F)
                    return up ? 3 : 7;
                else
                    return up ? 2 : 6;
            } else if (side == EnumFacing.SOUTH || side == EnumFacing.NORTH) {
                boolean south = side == EnumFacing.SOUTH;
                if (hitX < 0.5F && hitY < 0.5F)
                    return south ? 1 : 0;
                else if (hitX < 0.5F && hitY > 0.5F)
                    return south ? 5 : 4;
                else if (hitX > 0.5F && hitY < 0.5F)
                    return south ? 3 : 2;
                else
                    return south ? 7 : 6;
            } else if (side == EnumFacing.EAST || side == EnumFacing.WEST) {
                boolean east = side == EnumFacing.EAST;
                if (hitZ > 0.5F && hitY < 0.5F)
                    return east ? 0 : 2;
                else if (hitZ > 0.5F && hitY > 0.5F)
                    return east ? 4 : 6;
                else if (hitZ < 0.5F && hitY < 0.5F)
                    return east ? 1 : 3;
                else
                    return east ? 5 : 7;
            }
        }
        return 0;
    }

    private static EnumMoulding getMoulding(EnumFacing side, float hitX, float hitY, float hitZ) {
        float rX = 1.0F - hitX;
        float rY = 1.0F - hitY;
        float rZ = 1.0F - hitZ;
        if (side == EnumFacing.DOWN || side == EnumFacing.UP) {
            if (hitX < 0.25F && hitZ < 0.25F)
                return EnumMoulding.VERTNORTHWEST;
            else if (hitX > 0.75F && hitZ < 0.25F)
                return EnumMoulding.VERTNORTHEAST;
            else if (hitX < 0.25F && hitZ > 0.75F)
                return EnumMoulding.VERTSOUTHWEST;
            else if (hitX > 0.75F && hitZ > 0.75F)
                return EnumMoulding.VERTSOUTHEAST;
            else if (side == EnumFacing.UP) {
                if (hitX < 0.5F && hitZ < 0.5F)
                    return hitX < hitZ ? EnumMoulding.DOWNWEST : EnumMoulding.DOWNNORTH;
                else if (hitX < 0.5F && hitZ > 0.5F)
                    return hitX < rZ ? EnumMoulding.DOWNWEST : EnumMoulding.DOWNSOUTH;
                else if (hitX > 0.5F && hitZ < 0.5F)
                    return rX < hitZ ? EnumMoulding.DOWNEAST : EnumMoulding.DOWNSOUTH;
                else
                    return hitX < hitZ ? EnumMoulding.DOWNNORTH : EnumMoulding.DOWNEAST;
            } else {
                if (hitX < 0.5F && hitZ < 0.5F)
                    return hitX < hitZ ? EnumMoulding.UPWEST : EnumMoulding.UPNORTH;
                else if (hitX < 0.5F && hitZ > 0.5F)
                    return hitX < rZ ? EnumMoulding.UPWEST : EnumMoulding.UPSOUTH;
                else if (hitX > 0.5F && hitZ < 0.5F)
                    return rX < hitZ ? EnumMoulding.UPEAST : EnumMoulding.UPSOUTH;
                else
                    return hitX < hitZ ? EnumMoulding.UPNORTH : EnumMoulding.UPEAST;
            }
        } else if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH) {
            if (hitX < 0.25F && hitY < 0.25F)
                return EnumMoulding.DOWNWEST;
            else if (hitX > 0.75F && hitY < 0.25F)
                return EnumMoulding.DOWNEAST;
            else if (hitX < 0.25F && hitY > 0.75F)
                return EnumMoulding.UPWEST;
            else if (hitX > 0.75F && hitY > 0.75F)
                return EnumMoulding.UPEAST;
            else if (side == EnumFacing.SOUTH) {
                if (hitX < 0.5F && hitY < 0.5F)
                    return hitX < hitY ? EnumMoulding.VERTNORTHWEST : EnumMoulding.DOWNNORTH;
                else if (hitX < 0.5F && hitY > 0.5F)
                    return hitX < rY ? EnumMoulding.VERTNORTHWEST : EnumMoulding.UPNORTH;
                else if (hitX > 0.5F && hitY < 0.5F)
                    return rX < hitY ? EnumMoulding.VERTNORTHEAST : EnumMoulding.DOWNNORTH;
                else
                    return hitX < hitY ? EnumMoulding.VERTNORTHEAST : EnumMoulding.UPNORTH;
            } else {
                if (hitX < 0.5F && hitY < 0.5F)
                    return hitX < hitY ? EnumMoulding.VERTSOUTHWEST : EnumMoulding.DOWNSOUTH;
                else if (hitX < 0.5F && hitY > 0.5F)
                    return hitX < rY ? EnumMoulding.VERTSOUTHWEST : EnumMoulding.UPSOUTH;
                else if (hitX > 0.5F && hitY < 0.5F)
                    return rX < hitY ? EnumMoulding.VERTSOUTHEAST : EnumMoulding.DOWNSOUTH;
                else
                    return hitX < hitY ? EnumMoulding.VERTSOUTHEAST : EnumMoulding.UPSOUTH;
            }
        } else if (side == EnumFacing.WEST || side == EnumFacing.EAST) {
            if (hitZ < 0.25F && hitY < 0.25F)
                return EnumMoulding.DOWNNORTH;
            else if (hitZ > 0.75F && hitY < 0.25F)
                return EnumMoulding.DOWNSOUTH;
            else if (hitZ < 0.25F && hitY > 0.75F)
                return EnumMoulding.UPNORTH;
            else if (hitZ > 0.75F && hitY > 0.75F)
                return EnumMoulding.UPSOUTH;
            else if (side == EnumFacing.EAST) {
                if (hitZ < 0.5F && hitY < 0.5F)
                    return hitZ < hitY ? EnumMoulding.VERTNORTHWEST : EnumMoulding.DOWNWEST;
                else if (hitZ < 0.5F && hitY > 0.5F)
                    return hitZ < rY ? EnumMoulding.VERTNORTHWEST : EnumMoulding.UPWEST;
                else if (hitZ > 0.5F && hitY < 0.5F)
                    return rZ < hitY ? EnumMoulding.VERTSOUTHWEST : EnumMoulding.DOWNWEST;
                else
                    return hitZ < hitY ? EnumMoulding.VERTSOUTHWEST : EnumMoulding.UPWEST;
            } else {
                if (hitZ < 0.5F && hitY < 0.5F)
                    return hitZ < hitY ? EnumMoulding.VERTNORTHEAST : EnumMoulding.DOWNEAST;
                else if (hitZ < 0.5F && hitY > 0.5F)
                    return hitZ < rY ? EnumMoulding.VERTNORTHEAST : EnumMoulding.UPEAST;
                else if (hitZ > 0.5F && hitY < 0.5F)
                    return rZ < hitY ? EnumMoulding.VERTSOUTHEAST : EnumMoulding.DOWNEAST;
                else
                    return hitZ < hitY ? EnumMoulding.VERTSOUTHEAST : EnumMoulding.UPEAST;
            }
        }
        return EnumMoulding.DOWNWEST;
    }

    public static BlockPos movePos(BlockPos source, EnumFacing facing) {
        return source.add(facing.getDirectionVec());
    }

    /**
     * Used to iterate through axis when right-clicking on the axle.
     *
     * @param axis Current axis.
     * @return Next axis in an cyclical X-Y-Z order.
     */
    public static EnumFacing.Axis getNextAxis(EnumFacing.Axis axis) {
        switch (axis) {
            case X:
                return Y;
            case Y:
                return Z;
            case Z:
            default:
                return X;
        }
    }

    /**
     * Get the value that was used to describe axis before EnumFacing.Axis.
     *
     * @param axis The modern axis.
     * @return 2 for X, 0 for Y, 1 for Z.
     */
    public static int getLegacyAxis(EnumFacing.Axis axis) {
        switch (axis) {
            case X:
                return 2;
            case Y:
                return 0;
            case Z:
            default:
                return 1;
        }
    }

    /**
     * Get the axis from the legacy axis value.
     *
     * @param axis The legacy axis integer, in [0,2].
     * @return X for 2, Y for 0, Z for 1. Default to X if undefined.
     */
    public static EnumFacing.Axis getAxisFromLegacy(int axis) {
        switch (axis) {
            case 0:
                return Y;
            case 1:
                return Z;
            case 2:
            default:
                return X;
        }
    }

    private enum EnumMoulding {
        DOWNWEST(0), DOWNNORTH(1), DOWNSOUTH(2), DOWNEAST(3), UPWEST(4), UPNORTH(5), UPSOUTH(6), UPEAST(
                7), VERTNORTHWEST(8), VERTNORTHEAST(9), VERTSOUTHWEST(10), VERTSOUTHEAST(11);

        private final int meta;

        EnumMoulding(int meta) {
            this.meta = meta;
        }

        public int getMeta() {
            return meta;
        }
    }
}
