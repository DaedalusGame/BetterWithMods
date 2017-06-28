package betterwithmods.common.blocks;

import betterwithmods.api.block.IAxle;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.util.DirUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockBrokenGearbox extends BWMBlock implements IMechanicalBlock {
    public static final PropertyInteger STAGE = PropertyInteger.create("repair", 0, 1);
    public BlockBrokenGearbox() {
        super (Material.WOOD);
        this.setHardness(2.0F);
        this.setDefaultState(getDefaultState().withProperty(DirUtils.FACING, EnumFacing.UP).withProperty(STAGE, 0));
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        setTickRandomly(true);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("tooltip.broken_gearbox.name"));
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (MechanicalUtil.isBlockPoweredByAxle(worldIn, pos, this)) {
            for (int i = 0; i < 5; i++) {
                float flX = pos.getX() + rand.nextFloat();
                float flY = pos.getY() + rand.nextFloat() * 0.5F + 1.0F;
                float flZ = pos.getZ() + rand.nextFloat();

                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, flX, flY, flZ, 0.0D, 0.0D, 0.0D);
            }
            worldIn.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.BLOCKS, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.45F);
        }
        super.randomTick(worldIn, pos, state, rand);
    }


    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ, int meta, EntityLivingBase placer) {
        IBlockState state = super.getStateForPlacement(world, pos, side, flX, flY, flZ, meta, placer);
        return state.withProperty(DirUtils.FACING, side.getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        EnumFacing facing = DirUtils.convertEntityOrientationToFacing(entity, EnumFacing.NORTH);
        world.setBlockState(pos, world.getBlockState(pos).withProperty(DirUtils.FACING, facing));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack hold = player.getHeldItem(hand);
        if (!hold.isEmpty()) {
            if (BWOreDictionary.listContains(hold, OreDictionary.getOres("gearWood"))) {
                if (world.isRemote)
                    return true;
                else {
                    advanceStage(world, pos, state);
                    if (!player.capabilities.isCreativeMode)
                        hold.shrink(1);
                    return true;
                }
            }
        }
        return false;
    }

    private void advanceStage(World world, BlockPos pos, IBlockState state) {
        if (state.getValue(STAGE) == 0)
            world.setBlockState(pos, state.withProperty(STAGE, 1));
        else
            world.setBlockState(pos, BWMBlocks.GEARBOX.getDefaultState().withProperty(DirUtils.FACING, state.getValue(DirUtils.FACING)).withProperty(BlockGearbox.ISACTIVE, false));
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> list = super.getDrops(world, pos, state, fortune);
        if (state.getValue(STAGE) == 1)
            list.add(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR));
        return list;
    }

    private boolean isAxle(IBlockAccess world, BlockPos origin, EnumFacing facing) {
        BlockPos pos = origin.offset(facing);
        Block block = world.getBlockState(pos).getBlock();
        return block instanceof IAxle && ((IAxle) block).isAxleOrientedToFacing(world, pos, facing.getOpposite());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean[] dirs = new boolean[6];
        for (int i = 0; i < 6; i++) {
            EnumFacing facing = EnumFacing.getFront(i);
            dirs[i] = isAxle(world, pos, facing) && state.getValue(DirUtils.FACING) != facing;
        }
        return state.withProperty(DirUtils.DOWN, dirs[0])
                .withProperty(DirUtils.UP, dirs[1])
                .withProperty(DirUtils.NORTH, dirs[2])
                .withProperty(DirUtils.SOUTH, dirs[3])
                .withProperty(DirUtils.WEST, dirs[4])
                .withProperty(DirUtils.EAST, dirs[5]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(STAGE) == 1 ? 8 : 0;
        meta += state.getValue(DirUtils.FACING).getIndex();
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int stage = 0;
        if (meta > 7) {
            stage = 1;
            meta -= 8;
        }
        return this.getDefaultState().withProperty(STAGE, stage).withProperty(DirUtils.FACING, EnumFacing.getFront(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.FACING, STAGE, DirUtils.UP, DirUtils.DOWN, DirUtils.NORTH, DirUtils.SOUTH, DirUtils.WEST, DirUtils.EAST);
    }

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return false;
    }

    @Override
    public boolean isInputtingMechPower(World world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOutputtingMechPower(World world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canInputPowerToSide(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        EnumFacing facing = world.getBlockState(pos).getValue(DirUtils.FACING);
        return dir == facing;
    }

    @Override
    public void overpower(World world, BlockPos pos) {

    }

    @Override
    public boolean isMechanicalOn(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public void setMechanicalOn(World world, BlockPos pos, boolean isOn) {

    }

    @Override
    public boolean isMechanicalOnFromState(IBlockState state) {
        return false;
    }
}
