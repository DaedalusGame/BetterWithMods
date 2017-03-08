package betterwithmods.config;

import betterwithmods.BWMod;
import betterwithmods.network.BWNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

public class ConfigSyncHandler {
    @SideOnly(Side.CLIENT)
    private static boolean requiresRestart;

    // syncs the data to the current config
    public static void syncConfig(List<ConfigCategory> categories) {
        requiresRestart = false;
        boolean changed = false;
        BWMod.logger.info("Syncing Config with Server");

        for (ConfigCategory serverCategory : categories) {
            if (serverCategory.getName().equals("cosmetic"))
                continue;
            // get the local equivalent
            ConfigCategory category = BWConfig.config.getCategory(serverCategory.getName());

            // sync all the properties
            for (Map.Entry<String, Property> entry : serverCategory.entrySet()) {
                String name = entry.getKey();
                Property serverProp = entry.getValue();

                // hopefully present locally?
                Property prop = category.get(name);
                if (prop == null) {
                    // use the server one
                    category.put(name, serverProp);
                } else {
                    // we try to use the preset one because it contains comments n stuff
                    if (!prop.getString().equals(serverProp.getString())) {
                        // new value, update it
                        prop.setValue(serverProp.getString());
                        requiresRestart |= prop.requiresMcRestart();
                        changed = true;
                        BWMod.logger.debug("Syncing %s - %s: %s", category.getName(), prop.getName(), prop.getString());
                    }
                }
            }
        }

        // if we changed something... disconnect and tell the player to restart?
        if (BWConfig.config.hasChanged()) {
            BWConfig.config.save();
        }

        if (changed) {
            MinecraftForge.EVENT_BUS.register(new ConfigSyncHandler());
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent evt) {
        if (evt.player == null || !(evt.player instanceof EntityPlayerMP) || FMLCommonHandler.instance().getSide().isClient())
            return;

        ConfigSyncPacket pkt = new ConfigSyncPacket();
        pkt.categories.add(BWConfig.HARDCORE_CAT);
        BWNetwork.sendTo(pkt, (EntityPlayerMP) evt.player);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void playerJoinedWorld(TickEvent.ClientTickEvent evt) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (requiresRestart) {
            player.sendMessage(new TextComponentString("[Better With Mods] " + I18n.format("config.sync.restart")));
        } else {
            player.sendMessage(new TextComponentString("[Better With Mods] " + I18n.format("config.sync.ok")));
        }
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
