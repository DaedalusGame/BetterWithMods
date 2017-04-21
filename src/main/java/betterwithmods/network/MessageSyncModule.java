package betterwithmods.network;


/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [15/07/2016, 06:03:43 (GMT)]
 */

import com.google.common.collect.Lists;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class MessageSyncModule extends NetworkMessage {

    public final List<ConfigCategory> categories = Lists.newLinkedList();

    public MessageSyncModule() {
    }

    @Override
    public IMessage handleMessage(MessageContext context) {
         ModuleSync.syncConfig(categories);
        return null;
    }
}