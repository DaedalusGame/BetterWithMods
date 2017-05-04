package betterwithmods.common.blocks;

import betterwithmods.api.block.IMultiVariants;
import betterwithmods.common.BWMBlocks;
import betterwithmods.util.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockBWMPane extends BWMBlock implements IMultiVariants {
    public static final PropertyEnum<EnumPaneType> TYPES = PropertyEnum.create("type", EnumPaneType.class);

    public BlockBWMPane() {
        super(Material.WOOD);
        this.setHardness(2.0F);
        this.setDefaultState(getDefaultState().withProperty(TYPES, EnumPaneType.GRATE));
        this.setSoundType(SoundType.WOOD);
    }

    @Override
    public String[] getVariants() {
        return new String[]{"", "", "east=false,north=true,south=true,type=wicker,west=false"};
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
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean pass) {
        state = state.getActualState(world, pos);

        float minY = 0.001F;
        float maxY = 0.999F;
        float minX = 0.4375F;
        float maxX = 0.5625F;
        float minZ = 0.4375F;
        float maxZ = 0.5625F;
        if (state.getValue(DirUtils.NORTH))
            minZ = 0.0F;
        if (state.getValue(DirUtils.SOUTH))
            maxZ = 1.0F;
        if (state.getValue(DirUtils.WEST))
            minX = 0.0F;
        if (state.getValue(DirUtils.EAST))
            maxX = 1.0F;

        AxisAlignedBB stick = new AxisAlignedBB(0.5F - 0.0625F, 0.0F, 0.5F - 0.0625F, 0.5625F, 1.0F, 0.5625F);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, stick);

        if (minZ == 0.0F || maxZ == 1.0F) {
            AxisAlignedBB extZ = new AxisAlignedBB(0.4375F, minY, minZ, 0.5625F, maxY, maxZ);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, extZ);
        }

        if (minX == 0.0F || maxX == 1.0F) {
            AxisAlignedBB extX = new AxisAlignedBB(minX, minY, 0.4375F, maxX, maxY, 0.5625F);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, extX);
        }
    }

    private final Map<PropertyBool, AxisAlignedBB> bounds = new HashMap<PropertyBool, AxisAlignedBB>() {{
        //new AxisAlignedBB(0.4375F, 0.0F,0.4375F, 0.5625F, 1.0F, 0.5625F)
        put(DirUtils.NORTH, new AxisAlignedBB(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5625F));
        put(DirUtils.SOUTH, new AxisAlignedBB(0.4375F, 0.0F, 0.4375F, 0.5625F, 1.0F, 1.0F));
        put(DirUtils.WEST, new AxisAlignedBB(0.0F, 0.0F, 0.4375F, 0.5625F, 1.0F, 0.5625F));
        put(DirUtils.EAST, new AxisAlignedBB(0.4375F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F));
    }};

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        state = state.getActualState(world, pos);
        AxisAlignedBB bound = new AxisAlignedBB(0.4375F, 0.0F, 0.4375F, 0.5625F, 1.0F, 0.5625F);
        for (PropertyBool dir : DirUtils.DIR_PROP_HORIZ)
            if (state.getValue(dir))
                bound = bound.union(bounds.get(dir));
        return bound;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(item, 1, 2));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        switch (state.getValue(TYPES)) {
            case SLATS:
                return Item.getItemFromBlock(BWMBlocks.SLATS);
            case WICKER:
                return Item.getItemFromBlock(this);
            default:
                return Item.getItemFromBlock(BWMBlocks.GRATE);
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        if (state.getValue(TYPES) == EnumPaneType.WICKER)
            return 2;
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public boolean isCompatiblePane(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        BlockPos neighbor = pos.offset(dir);
        Block block = world.getBlockState(neighbor).getBlock();
        return block instanceof BlockBWMPane;
    }

    public boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        return world.getBlockState(pos).isOpaqueCube() || world.getBlockState(pos).getBlock().isBlockSolid(world, pos, dir.getOpposite());
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        return entity instanceof EntityPlayer && world.getBlockState(pos).getValue(TYPES) != EnumPaneType.WICKER;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean north = canConnectTo(world, pos.north(), EnumFacing.NORTH) || isCompatiblePane(world, pos, EnumFacing.NORTH);
        boolean east = canConnectTo(world, pos.east(), EnumFacing.EAST) || isCompatiblePane(world, pos, EnumFacing.EAST);
        boolean south = canConnectTo(world, pos.south(), EnumFacing.SOUTH) || isCompatiblePane(world, pos, EnumFacing.SOUTH);
        boolean west = canConnectTo(world, pos.west(), EnumFacing.WEST) || isCompatiblePane(world, pos, EnumFacing.WEST);
        return state.withProperty(DirUtils.NORTH, north).withProperty(DirUtils.EAST, east)
                .withProperty(DirUtils.SOUTH, south).withProperty(DirUtils.WEST, west);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPES).getMeta();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPES, EnumPaneType.byMeta(meta));
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPES, DirUtils.SOUTH, DirUtils.EAST, DirUtils.NORTH, DirUtils.WEST);
    }

    public enum EnumPaneType implements IStringSerializable {
        GRATE(0, "grate"),
        SLATS(1, "slats"),
        WICKER(2, "wicker");

        private static final EnumPaneType[] META_LOOKUP = new EnumPaneType[values().length];

        static {
            for (EnumPaneType type : values()) {
                META_LOOKUP[type.getMeta()] = type;
            }
        }

        private String name;
        private int meta;

        EnumPaneType(int meta, String name) {
            this.meta = meta;
            this.name = name;
        }

        public static EnumPaneType byMeta(int meta) {
            return META_LOOKUP[meta];
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
