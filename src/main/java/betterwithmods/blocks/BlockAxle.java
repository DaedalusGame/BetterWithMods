package betterwithmods.blocks;

import betterwithmods.BWMBlocks;
import betterwithmods.BWSounds;
import betterwithmods.api.block.IAxle;
import betterwithmods.api.block.IMechanical;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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

import java.util.Random;

public class BlockAxle extends BWMBlock implements IMechanical, IAxle, IMultiVariants {
    public static final PropertyInteger AXLEDIR = PropertyInteger.create("dir", 0, 2);
    public static final PropertyInteger SIGNAL = PropertyInteger.create("signal", 0, 4);
    public static final int tickRate = 1;
    public static final EnumFacing[][] facings = {{EnumFacing.UP, EnumFacing.DOWN}, {EnumFacing.NORTH, EnumFacing.SOUTH}, {EnumFacing.WEST, EnumFacing.EAST}};

    //TODO: Make a reinforced axle with a max power of 5
    public BlockAxle() {
        super(Material.WOOD);
        this.setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXLEDIR, 0).withProperty(SIGNAL, 0));
        this.setSoundType(SoundType.WOOD);
    }

    @Override
    public String[] getVariants() {
        return new String[]{"dir=0,signal=0"};
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        boolean emptyHands = player.getHeldItem(EnumHand.MAIN_HAND) == null && player.getHeldItem(EnumHand.OFF_HAND) == null && player.isSneaking();

        if (world.isRemote && emptyHands)
            return true;
        else if (!world.isRemote && emptyHands) {
            int dir = state.getValue(AXLEDIR) == 2 ? 0 : state.getValue(AXLEDIR) + 1;
            world.playSound(null, pos, this.getSoundType(state, world, pos, player).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
            world.setBlockState(pos, state.withProperty(SIGNAL, 0).withProperty(AXLEDIR, dir));
            world.notifyNeighborsOfStateChange(pos, this);
            world.scheduleBlockUpdate(pos, this, 10, 5);
            return true;
        }
        return false;
    }

    @Override
    public int tickRate(World world) {
        return tickRate;
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
        int ori = state.getValue(AXLEDIR);
        AxisAlignedBB axis = new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
        switch (ori) {
            case 0:
                break;
            case 1:
                axis = new AxisAlignedBB(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);
                break;
            case 2:
                axis = new AxisAlignedBB(0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
                break;
        }
        return axis;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ, int meta, EntityLivingBase placer) {
        IBlockState state = super.onBlockPlaced(world, pos, side, flX, flY, flZ, meta, placer);
        return setAxisAlignment(state, side);
    }

    @Override
    public EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
        return EnumFacing.getFront(world.getBlockState(pos).getValue(AXLEDIR).intValue() * 2);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, tickRate, 5);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        validatePowerLevel(world, pos);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        setPowerLevel(world, pos, 0);
        validatePowerLevel(world, pos);
    }

    private IBlockState setAxisAlignment(IBlockState state, EnumFacing facing) {
        int axis;
        switch (facing) {
            case DOWN:
            case UP:
                axis = 0;
                break;
            case NORTH:
            case SOUTH:
                axis = 1;
                break;
            default:
                axis = 2;
        }
        return state.withProperty(AXLEDIR, axis).withProperty(SIGNAL, 0);
    }

    private void setPowerLevel(World world, BlockPos pos, int power) {
        IBlockState state = world.getBlockState(pos);

        state = setPowerLevelInState(state, power);

        world.setBlockState(pos, state);
    }

    private IBlockState setPowerLevelInState(IBlockState state, int power) {
        return state.withProperty(SIGNAL, power);
    }

    private void validatePowerLevel(World world, BlockPos pos) {
        int currentPower = getPowerLevel(world, pos);
        int axis = getAxisAlignment(world, pos);

        int maxNeighborPower = 0;
        int greaterPowerNeighbors = 0;

        for (int i = 0; i < 2; i++) {
            EnumFacing dir = facings[axis][i];
            BlockPos pos2 = pos.offset(dir); //position of block checked
            Block block = world.getBlockState(pos2).getBlock();

            if (block instanceof IMechanical) {
                IMechanical mech = (IMechanical) block;
                int powerLevel = mech.getMechPowerLevelToFacing(world, pos2, dir.getOpposite());

                if (powerLevel > maxNeighborPower)
                    maxNeighborPower = powerLevel;
                if (powerLevel > currentPower)
                    greaterPowerNeighbors++;
            }
        }

        if (greaterPowerNeighbors >= 2) {
            breakAxle(world, pos);
            return;
        }

        int newPower = currentPower;

        if (maxNeighborPower > currentPower) {
            if (maxNeighborPower == 1) {
                breakAxle(world, pos);
                return;
            }
            newPower = maxNeighborPower - 1;
        } else
            newPower = 0;

        if (newPower != currentPower)
            setPowerLevel(world, pos, newPower);
    }

    public void breakAxle(World world, BlockPos pos) {
        if (!world.isRemote) {
            InvUtils.ejectStackWithOffset(world, pos, new ItemStack(BWMBlocks.AXLE, 1, 0));
            world.setBlockToAir(pos);
        }
    }

    @Override
    public int getMechPowerLevelToFacing(World world, BlockPos pos, EnumFacing dir) {
        int alignment = getAxisAlignment(world, pos);
        if (dir.ordinal() >> 1 == alignment)
            return getPowerLevel(world, pos);
        return 0;
    }

    @Override
    public boolean isMechanicalJunction() {
        return false;
    }

    @Override
    public int getPowerLevel(IBlockAccess world, BlockPos pos) {
        return getPowerLevelFromState(world.getBlockState(pos));
    }

    private int getPowerLevelFromState(IBlockState state) {
        return state.getValue(SIGNAL).intValue();
    }

    @Override
    public int getAxisAlignment(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getValue(AXLEDIR);
    }

    @Override
    public boolean isAxleOrientedToFacing(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        int axis = getAxisAlignment(world, pos);

        return dir == facings[axis][0] || dir == facings[axis][1];
    }

    public void overpower(World world, BlockPos pos) {
        int axis = getAxisAlignment(world, pos);

        overpowerBlockToSide(world, pos, axis, facings[axis][0]);
        overpowerBlockToSide(world, pos, axis, facings[axis][1]);
    }

    private void overpowerBlockToSide(World world, BlockPos pos, int source, EnumFacing dir) {
        BlockPos pos2 = pos.offset(dir);

        Block block = world.getBlockState(pos2).getBlock();

        if ((block == BWMBlocks.AXLE)) {
            int axis = ((BlockAxle) world.getBlockState(pos2).getBlock()).getAxisAlignment(world, pos2);

            if (axis == source)
                overpowerBlockToSide(world, pos2, source, dir);
        } else if (block instanceof IMechanicalBlock) {
            IMechanicalBlock mech = (IMechanicalBlock) block;

            if (mech.canInputPowerToSide(world, pos2, dir.getOpposite()))
                mech.overpower(world, pos2);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (state.getValue(SIGNAL) > 0) {
            emitAxleParticles(world, pos, rand);

            if (rand.nextInt(200) == 0)
                world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, BWSounds.WOODCREAK, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.1F + 0.5F, false);
        }
    }

    private void emitAxleParticles(World world, BlockPos pos, Random rand) {
        int pow = getPowerLevel(world, pos);
        for (int i = 0; i < pow; i++) {
            float flX = pos.getX() + rand.nextFloat();
            float flY = pos.getY() + rand.nextFloat() * 0.5F + 0.625F;
            float flZ = pos.getZ() + rand.nextFloat();
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, flX, flY, flZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private int powerLevelFromMeta(int meta) {
        if (meta < 3)
            return 0;
        else if (meta < 6)
            return 1;
        else if (meta < 9)
            return 2;
        else if (meta < 12)
            return 3;
        else if (meta < 15)
            return 4;
        else
            return 0;
    }

    private int orientationFromMeta(int meta) {
        int powerLevel = 3 * powerLevelFromMeta(meta);
        return meta - powerLevel;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(SIGNAL) * 3) + state.getValue(AXLEDIR);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int power = powerLevelFromMeta(meta);
        int ori = orientationFromMeta(meta);
        return this.getDefaultState().withProperty(AXLEDIR, ori).withProperty(SIGNAL, power);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXLEDIR, SIGNAL);
    }
}
