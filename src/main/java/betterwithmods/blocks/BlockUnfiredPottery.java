package betterwithmods.blocks;

import betterwithmods.api.block.IMultiVariants;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockUnfiredPottery extends BWMBlock implements IMultiVariants {
    public static final PropertyEnum<EnumPotteryType> POTTERYTYPE = PropertyEnum.create("potterytype",
            BlockUnfiredPottery.EnumPotteryType.class);
    private static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    private static final AxisAlignedBB URN_AABB = new AxisAlignedBB(0.3125D, 0.0F, 0.3125D, 0.6875D, 0.625D, 0.6875D);
    private static final AxisAlignedBB VASE_AABB = new AxisAlignedBB(0.125D, 0, 0.125D, 0.875D, 1.0D, 0.875D);

    public BlockUnfiredPottery() {
        super(Material.CLAY);
        this.setSoundType(SoundType.GROUND);
        this.setHardness(0.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POTTERYTYPE, EnumPotteryType.CRUCIBLE));
    }

    @Override
    public String[] getVariants() {
        return new String[]{"potterytype=crucible", "potterytype=planter", "potterytype=urn", "potterytype=vase"};
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ,
                                            int meta, EntityLivingBase entity, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, side, flX, flY, flZ, meta, entity, hand);
        return state.withProperty(POTTERYTYPE, EnumPotteryType.byMeta(meta));
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
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i = 0; i < 4; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(POTTERYTYPE).getMeta();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumPotteryType type = state.getValue(POTTERYTYPE);
        switch (type) {
            case CRUCIBLE:
            case PLANTER:
                return BLOCK_AABB;
            case URN:
                return URN_AABB;
            case VASE:
            default:
                return VASE_AABB;
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.isSideSolid(pos.down(), EnumFacing.UP);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        if (!world.isSideSolid(pos.down(), EnumFacing.UP)) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POTTERYTYPE, EnumPotteryType.byMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POTTERYTYPE).getMeta();
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POTTERYTYPE);
    }

    public enum EnumPotteryType implements IStringSerializable {
        CRUCIBLE(0, "crucible"), PLANTER(1, "planter"), URN(2, "urn"), VASE(3, "vase");
        private static final EnumPotteryType[] META_LOOKUP = new EnumPotteryType[values().length];

        static {
            for (EnumPotteryType type : values()) {
                META_LOOKUP[type.getMeta()] = type;
            }
        }

        private String name;
        private int meta;

        EnumPotteryType(int meta, String name) {
            this.meta = meta;
            this.name = name;
        }

        public static EnumPotteryType byMeta(int meta) {
            return META_LOOKUP[meta];
        }

        public int getMeta() {
            return meta;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
