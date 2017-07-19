package betterwithmods.common.blocks.mechanical;

import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.tile.gen.IColor;
import betterwithmods.common.blocks.tile.gen.TileEntityWindmillHorizontal;
import betterwithmods.common.blocks.tile.gen.TileEntityWindmillVertical;
import betterwithmods.util.ColorUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWindmill extends BlockMillGenerator {
    public BlockWindmill() {
        super(Material.WOOD);
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(BWMItems.WINDMILL, 1, state.getValue(AXIS) == EnumFacing.Axis.Y ? 2 : 0);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        //TODO
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        TileEntity tile = world.getTileEntity(pos);

        if (world.isRemote && !stack.isEmpty() && ColorUtils.contains(stack.getItem(), stack.getItemDamage()))
            return true;

        if (!world.isRemote && tile != null && tile instanceof IColor && !stack.isEmpty() && ColorUtils.contains(stack.getItem(), stack.getItemDamage())) {
            IColor color = (IColor) tile;
            int meta = ColorUtils.get(stack.getItem(), stack.getItemDamage()); //reverseMeta[stack.getItemDamage()];
            if (color.dyeBlade(meta)) {
                if (!player.capabilities.isCreativeMode)
                    stack.shrink(1);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        if (state.getValue(AXIS) == EnumFacing.Axis.Y)
            return new TileEntityWindmillVertical();
        else
            return new TileEntityWindmillHorizontal();
    }
}
