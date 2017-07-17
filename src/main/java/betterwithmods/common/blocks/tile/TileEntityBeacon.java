package betterwithmods.common.blocks.tile;

import betterwithmods.module.hardcore.hcbeacons.IBeaconEffect;
import betterwithmods.module.hardcore.hcbeacons.SpawnBeaconEffect;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

import static betterwithmods.module.hardcore.hcbeacons.HCBeacons.BEACON_EFFECTS;

/**
 * Created by primetoxinz on 7/17/17.
 */
public class TileEntityBeacon extends TileBasic implements ITickable {

    private int level;
    private int prevLevel;
    private IBlockState type = Blocks.AIR.getDefaultState();
    private IBeaconEffect effect;
    private int tick;

    public TileEntityBeacon() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void update() {
        if (tick <= 0) {
            Pair<Integer, IBlockState> current = calcLevel();
            level = current.getKey();
            type = current.getValue();
            if (level > 0) {
                System.out.println(level);
                if (level != prevLevel) {
                    this.world.playBroadcastSound(1023, getPos(), 0);
                }
                effect = BEACON_EFFECTS.get(type);
                if (effect != null)
                    effect.effect(world, pos, level);
            }
            if (level != prevLevel) {
                prevLevel = level;
            }
            tick = 120;
        }
        tick--;
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
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        level = compound.getInteger("level");
        prevLevel = compound.getInteger("prevLevel");
        tick = compound.getInteger("tick");

        NBTTagCompound tag = (NBTTagCompound) compound.getTag("type");
        type = NBTUtil.readBlockState(tag);
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

        if (!world.isRemote && this.effect instanceof SpawnBeaconEffect) {
            System.out.println(SpawnBeaconEffect.SPAWN_LIST);
            this.world.playBroadcastSound(1023, getPos(), 0);
            return ((SpawnBeaconEffect) this.effect).processInteraction(world, getPos(), getLevel() - 1, player);
        }
        return false;
    }


    public int getLevel() {
        return level;
    }

    public boolean isEnabled() {
        return level > 0;
    }


    public void onRemoved() {
        MinecraftForge.EVENT_BUS.unregister(this);
        SpawnBeaconEffect.removeAll(getPos());
        System.out.println(SpawnBeaconEffect.SPAWN_LIST);
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

}
