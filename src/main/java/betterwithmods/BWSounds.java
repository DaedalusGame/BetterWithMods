package betterwithmods;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BWSounds {
    public static SoundEvent WOODCREAK;
    public static SoundEvent STONEGRIND;
    public static SoundEvent BELLOW;
    public static SoundEvent WOODCHIME;
    public static SoundEvent METALCHIME;

    public static void registerSounds() {
        WOODCREAK = registerSound("block.wood.creak");
        STONEGRIND = registerSound("block.stone.grind");
        BELLOW = registerSound("block.wood.bellow");
        WOODCHIME = registerSound("block.wood.chime");
        METALCHIME = registerSound("block.metal.chime");
    }

    public static SoundEvent registerSound(String soundName) {
        ResourceLocation soundID = new ResourceLocation(BWMod.MODID, soundName);
        return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
    }
}
