package betterwithmods.module.gameplay;

import betterwithmods.module.Module;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.HCHunger;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class Gameplay extends Module {
    public static double crankExhaustion;
    public static boolean kidFriendly;

    @Override
    public void addFeatures() {
        registerFeature(new Debarking());
        registerFeature(new MechanicalBreakage());
        registerFeature(new MetalReclaming());
        registerFeature(new NuggetCompression());
        registerFeature(new HarderSteelRecipe());
        registerFeature(new AnvilRecipes());
        registerFeature(new CraftingRecipes());
        registerFeature(new CauldronRecipes());
        registerFeature(new CrucibleRecipes());
        registerFeature(new KilnRecipes());
        registerFeature(new MillRecipes());
        registerFeature(new SawRecipes());
        registerFeature(new TurntableRecipes());
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        crankExhaustion = loadPropDouble("Crank Exhaustion", "How much saturation turning the crank eats. Set to 0.0 to disable.", 6.0, 0.0, 6.0);
        kidFriendly = loadPropBool("Kid Friendly", "Makes some features more kid friendly", false);
    }

    public static double getCrankExhaustion() {
        return crankExhaustion * (ModuleLoader.isFeatureEnabled(HCHunger.class) ? 3 : 1);
    }


}

