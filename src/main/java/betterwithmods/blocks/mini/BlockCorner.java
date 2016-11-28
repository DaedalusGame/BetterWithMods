package betterwithmods.blocks.mini;

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

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        //TODO Cache AABB
        int ori = state.getValue(ORIENTATION);
        switch (ori) {
            case 1:
                return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
            case 2:
                return new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
            case 3:
                return new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
            case 4:
                return new AxisAlignedBB(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D);
            case 5:
                return new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D);
            case 6:
                return new AxisAlignedBB(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
            case 7:
                return new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
            default:
                return new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
        }
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
