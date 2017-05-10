package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

/**
 * Created by tyler on 4/20/17.
 */
public class MobSpawning extends Feature {
    private boolean slime;
    private boolean nether;

    @Override
    public void init(FMLInitializationEvent event) {
        if (nether) {
            NetherSpawnWhitelist.addBlock(Blocks.NETHERRACK);
            NetherSpawnWhitelist.addBlock(Blocks.NETHER_BRICK);
            NetherSpawnWhitelist.addBlock(Blocks.SOUL_SAND);
            NetherSpawnWhitelist.addBlock(Blocks.GRAVEL);
            NetherSpawnWhitelist.addBlock(Blocks.QUARTZ_BLOCK);
        }
    }

    @Override
    public void setupConfig() {
        slime = loadPropBool("Limit Slime Spawning", "Slimes can only spawn on natural blocks", true);
        nether = loadPropBool("Limit Nether Spawning", "Nether Mobs can only spawn on nether blocks", true);
    }

    @SubscribeEvent
    public void denySlimeSpawns(LivingSpawnEvent.CheckSpawn evt) {

        if (evt.getResult() == Event.Result.ALLOW)
            return;
        if (!slime)
            return;
        if (evt.getWorld() != null && evt.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD) {
            if (evt.getEntityLiving() instanceof EntitySlime) {
                BlockPos pos = new BlockPos(evt.getEntity().posX, evt.getEntity().posY - 1, evt.getEntity().posZ);
                if (evt.getWorld().getBlockState(pos).getMaterial() != Material.GRASS && evt.getWorld().getBlockState(pos).getMaterial() != Material.ROCK && evt.getWorld().getBlockState(pos).getMaterial() != Material.GROUND)
                    evt.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public void denyNetherSpawns(LivingSpawnEvent.CheckSpawn evt) {
        if (evt.getResult() == Event.Result.ALLOW)
            return;
        if (!nether)
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
                    evt.setResult(Event.Result.DENY);
            }
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return super.hasSubscriptions();
    }

    public static class NetherSpawnWhitelist {
        private static final ArrayList<String> whitelist = new ArrayList<>();

        public static void addBlock(Block block) {
            for (int i = 0; i < 16; i++)
                whitelist.add(block + ":" + i);
        }

        public static void addBlock(Block block, int meta) {
            whitelist.add(block + ":" + meta);
        }

        public static boolean contains(Block block, int meta) {
            return whitelist.contains(block + ":" + meta);
        }

        public static void remove(Block block, int meta) {
            if (whitelist.contains(block + ":" + meta))
                whitelist.remove(block + ":" + meta);
        }
    }

}
