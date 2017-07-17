package betterwithmods.common.blocks;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.blocks.tile.TileEntityBeacon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by primetoxinz on 7/17/17.
 */
public class BlockBeacon extends net.minecraft.block.BlockBeacon {

    public BlockBeacon() {
        super();
        setRegistryName("minecraft:beacon");
        setCreativeTab(BWCreativeTabs.BWTAB);
        setUnlocalizedName("beacon");
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.isCreative()) {
            ((TileEntityBeacon) worldIn.getTileEntity(pos)).processInteraction(playerIn.getHeldItemMainhand());
            return true;
        }
        return false;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        return;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityBeacon();
    }
}
