package betterwithmods.common.blocks;

import betterwithmods.api.block.IAxle;
import betterwithmods.api.block.IMechanical;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWSounds;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
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

import static net.minecraft.util.EnumFacing.Axis.Y;

public class BlockAxle extends BlockRotate implements IMechanical, IAxle, IMultiVariants {
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.<EnumFacing.Axis>create("axis", EnumFacing.Axis.class);
    public static final PropertyInteger SIGNAL = PropertyInteger.create("signal", 0, 4);
    public static final int TICK_RATE = 1;

    private static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
    private static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
    private static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);

    //TODO: Make a reinforced axle with a max power of 5
    public BlockAxle() {
        super(Material.WOOD);
        this.setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, Y).withProperty(SIGNAL, 0));
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 0);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public String[] getVariants() {
        return new String[]{"axis=y,signal=0"};
    }

    @Override
    public void nextState(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.withProperty(SIGNAL, 0).cycleProperty(AXIS));
    }

    @Override
    public int tickRate(World world) {
        return TICK_RATE;
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
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, TICK_RATE, 5);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        validatePowerLevel(world, pos);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        setPowerLevel(world, pos, 0);
        validatePowerLevel(world, pos);
    }

    private void setPowerLevel(World world, BlockPos pos, int power) {
        IBlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.withProperty(SIGNAL, power));
    }

    private void validatePowerLevel(World world, BlockPos pos) {
        int currentPower = getPowerLevel(world, pos);
        int maxNeighborPower = 0;
        int greaterPowerNeighbors = 0;

        EnumFacing.Axis axis = world.getBlockState(pos).getValue(AXIS);
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (dir.getAxis() == axis) {
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
        }

        if (greaterPowerNeighbors >= 2) {
            breakAxle(world, pos);
            return;
        }

        int newPower;

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
        EnumFacing.Axis axis = world.getBlockState(pos).getValue(AXIS);
        if (dir.getAxis() == axis)
            return getPowerLevel(world, pos);
        else
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
        return state.getValue(SIGNAL);
    }

    @Override
    public int getAxisAlignment(IBlockAccess world, BlockPos pos) {
        return DirUtils.getLegacyAxis(world.getBlockState(pos).getValue(AXIS));
    }

    @Override
    public boolean isAxleOrientedToFacing(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        EnumFacing.Axis axis = world.getBlockState(pos).getValue(AXIS);
        return axis == dir.getAxis();
    }

    public void overpower(World world, BlockPos pos) {
        EnumFacing.Axis axis = world.getBlockState(pos).getValue(AXIS);
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (dir.getAxis() == axis) {
                overpowerBlockToSide(world, pos, axis, dir);
            }
        }
    }

    private void overpowerBlockToSide(World world, BlockPos pos, EnumFacing.Axis sourceAxis, EnumFacing dir) {
        BlockPos pos2 = pos.offset(dir);

        Block block = world.getBlockState(pos2).getBlock();

        if ((block == BWMBlocks.AXLE)) {
            EnumFacing.Axis axis = world.getBlockState(pos2).getValue(AXIS);

            if (axis == sourceAxis)
                overpowerBlockToSide(world, pos2, sourceAxis, dir);
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
                world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, BWSounds.WOODCREAK, SoundCategory.BLOCKS, 0.15F, rand.nextFloat() * 0.1F + 0.5F, false);
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

    private EnumFacing.Axis orientationFromMeta(int meta) {
        int powerLevel = 3 * powerLevelFromMeta(meta);
        return DirUtils.getAxisFromLegacy(meta - powerLevel);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(SIGNAL) * 3) + DirUtils.getLegacyAxis(state.getValue(AXIS));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int power = powerLevelFromMeta(meta);
        EnumFacing.Axis ori = orientationFromMeta(meta);
        return this.getDefaultState().withProperty(AXIS, ori).withProperty(SIGNAL, power);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, SIGNAL);
    }
}
