package betterwithmods.module.industry.pollution;

import betterwithmods.api.capabilities.PollutionCapability;
import betterwithmods.api.tile.IPollutant;
import betterwithmods.common.items.tools.ItemSoulforgeArmor;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PollutionHandler {
    private HashMap<ChunkPos, Float> pollution = new HashMap<>();
    public HashMap<String, Float> biomeMods = new HashMap<>();

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load evt) {
        if (!evt.getWorld().isRemote) {
            ChunkPos pos = evt.getChunk().getPos();
            float pollute = 0.0F;
            if (evt.getData().hasKey("bwm_pollution")) {
                NBTTagCompound tag = evt.getData().getCompoundTag("bwm_pollution");
                if (tag.hasKey("pollution"))
                    pollute = tag.getFloat("pollution");
            }
            pollution.put(pos, pollute);
        }
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkDataEvent.Save evt) {
        if (!evt.getWorld().isRemote) {
            ChunkPos pos = evt.getChunk().getPos();
            if (pollution.containsKey(pos)) {
                NBTTagCompound tag = evt.getData();
                NBTTagCompound t = new NBTTagCompound();
                t.setFloat("pollution", pollution.get(pos));
                tag.setTag("bwm_pollution", t);
                if (!evt.getChunk().isLoaded())
                    pollution.remove(pos);
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent evt) {
        //TODO: Why does this trigger twice a tick?
        if (!evt.world.isRemote && evt.phase == TickEvent.Phase.START && evt.side == Side.SERVER) {
            long time = evt.world.getWorldTime();
            //TODO: Crashy because it likes to tick twice.
            /*
            if (time % 8000 == 0) {
                if (!pollution.isEmpty()) {
                    List<ChunkPos> toUpdate = new ArrayList<>();
                    for (ChunkPos pos : pollution.keySet()) {
                        Chunk chunk = evt.world.getChunkFromChunkCoords(pos.x, pos.z);
                        if (chunk.isLoaded()) {
                            Random rand = chunk.getRandomWithSeed(9850327L);
                            if (rand.nextInt(30) == 0) {
                                toUpdate.add(pos);
                            }
                        }
                    }
                    if (!toUpdate.isEmpty()) {
                        calculatePollutionSpread(evt.world, toUpdate);
                    }
                }
            }*/
            //TODO: Kill leaves if acid rain is happening.
            if (time % 1000 == 0) {
                //TODO: Modifiers kill the HashMap for some reason. Probably due to ticking twice?
                calculatePollutionReduction(evt.world);
            }
            //TODO: Yet this is fine???
            List<TileEntity> tiles = evt.world.loadedTileEntityList.stream().filter(tileEntity -> tileEntity.hasCapability(PollutionCapability.POLLUTION, null)).collect(Collectors.toList());
            if (!tiles.isEmpty()) {
                for (TileEntity tile : tiles) {
                    IPollutant pollutant = tile.getCapability(PollutionCapability.POLLUTION, null);
                    if (pollutant.isPolluting() && pollution.containsKey(new ChunkPos(tile.getPos()))) {
                        ChunkPos p = new ChunkPos(tile.getPos());
                        float pollute = pollution.get(p);
                        pollution.put(p, pollute + pollutant.getPollutionRate());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent evt) {
        if (evt.player instanceof EntityPlayerMP && evt.phase == TickEvent.Phase.END) {
            EntityPlayerMP player = (EntityPlayerMP) evt.player;
            if (!player.capabilities.isCreativeMode && isRaining(player.getEntityWorld(), player.getPosition()) && player.getEntityWorld().canSeeSky(player.getPosition())) {
                ChunkPos pos = new ChunkPos(player.getPosition());
                float pollution = this.pollution.get(pos);
                if (pollution > 6000F && player.getEntityWorld().getWorldTime() % 20 == 0) {
                    if (!PlayerHelper.hasFullSet(player, ItemSoulforgeArmor.class)) {
                        //TODO: Add acid damage here.
                    }
                }
            }
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

    private void calculatePollutionReduction(World world) {
        float pollutionMod = 1.0F;
        System.out.println("Calculating pollution reduction...");
        if (!this.pollution.isEmpty()) {
            for (ChunkPos pos : this.pollution.keySet()) {
                Chunk chunk = world.getChunkFromChunkCoords(pos.x, pos.z);
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

    private float getPollutionReduction(BiomeDictionary.Type type) {
        return biomeMods.containsKey(type.getName()) ? biomeMods.get(type.getName()) : 1.0F;
    }

    private void calculatePollutionSpread(World world, List<ChunkPos> pos) {
        List<ChunkPos> finalPos = new ArrayList<>();
        for (ChunkPos p : pos) {
            if (!world.getChunkFromChunkCoords(p.x, p.z).isLoaded()) {
                continue;
            }
            boolean valid = true;
            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    if (x * x != z * z) {
                        ChunkPos toCheck = world.getChunkFromChunkCoords(p.x + x, p.z + z).getPos();
                        if (finalPos.contains(toCheck))
                            valid = false;
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
                    if (world.getChunkFromChunkCoords(pos.x + x, pos.z + z).isLoaded() && this.pollution.containsKey(world.getChunkFromChunkCoords(pos.x + x, pos.z + z).getPos())) {
                        validChunks.add(world.getChunkFromChunkCoords(pos.x + x, pos.z + z).getPos());
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
    }

    public float getPollutionStat(ChunkPos pos) {
        if (pollution.containsKey(pos))
            return pollution.get(pos);
        return -1;
    }
}
