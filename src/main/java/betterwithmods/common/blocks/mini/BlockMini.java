package betterwithmods.common.blocks.mini;

import betterwithmods.api.block.IAdvancedRotationPlacement;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BWMBlock;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.util.InvUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;

public abstract class BlockMini extends BWMBlock implements IMultiVariants, IAdvancedRotationPlacement {
    public static final Material MINI = new Material(MapColor.WOOD);
    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 15);
    public static final PropertyInteger ORIENTATION = createOrientation();

    public BlockMini(Material material) {
        super(material);
    }


    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return getMaterial(blockState) == Material.WOOD ? 2.0F : 3.0F;
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return getMaterial(state) == Material.ROCK ? SoundType.STONE : SoundType.WOOD;
    }

    @Nullable
    @Override
    public String getHarvestTool(IBlockState state) {
        return getMaterial(state) == Material.ROCK ? "pickaxe" : "axe";
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return 1;
    }

    public int getUsedTypes() {
        return 6;
    }

    @Override
    public String[] getVariants() {
        ArrayList<String> variants = Lists.newArrayList();
        for (int i = 0; i < getUsedTypes(); i++) {
            variants.add(String.format("orientation=3,type=%s", i));
        }
        return variants.toArray(new String[variants.size()]);
    }

    public static PropertyInteger createOrientation() {
        return PropertyInteger.create("orientation", 0, 5);
    }

    public int getMaxOrientation() {
        return 5;
    }

    public boolean rotate(World world, BlockPos pos, IBlockState state, EntityPlayer player, PropertyInteger property) {
        boolean emptyHands = player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.OFF_HAND).isEmpty() && player.isSneaking();
        if (world.isRemote && emptyHands)
            return true;
        else if (!world.isRemote && emptyHands) {
            int nextOrient = (state.getValue(property) + 1) % (getMaxOrientation() + 1);
            world.playSound(null, pos, this.getSoundType(state, world, pos, player).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
            world.setBlockState(pos, state.withProperty(property, nextOrient));
            world.notifyNeighborsOfStateChange(pos, this, false);
            world.scheduleBlockUpdate(pos, this, 10, 5);
            return true;
        }
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return rotate(worldIn, pos, state, playerIn, ORIENTATION);
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
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float flX, float flY, float flZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getStateForAdvancedRotationPlacement(getDefaultState(),facing,flX,flY,flZ);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getStateForPlacement(worldIn,pos,facing,hitX,hitY,hitZ,meta,placer,placer.getActiveHand());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityMultiType) {
            int meta = stack.getItemDamage();
            ((TileEntityMultiType) world.getTileEntity(pos)).setCosmeticType(meta);
            world.setBlockState(pos, state.withProperty(TYPE, meta));
        }
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityMultiType) {
            return new ItemStack(this, 1, ((TileEntityMultiType) world.getTileEntity(pos)).getCosmeticType());
        }
        return new ItemStack(this, 1, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i = 0; i < getUsedTypes(); i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.025F);

        stack = new ItemStack(this, 1, state.getValue(TYPE));
        InvUtils.ejectStackWithOffset(world, pos, stack);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityMultiType();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (!((World)world).isRemote && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityMultiType)
            return state.withProperty(TYPE,((TileEntityMultiType) world.getTileEntity(pos)).getCosmeticType());
        return state;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE);
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

    @Override
    public IBlockState getRenderState(World world, BlockPos pos, EnumFacing facing, float flX, float flY, float flZ, int meta, EntityLivingBase placer) {
        return getStateForPlacement(world, pos, facing, flX, flY, flZ, meta, placer).withProperty(TYPE, meta);
    }

    public enum EnumType {

        STONE(0, "stone", Blocks.STONE),
        STONEBRICK(1, "stone_brick", Blocks.STONEBRICK),
        WHITESTONE(2, "whitestone", new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.WHITESTONE.getMeta())),
        NETHERBRICK(3, "nether_brick", Blocks.NETHER_BRICK),
        BRICK(4, "brick", Blocks.BRICK_BLOCK),
        SANDSTONE(5, "sandstone", Blocks.SANDSTONE);

        private static final BlockMini.EnumType[] META_LOOKUP = new BlockMini.EnumType[values().length];

        static {
            for (BlockMini.EnumType blockmini$enumtype : values()) {
                META_LOOKUP[blockmini$enumtype.getMetadata()] = blockmini$enumtype;
            }
        }

        private final int meta;
        private final String name;
        private final ItemStack block;

        EnumType(int metaIn, String nameIn, Block blockIn) {
            this(metaIn, nameIn, new ItemStack(blockIn));
        }

        EnumType(int metaIn, String nameIn, ItemStack blockIn) {
            this.meta = metaIn;
            this.name = nameIn;
            this.block = blockIn;
        }

        public static BlockMini.EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata() {
            return this.meta;
        }

        public String getName() {
            return this.name;
        }

        public ItemStack getBlock() {
            return this.block;
        }
    }
}
