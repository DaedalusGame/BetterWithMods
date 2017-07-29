package betterwithmods.common;

import betterwithmods.BWMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = BWMod.MODID)
public class BWSounds {
    @GameRegistry.ObjectHolder("betterwithmods:block.wood.creak")
    public final static SoundEvent WOODCREAK = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.stone.grind")
    public final static SoundEvent STONEGRIND = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.wood.bellow")
    public final static SoundEvent BELLOW = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.wood.chime")
    public final static SoundEvent WOODCHIME = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.metal.chime")
    public final static SoundEvent METALCHIME = null;
    @GameRegistry.ObjectHolder("betterwithmods:entity.player.oof")
    public final static SoundEvent OOF = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.metal.hacksaw")
    public final static SoundEvent METAL_HACKSAW = null;

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(registerSound("block.wood.creak"));
        event.getRegistry().register(registerSound("block.stone.grind"));
        event.getRegistry().register(registerSound("block.wood.bellow"));
        event.getRegistry().register(registerSound("block.wood.chime"));
        event.getRegistry().register(registerSound("block.metal.chime"));
        event.getRegistry().register(registerSound("block.metal.hacksaw"));
        event.getRegistry().register(registerSound("entity.player.oof"));
    }

    public static SoundEvent registerSound(String soundName) {
        ResourceLocation soundID = new ResourceLocation(BWMod.MODID, soundName);
        SoundEvent sound = new SoundEvent(soundID).setRegistryName(soundID);
        return sound;
    }
}
