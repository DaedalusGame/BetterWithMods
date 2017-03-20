package betterwithmods.common.blocks.tile;

import betterwithmods.common.blocks.BlockAdvBellows;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/16/17
 */
public class TileEntityAdvBellows extends TileEntity implements ITickable{

    private int tick;
    private boolean continuous = false;
    @Override
    public void update() {
        IBlockState state = world.getBlockState(pos);
        BlockAdvBellows block = (BlockAdvBellows) state.getBlock();
        if(block.isMechanicalOn(world,pos)) {
            if(block.isTriggerMechanicalState(state)) {
                block.blow(world,pos);

            }
            if(tick >= 37) {
                block.setTriggerMechanicalStateChange(world, pos, continuous = !continuous);
                if (!world.isRemote)
                    block.playStateChangeSound(world, pos);
                tick = 0;
            }
            tick++;
        }
    }
}
