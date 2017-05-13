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
    default void preInit() {}

    default void init() {}

    default void postInit(){}

    @SideOnly(Side.CLIENT)
    default void preInitClient(){}

    @SideOnly(Side.CLIENT)
    default void initClient(){}

    @SideOnly(Side.CLIENT)
    default void postInitClient(){}
}
