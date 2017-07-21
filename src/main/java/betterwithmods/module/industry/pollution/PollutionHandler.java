package betterwithmods.module.industry.pollution;

import betterwithmods.api.capabilities.PollutionCapability;
import betterwithmods.api.tile.IPollutant;
import betterwithmods.common.items.tools.ItemSoulforgeArmor;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PollutionHandler {
    public HashMap<String, Float> biomeMods = new HashMap<>();

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load evt) {
        if (!evt.getWorld().isRemote && evt.getWorld().hasCapability(WorldPollutionCapability.POLLUTION, null)) {
            IWorldPollution pollution = evt.getWorld().getCapability(WorldPollutionCapability.POLLUTION, null);
            if (pollution != null) {
                ChunkPos pos = evt.getChunk().getPos();
                if (evt.getData().hasKey("bwm_pollution")) {
                    NBTTagCompound tag = evt.getData().getCompoundTag("bwm_pollution");
                    pollution.readNBT(pos, tag);
                }
            }
        }
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkDataEvent.Save evt) {
        if (!evt.getWorld().isRemote && evt.getWorld().hasCapability(WorldPollutionCapability.POLLUTION, null)) {
            IWorldPollution pollution = evt.getWorld().getCapability(WorldPollutionCapability.POLLUTION, null);
            if (pollution != null) {
                ChunkPos pos = evt.getChunk().getPos();
                if (pollution.getPollution(pos) > -1) {
                    NBTTagCompound tag = evt.getData();
                    tag.setTag("bwm_pollution", pollution.writeNBT(pos, new NBTTagCompound()));
                    if (!evt.getChunk().isLoaded())
                        pollution.removePollution(pos);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent evt) {
        World world = evt.world;
        if (world.hasCapability(WorldPollutionCapability.POLLUTION, null) && !evt.world.isRemote && evt.phase == TickEvent.Phase.START) {
            IWorldPollution pollution = world.getCapability(WorldPollutionCapability.POLLUTION, null);
            if (pollution != null) {
                long time = world.getWorldTime();
                //TODO: Crashy because it likes to tick twice.
                //if (time % 8000 == 0) {
                //pollution.calculatePollutionSpread();
                //}
                //TODO: Kill leaves if acid rain is happening.
                if (time % 1000 == 0) {
                    pollution.calculatePollutionReduction();
                }
                List<TileEntity> tiles = evt.world.loadedTileEntityList.stream().filter(tileEntity -> tileEntity.hasCapability(PollutionCapability.POLLUTION, null)).collect(Collectors.toList());
                if (!tiles.isEmpty()) {
                    for (TileEntity tile : tiles) {
                        IPollutant pollutant = tile.getCapability(PollutionCapability.POLLUTION, null);
                        if (pollutant.isPolluting() && pollution.getPollution(new ChunkPos(tile.getPos())) > -1) {
                            ChunkPos p = new ChunkPos(tile.getPos());
                            float pollute = pollution.getPollution(p);
                            pollution.setPollution(p, pollute + pollutant.getPollutionRate());
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent evt) {
        if (evt.player instanceof EntityPlayerMP && evt.phase == TickEvent.Phase.END) {
            EntityPlayerMP player = (EntityPlayerMP) evt.player;
            if (player.getEntityWorld().hasCapability(WorldPollutionCapability.POLLUTION, null)) {
                IWorldPollution pollution = player.getEntityWorld().getCapability(WorldPollutionCapability.POLLUTION, null);
                if (pollution != null && !player.capabilities.isCreativeMode && isRaining(player.getEntityWorld(), player.getPosition()) && player.getEntityWorld().canSeeSky(player.getPosition())) {
                    ChunkPos pos = new ChunkPos(player.getPosition());
                    float pollute = pollution.getPollution(pos);
                    if (pollute > 6000F && player.getEntityWorld().getWorldTime() % 20 == 0) {
                        if (!PlayerHelper.hasFullSet(player, ItemSoulforgeArmor.class)) {
                            //TODO: Add acid damage here.
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void attachWorldCapability(AttachCapabilitiesEvent<World> evt) {
        final World world = evt.getObject();
        if (!world.isRemote) {
            evt.addCapability(new ResourceLocation("betterwithmods", "world_pollution"), new ICapabilitySerializable<NBTTagCompound>() {
                IWorldPollution instance = new WorldPollutionCapability.Default(world);

                @Override
                public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                    return capability == WorldPollutionCapability.POLLUTION;
                }

                @Override
                public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                    return capability == WorldPollutionCapability.POLLUTION ? WorldPollutionCapability.POLLUTION.cast(instance) : null;
                }

                @Override
                public NBTTagCompound serializeNBT() {
                    return null;
                }

                @Override
                public void deserializeNBT(NBTTagCompound tag) {

                }
            });
        }
    }

    @SubscribeEvent
    public void attachPollutantCapability(AttachCapabilitiesEvent<TileEntity> evt) {
        TileEntity tile = evt.getObject();
        if (tile instanceof TileEntityFurnace) {
            final TileEntityFurnace furnace = (TileEntityFurnace)tile;
            evt.addCapability(new ResourceLocation("betterwithmods", "furnace_pollution"), new ICapabilitySerializable<NBTTagCompound>() {
                IPollutant instance = new CapabilityFurnacePollution(furnace);

                @Override
                public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                    return capability == PollutionCapability.POLLUTION;
                }

                @Override
                public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                    return capability == PollutionCapability.POLLUTION ? PollutionCapability.POLLUTION.cast(instance) : null;
                }

                @Override
                public NBTTagCompound serializeNBT() {
                    return null;
                }

                @Override
                public void deserializeNBT(NBTTagCompound tag) {

                }
            });
        }
    }

    private boolean isRaining(World world, BlockPos pos) {
        return world.isRaining() && world.getBiome(pos).canRain();
    }
/*
    private void calculatePollutionReduction(World world) {
        float pollutionMod = 1.0F;
        System.out.println("Calculating pollution reduction...");
        if (!this.pollution.isEmpty()) {
            for (ChunkPos pos : this.pollution.keySet()) {
                Chunk chunk = world.getChunkProvider().getLoadedChunk(pos.x, pos.z);
                if (chunk != null) {
                    Biome biome = Biome.getBiome(chunk.getBiomeArray()[127], Biomes.PLAINS);
                    for (BiomeDictionary.Type type : BiomeDictionary.getTypes(biome)) {
                        pollutionMod *= getPollutionReduction(type);
                    }
                    if (world.isRaining() && biome.canRain())
                        pollutionMod *= 0.8F;
                    float stat = getPollutionStat(pos) * pollutionMod;
                    pollution.put(pos, stat);
                }
            }
        }
    }

    private float getPollutionReduction(BiomeDictionary.Type type) {
        return biomeMods.containsKey(type.getName()) ? biomeMods.get(type.getName()) : 1.0F;
    }

    private void calculatePollutionSpread(World world, List<ChunkPos> pos) {
        List<ChunkPos> finalPos = new ArrayList<>();
        for (ChunkPos p : pos) {
            Chunk chunk = world.getChunkProvider().getLoadedChunk(p.x, p.z);
            if (chunk == null || !chunk.isLoaded()) {
                continue;
            }
            boolean valid = true;
            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    if (x * x != z * z) {
                        Chunk toCheck = world.getChunkProvider().getLoadedChunk(p.x + x, p.z + z);
                        if (toCheck != null && toCheck.isLoaded()) {
                            if (finalPos.contains(toCheck.getPos()))
                                valid = false;
                        }
                    }
                }
            }
            if (valid)
                finalPos.add(p);
        }
        finalPos.forEach(p -> calculatePollutionSpread(world, p));
    }

    private void calculatePollutionSpread(World world, ChunkPos pos) {
        float pollution = getPollutionStat(pos);
        List<ChunkPos> validChunks = new ArrayList<>();
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                if (x * x != z * z) {
                    Chunk chunk = world.getChunkProvider().getLoadedChunk(pos.x + x, pos.z + z);
                    if (chunk != null) {
                        if (chunk.isLoaded() && this.pollution.containsKey(chunk.getPos())) {
                            validChunks.add(chunk.getPos());
                        }
                    }
                }
            }
        }
        if (!validChunks.isEmpty()) {
            for (ChunkPos p : validChunks) {
                float pollutionCheck = getPollutionStat(p);
                if (pollution == pollutionCheck) {
                } else if (pollution > pollutionCheck) {
                    calculateNewPollution(getPollutionStat(pos), getPollutionStat(p));
                } else {
                    calculateNewPollution(getPollutionStat(p), getPollutionStat(pos));
                }
            }
        }
    }
//TODO: Numbers may need to be adjusted.
    private void calculateNewPollution(float from, float to) {
        if (to / from > 0.2F) {
            float change = from * 0.2F;
            from -= change;
            to += change;
        }
        else {
            float difference = to / from;
            if (difference == 0) difference = from / 4;
            else difference /= 3;
            from -= difference;
            to += difference;
        }
    }

    private int findLeafCount(World world, ChunkPos pos) {
        int leaves = 0;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                BlockPos ground = world.getHeight(new BlockPos(pos.getXStart() + x, 64, pos.getZStart() + z));
                for (int y = 1; y < 10; y++) {
                    BlockPos toCheck = ground.up(y);
                    IBlockState state = world.getBlockState(toCheck);
                    if (state.getBlock() instanceof BlockLeaves && state.getValue(BlockLeaves.DECAYABLE))
                        leaves++;
                }
            }
        }
        return leaves;
    }*/

    public float getPollutionStat(World world, ChunkPos pos) {
        if (world.hasCapability(WorldPollutionCapability.POLLUTION, null)) {
            IWorldPollution pollution = world.getCapability(WorldPollutionCapability.POLLUTION, null);
            return pollution.getPollution(pos);
        }
        return -1;
    }
}
