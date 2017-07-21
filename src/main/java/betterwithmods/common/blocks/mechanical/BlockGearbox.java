package betterwithmods.common.blocks.mechanical;

import betterwithmods.api.block.IOverpower;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWSounds;
import betterwithmods.common.blocks.BlockBrokenGearbox;
import betterwithmods.common.blocks.BlockRotate;
import betterwithmods.common.blocks.tile.TileGearbox;
import betterwithmods.util.DirUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class BlockGearbox extends BlockRotate implements IBlockActive, IOverpower {
    private final int maxPower;

    public BlockGearbox(int maxPower) {
        super(Material.WOOD);
        this.maxPower = maxPower;
        this.setHardness(2.0F);
        this.setDefaultState(getDefaultState().withProperty(DirUtils.FACING, EnumFacing.UP).withProperty(ACTIVE, false));
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("tooltip.gearbox.name"));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, side, flX, flY, flZ, meta, placer, hand);
        return setFacingInBlock(state, side.getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        EnumFacing facing = DirUtils.convertEntityOrientationToFacing(entity, EnumFacing.NORTH);
        world.setBlockState(pos, world.getBlockState(pos).withProperty(DirUtils.FACING, facing));
    }

    @Override
    public void nextState(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.cycleProperty(DirUtils.FACING).withProperty(ACTIVE, false));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) withTile(world, pos).ifPresent(t -> System.out.println(t));
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, 5, 5);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        onChange(world, pos);
        world.scheduleUpdate(pos, this, 5);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        onChange(world, pos);
    }

    public void onChange(World world, BlockPos pos) {
        if (!world.isRemote) {
            withTile(world, pos).ifPresent(TileGearbox::onChanged);
        }
//        world.notifyNeighborsOfStateExcept(pos,this,getFacing(world,pos));
    }


    public EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
        return getFacingFromState(world.getBlockState(pos));
    }

    public EnumFacing getFacingFromState(IBlockState state) {
        return state.getValue(DirUtils.FACING);
    }

    @Override
    public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing) {
        return state.withProperty(DirUtils.FACING, facing);
    }

    @Override
    public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canRotateHorizontally(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canRotateVertically(IBlockAccess world, BlockPos pos) {
        return true;
    }

//    @Override
//    public void rotateAroundYAxis(World world, BlockPos pos, boolean reverse) {
//        EnumFacing facing = getFacing(world, pos);
//        EnumFacing newFacing = DirUtils.rotateFacingAroundY(facing, reverse);
//        if (newFacing != facing) {
//            if (isGearboxOn(world, pos)) {
//                setGearboxState(world, pos, false);
//            }
//
//            world.setBlockState(pos, world.getBlockState(pos).withProperty(DirUtils.FACING, newFacing));
//
//            world.markBlockRangeForRenderUpdate(pos, pos);
//
//            world.scheduleBlockUpdate(pos, this, 10, 5);
//
//            MechanicalUtil.destoryHorizontalAxles(world, pos);
//        }
//    }


    private void emitGearboxParticles(World world, BlockPos pos, Random rand) {
        for (int i = 0; i < 5; i++) {
            float flX = pos.getX() + rand.nextFloat();
            float flY = pos.getY() + rand.nextFloat() * 0.5F + 1.0F;
            float flZ = pos.getZ() + rand.nextFloat();

            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, flX, flY, flZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (state.getValue(ACTIVE)) {
            emitGearboxParticles(world, pos, rand);
            if (rand.nextInt(50) == 0)
                world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, BWSounds.WOODCREAK, SoundCategory.BLOCKS, 0.25F, world.rand.nextFloat() * 0.25F + 0.25F, false);
        }
    }


    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean[] dirs = new boolean[6];
        for (int i = 0; i < 6; i++) {
            EnumFacing facing = EnumFacing.getFront(i);
            dirs[i] = MechanicalUtil.isAxle(world, pos.offset(facing), facing.getOpposite()) && this.getFacing(world, pos) != facing;
        }
        return state.withProperty(DirUtils.DOWN, dirs[0]).withProperty(DirUtils.UP, dirs[1]).withProperty(DirUtils.NORTH, dirs[2]).withProperty(DirUtils.SOUTH, dirs[3]).withProperty(DirUtils.WEST, dirs[4]).withProperty(DirUtils.EAST, dirs[5]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facing = state.getValue(DirUtils.FACING).getIndex();
        int active = isActive(state) ? 1 : 0;
        return active | facing << 1;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ACTIVE, (meta & 1) == 1).withProperty(DirUtils.FACING, EnumFacing.getFront(meta >> 1));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.FACING, ACTIVE, DirUtils.UP, DirUtils.DOWN, DirUtils.NORTH, DirUtils.SOUTH, DirUtils.WEST, DirUtils.EAST);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World worldIn, BlockPos pos) {
        return isActive(state) ? 15 : 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileGearbox(maxPower);
    }

    public Optional<TileGearbox> withTile(World world, BlockPos pos) {
        return Optional.of(getTile(world, pos));
    }

    public TileGearbox getTile(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileGearbox)
            return (TileGearbox) tile;
        return null;
    }

    @Override
    public void onChangeActive(World world, BlockPos pos, boolean newValue) {
        if (newValue) {
            world.playSound(null, pos, BWSounds.WOODCREAK, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.25F + 0.25F);
        }
    }

    @Override
    public void overpower(World world, BlockPos pos) {
        for (int i = 0; i < 10; i++) {
            world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.45F);
        }
        for (int i = 0; i < 5; i++) {
            float flX = pos.getX() + world.rand.nextFloat();
            float flY = pos.getY() + world.rand.nextFloat() * 0.5F + 1.0F;
            float flZ = pos.getZ() + world.rand.nextFloat();

            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, flX, flY, flZ, 0.0D, 0.0D, 0.0D);
        }
        EnumFacing facing = world.getBlockState(pos).getValue(DirUtils.FACING);
        BlockBrokenGearbox.EnumType type = this == BWMBlocks.WOODEN_GEARBOX ? BlockBrokenGearbox.EnumType.WOOD : BlockBrokenGearbox.EnumType.STEEL;
        world.setBlockState(pos, BWMBlocks.BROKEN_GEARBOX.getDefaultState().withProperty(DirUtils.FACING,facing).withProperty(BlockBrokenGearbox.TYPE, type));
    }

}
