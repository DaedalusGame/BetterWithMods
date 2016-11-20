package betterwithmods.event;

import betterwithmods.config.BWConfig;
import betterwithmods.util.DispenserBehaviorFiniteWater;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BucketEvent {
    public static void editModdedFluidDispenseBehavior() {
        if (!BWConfig.hardcoreFluidContainer)
            return;
        RegistryDefaulted<Item, IBehaviorDispenseItem> reg = BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY;
        for (Item item : Item.REGISTRY) {
            if (isFluidContainer(new ItemStack(item))) {
                if (reg.getObject(item) instanceof DispenseFluidContainer)
                    reg.putObject(item, new DispenserBehaviorFiniteWater());
            }
        }
    }

    private static boolean isFluidContainer(ItemStack stack) {
        if (stack != null && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))
            return true;
        return false;
    }

    private static int getCapacity(ItemStack stack) {
        ItemStack copy = stack.copy();
        if (FluidUtil.getFluidHandler(copy) != null) {
            net.minecraftforge.fluids.capability.IFluidHandler handler = FluidUtil.getFluidHandler(copy);
            handler.drain(Integer.MAX_VALUE, true);
            return handler.fill(new FluidStack(FluidRegistry.WATER, Integer.MAX_VALUE), true);
        }
        return 0;
    }

    private static boolean containsWater(ItemStack stack) {
        if (isFluidContainer(stack)) {
            if (FluidUtil.getFluidContained(stack) != null && FluidUtil.getFluidContained(stack).getFluid() == FluidRegistry.WATER)
                return true;
        }
        return false;
    }

    @SubscribeEvent
    public void fluidContainerUse(PlayerInteractEvent.RightClickBlock evt) {
        if (!BWConfig.hardcoreFluidContainer)
            return;

        ItemStack toCheck = evt.getEntityPlayer().getHeldItem(evt.getHand());
        if (toCheck == null || toCheck.getItem() == Items.WATER_BUCKET || !isFluidContainer(toCheck))
            return;

        if (FluidUtil.getFluidContained(toCheck) == null)
            return;

        if (!(FluidUtil.getFluidContained(toCheck).containsFluid(new FluidStack(FluidRegistry.WATER, 1000))))
            return;

        if (!evt.getWorld().isRemote) {
            if (containsWater(toCheck))
                evt.setUseBlock(Event.Result.DENY);

            Block block = evt.getWorld().getBlockState(evt.getPos()).getBlock();

            boolean replaceable = block.isReplaceable(evt.getWorld(), evt.getPos());
            BlockPos pos = replaceable ? evt.getPos() : evt.getPos().offset(evt.getFace());

            if (evt.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD) {
                ItemStack equip = evt.getEntityPlayer().getHeldItem(evt.getHand());
                if (!block.onBlockActivated(evt.getWorld(), evt.getPos(), evt.getWorld().getBlockState(evt.getPos()), evt.getEntityPlayer(), evt.getHand(), evt.getFace(), 0.5F, 0.5F, 0.5F) && equip != ItemStack.field_190927_a && containsWater(equip)) {
                    if (evt.getWorld().getBlockState(pos).getBlock().isAir(evt.getWorld().getBlockState(pos), evt.getWorld(), pos) || evt.getWorld().getBlockState(pos).getBlock().isReplaceable(evt.getWorld(), pos)) {
                        Item item = equip.getItem();
                        if (isFluidContainer(equip) && item.getItemUseAction(equip) == EnumAction.NONE) {
                            if (getCapacity(equip) == Fluid.BUCKET_VOLUME) {
                                evt.getWorld().setBlockState(pos, Blocks.FLOWING_WATER.getStateFromMeta(2));
                                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                                    BlockPos p2 = pos.offset(facing);
                                    if (evt.getWorld().getBlockState(p2).getBlock().isAir(evt.getWorld().getBlockState(p2), evt.getWorld(), p2) || evt.getWorld().getBlockState(p2).getBlock().isReplaceable(evt.getWorld(), p2))
                                        evt.getWorld().setBlockState(p2, Blocks.FLOWING_WATER.getStateFromMeta(5));
                                }
                                if (!evt.getEntityPlayer().capabilities.isCreativeMode) {
                                    if (equip.func_190916_E() == 1) {
                                        EnumHand hand = evt.getHand();
                                        evt.getEntityPlayer().setItemStackToSlot(hand == EnumHand.OFF_HAND ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND, item.hasContainerItem(equip) ? item.getContainerItem(equip).copy() : ItemStack.field_190927_a);
                                    } else if (equip.func_190916_E() > 1) {
                                        equip.func_190918_g(1);
                                        if (item.hasContainerItem(equip))
                                            evt.getEntityPlayer().inventory.addItemStackToInventory(item.getContainerItem(equip).copy());
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
    }

    @SubscribeEvent
    public void bucketUse(PlayerInteractEvent.RightClickBlock evt) {
        if (!BWConfig.hardcoreBuckets)
            return;

        ItemStack toCheck = evt.getEntityPlayer().getHeldItem(evt.getHand());

        if (toCheck == ItemStack.field_190927_a || toCheck.getItem() != Items.WATER_BUCKET)
            return;

        if (!evt.getWorld().isRemote) {
            if (toCheck.getItem() == Items.WATER_BUCKET)
                evt.setUseBlock(Event.Result.DENY);

            Block block = evt.getWorld().getBlockState(evt.getPos()).getBlock();

            boolean replaceable = block.isReplaceable(evt.getWorld(), evt.getPos());
            BlockPos pos = replaceable ? evt.getPos() : evt.getPos().offset(evt.getFace());

            if (evt.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD) {
                ItemStack equip = evt.getEntityPlayer().getHeldItem(evt.getHand());
                if (!block.onBlockActivated(evt.getWorld(), evt.getPos(), evt.getWorld().getBlockState(evt.getPos()), evt.getEntityPlayer(), evt.getHand(), evt.getFace(), 0.5F, 0.5F, 0.5F) && equip != ItemStack.field_190927_a && equip.getItem() == Items.WATER_BUCKET) {
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
                                if (equip.func_190916_E() == 1) {
                                    EnumHand hand = evt.getHand();
                                    evt.getEntityPlayer().setItemStackToSlot(hand == EnumHand.OFF_HAND ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                                } else if (equip.func_190916_E() > 1) {
                                    equip.func_190918_g(1);
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
}
