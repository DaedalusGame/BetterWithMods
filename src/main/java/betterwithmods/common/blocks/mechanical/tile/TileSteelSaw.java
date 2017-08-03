package betterwithmods.common.blocks.mechanical.tile;

import betterwithmods.common.blocks.mini.IDamageDropped;
import betterwithmods.common.damagesource.BWDamageSource;
import betterwithmods.common.registry.blockmeta.managers.SawManager;
import betterwithmods.event.FakePlayerHandler;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.Random;

public class TileSteelSaw extends TileAxleMachine {
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox();
    }

    @Override
    public void update() {
        if (getPower() > 0 && !world.isRemote && world.getTotalWorldTime() % 10 == 0) {
            IBlockState state = world.getBlockState(pos);

            AxisAlignedBB box = new AxisAlignedBB(pos.getX()+0.5f,pos.getY()+0.5f,pos.getZ()+0.5f,pos.getX()+0.5f,pos.getY()+0.5f,pos.getZ()+0.5f);
            switch (state.getValue(DirUtils.AXIS)) {

                case X:
                    box = box.grow(0, 1, 1);
                    break;
                case Y:
                    box = box.grow(1, 0, 1);
                    break;
                case Z:
                    box = box.grow(1, 1, 0);
                    break;
            }
            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, box);
            if (!entities.isEmpty()) {
                entities.forEach(this::hitEntity);
                world.playSound(null, pos, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 1.0F + world.rand.nextFloat() * 0.1F, 1.5F + world.rand.nextFloat() * 0.1F);
            }

            Iterable<BlockPos> positions = BlockPos.PooledMutableBlockPos.getAllInBox((int) box.minX, (int) box.minY, (int) box.minZ, (int) box.maxX, (int) box.maxY, (int) box.maxZ);
            for (BlockPos pos : positions) {
                saw(world, pos, world.rand);
            }

        }
    }

    private void hitEntity(EntityLivingBase entity) {
        entity.recentlyHit = 60;
        if (world instanceof WorldServer) {
            DamageSource source = entity.getHealth() <= 10 ? DamageSource.causePlayerDamage(FakePlayerHandler.player) : BWDamageSource.saw;
            entity.attackEntityFrom(source, 10);
        }
    }

    @Override
    public byte getMaximumSignal() {
        return 5;
    }

    private void saw(World world, BlockPos pos, Random rand) {
        if (world.isRemote || !(world instanceof WorldServer))
            return;
        if (world.isAirBlock(pos))
            return;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        int harvestMeta = block.damageDropped(state);
        if (block instanceof IDamageDropped)
            harvestMeta = ((IDamageDropped) block).damageDropped(state, world, pos);

        if (SawManager.WOOD_SAW.contains(block, harvestMeta)) {
            List<ItemStack> products = SawManager.WOOD_SAW.getProducts(block, harvestMeta);
            world.setBlockToAir(pos);
            if (!products.isEmpty())
                InvUtils.ejectStackWithOffset(world, pos, products);
            world.playSound(null, pos, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 1.5F + rand.nextFloat() * 0.1F, 2.0F + rand.nextFloat() * 0.1F);
        } else if (SawManager.STEEL_SAW.contains(block, harvestMeta)) {
            List<ItemStack> products = SawManager.STEEL_SAW.getProducts(block, harvestMeta);
            world.setBlockToAir(pos);
            if (!products.isEmpty())
                InvUtils.ejectStackWithOffset(world, pos, products);
            world.playSound(null, pos, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 1.5F + rand.nextFloat() * 0.1F, 2.0F + rand.nextFloat() * 0.1F);
        }
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }

    @Override
    public BlockPos getPos() {
        return super.getPos();
    }
}
