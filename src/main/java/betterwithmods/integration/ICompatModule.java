package betterwithmods.integration;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Represents an integration module for a mod.
 * Client methods should be called only by {@link betterwithmods.proxy.ClientProxy}
 *
 * @author Koward
 */
public interface ICompatModule {
    void preInit();

    void init();

    void postInit();

    @SideOnly(Side.CLIENT)
    void preInitClient();

    @SideOnly(Side.CLIENT)
    void initClient();

    @SideOnly(Side.CLIENT)
    void postInitClient();
}
