package betterwithmods.network;

import betterwithmods.client.gui.GuiHunger;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by primetoxinz on 6/24/17.
 */
public class MessageGuiShake extends NetworkMessage {
    @Override
    public IMessage handleMessage(MessageContext context) {
        GuiHunger.INSTANCE.shake();
        return super.handleMessage(context);
    }
}
