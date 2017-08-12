package betterwithmods.module.hardcore;

import betterwithmods.BWMod;
import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.world.WorldType.FLAT;

/**
 * Created by tyler on 4/20/17.
 */
@SuppressWarnings("unused")
public class HCSpawn extends Feature {

    public static int HARDCORE_SPAWN_RADIUS;
    public static int HARDCORE_SPAWN_COOLDOWN_RADIUS;
    public static int HARDCORE_SPAWN_COOLDOWN;//20 min
    public static int HARDCORE_SPAWN_MAX_ATTEMPTS = 20;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(SpawnSaving.class, new CapabilitySpawn(), SpawnSaving::new);
    }

    @Override
    public void setupConfig() {

        HARDCORE_SPAWN_RADIUS = loadPropInt("Hardcore Spawn Radius", "Radius from original spawn which you will be randomly spawned", 2000);
        HARDCORE_SPAWN_COOLDOWN_RADIUS = loadPropInt("Hardcore Spawn Cooldown Radius", "Radius from your previous spawn you will spawn if you die during a cooldown period", 100);
        HARDCORE_SPAWN_COOLDOWN = loadPropInt("Hardcore Spawn Cooldown Ticks", "Amount of time after a HCSpawn which you will continue to spawn in the same area", 12000);

        super.setupConfig();
    }

    @Override
    public String getFeatureDescription() {
        return "Makes it so death is actual issues as you will spawn randomly within a 2000 block radius of your original spawn. Compasses still point to Original BindingPoint";
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    /**
     * Random Respawn. Less intense when there is a short time since death.
     */
    @SubscribeEvent
    public void randomRespawn(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof EntityPlayerMP)) return;
        if (event.getEntity().getEntityWorld().getWorldType() == FLAT)
            return;
        EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
        if (PlayerHelper.isSurvival(player)) {
            if (!ModuleLoader.isFeatureEnabled(HCBeds.class)) {
                if (player.getBedLocation() != null) {
                    return;
                }
            }
            int timeSinceDeath = player.getStatFile().readStat(StatList.TIME_SINCE_DEATH);
            boolean isNew = timeSinceDeath >= HARDCORE_SPAWN_COOLDOWN;
            BlockPos newPos = getRespawnPoint(player, isNew ? player.world.getSpawnPoint() : getSpawn(player), HARDCORE_SPAWN_RADIUS);
            setSpawn(player, newPos);
            player.setSpawnPoint(newPos, true);
        }
    }


//
//    public void clearConditions(EntityPlayer player) {
//        World world = player.world;
//        WorldInfo worldinfo = world.getWorldInfo();
//        if(!(player.getServer() instanceof DedicatedServer) && world.playerEntities.size() == 1) {
//            world.setWorldTime(1000); //Early Morning
//            //Clear Weather
//            worldinfo.setCleanWeatherTime(18000);
//            worldinfo.setRainTime(0);
//            worldinfo.setThunderTime(0);
//            worldinfo.setRaining(false);
//            worldinfo.setThundering(false);
//        }
//    }

    /**
     * Find a random position to respawn. Tries 20 times maximum to find a
     * suitable place. Else, the previous SP will remain unchanged.
     *
     * @param spawnFuzz A "size coefficient" variable. Proportional to distance
     *                  between spawn points.
     * @return The new BlockPos
     */
    private BlockPos getRespawnPoint(EntityPlayer player, BlockPos spawnPoint, int spawnFuzz) {
        World world = player.getEntityWorld();
        BlockPos ret = spawnPoint;
        if (!world.provider.isNether()) {
            boolean found = false;
            for (int tryCounter = 0; tryCounter < HARDCORE_SPAWN_MAX_ATTEMPTS; tryCounter++) {
                ret = spawnPoint;
                double fuzzVar = world.rand.nextDouble() * spawnFuzz;
                double angle = world.rand.nextDouble();
                double customX = (double) (-MathHelper
                        .sin((float) (angle * 360.0D))) * fuzzVar;
                double customZ = (double) MathHelper
                        .cos((float) (angle * 360.0D)) * fuzzVar;
                ret = ret.add(MathHelper.floor(customX) + 0.5D, 1.5D, MathHelper.floor(customZ) + 0.5D);
                ret = world.getTopSolidOrLiquidBlock(ret);

                // Check if the position is correct
                int cmp = ret.getY() - world.provider.getAverageGroundLevel();
                Material check = world.getBlockState(ret).getMaterial();
                if (cmp >= 0 && (check == null || !check.isLiquid())) {
                    found = true;
                    break;
                }
            }
            if (!found) BWMod.logger.warn("New respawn point could not be found.");
        }

        return ret;
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    public static void setSpawn(EntityPlayer player, BlockPos pos) {
        if (player.hasCapability(SPAWN_CAP, null)) {
            SpawnSaving cap = player.getCapability(SPAWN_CAP, null);
            cap.setPos(pos);
        }
    }

    public static BlockPos getSpawn(EntityPlayer player) {
        if (player.hasCapability(SPAWN_CAP, null)) {
            SpawnSaving cap = player.getCapability(SPAWN_CAP, null);
            return cap.getPos();
        }
        return player.world.getSpawnPoint();
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(BWMod.MODID, "spawn_position"), new SpawnSaving((EntityPlayer) event.getObject()));
        }
    }

    @SubscribeEvent
    public void clone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            setSpawn(event.getEntityPlayer(), getSpawn(event.getOriginal()));
        }
    }


    @SuppressWarnings("CanBeFinal")
    @CapabilityInject(SpawnSaving.class)
    public static Capability<SpawnSaving> SPAWN_CAP = null;

    public static class CapabilitySpawn implements Capability.IStorage<SpawnSaving> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<SpawnSaving> capability, SpawnSaving instance, EnumFacing side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<SpawnSaving> capability, SpawnSaving instance, EnumFacing side, NBTBase nbt) {
            instance.deserializeNBT((NBTTagCompound) nbt);
        }
    }

    private class SpawnSaving implements ICapabilitySerializable<NBTTagCompound> {

        private BlockPos pos;

        public SpawnSaving() {
        }

        public SpawnSaving(EntityPlayer player) {
            pos = player.world.getSpawnPoint();
        }

        public BlockPos getPos() {
            return pos;
        }

        public void setPos(BlockPos pos) {
            this.pos = pos;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == SPAWN_CAP;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == SPAWN_CAP)
                return SPAWN_CAP.cast(this);
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setLong("spawn", pos.toLong());
            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            pos = BlockPos.fromLong(nbt.getLong("spawn"));
        }
    }

}
