package betterwithmods.integration.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityDynamo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityImmersiveAxle extends TileEntity implements ITickable {
    private boolean overpowered = false;

    @Override
    public void update() {
        if (!getWorld().isRemote) {
            if (getWorld().getBlockState(pos).getBlock() instanceof BlockImmersiveAxle) {
                if (((BlockImmersiveAxle) getWorld().getBlockState(pos).getBlock()).isMechanicalOn(getWorld(), pos)) {
                    if (!this.getWorld().isRaining() && !this.getWorld().isThundering() && overpowered)
                        overpowered = false;
                    double velocity = overpowered ? 16 : 4;
                    EnumFacing facing = EnumFacing.getFront(getWorld().getBlockState(pos).getValue(BlockImmersiveAxle.AXLEDIR) * 2);
                    TileEntity entity = getWorld().getTileEntity(pos.offset(facing));
                    TileEntity oppEntity = getWorld().getTileEntity(pos.offset(facing.getOpposite()));
                    if (entity instanceof TileEntityDynamo)
                        ((TileEntityDynamo) entity).inputRotation(velocity, facing);
                    else if (oppEntity instanceof TileEntityDynamo)
                        ((TileEntityDynamo) oppEntity).inputRotation(velocity, facing.getOpposite());
                }
            }
        }
    }

    public void setOverpowered() {
        if (!overpowered && (this.getWorld().isRaining() || this.getWorld().isThundering()))
            overpowered = true;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}
