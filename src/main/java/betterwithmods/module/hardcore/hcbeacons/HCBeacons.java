package betterwithmods.module.hardcore.hcbeacons;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.module.Feature;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
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
        BEACON_EFFECTS.put(Blocks.IRON_BLOCK.getDefaultState(), (world, pos, level) -> {

        });
        BEACON_EFFECTS.put(Blocks.GLASS.getDefaultState(), (world, pos, level) -> {
        });
        BEACON_EFFECTS.put(Blocks.LAPIS_BLOCK.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(BWRegistry.POTION_TRUESIGHT, 125, 1))));
        BEACON_EFFECTS.put(Blocks.GLOWSTONE.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 125, 1))));
        BEACON_EFFECTS.put(Blocks.GOLD_BLOCK.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 120, level-1))));
        BEACON_EFFECTS.put(BWMBlocks.AESTHETIC.getDefaultState().withProperty(BlockAesthetic.TYPE, BlockAesthetic.EnumType.DUNG), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(MobEffects.POISON, 120, level))));
        BEACON_EFFECTS.put(BWMBlocks.AESTHETIC.getDefaultState().withProperty(BlockAesthetic.TYPE, BlockAesthetic.EnumType.HELLFIRE), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 120, level))));
    }

    @Override
    public String getFeatureDescription() {
        return "";
    }
}
