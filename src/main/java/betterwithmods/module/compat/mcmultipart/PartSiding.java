package betterwithmods.module.compat.mcmultipart;

import betterwithmods.common.blocks.mini.BlockSiding;
import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by primetoxinz on 6/19/17.
 */
public class PartSiding extends PartMini<BlockSiding> {

    public PartSiding(BlockSiding block) {
        super(block);
    }

    @Override
    public IPartSlot getSlotForPlacement(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) {
        int o = state.getValue(this.getBlock().getOrientationProperty());
        EnumFaceSlot slot = EnumFaceSlot.VALUES[o];
//        if(!world.isAirBlock(pos)) {
//            slot = slot.getOpposite();
//        }
        System.out.println(slot + "," + o);
        return slot;
    }

    @Override
    public IPartSlot getSlotFromWorld(IBlockAccess world, BlockPos pos, IBlockState state) {
        int o = state.getValue(this.getBlock().getOrientationProperty());
        return EnumFaceSlot.VALUES[o].getOpposite();
    }

    @Override
    public IMultipartTile convertToMultipartTile(TileEntity tileEntity) {
        return IMultipartTile.wrap(tileEntity);
    }
}
