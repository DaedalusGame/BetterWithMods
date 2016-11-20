package betterwithmods.network;

import betterwithmods.blocks.tile.IMechSubtype;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileUpdatePacket extends AbstractPacketSided {
    public BlockPos pos;
    public int sub;

    public TileUpdatePacket() {

    }

    public TileUpdatePacket(BlockPos pos, int sub) {
        this.pos = pos;
        this.sub = sub;
    }

    @Override
    public void handleClientThreaded(NetHandlerPlayClient client) {
        TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(pos);
        if (tile instanceof IMechSubtype) {
            ((IMechSubtype) tile).setSubtype(sub);
        }
    }

    @Override
    public void handleServerThreaded(NetHandlerPlayServer server) {
        throw new UnsupportedOperationException("Clientside only");
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = readPos(buf);
        sub = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writePos(pos, buf);
        buf.writeInt(sub);
    }
}
