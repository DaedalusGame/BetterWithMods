package betterwithmods.event;

import betterwithmods.BWMItems;
import betterwithmods.config.BWConfig;
import betterwithmods.entity.EntityShearedCreeper;
import betterwithmods.util.InvUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class MobDropEvent 
{
	private static int[] fearLevel = {1600, 1500, 1400, 1300, 1200, 1100, 1000, 900, 800, 700, 600, 500, 400, 300, 200, 100};
	private static Random rand = new Random();

	@SubscribeEvent
	public void mobDungProduction(LivingEvent.LivingUpdateEvent evt)
	{
		if(evt.getEntityLiving().worldObj.isRemote)
			return;

		if(!BWConfig.produceDung)
			return;

		if(evt.getEntityLiving() instanceof EntityAnimal)
		{
			EntityAnimal animal = (EntityAnimal)evt.getEntityLiving();
			if(animal instanceof EntityWolf)
			{
				if(!animal.worldObj.canSeeSky(animal.getPosition()));
				{
					if(animal.getGrowingAge() > 99)
					{
						int light = animal.worldObj.getLight(animal.getPosition());
						if(animal.getGrowingAge() == fearLevel[light])
						{
							evt.getEntityLiving().entityDropItem(new ItemStack(BWMItems.MATERIAL, 1, 5), 0.0F);
							animal.setGrowingAge(99);
						}
					}
				}
			}
			if(!(animal instanceof EntityRabbit)) {
				if (animal.getGrowingAge() == 100) {
					evt.getEntityLiving().entityDropItem(new ItemStack(BWMItems.MATERIAL, 1, 5), 0.0F);
				} else if (animal.isInLove()) {
					if (rand.nextInt(1200) == 0) {
						evt.getEntityLiving().entityDropItem(new ItemStack(BWMItems.MATERIAL, 1, 5), 0.0F);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void mobDiesBySaw(LivingDropsEvent evt)
	{
		if(evt.getSource().damageType.equals("saw") || evt.getSource().damageType.equals("choppingBlock"))
		{
			if(!(evt.getEntityLiving() instanceof EntityPlayer)) {
				for (EntityItem item : evt.getDrops()) {
					ItemStack stack = item.getEntityItem();
					if (stack.getMaxStackSize() != 1 && evt.getEntity().worldObj.rand.nextBoolean())
						item.setEntityItemStack(new ItemStack(stack.getItem(), stack.stackSize + 1, stack.getItemDamage()));
				}
			}
			if(evt.getEntityLiving() instanceof EntityAgeable)
				addDrop(evt, new ItemStack(BWMItems.MATERIAL, 1, 5));
			int headChance = evt.getEntityLiving().worldObj.rand.nextInt(12);
			if(evt.getSource().damageType.equals("choppingBlock") && headChance < 5)
			{
				if(evt.getEntityLiving() instanceof EntitySkeleton)
				{
					EntitySkeleton skeltal = (EntitySkeleton)evt.getEntityLiving();
					if(skeltal.func_189771_df() != SkeletonType.STRAY)
						addDrop(evt, new ItemStack(Items.SKULL, 1, skeltal.func_189771_df().func_190135_a()));
				}
				else if(evt.getEntityLiving() instanceof EntityZombie)
					addDrop(evt, new ItemStack(Items.SKULL, 1, 2));
				else if(evt.getEntityLiving() instanceof EntityCreeper)
					addDrop(evt, new ItemStack(Items.SKULL, 1, 4));
				else if(evt.getEntityLiving() instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer)evt.getEntityLiving();
					ItemStack drop = new ItemStack(Items.SKULL, 1, 3);
					NBTTagCompound name = new NBTTagCompound();
					name.setString("SkullOwner", player.getDisplayNameString());
					drop.setTagCompound(name);
					addDrop(evt, drop);
				}
			}
		}
	}

	@SubscribeEvent
	public void mobDrops(LivingDropsEvent evt)
	{
		if(!BWConfig.hardcoreGunpowder)
			return;
		if(evt.getEntity() instanceof EntityCreeper || evt.getEntity() instanceof EntityGhast)
		{
			for(EntityItem item : evt.getDrops())
			{
				ItemStack stack = item.getEntityItem();
				if(stack.getItem() == Items.GUNPOWDER)
				{
					if(rand.nextBoolean())
						item.setEntityItemStack(new ItemStack(BWMItems.MATERIAL, stack.stackSize, 26));
					else
						item.setEntityItemStack(new ItemStack(BWMItems.MATERIAL, stack.stackSize, 25));
				}
			}
		}
	}

	public void addDrop(LivingDropsEvent evt, ItemStack drop)
	{
		EntityItem item = new EntityItem(evt.getEntityLiving().worldObj, evt.getEntityLiving().posX, evt.getEntityLiving().posY, evt.getEntityLiving().posZ, drop);
		item.setDefaultPickupDelay();
		evt.getDrops().add(item);
	}

	@SubscribeEvent
	public void shearCreeper(PlayerInteractEvent.EntityInteractSpecific e) {
		Entity creeper = e.getTarget();
		if(creeper instanceof EntityCreeper) {
			if(e.getSide().isServer() && creeper.isEntityAlive() && e.getItemStack() != null) {
				if (e.getItemStack().getItem() instanceof ItemShears) {
					InvUtils.ejectStack(e.getWorld(), creeper.posX, creeper.posY, creeper.posZ, new ItemStack(BWMItems.CREEPER_OYSTER));
					EntityShearedCreeper shearedCreeper = new EntityShearedCreeper(e.getWorld());
					creeper.attackEntityFrom(new DamageSource(""), 0);
					copyEntityInfo(creeper, shearedCreeper);
					e.getWorld().playSound(null, shearedCreeper.posX, shearedCreeper.posY, shearedCreeper.posZ, SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.HOSTILE, 1, 0.3F);
					e.getWorld().playSound(null, shearedCreeper.posX, shearedCreeper.posY, shearedCreeper.posZ, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.HOSTILE, 1, 1F);
					creeper.setDead();
					e.getWorld().spawnEntityInWorld(shearedCreeper);
				}
			}
		}
	}

	public void copyEntityInfo(Entity copyFrom, Entity copyTo) {
		copyTo.setPositionAndRotation(copyFrom.posX, copyFrom.posY, copyFrom.posZ,copyFrom.rotationYaw,copyFrom.rotationPitch);
		copyTo.setRotationYawHead(copyFrom.getRotationYawHead());
	}
}
