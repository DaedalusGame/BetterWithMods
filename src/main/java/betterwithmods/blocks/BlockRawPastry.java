package betterwithmods.blocks;

import betterwithmods.api.IMultiLocations;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

/**
 * Created by blueyu2 on 11/19/16.
 */
public class BlockRawPastry extends Block implements IMultiLocations {
    public static final PropertyEnum<BlockRawPastry.EnumType> VARIANT = PropertyEnum.create("variant", BlockRawPastry.EnumType.class);

    public BlockRawPastry() {
        super(Material.CAKE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.CAKE));
        this.setHardness(0.1F);
        this.setSoundType(SoundType.CLOTH);
        this.setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (BlockRawPastry.EnumType blockrawpastry$enumtype : BlockRawPastry.EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, blockrawpastry$enumtype.getMetadata()));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canBlockStay(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(VARIANT).getAABB();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return state.getCollisionBoundingBox(worldIn, pos);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
    }

    @Override
    public String[] getLocations() {
        ArrayList<String> variants = new ArrayList<>();
        for (EnumType variant : EnumType.values()) {
            variants.add(variant.getName());
        }
        return variants.toArray(new String[variants.size()]);
    }

    public enum EnumType implements IStringSerializable {
        CAKE(0, "raw_cake", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)),
        PUMPKIN(1, "raw_pumpkin_pie", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)),
        COOKIE(2, "raw_cookie", new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D));

        private static final BlockRawPastry.EnumType[] META_LOOKUP = new BlockRawPastry.EnumType[values().length];

        static {
            for (BlockRawPastry.EnumType blockrawpastry$enumtype : values()) {
                META_LOOKUP[blockrawpastry$enumtype.getMetadata()] = blockrawpastry$enumtype;
            }
        }

        private final int meta;
        private final String name;
        private final AxisAlignedBB aabb;

        EnumType(int metaIn, String nameIn, AxisAlignedBB aabbIn) {
            this.meta = metaIn;
            this.name = nameIn;
            this.aabb = aabbIn;
        }

        public static BlockRawPastry.EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata() {
            return this.meta;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public AxisAlignedBB getAABB() {
            return this.aabb;
        }

        public String toString() {
            return this.name;
        }
    }
}
