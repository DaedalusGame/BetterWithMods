package betterwithmods.common.blocks;

import betterwithmods.BWMod;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.common.blocks.tile.TileEntityCauldron;
import betterwithmods.common.blocks.tile.TileEntityCrucible;
import betterwithmods.common.blocks.tile.TileEntityDragonVessel;
import betterwithmods.util.DirUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/3/17
 */
public class BlockCookingPot extends BWMBlock implements IMultiVariants {
    public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

    public BlockCookingPot() {
        super(Material.ROCK);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }


    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i = 0; i < EnumType.META_LOOKUP.length; i++) {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        switch (state.getValue(TYPE)) {
            default:
                return SoundType.STONE;
            case CRUCIBLE:
                return SoundType.GLASS;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE, DirUtils.TILTING);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return super.getActualState(state, worldIn, pos);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).getMeta();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, EnumType.byMeta(meta));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            if (world.getTileEntity(pos) != null && world.getTileEntity(pos).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                player.openGui(BWMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        switch (state.getValue(TYPE)) {
            case CRUCIBLE:
                return new TileEntityCrucible();
            case CAULDRON:
                return new TileEntityCauldron();
            case DRAGONVESSEL:
                return new TileEntityDragonVessel();
            default:
                return super.createTileEntity(world, state);
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public String[] getVariants() {
        return new String[]{
                "facing=up,type=crucible",
                "facing=up,type=cauldron",
                "facing=up,type=dragonvessel",
        };
    }

    public enum EnumType implements IStringSerializable {
        CRUCIBLE(0, "crucible"),
        CAULDRON(1, "cauldron"),
        DRAGONVESSEL(2, "dragonvessel");

        private static final EnumType[] META_LOOKUP = values();

        private int meta;
        private String name;

        EnumType(int meta, String name) {
            this.meta = meta;
            this.name = name;
        }

        public static EnumType byMeta(int meta) {
            return META_LOOKUP[meta % META_LOOKUP.length];
        }

        @Override
        public String getName() {
            return name;
        }

        public int getMeta() {
            return meta;
        }

    }
}
