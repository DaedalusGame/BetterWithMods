package betterwithmods.event;

import betterwithmods.BWMod;
import betterwithmods.config.BWConfig;
import betterwithmods.util.player.EntityPlayerExt;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Makes death a problem again.
 * 
 * @author Koward
 *
 */
public class RespawnEventHandler {
	/**
	 * Disable Beds
	 * @param event
	 */
	@SubscribeEvent
	public void onSleepInBed(PlayerSleepInBedEvent event) {
		if(!BWConfig.hardcoreBeds) return;
		if(EntityPlayerExt.isSurvival(event.getEntityPlayer())) event.setResult(EntityPlayer.SleepResult.NOT_SAFE);
	}

	static final int HARDCORE_SPAWN_RADIUS = 2000;
	static final int HARDCORE_SPAWN_COOLDOWN = 24000;//20 min
	static final int HARDCORE_SPAWN_MAX_ATTEMPTS = 20;

	/**
	 * Random Respawn. Less intense when there is a short time since death.
	 * @param event
	 */
	@SubscribeEvent
	public void randomRespawn(LivingDeathEvent event) {
		if(!BWConfig.hardcoreSpawn) return;
		if (!(event.getEntity() instanceof EntityPlayerMP)) return; 
		EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
		if(EntityPlayerExt.isSurvival(player)) {
			int timeSinceDeath = player.getStatFile().readStat(StatList.TIME_SINCE_DEATH);
			int spawnFuzz = timeSinceDeath >= HARDCORE_SPAWN_COOLDOWN ? HARDCORE_SPAWN_RADIUS : 100;
			BlockPos newPos = getRespawnPoint(player, spawnFuzz);
			player.setSpawnPoint(newPos, true);
		}
	}

	/**
	 * Find a random position to respawn. Tries 20 times maximum to find a
	 * suitable place. Else, the previous SP will remain unchanged.
	 * 
	 * @param worldObj
	 * @param spawnFuzz
	 *            A "size coefficient" variable. Proportional to distance
	 *            between spawn points.
	 * @return The new BlockPos
	 */
	public BlockPos getRespawnPoint(EntityPlayer player, int spawnFuzz) {
		World worldObj = player.worldObj;
		BlockPos ret = worldObj.getSpawnPoint();
		if (!worldObj.provider.getHasNoSky()) {
			boolean found = false;
			for (int tryCounter = 0; tryCounter < HARDCORE_SPAWN_MAX_ATTEMPTS; tryCounter++) {
				ret = worldObj.getSpawnPoint();
				double fuzzVar = worldObj.rand.nextDouble() * spawnFuzz;
				double angle = worldObj.rand.nextDouble();
				double customX = (double) (-MathHelper
						.sin((float) (angle * 360.0D))) * fuzzVar;
				double customZ = (double) MathHelper
						.cos((float) (angle * 360.0D)) * fuzzVar;
				ret = ret.add(MathHelper.floor_double(customX) + 0.5D, 1.5D,
						MathHelper.floor_double(customZ) + 0.5D);
				ret = worldObj.getTopSolidOrLiquidBlock(ret);

				// Check if the position is correct
				int cmp = ret.getY()
						- worldObj.provider.getAverageGroundLevel();
				Material check = worldObj.getBlockState(ret).getMaterial();
				if (cmp >= 0 && (check == null || !check.isLiquid())) {
					found = true;
					break;
				}
			}
			if(!found) BWMod.logger.warn("New respawn point could not be found.");
		}

		return ret;
	}
}
