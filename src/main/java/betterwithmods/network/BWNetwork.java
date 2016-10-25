package betterwithmods.network;

import betterwithmods.BWMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class BWNetwork extends NetWrapper {
    public static BWNetwork INSTANCE = new BWNetwork();

    public BWNetwork() {
        super(BWMod.MODID);
    }

    public static void sendToAll(AbstractPacket pkt) {
        INSTANCE.net.sendToAll(pkt);
    }

    public static void sendTo(AbstractPacket pkt, EntityPlayerMP player) {
        INSTANCE.net.sendTo(pkt, player);
    }

    public static void sendToAllAround(AbstractPacket pkt, NetworkRegistry.TargetPoint point) {
        INSTANCE.net.sendToAllAround(pkt, point);
    }

    public static void sendToDimension(AbstractPacket pkt, int dimID) {
        INSTANCE.net.sendToDimension(pkt, dimID);
    }

    public static void sendToServer(AbstractPacket pkt) {
        INSTANCE.net.sendToServer(pkt);
    }

    public static void sendToClient(WorldServer world, BlockPos pos, AbstractPacket pkt) {
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        for (EntityPlayer player : world.playerEntities) {
            if (!(player instanceof EntityPlayerMP))
                continue;
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            if (world.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, chunk.xPosition, chunk.zPosition))
                BWNetwork.sendTo(pkt, playerMP);
        }
    }

    public void init() {
        this.registerClientPacket(TileUpdatePacket.class);
    }
}
