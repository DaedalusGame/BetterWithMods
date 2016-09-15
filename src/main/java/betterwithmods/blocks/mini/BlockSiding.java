package betterwithmods.blocks.mini;

import betterwithmods.util.DirUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSiding extends BlockMini
{
    public BlockSiding(Material mat)
    {
        super(mat, mat == Material.WOOD ? "wood_siding" : "stone_siding");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, 0).withProperty(ORIENTATION, 0));
        if(mat == Material.WOOD)
        {
            this.setHardness(2.0F);
        }
        else
        {
            this.setHardness(3.0F);
            this.setResistance(5.0F);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        int ori = state.getValue(ORIENTATION);
        switch(ori)
        {
            case 1: return new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
            case 2: return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
            case 3: return new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D);
            case 4: return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D);
            case 5: return new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            default: return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
        }
    }

    @Override
    public IBlockState metaBlockPlace(EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState state = this.getDefaultState();
        int ori = DirUtils.getPlacementMeta("siding", facing, hitX, hitY, hitZ);
        return state.withProperty(ORIENTATION, ori);
    }
}
