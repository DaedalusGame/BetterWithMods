package betterwithmods.network;

import betterwithmods.common.blocks.IRenderUpdate;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by primetoxinz on 7/19/17.
 */
public class MessageRenderUpdate extends NetworkMessage {
    public BlockPos pos;


    public MessageRenderUpdate() {
    }

    public MessageRenderUpdate(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public IMessage handleMessage(MessageContext context) {
        World world = context.side == Side.CLIENT ? Minecraft.getMinecraft().world: context.getServerHandler().player.world;
        if(world != null) {
            Block block = world.getBlockState(pos).getBlock();
            if (block instanceof IRenderUpdate) {
                ((IRenderUpdate) block).update(world, pos);
            }
        }
        return super.handleMessage(context);
    }
}
