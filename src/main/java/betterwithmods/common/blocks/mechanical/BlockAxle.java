package betterwithmods.common.blocks.mechanical;

import betterwithmods.api.block.IOverpower;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.BWSounds;
import betterwithmods.common.blocks.BlockRotate;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

import static net.minecraft.util.EnumFacing.Axis.Y;

public class BlockAxle extends BlockRotate implements IOverpower, IBlockActive {
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    private static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
    private static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
    private static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);

    public BlockAxle() {
        super(Material.WOOD);
        this.setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, Y).withProperty(ACTIVE, false));
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 0);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, ACTIVE);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(AXIS, facing.getAxis());
    }


    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AXIS, DirUtils.getAxis(meta >> 2)).withProperty(ACTIVE, (meta & 1) == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int axis = state.getValue(AXIS).ordinal() << 2;
        int active = state.getValue(ACTIVE) ? 1 : 0;
        return active | axis;
    }


    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        withTile(worldIn, pos).ifPresent(t -> {
            t.setSignal((byte) 0);
            t.setPower(0);
        });
        onChange(worldIn, pos);
        super.updateTick(worldIn, pos, state, rand);
    }

    @Override
    public void nextState(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.withProperty(ACTIVE, false).cycleProperty(AXIS));
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
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(AXIS)) {
            case X:
                return X_AABB;
            case Y:
                return Y_AABB;
            case Z:
            default:
                return Z_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) withTile(world, pos).ifPresent(t -> System.out.println(t));
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, 1, 5);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        onChange(world, pos);
    }

    public void onChange(World world, BlockPos pos) {
        if (!world.isRemote) {
            withTile(world, pos).ifPresent(TileAxle::onChanged);
        }
    }


    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileAxle(4, (byte) 3);
    }

    public Optional<TileAxle> withTile(World world, BlockPos pos) {
        return Optional.of(getTile(world, pos));
    }

    public TileAxle getTile(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileAxle)
            return (TileAxle) tile;
        return null;
    }

    public EnumFacing[] getAxisDirections(IBlockState state) {
        return DirUtils.getAxisDirection(state.getValue(AXIS));
    }

    public EnumFacing.Axis getAxis(IBlockState state) {
        return state.getValue(AXIS);
    }

    @Override
    public void overpower(World world, BlockPos pos) {
        world.setBlockToAir(pos);
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(this, 1, damageDropped(world.getBlockState(pos))));
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (isActive(state)) {
            emitAxleParticles(world, pos, rand);
            if (rand.nextInt(200) == 0)
                world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, BWSounds.WOODCREAK, SoundCategory.BLOCKS, 0.15F, rand.nextFloat() * 0.1F + 0.5F, false);
        }
    }

    private void emitAxleParticles(World world, BlockPos pos, Random rand) {
        int pow = withTile(world, pos).map(t -> t.getPower()).orElse(0);
        for (int i = 0; i < pow; i++) {
            float flX = pos.getX() + rand.nextFloat();
            float flY = pos.getY() + rand.nextFloat() * 0.5F + 0.625F;
            float flZ = pos.getZ() + rand.nextFloat();
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, flX, flY, flZ, 0.0D, 0.0D, 0.0D);
        }
    }

}
