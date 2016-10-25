package betterwithmods.network;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class AbstractPacketSided extends AbstractPacket {
    @Override
    public final IMessage handleClient(final NetHandlerPlayClient client) {
        FMLCommonHandler.instance().getWorldThread(client).addScheduledTask(new Runnable() {
            @Override
            public void run() {
                handleClientThreaded(client);
            }
        });
        return null;
    }

    @Override
    public final IMessage handleServer(final NetHandlerPlayServer server) {
        FMLCommonHandler.instance().getWorldThread(server).addScheduledTask(new Runnable() {
            @Override
            public void run() {
                handleServerThreaded(server);
            }
        });
        return null;
    }

    public abstract void handleClientThreaded(NetHandlerPlayClient client);

    public abstract void handleServerThreaded(NetHandlerPlayServer server);
}
