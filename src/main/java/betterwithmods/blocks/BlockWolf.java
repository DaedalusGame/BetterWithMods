package betterwithmods.blocks;

import betterwithmods.util.DirUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockWolf extends BWMBlock {
    public BlockWolf() {
        super(Material.CLOTH);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.CLOTH);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DirUtils.FACING, EnumFacing.NORTH));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (world.isRemote) {
            for (int i = 0; i < 15; i++)
                world.spawnParticle(EnumParticleTypes.HEART, pos.getX() + world.rand.nextFloat(), pos.getY() + 1.0F, pos.getZ() + world.rand.nextFloat(), 0.0F, 0.1F, 0.0F);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.getHeldItemMainhand() == ItemStack.field_190927_a && player.getHeldItemOffhand() == ItemStack.field_190927_a) {
            if (world.isRemote)
                world.spawnParticle(EnumParticleTypes.HEART, pos.getX() + world.rand.nextFloat(), pos.getY() + 1.0F, pos.getZ() + world.rand.nextFloat(), 0.0F, 0.1F, 0.0F);
            return true;
        }
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ, int meta, EntityLivingBase placer) {
        IBlockState state = super.getStateForPlacement(world, pos, side, flX, flY, flZ, meta, placer);
        return state.withProperty(DirUtils.FACING, DirUtils.convertEntityOrientationToFacing(placer, side));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (rand.nextInt(200) == 0) {
            if (rand.nextBoolean())
                world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENTITY_WOLF_AMBIENT, SoundCategory.BLOCKS, 0.7F, 1.0F, false);
            else
                world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENTITY_WOLF_PANT, SoundCategory.BLOCKS, 0.7F, 1.0F, false);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DirUtils.FACING).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DirUtils.FACING, EnumFacing.getFront(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.FACING);
    }
}
