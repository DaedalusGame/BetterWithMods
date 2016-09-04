package betterwithmods.blocks;

import betterwithmods.util.DirUtils;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockGearBoostedRail extends BlockRailBase
{
    public static final PropertyEnum<EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate<EnumRailDirection>()
    {
        @Override
        public boolean apply(@Nullable BlockRailBase.EnumRailDirection railDirection)
        {
            return railDirection != EnumRailDirection.NORTH_EAST && railDirection != EnumRailDirection.NORTH_WEST && railDirection != EnumRailDirection.SOUTH_EAST && railDirection != EnumRailDirection.SOUTH_WEST;
        }
    });
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockGearBoostedRail()
    {
        super(false);
        this.setUnlocalizedName("bwm:booster");
        this.setHardness(0.7F);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH).withProperty(POWERED, false));
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(world, pos, state);
        if(!world.isRemote)
            state.neighborChanged(world, pos, this);
    }

    @Override
    protected void updateState(IBlockState state, World world, BlockPos pos, Block block)
    {
        boolean pow = state.getValue(POWERED);
        boolean gear = world.getBlockState(pos.down()).getBlock() instanceof BlockGearbox && isGearboxOn(state, world, pos);
        if(pow != gear)
        {
            world.setBlockState(pos, state.withProperty(POWERED, gear), 3);
            world.notifyNeighborsOfStateChange(pos.down(), this);
            if(state.getValue(SHAPE).isAscending())
                world.notifyNeighborsOfStateChange(pos.up(), this);
        }
    }

    private boolean isGearboxOn(IBlockState state, World world, BlockPos pos)
    {
        EnumRailDirection dir = state.getValue(SHAPE);
        IBlockState below = world.getBlockState(pos.down());
        EnumFacing face = below.getValue(DirUtils.FACING);
        boolean correctFace = false;
        if(dir == EnumRailDirection.ASCENDING_EAST || dir == EnumRailDirection.ASCENDING_WEST || dir == EnumRailDirection.EAST_WEST)
        {
            correctFace = face == EnumFacing.DOWN || face == EnumFacing.NORTH || face == EnumFacing.SOUTH;
        }
        else if(dir == EnumRailDirection.ASCENDING_NORTH || dir == EnumRailDirection.ASCENDING_SOUTH || dir == EnumRailDirection.NORTH_SOUTH)
        {
            correctFace = face == EnumFacing.DOWN || face == EnumFacing.EAST || face == EnumFacing.WEST;
        }
        return correctFace && ((BlockGearbox)below.getBlock()).isGearboxOn(world, pos.down());
    }

    @Override
    public boolean isFlexibleRail(IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public IProperty<EnumRailDirection> getShapeProperty()
    {
        return SHAPE;
    }

    @Override
    public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos)
    {
        BlockPos down = pos.down();
        Block block = world.getBlockState(down).getBlock();
        if(block instanceof BlockGearbox)
        {
            boolean isRedstonePowered = world.isBlockPowered(down);
            if(!isRedstonePowered)
            {
                BlockGearbox gear = (BlockGearbox)block;
                EnumFacing face = world.getBlockState(down).getValue(DirUtils.FACING);
                if(face != EnumFacing.UP)
                {
                    if(gear.isGearboxOn(world, down))
                    {
                        IBlockState state = world.getBlockState(pos);
                        if(state.getBlock() == this)
                        {
                            double planarMotion = Math.sqrt(cart.motionX * cart.motionX + cart.motionZ * cart.motionZ);
                            if(state.getValue(SHAPE) == EnumRailDirection.ASCENDING_NORTH || state.getValue(SHAPE) == EnumRailDirection.ASCENDING_SOUTH || state.getValue(SHAPE) == EnumRailDirection.NORTH_SOUTH)
                            {
                                if(face == EnumFacing.EAST || face == EnumFacing.WEST)
                                {
                                    if(face == EnumFacing.EAST)
                                    {
                                        if(planarMotion > 0.01D)
                                        {
                                            if(cart.motionZ > 0.0D)
                                            {
                                                cart.motionZ -= cart.motionZ / planarMotion * 0.06D;
                                                if(!world.isRemote && planarMotion > 0.02D && world.rand.nextDouble() < planarMotion)
                                                    world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 1.0F, 2.0F);
                                            }
                                            else
                                                cart.motionZ += cart.motionZ / planarMotion * 0.06D;
                                        }
                                        else if(!world.getBlockState(pos.west()).isOpaqueCube())
                                            cart.motionZ = -0.02D;
                                    }
                                    else if(face == EnumFacing.WEST)
                                    {
                                        if(planarMotion > 0.01D)
                                        {
                                            if(cart.motionZ < 0.0D)
                                            {
                                                cart.motionZ -= cart.motionZ / planarMotion * 0.06D;
                                                if(!world.isRemote && planarMotion > 0.02D && world.rand.nextDouble() < planarMotion)
                                                    world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 1.0F, 2.0F);
                                            }
                                            else
                                                cart.motionZ += cart.motionZ / planarMotion * 0.06D;
                                        }
                                        else if(!world.getBlockState(pos.east()).isOpaqueCube())
                                            cart.motionZ = 0.02D;
                                    }
                                }
                                else if(face == EnumFacing.DOWN)
                                {
                                    if(planarMotion > 0.01D)
                                        cart.motionZ += cart.motionZ / planarMotion * 0.06D;
                                    else
                                    {
                                        if(world.getBlockState(pos.west()).isOpaqueCube())
                                            cart.motionZ = 0.02D;
                                        else if(world.getBlockState(pos.east()).isOpaqueCube())
                                            cart.motionZ = -0.02D;
                                    }
                                }
                            }
                            else if(state.getValue(SHAPE) == EnumRailDirection.ASCENDING_EAST || state.getValue(SHAPE) == EnumRailDirection.ASCENDING_WEST || state.getValue(SHAPE) == EnumRailDirection.EAST_WEST)
                            {
                                if(face == EnumFacing.NORTH || face == EnumFacing.SOUTH)
                                {
                                    if(face == EnumFacing.SOUTH)
                                    {
                                        if(planarMotion > 0.01D)
                                        {
                                            if(cart.motionX > 0.0D)
                                            {
                                                cart.motionX -= cart.motionX / planarMotion * 0.06D;
                                                if(!world.isRemote && planarMotion > 0.02D && world.rand.nextDouble() < planarMotion)
                                                    world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 1.0F, 2.0F);
                                            }
                                            else
                                                cart.motionX += cart.motionX / planarMotion * 0.06D;
                                        }
                                        else if(!world.getBlockState(pos.west()).isOpaqueCube())
                                            cart.motionX = -0.02D;
                                    }
                                    else if(face == EnumFacing.NORTH)
                                    {
                                        if(planarMotion > 0.01D)
                                        {
                                            if(cart.motionX < 0.0D)
                                            {
                                                cart.motionX -= cart.motionX / planarMotion * 0.06D;
                                                if(!world.isRemote && planarMotion > 0.02D && world.rand.nextDouble() < planarMotion)
                                                    world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 1.0F, 2.0F);
                                            }
                                            else
                                                cart.motionX += cart.motionX / planarMotion * 0.06D;
                                        }
                                        else if(!world.getBlockState(pos.east()).isOpaqueCube())
                                            cart.motionX = 0.02D;
                                    }
                                }
                                else if(face == EnumFacing.DOWN)
                                {
                                    if(planarMotion > 0.01D)
                                        cart.motionX += cart.motionX / planarMotion * 0.06D;
                                    else
                                    {
                                        if(world.getBlockState(pos.west()).isOpaqueCube())
                                            cart.motionX = 0.02D;
                                        else if(world.getBlockState(pos.east()).isOpaqueCube())
                                            cart.motionX = -0.02D;
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        IBlockState state = world.getBlockState(pos);
                        if(state.getBlock() == this) {
                            double planarMotion = Math.sqrt(cart.motionX * cart.motionX + cart.motionZ * cart.motionZ);
                            if (planarMotion > 0.01D) {
                                double zMotion = Math.sqrt(cart.motionZ * cart.motionZ);
                                double xMotion = Math.sqrt(cart.motionX * cart.motionX);
                                if (xMotion > 0.0D) {
                                    cart.motionX -= cart.motionX / planarMotion * 0.06D;
                                } else if (zMotion > 0.0D) {
                                    cart.motionZ -= cart.motionZ / planarMotion * 0.06D;
                                }
                                if (!world.isRemote && planarMotion > 0.02D && world.rand.nextDouble() < planarMotion)
                                    world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 1.0F, 2.0F);
                            } else if(state.getValue(SHAPE) == EnumRailDirection.EAST_WEST || state.getValue(SHAPE) == EnumRailDirection.NORTH_SOUTH)
                            {
                                cart.motionX = 0.0D;
                                cart.motionZ = 0.0D;
                            }
                        }
                    }
                }

            }
        }
        else
        {
            if(!world.isRemote)
            {
                double planarMotion = Math.sqrt(cart.motionX * cart.motionX + cart.motionZ * cart.motionZ);
                if(planarMotion > 0.02D && world.rand.nextDouble() < planarMotion)
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 1.0F, 2.0F);
            }
        }
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return Blocks.GOLDEN_RAIL.withRotation(state, rot);
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return Blocks.GOLDEN_RAIL.withMirror(state, mirrorIn);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta & 7)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(SHAPE).getMetadata();

        if (state.getValue(POWERED))
        {
            i |= 8;
        }

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {SHAPE, POWERED});
    }
}
