package betterwithmods.event;

import betterwithmods.config.BWConfig;
import betterwithmods.entity.ai.EntityAIFlee;
import betterwithmods.entity.ai.EntityAISearchFood;
import betterwithmods.util.NetherSpawnWhitelist;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class NetherSpawnEvent {
    private static final Random rand = new Random();

    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent evt) {
        if (evt.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) evt.getEntity();
            if (entity instanceof EntityAnimal) {
                ((EntityAnimal) entity).tasks.addTask(3, new EntityAISearchFood((EntityAnimal) entity));
                if (!(entity instanceof EntityTameable)) {
                    float speed = 1.25F;
                    if (entity instanceof EntityCow)
                        speed = 2.0F;
                    else if (entity instanceof EntityChicken)
                        speed = 1.4F;
                    ((EntityAnimal) entity).tasks.addTask(0, new EntityAIFlee((EntityCreature) entity, speed));
                }
            }
        }
    }

    @SubscribeEvent
    public void giveEndermenEndStone(LivingSpawnEvent evt) {
        EntityLivingBase entity = evt.getEntityLiving();

        if (evt.getWorld().provider.getDimensionType() == DimensionType.THE_END) {
            if (entity instanceof EntityEnderman) {
                if (rand.nextInt(2000) == 0)
                    ((EntityEnderman) entity).setHeldBlockState(Blocks.END_STONE.getDefaultState());
            }
        }
    }

    @SubscribeEvent
    public void generateMossNearSpawner(TickEvent.WorldTickEvent evt) {
        List<BlockPos> positions = evt.world.loadedTileEntityList.stream().filter(t -> t instanceof TileEntityMobSpawner).map(TileEntity::getPos).collect(Collectors.toList());
        positions.forEach(pos -> {
            int x = rand.nextInt(9) - 4;
            int y = rand.nextInt(5) - 1;
            int z = rand.nextInt(9) - 4;
            BlockPos check = pos.add(x, y, z);
            IBlockState state = evt.world.getBlockState(check);
            if ((state.getBlock() == Blocks.COBBLESTONE || (state.getBlock() == Blocks.STONEBRICK && state.getBlock().getMetaFromState(state) == 0)) && rand.nextInt(30) == 0) {
                IBlockState changeState = state.getBlock() == Blocks.COBBLESTONE ? Blocks.MOSSY_COBBLESTONE.getDefaultState() : Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
                evt.world.setBlockState(check, changeState);
            }
        });
    }

    @SubscribeEvent
    public void denySlimeSpawns(CheckSpawn evt) {
        if (evt.getResult() == Result.ALLOW)
            return;
        if (!BWConfig.slimeSpawn)
            return;
        if (evt.getWorld() != null && evt.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD) {
            if (evt.getEntityLiving() instanceof EntitySlime) {
                BlockPos pos = new BlockPos(evt.getEntity().posX, evt.getEntity().posY - 1, evt.getEntity().posZ);
                if (evt.getWorld().getBlockState(pos).getMaterial() != Material.GRASS && evt.getWorld().getBlockState(pos).getMaterial() != Material.ROCK && evt.getWorld().getBlockState(pos).getMaterial() != Material.GROUND)
                    evt.setResult(Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public void denyNetherSpawns(CheckSpawn evt) {
        if (evt.getResult() == Result.ALLOW)
            return;
        if (!BWConfig.netherSpawn)
            return;
        if (evt.getWorld() != null && evt.getWorld().provider.getDimension() == -1) {
            if (evt.getEntityLiving().isCreatureType(EnumCreatureType.MONSTER, false)) {
                double monX = evt.getEntity().posX;
                double monY = evt.getEntity().posY;
                double monZ = evt.getEntity().posZ;
                int x = MathHelper.floor(monX);
                int y = MathHelper.floor(monY);
                int z = MathHelper.floor(monZ);
                BlockPos pos = new BlockPos(x, y - 1, z);
                Block block = evt.getWorld().getBlockState(pos).getBlock();
                int meta = evt.getWorld().getBlockState(pos).getBlock().getMetaFromState(evt.getWorld().getBlockState(pos));
                if (!evt.getWorld().isAirBlock(pos) && !NetherSpawnWhitelist.contains(block, meta))
                    evt.setResult(Result.DENY);
            }
        }
    }
}
