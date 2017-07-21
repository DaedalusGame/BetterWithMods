package betterwithmods.common.blocks;

import betterwithmods.api.block.IMultiVariants;
import betterwithmods.util.DirUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by primetoxinz on 7/21/17.
 */
public class BlockBrokenGearbox extends BWMBlock implements IMultiVariants {
    public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);


    public BlockBrokenGearbox() {
        super(Material.WOOD);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this,1,0));
        items.add(new ItemStack(this,1,1));
    }

    @Override
    public String[] getVariants() {
        return new String[]{"type=wood","type=steel"};
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.FACING, TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(DirUtils.FACING, EnumFacing.VALUES[meta & 3]).withProperty(TYPE, EnumType.VALUES[meta >> 3]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facing = state.getValue(DirUtils.FACING).getIndex();
        int type = state.getValue(TYPE).ordinal();
        return facing | type << 3;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        if (world.getBlockState(pos).getValue(TYPE) == EnumType.STEEL)
            return 4000f;
        return 0;
    }

    @Override
    public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {
        if (state.getValue(TYPE) == EnumType.STEEL)
            return 100f;
        return 3.5f;
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        if (state.getValue(TYPE) == EnumType.STEEL)
            return SoundType.METAL;
        return SoundType.WOOD;
    }

    @Override
    public Material getMaterial(IBlockState state) {
        if (state.getValue(TYPE) == EnumType.STEEL)
            return Material.IRON;
        return Material.WOOD;
    }


    public enum EnumType implements IStringSerializable {
        WOOD("wood"),
        STEEL("steel");
        public static EnumType[] VALUES = values();
        private String name;

        EnumType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
