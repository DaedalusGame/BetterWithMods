package betterwithmods.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BWFluidRegistry {
    public static Fluid STEAM = new Fluid("steam", new ResourceLocation("betterwithmods", "blocks/steam_still"), new ResourceLocation("betterwithmods", "blocks/steam_flowing"));

    public static void init() {
        FluidRegistry.registerFluid(STEAM);
    }
}
