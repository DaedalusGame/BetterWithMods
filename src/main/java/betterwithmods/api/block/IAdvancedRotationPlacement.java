package betterwithmods.api.block;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public interface IAdvancedRotationPlacement {

    default IBlockState getStateForAdvancedRotationPlacement(IBlockState defaultState, PropertyInteger facingProperty, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = defaultState;
        float hitXFromCenter = hitX - 0.5F;
        float hitYFromCenter = hitY - 0.5F;
        float hitZFromCenter = hitZ - 0.5F;
        System.out.printf("%s,%s,%s\n", hitXFromCenter, hitYFromCenter, hitZFromCenter);
        switch (facing.getAxis()) {
            case Y:
                if (inCenter(hitXFromCenter, hitZFromCenter, 0.25f)) {
                    state = state.withProperty(facingProperty, facing.getOpposite().getIndex());
                } else if (Math.max(Math.abs(hitXFromCenter), Math.abs(hitZFromCenter)) == Math.abs(hitXFromCenter)) {
                    state = state.withProperty(facingProperty, ((hitXFromCenter > 0) ? EnumFacing.EAST : EnumFacing.WEST).getIndex());
                } else {
                    state = state.withProperty(facingProperty, ((hitZFromCenter > 0) ? EnumFacing.SOUTH : EnumFacing.NORTH).getIndex());
                }
                break;
//            case X:
//                if (Math.max(Math.abs(hitYFromCenter), Math.abs(hitZFromCenter)) == Math.abs(hitYFromCenter)) {
//                    state = state.withProperty(facingProperty, (hitXFromCenter > 0) ? EnumFacing.EAST : EnumFacing.WEST);
//                } else {
//                    state = state.withProperty(facingProperty, (hitZFromCenter > 0) ? EnumFacing.SOUTH : EnumFacing.NORTH);
//                }
//                break;
//            case Z:
//                break;
            default:
                state = state.withProperty(facingProperty, facing.getOpposite().getIndex());
                break;
        }

        return state;
    }

    default boolean inCenter(float hit1, float hit2, float max) {
        return Math.abs(hit1) <= max && Math.abs(hit2) <= max;
    }
}