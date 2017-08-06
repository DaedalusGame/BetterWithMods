package betterwithmods.event;

import betterwithmods.util.player.Profiles;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class FakePlayerHandler {
    public static FakePlayer player;

    public static FakePlayer getPlayer() {
        return player;
    }

    //Initializing a static fake player for saws, so spawn isn't flooded with player equipping sounds when mobs hit the saw.
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load evt) {
        if (evt.getWorld() instanceof WorldServer) {
            player = FakePlayerFactory.get((WorldServer) evt.getWorld(), Profiles.BWMSAW);
            ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
            sword.addEnchantment(Enchantment.getEnchantmentByLocation("looting"), 2);
            player.setHeldItem(EnumHand.MAIN_HAND, sword);
        }
    }
    //Not sure if this would be needed, but can't be too safe.
    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload evt) {
//        if (evt.getWorld() instanceof WorldServer) {
//            if (player != null) {
//                player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
//                player = null;
//            }
//        }
    }
}
