package betterwithmods.integration.minetweaker;

import betterwithmods.integration.ICompatModule;
import minetweaker.MineTweakerAPI;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by tyler on 9/4/16.
 */
@SuppressWarnings("unused")
public class MineTweaker implements ICompatModule {
    public static final String MODID = "MineTweaker3";

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        MineTweakerAPI.registerClass(Saw.class);
        MineTweakerAPI.registerClass(Kiln.class);
        MineTweakerAPI.registerClass(Cauldron.class);
        MineTweakerAPI.registerClass(StokedCauldron.class);
        MineTweakerAPI.registerClass(Crucible.class);
        MineTweakerAPI.registerClass(StokedCrucible.class);
        MineTweakerAPI.registerClass(Mill.class);
//        MineTweakerAPI.registerClass(Buoyancy.class); really need to rewrite the Bouyancy registration, it's a generics nightmare
//        MineTweakerAPI.registerClass(HopperFilter.class);
        MineTweakerAPI.registerClass(SteelAnvil.class);
        MineTweakerAPI.registerClass(ChopRecipe.class);
    }

    @Override
    public void postInit() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void postInitClient() {

    }
}
