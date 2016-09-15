package betterwithmods.event;

import betterwithmods.config.BWConfig;
import betterwithmods.util.NetherSpawnWhitelist;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;

import java.util.Random;

public class NetherSpawnEvent 
{
	private static Random rand = new Random();

	@SubscribeEvent
	public void giveEndermenEndStone(LivingSpawnEvent evt)
	{
		EntityLivingBase entity = evt.getEntityLiving();

		if(evt.getWorld().provider.getDimensionType() == DimensionType.THE_END)
		{
			if(entity instanceof EntityEnderman)
			{
				if(rand.nextInt(2000) == 0)
					((EntityEnderman)entity).setHeldBlockState(Blocks.END_STONE.getDefaultState());
			}
		}
	}

	@SubscribeEvent
	public void generateMossNearSpawner(LivingSpawnEvent evt)
	{
		EntityLivingBase entity = evt.getEntityLiving();
		BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
		boolean nearSpawner = evt.getWorld().getBlockState(pos.down()).getBlock() == Blocks.MOB_SPAWNER;
		BlockPos spawn = pos.down();

		if(nearSpawner)
		{
			int x = rand.nextInt(9) - 4;
			int y = rand.nextInt(5) - 1;
			int z = rand.nextInt(9) - 4;
			BlockPos check = spawn.add(x, y, z);
			IBlockState state = evt.getWorld().getBlockState(check);
			if((state.getBlock() == Blocks.COBBLESTONE || (state.getBlock() == Blocks.STONEBRICK && state.getBlock().getMetaFromState(state) == 0)) && rand.nextInt(30) == 0)
			{
				IBlockState changeState = state.getBlock() == Blocks.COBBLESTONE ? Blocks.MOSSY_COBBLESTONE.getDefaultState() : Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
				evt.getWorld().setBlockState(check, changeState);
			}
		}
	}

	@SubscribeEvent
	public void denySlimeSpawns(CheckSpawn evt)
	{
		if(evt.getResult() == Result.ALLOW)
			return;
		if(!BWConfig.slimeSpawn)
			return;
		if(evt.getWorld() != null && evt.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD)
		{
			if(evt.getEntityLiving() instanceof EntitySlime)
			{
				BlockPos pos = new BlockPos(evt.getEntity().posX, evt.getEntity().posY - 1, evt.getEntity().posZ);
				if(evt.getWorld().getBlockState(pos).getMaterial() != Material.GRASS && evt.getWorld().getBlockState(pos).getMaterial() != Material.ROCK && evt.getWorld().getBlockState(pos).getMaterial() != Material.GROUND)
					evt.setResult(Result.DENY);
			}
		}
	}

	@SubscribeEvent
	public void denyNetherSpawns(CheckSpawn evt)
	{
		if(evt.getResult() == Result.ALLOW)
			return;
		if(!BWConfig.netherSpawn)
			return;
		if(evt.getWorld() != null && evt.getWorld().provider.getDimension() == -1)
		{
			if(evt.getEntityLiving().isCreatureType(EnumCreatureType.MONSTER, false))
			{
				double monX = evt.getEntity().posX;
				double monY = evt.getEntity().posY;
				double monZ = evt.getEntity().posZ;
				int x = MathHelper.floor_double(monX);
				int y = MathHelper.floor_double(monY);
				int z = MathHelper.floor_double(monZ);
				BlockPos pos = new BlockPos(x, y - 1, z);
				Block block = evt.getWorld().getBlockState(pos).getBlock();
				int meta = evt.getWorld().getBlockState(pos).getBlock().getMetaFromState(evt.getWorld().getBlockState(pos));
				if(!NetherSpawnWhitelist.contains(block, meta))
					evt.setResult(Result.DENY);
			}
		}
	}
}
