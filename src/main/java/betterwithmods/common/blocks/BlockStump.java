package betterwithmods.common.blocks;

import betterwithmods.api.block.IMultiVariants;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.hardcore.HCStumping;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.block.BlockPlanks.VARIANT;

/**
 * @author Koward
 */
public class BlockStump extends Block implements IMultiVariants {
    private static final int MIN_TRUNK_HEIGHT = 2;

    public BlockStump() {
        super(Material.WOOD);
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setHardness(40F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.OAK));
        this.setHarvestLevel("axe", 0);
        Blocks.FIRE.setFireInfo(this, 5, 5);
    }

    /**
     * Whether a stump could be placed or not.
     *
     * @param worldIn  The world where stump will be placed.
     * @param position The position at the base of the tree.
     * @return true when the trunk is high enough.
     */
    public static boolean canPlaceStump(World worldIn, BlockPos position) {
        for (int i = 0; i < MIN_TRUNK_HEIGHT; i++) {
            IBlockState state = worldIn.getBlockState(position.up(i));
            if (!(state.getBlock() == Blocks.LOG ||
                    state.getBlock() == Blocks.LOG2)) {
                return false;
            }
            if (i == 0 && state.getProperties().containsKey(BlockLog.LOG_AXIS)
                    && state.getValue(BlockLog.LOG_AXIS) != BlockLog.EnumAxis.Y) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return a stump states that matches the log given in parameter.
     *
     * @param log The log of the tree.
     * @return If possible a state with the same {@link BlockPlanks.EnumType} as the log, null otherwise.
     */
    @Nullable
    public static IBlockState getStump(IBlockState log) {
        BlockPlanks.EnumType variant = HCStumping.getWoodType(log);
        if (variant == null) return null;
        return BWMBlocks.STUMP.getDefaultState().withProperty(BlockPlanks.VARIANT, variant);
    }

    @Override
    public boolean isWood(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        if (state.getProperties().containsKey(BlockPlanks.VARIANT)) {
            BlockPlanks.EnumType type = state.getValue(BlockPlanks.VARIANT);
            if (type.getMetadata() < 4)
                return new ItemStack(Blocks.LOG, 1, type.getMetadata());
            else
                return new ItemStack(Blocks.LOG2, 1, type.getMetadata() - 4);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> ret = Lists.newArrayList();

        if (state.getProperties().containsKey(BlockPlanks.VARIANT)) {
            ret.add(new ItemStack(BWMItems.BARK, 1, state.getValue(BlockPlanks.VARIANT).getMetadata()));
        }
        ret.add(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST, 16));
        return ret;
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        for (int i = 0; i < 4; ++i) {
            spawnAsEntity(worldIn, pos, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST, 1));
        }
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        BlockPlanks.EnumType type = state.getValue(VARIANT);
        return type.getMapColor();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            items.add(new ItemStack(this, 1, type.getMetadata()));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public String[] getVariants() {
        ArrayList<String> variants = new ArrayList<>();
        for (BlockPlanks.EnumType blockplanks$enumtype : BlockPlanks.EnumType.values()) {
            variants.add("variant=" + blockplanks$enumtype.getName());
        }
        return variants.toArray(new String[BlockPlanks.EnumType.values().length]);
    }


    @Override
    public boolean isToolEffective(String type, IBlockState state) {
        return false;
    }
}
