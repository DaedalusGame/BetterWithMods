package betterwithmods.module.hardcore.hcbeacons;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockSteel;
import betterwithmods.common.items.tools.ItemSoulforgeArmor;
import betterwithmods.module.Feature;
import betterwithmods.util.player.PlayerHelper;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.HashMap;

/**
 * Created by primetoxinz on 7/17/17.
 */
public class HCBeacons extends Feature {

    public static final HashMap<IBlockState, IBeaconEffect> BEACON_EFFECTS = Maps.newHashMap();

    @Override
    public void init(FMLInitializationEvent event) {
        BEACON_EFFECTS.put(Blocks.GLASS.getDefaultState(), (world, pos, level) -> {
        });
        BEACON_EFFECTS.put(Blocks.IRON_BLOCK.getDefaultState(), (world, pos, level) -> {
            //TODO substitute ItemCompass.
        });
        BEACON_EFFECTS.put(Blocks.EMERALD_BLOCK.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachEntityAround(EntityLivingBase.class, world, pos, level, entity -> entity.addPotionEffect(new PotionEffect(BWRegistry.POTION_LOOTING, 125, level - 1, true, false))));
        BEACON_EFFECTS.put(Blocks.LAPIS_BLOCK.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(BWRegistry.POTION_TRUESIGHT, 125, 1))));
        BEACON_EFFECTS.put(Blocks.DIAMOND_BLOCK.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(BWRegistry.POTION_FORTUNE, 125, level - 1))));
        BEACON_EFFECTS.put(Blocks.GLOWSTONE.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 125, 1))));
        BEACON_EFFECTS.put(Blocks.GOLD_BLOCK.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 120, level))));
        BEACON_EFFECTS.put(Blocks.SLIME_BLOCK.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 120, level))));
        BEACON_EFFECTS.put(BlockAesthetic.getVariant(BlockAesthetic.EnumType.DUNG), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> {
                    if (!PlayerHelper.hasFullSet((EntityPlayer) player, ItemSoulforgeArmor.class)) {
                        player.addPotionEffect(new PotionEffect(MobEffects.POISON, 120, level));
                        player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 120, level));
                    }
                }
        ));
        BEACON_EFFECTS.put(Blocks.COAL_BLOCK.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> {
                    if (!PlayerHelper.hasFullSet((EntityPlayer) player, ItemSoulforgeArmor.class)) {
                        player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 120, level));
                    }
                }
        ));
        BEACON_EFFECTS.put(BlockAesthetic.getVariant(BlockAesthetic.EnumType.HELLFIRE), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 120, level))));
        BEACON_EFFECTS.put(Blocks.PRISMARINE.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 120, level))));
        BEACON_EFFECTS.put(Blocks.SPONGE.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.setAir(player.getAir() - 1)));
        BEACON_EFFECTS.put(BWMBlocks.STEEL_BLOCK.getDefaultState().withProperty(BlockSteel.HEIGHT,15), new SpawnBeaconEffect());
    }

    @Override
    public String getFeatureDescription() {
        return "Overhauls the function of Beacons. Beacons have extended range, no longer have a GUI, and require the same material throughout the pyramid. The pyramid material determines the beacon effect, and additional tiers increase the range and strength of the effects. Some beacon types may also cause side effects to occur while a beacon is active.";
    }
}
