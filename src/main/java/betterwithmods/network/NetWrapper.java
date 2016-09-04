package betterwithmods.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetWrapper
{
    public final SimpleNetworkWrapper net;
    protected final AbstractPacketHandler handler;
    private int netID = 0;

    public NetWrapper(String channel)
    {
        net = NetworkRegistry.INSTANCE.newSimpleChannel(channel);
        handler = new AbstractPacketHandler();
    }

    public void registerPacket(Class<? extends AbstractPacket> packet)
    {
        registerClientPacket(packet);
        registerServerPacket(packet);
    }

    public void registerClientPacket(Class<? extends AbstractPacket> packet)
    {
        registerPacketOnSide(packet, Side.CLIENT);
    }

    public void registerServerPacket(Class<? extends AbstractPacket> packet)
    {
        registerPacketOnSide(packet, Side.SERVER);
    }

    private void registerPacketOnSide(Class<? extends AbstractPacket> packet, Side side)
    {
        net.registerMessage(handler, packet, netID++, side);
    }

    public static class AbstractPacketHandler implements IMessageHandler<AbstractPacket, IMessage>
    {
        @Override
        public IMessage onMessage(AbstractPacket packet, MessageContext ctx)
        {
            if(ctx.side == Side.SERVER)
                return packet.handleServer(ctx.getServerHandler());
            else
                return packet.handleClient(ctx.getClientHandler());
        }
    }
}
