package betterwithmods.event;

import net.minecraft.block.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import betterwithmods.config.BWConfig;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class BucketEvent 
{
    @SubscribeEvent
    public void bucketUse(PlayerInteractEvent.RightClickBlock evt)
    {
        if(!BWConfig.hardcoreBuckets)
            return;

        if(evt.getEntityPlayer().getHeldItem(evt.getHand()) == null || evt.getEntityPlayer().getHeldItem(evt.getHand()).getItem() != Items.WATER_BUCKET)
            return;

        if(!evt.getWorld().isRemote) {
            if (evt.getEntityPlayer().getHeldItem(evt.getHand()).getItem() == Items.WATER_BUCKET)
                evt.setUseBlock(Event.Result.DENY);

            Block block = evt.getWorld().getBlockState(evt.getPos()).getBlock();

            boolean replaceable = block.isReplaceable(evt.getWorld(), evt.getPos());
            BlockPos pos = replaceable ? evt.getPos() : evt.getPos().offset(evt.getFace());

            if (evt.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD) {
                ItemStack equip = evt.getEntityPlayer().getHeldItem(evt.getHand());
                if (!block.onBlockActivated(evt.getWorld(), evt.getPos(), evt.getWorld().getBlockState(evt.getPos()), evt.getEntityPlayer(), evt.getHand(), evt.getEntityPlayer().getHeldItem(evt.getHand()), evt.getFace(), 0.5F, 0.5F, 0.5F) && equip != null && equip.getItem() == Items.WATER_BUCKET) {
                    if (evt.getWorld().getBlockState(pos).getBlock().isAir(evt.getWorld().getBlockState(pos), evt.getWorld(), pos) || evt.getWorld().getBlockState(pos).getBlock().isReplaceable(evt.getWorld(), pos)) {
                        ItemBucket bucket = (ItemBucket) Items.WATER_BUCKET;
                        if (bucket.tryPlaceContainedLiquid(evt.getEntityPlayer(), evt.getWorld(), pos)) {
                            evt.getWorld().setBlockState(pos, Blocks.FLOWING_WATER.getStateFromMeta(2));
                            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                                BlockPos p2 = pos.offset(facing);
                                if (evt.getWorld().getBlockState(p2).getBlock().isAir(evt.getWorld().getBlockState(p2), evt.getWorld(), p2) || evt.getWorld().getBlockState(p2).getBlock().isReplaceable(evt.getWorld(), p2))
                                    evt.getWorld().setBlockState(p2, Blocks.FLOWING_WATER.getStateFromMeta(5));
                            }
                            if (!evt.getEntityPlayer().capabilities.isCreativeMode) {
                                if (equip.stackSize == 1) {
                                    EnumHand hand = evt.getHand();
                                    evt.getEntityPlayer().setItemStackToSlot(hand == EnumHand.OFF_HAND ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                                } else if (equip.stackSize > 1) {
                                    equip.stackSize -= 1;
                                    evt.getEntityPlayer().inventory.addItemStackToInventory(new ItemStack(Items.BUCKET));
                                    evt.setUseItem(Event.Result.DENY);
                                }
                            }
                            evt.setCanceled(true);
                        }
                    }
                }
            }
        }
    }
    /*
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void bucketUse(PlayerInteractEvent evt)
	{
		if(!BWConfig.hardcoreBuckets)
			return;
		
		if(evt.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			BlockPos pos = evt.getFace() != null ? evt.getPos().offset(evt.getFace()) : evt.getPos();
			if(evt.world.provider.isSurfaceWorld() && evt.getWorld().provider.getBiomeGenForCoords(pos) != BiomeGenBase.sky)
			{
				if(evt.entityPlayer.getCurrentEquippedItem() != null && evt.entityPlayer.getCurrentEquippedItem().getItem() == Items.water_bucket)
				{
					if(evt.world.getBlockState(pos).getBlock().onBlockActivated(evt.world, pos, evt.world.getBlockState(pos), evt.entityPlayer, evt.face, 0.5F, 0.5F, 0.5F))
					{
						evt.world.getBlockState(pos).getBlock().onBlockActivated(evt.world, pos, evt.world.getBlockState(pos), evt.entityPlayer, evt.face, 0.5F, 0.5F, 0.5F);
					}
					else
					{
						ItemBucket bucket = (ItemBucket)Items.water_bucket;
						if(bucket.tryPlaceContainedLiquid(evt.world, pos))//evt.world.getBlockState(pos).getBlock().isAir(evt.world, pos) || evt.world.getBlockState(pos).getBlock().isReplaceable(evt.world, pos))
						{
							evt.world.setBlockState(pos, Blocks.flowing_water.getStateFromMeta(2));
							/*for(int i = 2; i < 6; i++)
							{
								BlockPos p2 = pos.offset(EnumFacing.getFront(i));
								if(evt.world.getBlockState(p2).getBlock().isAir(evt.world, p2) || evt.world.getBlockState(p2).getBlock().isReplaceable(evt.world, p2))
									evt.world.setBlockState(pos, Blocks.flowing_water.getStateFromMeta(5));
							}
							if(!evt.entityPlayer.capabilities.isCreativeMode)
							{
								if(evt.entityPlayer.getCurrentEquippedItem().stackSize == 1)
									evt.entityPlayer.setCurrentItemOrArmor(0, new ItemStack(Items.bucket));
								else if(evt.entityPlayer.getCurrentEquippedItem().stackSize > 1)
								{
									evt.entityPlayer.getCurrentEquippedItem().stackSize -= 1;
									evt.entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.bucket));
								}
							}*//*
							deny(evt, false);
						}
					}
				}
			}
		}
	}
	
	private void deny(PlayerInteractEvent evt, boolean sendServerside)
	{
		evt.setResult(Event.Result.DENY);
		if(sendServerside && evt.getWorld().isRemote)
			FMLClientHandler.instance().getClientPlayerEntity().sendQueue.addToSendQueue(new CPacketPlayerBlockPlacement(evt.entityPlayer.inventory.getCurrentItem()));
		evt.setCanceled(true);
	}*/
}
