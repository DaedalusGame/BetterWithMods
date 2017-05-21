package betterwithmods.module.hardcore;

import betterwithmods.module.Feature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 5/21/17.
 */
public class HCNames extends Feature {
    private static final String TEAM = "BWMTeam";
    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        Scoreboard scoreboard = event.getServer().getEntityWorld().getScoreboard();
        scoreboard.createTeam(TEAM);
        scoreboard.getTeam(TEAM).setNameTagVisibility(Team.EnumVisible.NEVER);
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent e) {
        if(e.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntity();
            Team team = player.getTeam();
            Scoreboard scoreboard = e.getWorld().getScoreboard();
            if(team == null) {
                scoreboard.addPlayerToTeam(player.getName(),TEAM);
            }
        }

    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
