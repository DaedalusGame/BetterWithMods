package betterwithmods.common.blocks;

import betterwithmods.api.block.IMultiVariants;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAesthetic extends BWMBlock implements IMultiVariants {
    public static final PropertyEnum<BlockAesthetic.EnumType> blockType = PropertyEnum.create("blocktype", BlockAesthetic.EnumType.class);

    //Chopping Block, Chopping Block(dirty), Steel block, Hellfire block, Rope block, Flint block, Barrel (tile entity?) (6 - 11 subtypes)
    public BlockAesthetic() {
        super(Material.ROCK);
        this.setHardness(2.0F);
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setDefaultState(this.blockState.getBaseState().withProperty(blockType, BlockAesthetic.EnumType.CHOPBLOCK));
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return state.getValue(blockType) != EnumType.STEEL || entity instanceof EntityPlayer;
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        if (world.getBlockState(pos).getValue(blockType) != EnumType.STEEL)
            super.onBlockExploded(world, pos, explosion);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        if (world.getBlockState(pos).getValue(blockType) != EnumType.STEEL)
            return 10;
        else
            return 2000.0F;
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        //TODO might need to take steel blocks out of this or get a state based method added by forge
        //needed to make steel wither-proof
        return false;
    }

    @Override
    public String[] getVariants() {
        return new String[]{"blocktype=chopping", "blocktype=chopping_blood", "blocktype=steel", "blocktype=hellfire", "blocktype=rope", "blocktype=flint", "blocktype=whitestone", "blocktype=whitecobble", "blocktype=enderblock", "blocktype=padding", "blocktype=soap"};
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i = 0; i < EnumType.META_LOOKUP.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return (state.getValue(blockType)).getMeta();
    }

    @Override
    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        int meta = (world.getBlockState(pos).getValue(blockType)).getMeta();
        return meta == 3;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(blockType, BlockAesthetic.EnumType.byMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(blockType).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, blockType);
    }

    public enum EnumType implements IStringSerializable {
        CHOPBLOCK(0, "chopping", MapColor.STONE),
        CHOPBLOCKBLOOD(1, "chopping_blood", MapColor.NETHERRACK),
        STEEL(2, "steel", MapColor.IRON),
        HELLFIRE(3, "hellfire", MapColor.ADOBE),
        ROPE(4, "rope", MapColor.DIRT),
        FLINT(5, "flint", MapColor.STONE),
        WHITESTONE(6, "whitestone", MapColor.CLOTH),
        WHITECOBBLE(7, "whitecobble", MapColor.CLOTH),
        ENDERBLOCK(8, "enderblock", MapColor.CYAN),
        PADDING(9, "padding", MapColor.CLOTH),
        SOAP(10, "soap", MapColor.PINK);

        private static final BlockAesthetic.EnumType[] META_LOOKUP = new BlockAesthetic.EnumType[values().length];

        static {
            for (BlockAesthetic.EnumType blockType : values()) {
                META_LOOKUP[blockType.getMeta()] = blockType;
            }
        }

        private final int meta;
        private final String name;
        private final MapColor color;

        EnumType(int meta, String name, MapColor color) {
            this.meta = meta;
            this.name = name;
            this.color = color;
        }

        public static BlockAesthetic.EnumType byMeta(int meta) {
            return META_LOOKUP[meta];
        }

        @Override
        public String getName() {
            return name;
        }

        public int getMeta() {
            return meta;
        }

        public MapColor getMapColor() {
            return color;
        }
    }
}
