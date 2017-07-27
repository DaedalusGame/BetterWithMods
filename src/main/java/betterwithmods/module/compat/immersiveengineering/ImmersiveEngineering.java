package betterwithmods.module.compat.immersiveengineering;

import betterwithmods.module.CompatFeature;
import betterwithmods.module.hardcore.HCSeeds;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by primetoxinz on 7/26/17.
 */
@SuppressWarnings("unused")
public class ImmersiveEngineering extends CompatFeature {

    public ImmersiveEngineering() {
        super("immersiveengineering");
    }

    @GameRegistry.ObjectHolder("immersiveengineering:seed")
    public static final Item HEMP_SEED = null;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        HCSeeds.SEED_BLACKLIST.add(new ItemStack(HEMP_SEED));
    }
}
