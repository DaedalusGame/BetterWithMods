package betterwithmods.common.blocks;

import betterwithmods.common.blocks.tile.TileEntityAdvBellows;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/16/17
 */
public class BlockAdvBellows extends BlockBellows implements ITileEntityProvider{
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state.getValue(TRIGGER))
            return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.6875F, 1.0F);
        return FULL_BLOCK_AABB;
    }

    @Override
    public int tickRate(World world) {
        return 1;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        if (!isCurrentStateValid(world, pos)) {
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        boolean gettingPower = isInputtingMechPower(world, pos);
        boolean isMechOn = isMechanicalOn(world, pos);
        if(isMechOn != gettingPower) {
            setMechanicalOn(world,pos,gettingPower);
            if(!gettingPower)
                setTriggerMechanicalStateChange(world,pos,false);
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityAdvBellows();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAdvBellows();
    }
}
