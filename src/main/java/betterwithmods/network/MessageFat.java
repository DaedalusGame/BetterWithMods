package betterwithmods.network;

import betterwithmods.module.hardcore.hchunger.HCHunger;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by primetoxinz on 6/24/17.
 */
public class MessageFat extends NetworkMessage {
    public String playerName;

    public MessageFat() {

    }

    public MessageFat(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public IMessage handleMessage(MessageContext context) {
        HCHunger.ClientSide.doFat(playerName);
        return super.handleMessage(context);
    }
}
