package betterwithmods.common.blocks.tile;

import betterwithmods.common.blocks.BlockStake;
import betterwithmods.util.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by tyler on 5/17/17.
 */
public class TileStake extends TileBasicInventory {
    private boolean[] connected = new boolean[getInventorySize()];
    private BlockPos[] connectedBlock = new BlockPos[getInventorySize()];

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        connectSide(worldIn,side.getOpposite());
        return true;
    }
    public int getInventorySize() {
        return 6;
    }

    public void connectSide(World world, EnumFacing facing) {
        BlockPos stakePos = findStake(world,facing);
        int i = facing.getIndex();
        if(i > inventory.getSlots())
            return;
        if(pos != null && !connected[i]) {
            connected[i] = true;
            connectedBlock[i] = stakePos;
            inventory.setStackInSlot(i, new ItemStack(Items.STRING, WorldUtils.getDistance(getPos(),stakePos)));
        }
    }

    public BlockPos findStake(World world, EnumFacing facing) {
        BlockPos pos = getPos();
        boolean isStake = false;
        int i = 0;
        while(!isStake && i < 64) {
            pos = pos.offset(facing);
            if(world.getBlockState(pos).getBlock() instanceof BlockStake)
                isStake = true;
            i++;
        }
        if(isStake)
            return pos;
        return null;
    }
}
