package betterwithmods.network;

/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [11/01/2016, 21:58:25 (GMT)]
 */


import betterwithmods.BWMod;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(BWMod.MODID);

    private static int i = 0;

    public static void register(Class clazz, Side handlerSide) {
        INSTANCE.registerMessage(clazz, clazz, i++, handlerSide);
    }

    public static void sendToAllAround(IMessage message, World world, BlockPos pos) {
        INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 128));
    }
}