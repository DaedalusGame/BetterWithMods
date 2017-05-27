package betterwithmods.common.blocks.mini;

import betterwithmods.util.DirUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMoulding extends BlockMini {
    public static final PropertyInteger ORIENTATION = createOrientation();

    public BlockMoulding(Material mat) {
        super(mat);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, 0).withProperty(ORIENTATION, 0));
    }



    public static PropertyInteger createOrientation() {
        return PropertyInteger.create("orientation", 0, 11);
    }

    @Override
    public int getMaxOrientation() {
        return 11;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return rotate(worldIn, pos, state, playerIn, ORIENTATION);
    }

    private final AxisAlignedBB[] bounds = new AxisAlignedBB[]{
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D),
            new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D),
            new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
            new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D),
            new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D),
            new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D),
            new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 1.0D, 1.0D),
            new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D),
    };

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int ori = state.getValue(ORIENTATION);
        if (ori > 11 || ori < 1)
            return bounds[0];
        return bounds[ori];
    }

    @Override
    public IBlockState metaBlockPlace(EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = this.getDefaultState();
        int ori = DirUtils.getPlacementMeta("moulding", facing, hitX, hitY, hitZ);
        return state.withProperty(ORIENTATION, ori);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ORIENTATION, meta%getMaxOrientation());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ORIENTATION);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE, ORIENTATION);
    }
}
