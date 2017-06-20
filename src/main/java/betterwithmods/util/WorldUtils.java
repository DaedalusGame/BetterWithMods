package betterwithmods.util;

import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import static net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE;

/**
 * @author Koward
 */
public final class WorldUtils {
    private WorldUtils() {
    }

    /**
     * Based on {@link World#getLightFromNeighbors(BlockPos)} build 2185
     */
    public static int getNaturalLightFromNeighbors(World worldIn, BlockPos pos) {
        return getNaturalLight(worldIn, pos, true, 0);
    }

    /**
     * Based on {@link World#getLight(BlockPos, boolean)} build 2185
     */
    private static int getNaturalLight(World worldIn, BlockPos pos, boolean checkNeighbors, int amount) {
        if (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000) {
            if (checkNeighbors && worldIn.getBlockState(pos).useNeighborBrightness()) {
                int i1 = getNaturalLight(worldIn, pos.up(), false, 0);
                int i = getNaturalLight(worldIn, pos.east(), false, 0);
                int j = getNaturalLight(worldIn, pos.west(), false, 0);
                int k = getNaturalLight(worldIn, pos.south(), false, 0);
                int l = getNaturalLight(worldIn, pos.north(), false, 0);

                if (i > i1) {
                    i1 = i;
                }

                if (j > i1) {
                    i1 = j;
                }

                if (k > i1) {
                    i1 = k;
                }

                if (l > i1) {
                    i1 = l;
                }

                return i1;
            } else if (pos.getY() < 0) {
                return 0;
            } else {
                if (pos.getY() >= 256) {
                    pos = new BlockPos(pos.getX(), 255, pos.getZ());
                }

                Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
                return getNaturalLightSubtracted(chunk, pos, amount);
            }
        } else {
            return 15;
        }
    }

    /**
     * Based on {@link Chunk#getLightSubtracted(BlockPos, int)} build 2185
     */
    private static int getNaturalLightSubtracted(Chunk chunkIn, BlockPos pos, int amount) {
        int i = pos.getX() & 15;
        int j = pos.getY();
        int k = pos.getZ() & 15;
        ExtendedBlockStorage extendedblockstorage = chunkIn.getBlockStorageArray()[j >> 4];

        if (extendedblockstorage == NULL_BLOCK_STORAGE) {
            return !chunkIn.getWorld().provider.isNether() && amount < EnumSkyBlock.SKY.defaultLightValue ? EnumSkyBlock.SKY.defaultLightValue - amount : 0;
        } else {
            int l = chunkIn.getWorld().provider.isNether() ? 0 : extendedblockstorage.getSkyLight(i, j & 15, k);
            l = l - amount;

            if (l < 0) {
                l = 0;
            }

            return l;
        }
    }

    public static int getDistance(BlockPos pos1, BlockPos pos2) {
        if(pos1 != null && pos2 != null)
            return (int) pos1.getDistance(pos2.getX(),pos2.getY(), pos2.getZ());
        return 0;
    }

    public static boolean spawnGhast(World world, BlockPos pos) {
        EntityGhast ghast = new EntityGhast(world);
        for (int i = 0; i < 200; i++) {
            double xPos = pos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * 10.0D;
            double yPos = pos.getY() + world.rand.nextInt(21) - 10;
            double zPos = pos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * 10.0D;

            ghast.setLocationAndAngles(xPos, yPos, zPos, world.rand.nextFloat() * 360.0F, 0.0F);

            if (ghast.getCanSpawnHere()) {
                world.spawnEntity(ghast);
                return true;
            }
        }
        return false;
    }

}
