package betterwithmods.module.hardcore;

import betterwithmods.common.damagesource.BWDamageSource;
import betterwithmods.module.Feature;
import betterwithmods.util.player.GloomPenalty;
import betterwithmods.util.player.PlayerHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by tyler on 5/13/17.
 */
public class HCGloom extends Feature {
    private static final DataParameter<Integer> GLOOM_TICK = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
    private static final List<SoundEvent> sounds = Lists.newArrayList(SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundEvents.ENTITY_ENDERMEN_SCREAM, SoundEvents.ENTITY_SILVERFISH_AMBIENT, SoundEvents.ENTITY_WOLF_GROWL);
    private static Set<Integer> dimensionWhitelist;

    public static int getGloomTime(EntityPlayer player) {
        try {
            return player.getDataManager().get(GLOOM_TICK);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static void incrementGloomTime(EntityPlayer player) {
        setGloomTick(player, getGloomTime(player) + 1);
    }

    public static void setGloomTick(EntityPlayer player, int value) {
        player.getDataManager().set(GLOOM_TICK, value);
    }

    @Override
    public void setupConfig() {
        dimensionWhitelist = Sets.newHashSet(ArrayUtils.toObject(loadPropIntList("Gloom Dimension Whitelist", "Gloom is only available in these dimensions", new int[]{0})));
    }

    @SubscribeEvent
    public void onEntityInit(EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof EntityPlayer) {
            event.getEntity().getDataManager().register(GLOOM_TICK, 0);
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent e) {
        //FIXME hopefully fixes permanent gloom after dying???
        setGloomTick(e.player,0);
    }

    @SubscribeEvent
    public void inDarkness(TickEvent.PlayerTickEvent e) {

        EntityPlayer player = e.player;
        World world = player.getEntityWorld();

        if (!PlayerHelper.isSurvival(player) || !dimensionWhitelist.contains(world.provider.getDimension()))
            return;
        if (!world.isRemote) {
            int light = world.getLight(player.getPosition().up());
            int tick = getGloomTime(player);
            if (light <= 0 && !player.isPotionActive(MobEffects.NIGHT_VISION)) {
                incrementGloomTime(player);
            } else if (tick != 0) {
                setGloomTick(player, 0);
            }
        }

        GloomPenalty gloomPenalty = PlayerHelper.getGloomPenalty(player);
        if (gloomPenalty != GloomPenalty.NO_PENALTY) {
            playRandomSound(gloomPenalty, world, player);
            if (gloomPenalty == GloomPenalty.TERROR) {
                player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 100, 3));
                player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 20));
                if (world.getTotalWorldTime() % 40 == 0) {
                    if (world.rand.nextInt(2) == 0) {
                        if (world.isRemote)
                            player.playSound(SoundEvents.ENTITY_ENDERMEN_STARE, 0.7F, 0.8F + world.rand.nextFloat() * 0.2F);

                        player.attackEntityFrom(BWDamageSource.gloom, 1);
                    }
                }
            }
        }
    }

    public void playRandomSound(GloomPenalty gloom, World world, EntityPlayer player) {
        if (world.isRemote) {
            if (world.rand.nextInt((int) (200 / gloom.getModifier())) == 0) {
                player.playSound(SoundEvents.AMBIENT_CAVE, 0.7F, 0.8F + world.rand.nextFloat() * 0.2F);
                if (gloom != GloomPenalty.GLOOM && world.rand.nextInt((int) (10 / gloom.getModifier())) == 0)
                    player.playSound(sounds.get(world.rand.nextInt(sounds.size())), 0.7F, 0.8F + world.rand.nextFloat() * 0.2F);

            }
        }
    }

    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent event) {
        GloomPenalty penalty = PlayerHelper.getGloomPenalty(event.getEntity());
        if (penalty != GloomPenalty.NO_PENALTY) {
            float change;
            if (penalty != GloomPenalty.TERROR)
                change = (getGloomTime(event.getEntity()) / 2400f);
            else
                change = -(getGloomTime(event.getEntity()) / 100000f);
            event.setNewfov(event.getFov() + change);
        }
    }

    @Override
    public String getFeatureDescription() {
        return "Be afraid of the dark...";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

}
