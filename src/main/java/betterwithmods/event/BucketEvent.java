package betterwithmods.event;

import betterwithmods.config.BWConfig;
import betterwithmods.util.DispenserBehaviorFiniteWater;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

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
        return stack != ItemStack.EMPTY && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
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
        if (toCheck == ItemStack.EMPTY || toCheck.getItem() == Items.WATER_BUCKET || !isFluidContainer(toCheck))
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
                if (!block.onBlockActivated(evt.getWorld(), evt.getPos(), evt.getWorld().getBlockState(evt.getPos()), evt.getEntityPlayer(), evt.getHand(), evt.getFace(), 0.5F, 0.5F, 0.5F) && equip != ItemStack.EMPTY && containsWater(equip)) {
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
                                    if (equip.getCount() == 1) {
                                        EnumHand hand = evt.getHand();
                                        evt.getEntityPlayer().setItemStackToSlot(hand == EnumHand.OFF_HAND ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND, item.hasContainerItem(equip) ? item.getContainerItem(equip).copy() : ItemStack.EMPTY);
                                    } else if (equip.getCount() > 1) {
                                        equip.shrink(1);
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

        if (toCheck == ItemStack.EMPTY || toCheck.getItem() != Items.WATER_BUCKET)
            return;

        if (!evt.getWorld().isRemote) {
            if (toCheck.getItem() == Items.WATER_BUCKET)
                evt.setUseBlock(Event.Result.DENY);

            Block block = evt.getWorld().getBlockState(evt.getPos()).getBlock();

            boolean replaceable = block.isReplaceable(evt.getWorld(), evt.getPos());
            BlockPos pos = replaceable ? evt.getPos() : evt.getPos().offset(evt.getFace());

            if (evt.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD) {
                ItemStack equip = evt.getEntityPlayer().getHeldItem(evt.getHand());
                if (!block.onBlockActivated(evt.getWorld(), evt.getPos(), evt.getWorld().getBlockState(evt.getPos()), evt.getEntityPlayer(), evt.getHand(), evt.getFace(), 0.5F, 0.5F, 0.5F) && equip != ItemStack.EMPTY && equip.getItem() == Items.WATER_BUCKET) {
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
                                if (equip.getCount() == 1) {
                                    EnumHand hand = evt.getHand();
                                    evt.getEntityPlayer().setItemStackToSlot(hand == EnumHand.OFF_HAND ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                                } else if (equip.getCount() > 1) {
                                    equip.shrink(1);
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

    @SubscribeEvent
    public void checkPlayerInventory(TickEvent.PlayerTickEvent e) {
        World world = e.player.getEntityWorld();
        if (BWConfig.hardcoreLavaBuckets) {
            if (world.getTotalWorldTime() % 10 == 0) {
                if (!e.player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                    IItemHandler inv = e.player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                    BlockPos pos = e.player.getPosition();
                    for (int i = 0; i < inv.getSlots(); i++) {
                        ItemStack stack = inv.getStackInSlot(i);
                        if (world.rand.nextInt(50) == 0) {
                            if (stack != ItemStack.EMPTY && stack.isItemEqual(new ItemStack(Items.LAVA_BUCKET))) {
                                IFluidHandler bucket = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                                bucket.drain(1000, true);
                                world.playSound(e.player, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.PLAYERS, 1, 1);
                                placeLavaBucket(world, pos.offset(e.player.getHorizontalFacing()), 0);
                            }
                        }
                    }
                }
            }
        }
    }

    public void placeLavaBucket(World world, BlockPos pos, int depth) {
        if (depth >= 5)
            return;
        if (world.isAirBlock(pos)) {
            world.setBlockState(pos, Blocks.FLOWING_LAVA.getDefaultState());
        } else {
            placeLavaBucket(world, pos.offset(EnumFacing.VALUES[world.rand.nextInt(6)]), depth++);
        }
    }

    @SubscribeEvent
    public void onFillBucket(FillBucketEvent e) {
        if (BWConfig.hardcoreLavaBuckets) {
            if (e.getEntityPlayer().isPotionActive(MobEffects.FIRE_RESISTANCE))
                return;
            if (e.getTarget() != null && e.getTarget().getBlockPos() != null) {
                Block block = e.getWorld().getBlockState(e.getTarget().getBlockPos()).getBlock();
                if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) {
                    e.getEntityPlayer().attackEntityFrom(DamageSource.LAVA, 1);
                    e.getWorld().playSound(null, e.getTarget().getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1.5f);
                    e.setCanceled(true);
                }
            }
        }
    }
}
