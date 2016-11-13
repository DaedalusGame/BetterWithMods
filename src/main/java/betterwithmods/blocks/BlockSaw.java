package betterwithmods.blocks;

import betterwithmods.BWMBlocks;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.config.BWConfig;
import betterwithmods.craft.SawInteraction;
import betterwithmods.damagesource.BWDamageSource;
import betterwithmods.event.MobDropEvent;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockSaw extends BWMBlock implements IMechanicalBlock {
    public static final PropertyBool ISACTIVE = PropertyBool.create("isactive");
    private static final float HEIGHT = 0.71875F;
    private static final AxisAlignedBB D_AABB = new AxisAlignedBB(0.0F, 1.0F - HEIGHT, 0.0F, 1.0F, 1.0F, 1.0F);
    private static final AxisAlignedBB U_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, HEIGHT, 1.0F);
    private static final AxisAlignedBB N_AABB = new AxisAlignedBB(0.0F, 0.0F, 1.0F - HEIGHT, 1.0F, 1.0F, 1.0F);
    private static final AxisAlignedBB S_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, HEIGHT);
    private static final AxisAlignedBB W_AABB = new AxisAlignedBB(1.0F - HEIGHT, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    private static final AxisAlignedBB E_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, HEIGHT, 1.0F, 1.0F);

    public BlockSaw() {
        super(Material.WOOD);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DirUtils.FACING, EnumFacing.UP));
    }

    @Override
    public int tickRate(World world) {
        return 10;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float flX, float flY, float flZ, int meta, EntityLivingBase placer, ItemStack stack) {
        IBlockState state = super.getStateForPlacement(world, pos, facing, flX, flY, flZ, meta, placer, stack);
        return setFacingInBlock(state, DirUtils.getOpposite(facing));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        EnumFacing facing = DirUtils.convertEntityOrientationToFacing(entity, EnumFacing.NORTH);
        world.setBlockState(pos, world.getBlockState(pos).withProperty(DirUtils.FACING, facing));
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        state = state.getActualState(world, pos);
        EnumFacing facing = getFacingFromBlockState(state);
        switch (facing) {
            case DOWN:
                return D_AABB;
            case UP:
                return U_AABB;
            case NORTH:
                return N_AABB;
            case SOUTH:
                return S_AABB;
            case WEST:
                return W_AABB;
            case EAST:
            default:
                return E_AABB;
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        if (block == BWMBlocks.AXLE || block == BWMBlocks.HAND_CRANK)
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
        else
            world.scheduleBlockUpdate(pos, this, tickRate(world) + world.rand.nextInt(6), 5);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        boolean powered = isInputtingMechPower(world, pos);
        boolean isOn = isBlockOn(world, pos);

        if (isOn != powered) {
            emitSawParticles(world, pos, rand);
            setMechanicalOn(world, pos, powered);
            if (powered) {
                world.scheduleBlockUpdate(pos, this, tickRate(world) + rand.nextInt(6), 5);
                world.playSound(null, pos, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 1.0F + rand.nextFloat() * 0.1F, 1.5F + rand.nextFloat() * 0.1F);
            } else
                world.playSound(null, pos, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 1.0F + rand.nextFloat() * 0.1F, 0.75F + rand.nextFloat() * 0.1F);
        } else if (powered) {
            sawBlockInFront(world, pos, rand);
            world.scheduleBlockUpdate(pos, this, tickRate(world) + rand.nextInt(6), 5);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (isMechanicalOn(world, pos) && entity instanceof EntityLivingBase) {
            EnumFacing dir = getFacing(world, pos);

            DamageSource source = BWDamageSource.saw;
            int damage = 4;
            boolean unobstructed = true;
            for (int i = 0; i < 3; i++) {
                BlockPos pos2 = new BlockPos(pos.getX(), pos.getY() - i, pos.getZ()).offset(dir);
                Block block = world.getBlockState(pos2).getBlock();
                IBlockState blockState = world.getBlockState(pos2);
                if (block == BWMBlocks.AESTHETIC && blockState.getValue(BlockAesthetic.blockType).getMeta() < 2) {
                    source = BWDamageSource.choppingBlock;
                    damage *= 3;
                    if (blockState.getValue(BlockAesthetic.blockType).getMeta() == 0 && unobstructed)
                        world.setBlockState(pos2, BWMBlocks.AESTHETIC.getDefaultState().withProperty(BlockAesthetic.blockType, BlockAesthetic.EnumType.CHOPBLOCKBLOOD));
                    break;
                } else if (!world.isAirBlock(pos2) && !(block instanceof BlockLiquid) && !(block instanceof IFluidBlock))
                    break;
                else if (!world.isAirBlock(pos2))
                    unobstructed = false;
            }
            if (entity instanceof EntityMob && ((EntityLivingBase) entity).getHealth() <= damage) {
                ((EntityLivingBase) entity).recentlyHit = 60;
                if (world instanceof WorldServer)
                    performLastHit(entity, damage);
            } else if (entity.attackEntityFrom(source, damage)) {
                ((EntityLivingBase) entity).recentlyHit = 60;
                world.playSound(null, pos, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 1.0F + world.rand.nextFloat() * 0.1F, 1.5F + world.rand.nextFloat() * 0.1F);
            }
        }
    }

    private void performLastHit(Entity entity, float damage) {
        entity.attackEntityFrom(DamageSource.causePlayerDamage(MobDropEvent.player), damage);
    }

    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side != getFacingFromBlockState(state);
    }

    public EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
        return getFacingFromBlockState(world.getBlockState(pos));
    }

    @Override
    public EnumFacing getFacingFromBlockState(IBlockState state) {
        return state.getValue(DirUtils.FACING);
    }

    @Override
    public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing) {
        return state.withProperty(DirUtils.FACING, facing);
    }

    @Override
    public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos) {
        return getFacing(world, pos) != EnumFacing.DOWN && getFacing(world, pos) != EnumFacing.UP;
    }

    @Override
    public boolean canRotateVertically(IBlockAccess world, BlockPos pos) {
        return canRotateOnTurntable(world, pos);
    }

    @Override
    public void rotateAroundYAxis(World world, BlockPos pos, boolean reverse) {
        if (DirUtils.rotateAroundY(this, world, pos, reverse)) {
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
            MechanicalUtil.destoryHorizontalAxles(world, pos);
        }
    }

    public boolean isBlockOn(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getValue(ISACTIVE);
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
        return MechanicalUtil.isBlockPoweredByAxle(world, pos, this);
    }

    @Override
    public boolean isOutputtingMechPower(World world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canInputPowerToSide(IBlockAccess world, BlockPos pos,
                                       EnumFacing dir) {
        return dir != getFacing(world, pos);
    }

    @Override
    public void overpower(World world, BlockPos pos) {
        breakSaw(world, pos);
    }

    public void breakSaw(World world, BlockPos pos) {
        if (BWConfig.dropsSaw)
            InvUtils.ejectBrokenItems(world, pos, new ResourceLocation("betterwithmods", "block/saw"));
        /*
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(BWMItems.MATERIAL, 1, 0));
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(Blocks.PLANKS));
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(BWMItems.MATERIAL, 3, 22));
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(Items.IRON_INGOT, 2, 0));
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(BWMItems.MATERIAL, 4, 30));
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(BWMItems.MATERIAL, 3, 8));
        */
        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.3F, world.rand.nextFloat() * 0.1F + 0.45F);
        world.setBlockToAir(pos);
    }

    @Override
    public boolean isMechanicalOn(IBlockAccess world, BlockPos pos) {
        return isBlockOn(world, pos);
    }

    @Override
    public void setMechanicalOn(World world, BlockPos pos, boolean isOn) {
        boolean active = world.getBlockState(pos).getValue(ISACTIVE);
        if (isOn != active)
            world.setBlockState(pos, world.getBlockState(pos).withProperty(ISACTIVE, isOn));
    }

    public void emitSawParticles(World world, BlockPos pos, Random rand) {
        EnumFacing facing = getFacing(world, pos);
        float xPos = pos.getX();
        float yPos = pos.getY();
        float zPos = pos.getZ();
        float xExtent = 0.0F;
        float zExtent = 0.0F;

        switch (facing) {
            case DOWN:
                xPos += 0.5F;
                zPos += 0.5F;
                xExtent = 1.0F;
                break;
            case UP:
                xPos += 0.5F;
                zPos += 0.5F;
                yPos += 1.0F;
                xExtent = 1.0F;
                break;
            case NORTH:
                xPos += 0.5F;
                yPos += 0.5F;
                xExtent = 1.0F;
                break;
            case SOUTH:
                xPos += 0.5F;
                yPos += 0.5F;
                zPos += 1.0F;
                xExtent = 1.0F;
                break;
            case WEST:
                yPos += 0.5F;
                zPos += 0.5F;
                zExtent = 1.0F;
                break;
            default:
                yPos += 0.5F;
                zPos += 0.5F;
                xPos += 1.0F;
                zExtent = 1.0F;
        }

        for (int i = 0; i < 5; i++) {
            float smokeX = xPos + (rand.nextFloat() - 0.5F) * xExtent;
            float smokeY = yPos + rand.nextFloat() * 0.1F;
            float smokeZ = zPos + (rand.nextFloat() - 0.5F) * zExtent;
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void sawBlockInFront(World world, BlockPos pos, Random rand) {
        if (world.isRemote || !(world instanceof WorldServer))
            return;
        BlockPos pos2 = pos.offset(getFacing(world, pos));
        Block block = world.getBlockState(pos2).getBlock();
        int harvestMeta = block.damageDropped(world.getBlockState(pos2));
        if (SawInteraction.INSTANCE.contains(block, harvestMeta)) {
            List<ItemStack> products = SawInteraction.INSTANCE.getProducts(block, harvestMeta);
            if (!products.isEmpty())
                InvUtils.ejectStackWithOffset(world, pos2, products);
            world.setBlockToAir(pos2);
            world.playSound(null, pos2, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 1.5F + rand.nextFloat() * 0.1F, 2.0F + rand.nextFloat() * 0.1F);
        }
    }

    @Override
    public boolean isMechanicalOnFromState(IBlockState state) {
        return state.getValue(ISACTIVE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (isMechanicalOn(world, pos)) {
            emitSawParticles(world, pos, rand);

            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)));
            if (!entities.isEmpty() && entities.size() > 0)
                emitBloodParticles(world, pos);
        }
    }

    private void emitBloodParticles(World world, BlockPos pos) {
        EnumFacing facing = getFacing(world, pos);
        float xPos = pos.getX();
        float yPos = pos.getY();
        float zPos = pos.getZ();
        float xExtent = 0.0F;
        float zExtent = 0.0F;

        switch (facing) {
            case DOWN:
                xPos += 0.5F;
                zPos += 0.5F;
                xExtent = 1.0F;
                break;
            case UP:
                xPos += 0.5F;
                zPos += 0.5F;
                yPos += 1.0F;
                xExtent = 1.0F;
                break;
            case NORTH:
                xPos += 0.5F;
                yPos += 0.5F;
                xExtent = 1.0F;
                break;
            case SOUTH:
                xPos += 0.5F;
                yPos += 0.5F;
                zPos += 1.0F;
                xExtent = 1.0F;
                break;
            case WEST:
                yPos += 0.5F;
                zPos += 0.5F;
                zExtent = 1.0F;
                break;
            default:
                yPos += 0.5F;
                zPos += 0.5F;
                xPos += 1.0F;
                zExtent = 1.0F;
        }

        for (int i = 0; i < 20; i++) {
            float smokeX = xPos + (world.rand.nextFloat() - 0.5F) * xExtent;
            float smokeY = yPos + world.rand.nextFloat() * 0.1F;
            float smokeZ = zPos + (world.rand.nextFloat() - 0.5F) * zExtent;
            world.spawnParticle(EnumParticleTypes.REDSTONE, smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean isActive = meta > 7;
        if (isActive)
            meta -= 8;
        EnumFacing facing = EnumFacing.getFront(meta);
        return this.getDefaultState().withProperty(ISACTIVE, isActive).withProperty(DirUtils.FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(ISACTIVE) ? 8 : 0;
        meta += state.getValue(DirUtils.FACING).ordinal();
        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ISACTIVE, DirUtils.FACING);
    }
}
