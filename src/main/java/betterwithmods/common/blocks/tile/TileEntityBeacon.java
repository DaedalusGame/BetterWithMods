package betterwithmods.common.blocks.tile;

import betterwithmods.module.hardcore.hcbeacons.IBeaconEffect;
import betterwithmods.module.hardcore.hcbeacons.SpawnBeaconEffect;
import betterwithmods.util.ColorUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static betterwithmods.module.hardcore.hcbeacons.HCBeacons.BEACON_EFFECTS;

/**
 * Created by primetoxinz on 7/17/17.
 */
public class TileEntityBeacon extends net.minecraft.tileentity.TileEntityBeacon implements ITickable {

    private int level;
    private int prevLevel;
    private IBlockState type = Blocks.AIR.getDefaultState();
    private IBeaconEffect effect;
    private int tick;
    private List<BeamSegment> segments = Lists.newArrayList();

    @SideOnly(Side.CLIENT)
    private long beamRenderCounter;
    @SideOnly(Side.CLIENT)
    private float beamRenderScale;


    public TileEntityBeacon() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void update() {

        if (tick <= 0) {
            if (!world.canBlockSeeSky(pos)) {
                if (level != 0) {
                    this.level = 0;
                    this.prevLevel = 0;
                    world.playSound(null, pos, SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
                return;
            }
            Pair<Integer, IBlockState> current = calcLevel();
            level = current.getKey();
            type = current.getValue();
            if (level > 0) {

                if (level != prevLevel) {
                    this.world.playBroadcastSound(1023, getPos(), 0);
                    //TRIGGER ADVANCEMENT
                    this.world.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(pos, pos.add(1, -4, 1)).grow(10.0D, 5.0D, 10.0D)).forEach(player -> CriteriaTriggers.CONSTRUCT_BEACON.trigger(player, this));
                }
                effect = BEACON_EFFECTS.get(type);
                if (effect != null) {
                    effect.effect(world, pos, level);
                    calcSegments();
                }
            } else {
                this.segments.clear();
            }
            if (level != prevLevel) {
                prevLevel = level;
            }
            tick = 120;
        }
        tick--;
    }

    @SideOnly(Side.CLIENT)
    public float getBeamScale() {

        int i = (int) (this.world.getTotalWorldTime() - this.beamRenderCounter);
        this.beamRenderCounter = this.world.getTotalWorldTime();

        if (i > 1) {
            this.beamRenderScale -= (float) i / 40.0F;

            if (this.beamRenderScale < 0.0F) {
                this.beamRenderScale = 0.0F;
            }
        }

        this.beamRenderScale += 0.025F;

        if (this.beamRenderScale > 1.0F) {
            this.beamRenderScale = 1.0F;
        }

        return this.beamRenderScale;
    }

    private void calcSegments() {
        this.segments.clear();
        BeamSegment segment = new BeamSegment(ColorUtils.getColorFromBlock(world, getPos().up(), getPos()));
        this.segments.add(segment);
        BlockPos.MutableBlockPos pos;
        for (pos = new BlockPos.MutableBlockPos(getPos().up()); pos.getY() < 256; pos.move(EnumFacing.UP)) {
            float[] color = ColorUtils.getColorFromBlock(world, pos, getPos());
            if (!Arrays.equals(color, new float[]{1, 1, 1})) {
                color = ColorUtils.average(color, segment.getColors());
                if (Arrays.equals(color, segment.getColors())) {
                    segment.incrementHeight();
                } else {
                    segment = new BeamSegment(color);
                    segments.add(segment);
                }
            } else {
                segment.incrementHeight();
            }
        }
    }

    public List<BeamSegment> getSegments() {
        return segments;
    }

    private boolean isSameBlock(IBlockState state, int x, int y, int z) {
        BlockPos pos = getPos().add(x, y, z);
        return state == world.getBlockState(pos);
    }

    private boolean isValidBlock(IBlockState state) {
        return BEACON_EFFECTS.containsKey(state);
    }

    public Pair<Integer, IBlockState> calcLevel() {
        IBlockState state = world.getBlockState(pos.down());
        if (isValidBlock(state)) {
            int r;
            for (r = 1; r <= 4; r++) {
                for (int x = -r; x <= r; x++) {
                    for (int z = -r; z <= r; z++) {
                        if (!isSameBlock(state, x, -r, z))
                            return Pair.of(r - 1, state);
                    }
                }
            }
            return Pair.of(r - 1, state);
        }
        return Pair.of(0, Blocks.AIR.getDefaultState());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("level", level);
        compound.setInteger("prevLevel", prevLevel);
        compound.setInteger("tick", tick);
        NBTTagCompound tag = new NBTTagCompound();
        NBTUtil.writeBlockState(tag, type);
        compound.setTag("type", tag);

        if (SpawnBeaconEffect.SPAWN_LIST.containsKey(this.getPos())) {
            NBTTagList list = new NBTTagList();
            for (SpawnBeaconEffect.BindingPoint point : SpawnBeaconEffect.SPAWN_LIST.get(this.getPos())) {
                list.appendTag(point.serializeNBT());
            }
            compound.setTag("spawns", list);
        }

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        level = compound.getInteger("level");
        prevLevel = compound.getInteger("prevLevel");
        tick = compound.getInteger("tick");

        NBTTagCompound type = (NBTTagCompound) compound.getTag("type");
        this.type = NBTUtil.readBlockState(type);
        if (compound.hasKey("spawns")) {
            NBTTagList list = compound.getTagList("spawns", 10);
            HashSet<SpawnBeaconEffect.BindingPoint> points = Sets.newHashSet();
            for (Iterator<NBTBase> iter = list.iterator(); iter.hasNext(); ) {
                NBTTagCompound tag = (NBTTagCompound) iter.next();
                points.add(new SpawnBeaconEffect.BindingPoint(tag));
            }
            SpawnBeaconEffect.SPAWN_LIST.put(this.getPos(), points);
        }
        super.readFromNBT(compound);
    }

    public boolean processInteraction(World world, EntityPlayer player, ItemStack stack) {
        if (player.isCreative() && !stack.isEmpty()) {
            if (stack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                IBlockState state = block.getStateFromMeta(stack.getMetadata());
                if (!isValidBlock(state))
                    return false;
                int r;
                for (r = 1; r <= stack.getCount(); r++) {
                    for (int x = -r; x <= r; x++) {
                        for (int z = -r; z <= r; z++) {
                            this.world.setBlockState(getPos().add(x, -r, z), state);
                        }
                    }
                }
                return true;
            }
        }

        if (!world.isRemote) {
            this.world.playBroadcastSound(1023, getPos(), 0);
            return this.effect.processInteractions(world, getPos(), getLevels() - 1, player);
        }
        return false;
    }

    @Override
    public int getLevels() {
        return level;
    }

    public boolean isEnabled() {
        return level > 0;
    }


    public void onRemoved() {
        MinecraftForge.EVENT_BUS.unregister(this);
        SpawnBeaconEffect.removeAll(getPos());
    }

    @SubscribeEvent
    public void findSpawn(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
        if (SpawnBeaconEffect.shouldSpawnHere(this.getPos(), player, world)) {
            player.setSpawnPoint(this.getPos().up(), true);
        } else {
            player.setSpawnPoint(world.getSpawnPoint(), false);
        }
    }


    public class BeamSegment extends net.minecraft.tileentity.TileEntityBeacon.BeamSegment {
        public BeamSegment(float[] colorsIn) {
            super(colorsIn);
        }

        @Override
        protected void incrementHeight() {
            super.incrementHeight();
        }
    }
}
