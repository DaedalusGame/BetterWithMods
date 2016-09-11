package betterwithmods.blocks.mini;

import betterwithmods.util.DirUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockMoulding extends BlockMini
{
    private Material mat;
    public static final PropertyInteger ORIENTATION = createOrientation();

    public static PropertyInteger createOrientation()
    {
        return PropertyInteger.create("orientation", 0, 11);
    }

    public BlockMoulding(Material mat)
    {
        super(mat, mat == Material.WOOD ? "wood_moulding" : "stone_moulding");
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
        this.mat = mat;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        int ori = state.getValue(ORIENTATION);
        switch(ori)
        {
            case 1: return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
            case 2: return new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
            case 3: return new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
            case 4: return new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
            case 5: return new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
            case 6: return new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
            case 7: return new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
            case 8: return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D);
            case 9: return new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
            case 10: return new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 1.0D, 1.0D);
            case 11: return new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D);
            default: return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
        }
    }

    @Override
    public IBlockState metaBlockPlace(EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState state = this.getDefaultState();
        int ori = DirUtils.getPlacementMeta("moulding", facing, hitX, hitY, hitZ);
        return state.withProperty(ORIENTATION, ori);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ORIENTATION, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ORIENTATION);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE, ORIENTATION);
    }
}
