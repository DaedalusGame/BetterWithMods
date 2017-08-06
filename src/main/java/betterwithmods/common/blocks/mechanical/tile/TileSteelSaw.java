package betterwithmods.common.blocks.mechanical.tile;

import betterwithmods.common.blocks.mini.IDamageDropped;
import betterwithmods.common.damagesource.BWDamageSource;
import betterwithmods.common.registry.blockmeta.managers.SawManager;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import betterwithmods.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class TileSteelSaw extends TileAxleMachine {
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox();
    }

    public static final AxisAlignedBB X_BOX = new AxisAlignedBB(0, -1, -1, 0, 1, 1);
    public static final AxisAlignedBB Y_BOX = new AxisAlignedBB(-1, 0, -1, 1, 0, 1);
    public static final AxisAlignedBB Z_BOX = new AxisAlignedBB(-1, -1, 0, 1, 1, 0);
    public static final AxisAlignedBB[] BOXES = new AxisAlignedBB[]{X_BOX, Y_BOX, Z_BOX};

    @Override
    public int getPower() {
        if (!isValid())
            return 0;
        return super.getPower();
    }

    public boolean isValid() {
        IBlockState state = world.getBlockState(pos);
        boolean valid = true;
        Set<BlockPos> positions = WorldUtils.getPosAround(pos, state.getValue(DirUtils.AXIS));
        for (BlockPos check : positions) {
            if (check.equals(getPos()))
                continue;
            if (!saw(world, check, world.rand, true)) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    @Override
    public void update() {
        IBlockState state = world.getBlockState(pos);
        Set<BlockPos> positions = WorldUtils.getPosAround(pos, state.getValue(DirUtils.AXIS));
        if (getPower() > 0 && world.getTotalWorldTime() % 10 == 0) {
            AxisAlignedBB box = BOXES[state.getValue(DirUtils.AXIS).ordinal()].offset(pos);
            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, box.grow(.25));
            if (!entities.isEmpty()) {
                entities.forEach(this::hitEntity);
                world.playSound(null, pos, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 1.0F + world.rand.nextFloat() * 0.1F, 1.5F + world.rand.nextFloat() * 0.1F);
            }

            for (BlockPos pos : positions) {
                if (!pos.equals(getPos()))
                    saw(world, pos, world.rand, false);
            }

        }
    }

    private void hitEntity(EntityLivingBase entity) {
        entity.recentlyHit = 60;
        if (world instanceof WorldServer) {
            if (BWDamageSource.getSawDamage() != null)
                entity.attackEntityFrom(BWDamageSource.getSteelSawDamage(), 10);
        }
    }

    @Override
    public byte getMaximumSignal() {
        return 5;
    }

    private boolean saw(World world, BlockPos pos, Random rand, boolean simulate) {
        IBlockState state = world.getBlockState(pos);

        if (world.isRemote || !(world instanceof WorldServer))
            return true;
        if (world.isAirBlock(pos))
            return true;
        Block block = state.getBlock();
        int harvestMeta = block.damageDropped(state);
        if (block instanceof IDamageDropped)
            harvestMeta = ((IDamageDropped) block).damageDropped(state, world, pos);
        if (SawManager.WOOD_SAW.contains(block, harvestMeta)) {
            if (!simulate) {
                List<ItemStack> products = SawManager.WOOD_SAW.getProducts(block, harvestMeta);
                world.setBlockToAir(pos);
                if (!products.isEmpty())
                    InvUtils.ejectStackWithOffset(world, pos, products);
                world.playSound(null, pos, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 1.5F + rand.nextFloat() * 0.1F, 2.0F + rand.nextFloat() * 0.1F);
            }
            return true;
        } else if (SawManager.STEEL_SAW.contains(block, harvestMeta)) {

            if (!simulate) {
                List<ItemStack> products = SawManager.STEEL_SAW.getProducts(block, harvestMeta);
                world.setBlockToAir(pos);
                if (!products.isEmpty())
                    InvUtils.ejectStackWithOffset(world, pos, products);
                world.playSound(null, pos, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 1.5F + rand.nextFloat() * 0.1F, 2.0F + rand.nextFloat() * 0.1F);
            }
            return true;
        }
        return false;
    }

    @Override
    public World getBlockWorld() {
        return super.getBlockWorld();
    }

    @Override
    public BlockPos getBlockPos() {
        return super.getBlockPos();
    }
}
