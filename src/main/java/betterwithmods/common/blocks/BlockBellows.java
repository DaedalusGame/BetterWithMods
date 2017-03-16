package betterwithmods.common.blocks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWSounds;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockBellows extends BWMBlock implements IMechanicalBlock {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyBool TRIGGER = PropertyBool.create("trigger");

    public BlockBellows() {
        super(Material.WOOD);
        this.setTickRandomly(true);
        this.setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DirUtils.HORIZONTAL, EnumFacing.SOUTH)
                .withProperty(ACTIVE, false).withProperty(TRIGGER, false));
        this.setSoundType(SoundType.WOOD);
    }

    @Override
    public int tickRate(World world) {
        return 37;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        boolean emptyHands = playerIn.getHeldItem(EnumHand.MAIN_HAND) == ItemStack.EMPTY && playerIn.getHeldItem(EnumHand.OFF_HAND) == ItemStack.EMPTY && playerIn.isSneaking();

        if (worldIn.isRemote && emptyHands)
            return true;
        else if (!worldIn.isRemote && emptyHands) {
            worldIn.playSound(null, pos, this.getSoundType(state, worldIn, pos, playerIn).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            worldIn.setBlockState(pos, state.cycleProperty(DirUtils.HORIZONTAL).withProperty(ACTIVE, false));
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            worldIn.scheduleBlockUpdate(pos, this, 10, 5);
            return true;
        }
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return state.getBlock() == this && !state.getValue(ACTIVE);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.getBlock() == this && !state.getValue(ACTIVE);
    }

    @Deprecated
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ,
                                            int meta, EntityLivingBase living, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, side, flX, flY, flZ, meta, living, hand);
        if (side.ordinal() < 2)
            side = DirUtils.convertEntityOrientationToFlatFacing(living, side);
        return setFacingInBlock(state, side);
    }

    @Override
    public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing) {
        return state.withProperty(DirUtils.HORIZONTAL, facing);
    }

    @Override
    public EnumFacing getFacingFromBlockState(IBlockState state) {
        return state.getValue(DirUtils.HORIZONTAL);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
                                ItemStack stack) {
        if (entity == null)
            setFacingInBlock(state, EnumFacing.NORTH);
        EnumFacing facing = DirUtils.convertEntityOrientationToFlatFacing(entity);
        setFacingInBlock(state, facing);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state.getValue(ACTIVE))
            return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.6875F, 1.0F);
        return FULL_BLOCK_AABB;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        if (!isCurrentStateValid(world, pos)) {
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
            setTriggerMechanicalStateChange(world, pos, true);
        } else {
            boolean continuous = isTriggerMechanicalStateChange(world, pos);

            if (continuous) {
                if (isCurrentStateValid(world, pos)) {
                    setTriggerMechanicalStateChange(world, pos, false);
                }
            }
        }
    }

    public boolean isCurrentStateValid(World world, BlockPos pos) {
        boolean gettingPower = isInputtingMechPower(world, pos);
        boolean mechanicalOn = isMechanicalOn(world, pos);

        return gettingPower == mechanicalOn;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        boolean gettingPower = isInputtingMechPower(world, pos);
        boolean isMechOn = isMechanicalOn(world, pos);
        boolean continuous = isTriggerMechanicalStateChange(world, pos);

        if (isMechOn != gettingPower) {
            if (continuous) {
                setTriggerMechanicalStateChange(world, pos, false);
                setMechanicalOn(world, pos, gettingPower);
                world.scheduleBlockUpdate(pos, this, tickRate(world), 5);// world.markBlockForUpdate(pos);
                blow(world, pos);
            } else {
                world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
                setTriggerMechanicalStateChange(world, pos, true);
            }
        } else if (continuous) {
            setTriggerMechanicalStateChange(world, pos, false);
        }
    }

    public boolean isTriggerMechanicalState(IBlockState state) {
        return state.getValue(TRIGGER);
    }

    public boolean isTriggerMechanicalStateChange(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getValue(TRIGGER);
    }

    public void setTriggerMechanicalStateChange(World world, BlockPos pos, boolean continuous) {
        world.setBlockState(pos, world.getBlockState(pos).withProperty(TRIGGER, continuous));
    }

    @Override
    public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public void rotateAroundYAxis(World world, BlockPos pos, boolean reverse) {
        if (DirUtils.rotateAroundY(this, world, pos, reverse)) {
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
            MechanicalUtil.destoryHorizontalAxles(world, pos);
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);// world.markBlockForUpdate(pos);
        }
    }

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public boolean isInputtingMechPower(World world, BlockPos pos) {
        return MechanicalUtil.isBlockPoweredByAxle(world, pos, this) || MechanicalUtil.isPoweredByCrank(world, pos);
    }

    @Override
    public boolean isOutputtingMechPower(World world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canInputPowerToSide(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        EnumFacing facing = getFacingFromBlockState(world.getBlockState(pos));
        return dir != facing && dir != EnumFacing.UP;
    }

    @Override
    public void overpower(World world, BlockPos pos) {
        breakBellows(world, pos);
    }

    @Override
    public boolean isMechanicalOn(IBlockAccess world, BlockPos pos) {
        return isMechanicalOnFromState(world.getBlockState(pos));
    }

    @Override
    public void setMechanicalOn(World world, BlockPos pos, boolean isOn) {
        world.setBlockState(pos, world.getBlockState(pos).withProperty(ACTIVE, isOn));
    }

    @Override
    public boolean isMechanicalOnFromState(IBlockState state) {
        return state.getValue(ACTIVE);
    }

    public void blow(World world, BlockPos pos) {
        if (isMechanicalOn(world, pos)) {
            stokeFlames(world, pos);
            liftCollidingEntities(world, pos);
            world.playSound(null, pos, BWSounds.BELLOW, SoundCategory.BLOCKS, 0.7F, world.rand.nextFloat() * 0.25F + 2.5F);
        } else {
            world.playSound(null, pos, BWSounds.BELLOW, SoundCategory.BLOCKS, 0.2F, world.rand.nextFloat() * 0.25F + 2.5F);
        }
    }

    private void stokeFlames(World world, BlockPos pos) {
        EnumFacing dir = getFacingFromBlockState(world.getBlockState(pos));
        EnumFacing dirLeft = DirUtils.rotateFacingAroundY(getFacingFromBlockState(world.getBlockState(pos)), false);
        EnumFacing dirRight = DirUtils.rotateFacingAroundY(getFacingFromBlockState(world.getBlockState(pos)), true);

        for (int i = 0; i < 3; i++) {
            BlockPos dirPos = pos.offset(dir, 1 + i);
            //
            Block target = world.getBlockState(dirPos).getBlock();

            if (target == Blocks.FIRE || target == BWMBlocks.STOKED_FLAME)
                stokeFire(world, dirPos);
            else if (!world.isAirBlock(dirPos))
                break;
            //
            BlockPos posLeft = dirPos.offset(dirLeft);

            Block targetLeft = world.getBlockState(posLeft).getBlock();
            if (targetLeft == Blocks.FIRE || targetLeft == BWMBlocks.STOKED_FLAME)
                stokeFire(world, posLeft);
            //
            BlockPos posRight = dirPos.offset(dirRight);

            Block targetRight = world.getBlockState(posRight).getBlock();
            if (targetRight == Blocks.FIRE || targetRight == BWMBlocks.STOKED_FLAME)
                stokeFire(world, posRight);
        }
    }

    private void stokeFire(World world, BlockPos pos) {
        BlockPos down = pos.down();
        if (world.getBlockState(down).getBlock() == BWMBlocks.HIBACHI) {
            int flag = (world.getBlockState(pos).getBlock() == BWMBlocks.STOKED_FLAME) ? 4 : 3;
            world.setBlockState(pos, BWMBlocks.STOKED_FLAME.getDefaultState(), flag);
        } else
            world.setBlockToAir(pos);
    }

    private void liftCollidingEntities(World world, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        List<Entity> list = world.getEntitiesWithinAABB(Entity.class,
                new AxisAlignedBB(x, y + 0.6875F, z, x + 1, y + 1, z + 1));

        float extendedY = y + 1;

        if (list != null && list.size() > 0) {
            for (Entity entity : list) {
                if (!entity.isDead && (entity.canBePushed() || entity instanceof EntityItem)) {
                    double tempY = entity.getEntityBoundingBox().minY;

                    if (tempY < extendedY) {
                        double entityOffset = extendedY - tempY;
                        entity.setPosition(entity.posX, entity.posY + entityOffset, entity.posZ);
                    }
                }
            }
        }
    }

    public void breakBellows(World world, BlockPos pos) {
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(Blocks.WOODEN_SLAB, 2, 0));
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(BWMItems.MATERIAL, 1, 0));
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(BWMItems.MATERIAL, 2, 6));
        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.3F,
                world.rand.nextFloat() * 0.1F + 0.45F);
        world.setBlockToAir(pos);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = isMechanicalOnFromState(state) ? 8 : 0;
        meta += isTriggerMechanicalState(state) ? 4 : 0;
        return meta + state.getValue(DirUtils.HORIZONTAL).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean isActive = false;
        if (meta > 7) {
            isActive = true;
            meta -= 8;
        }
        boolean isTrigger = meta > 3;
        if (isTrigger)
            meta -= 4;
        return this.getDefaultState().withProperty(ACTIVE, isActive).withProperty(TRIGGER, isTrigger)
                .withProperty(DirUtils.HORIZONTAL, EnumFacing.getHorizontal(meta));
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.HORIZONTAL, ACTIVE, TRIGGER);
    }
}
