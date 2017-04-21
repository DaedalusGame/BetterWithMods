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

public class BlockCorner extends BlockMini {
    public static final PropertyInteger ORIENTATION = createOrientation();

    public BlockCorner(Material mat) {
        super(mat, mat == Material.WOOD ? "wood_corner" : "stone_corner");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, 0).withProperty(ORIENTATION, 0));
        if (mat == Material.WOOD) {
            this.setHardness(2.0F);
        } else {
            this.setHardness(3.0F);
            this.setResistance(5.0F);
        }
    }

    public static PropertyInteger createOrientation() {
        return PropertyInteger.create("orientation", 0, 7);
    }

    @Override
    public int getMaxOrientation() {
        return 7;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        //TODO: Maybe make this try to work with items that don't have a use action?
        return rotate(worldIn, pos, state, playerIn, ORIENTATION);
    }

    private static final AxisAlignedBB[] bounds = new AxisAlignedBB[]{
            new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D),
            new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D),
            new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D),
            new AxisAlignedBB(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D),
            new AxisAlignedBB(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D)
    };

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int ori = state.getValue(ORIENTATION);
        if(ori > 7 || ori < 1)
           return bounds[0];
        return bounds[ori];
    }

    @Override
    public IBlockState metaBlockPlace(EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = this.getDefaultState();
        int ori = DirUtils.getPlacementMeta("corner", facing, hitX, hitY, hitZ);
        return state.withProperty(ORIENTATION, ori);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ORIENTATION, meta);
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
