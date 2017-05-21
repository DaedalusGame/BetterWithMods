package betterwithmods.module.hardcore;

import betterwithmods.module.Feature;
import betterwithmods.util.DispenserBehaviorFiniteWater;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by tyler on 4/20/17.
 */
public class HCBuckets extends Feature {
    private static boolean hardcoreFluidContainer;
    private static boolean hardcoreLavaBuckets;

    @Override
    public String getFeatureDescription() {
        return "Makes it so water buckets cannot move an entire source block, making water a more valuable resource";
    }

    @Override
    public void setupConfig() {
        hardcoreFluidContainer = loadPropBool("Hardcore Fluid Container","Hardcore Buckets Affects Modded Fluid Containers", true);
        hardcoreLavaBuckets = loadPropBool("Hardcore Lava Buckets","Makes Lava Buckets really hot, be careful you might need some resistance to it!", true);
    }
    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }
    @Override
    public void init(FMLInitializationEvent event) {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.WATER_BUCKET, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                ItemStack outputStack = stack;
                BlockPos pos = source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
                if (source.getWorld().isAirBlock(pos)
                        || source.getWorld().getBlockState(pos).getBlock().isReplaceable(source.getWorld(), pos)) {
                    source.getWorld().setBlockState(pos, Blocks.FLOWING_WATER.getStateFromMeta(2));
                    for (EnumFacing face : EnumFacing.HORIZONTALS) {
                        BlockPos off = pos.offset(face);
                        if (source.getWorld().isAirBlock(off) || source.getWorld().getBlockState(off).getBlock()
                                .isReplaceable(source.getWorld(), off))
                            source.getWorld().setBlockState(off, Blocks.FLOWING_WATER.getStateFromMeta(5));
                    }
                    outputStack = new ItemStack(Items.BUCKET, 1);
                }
                return outputStack;
            }
        });
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        editModdedFluidDispenseBehavior();
    }

    public static void editModdedFluidDispenseBehavior() {
        if (!hardcoreFluidContainer)
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
        return !stack.isEmpty() && (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null) || stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null));
    }

    private static boolean isNonVanillaBucket(ItemStack stack) {
        return stack.getItem() != Items.BUCKET && stack.getItem() != Items.WATER_BUCKET && stack.getItem() != Items.LAVA_BUCKET && stack.getItem() != Items.MILK_BUCKET && isFluidContainer(stack);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void fillFromFlowing(PlayerInteractEvent.RightClickBlock e) {
        if (e.getHand() != EnumHand.MAIN_HAND)
            return;
        ItemStack stack = e.getItemStack();
        EntityPlayer player = e.getEntityPlayer();
        if (isFluidContainer(stack)) {
            FluidStack fluid = FluidUtil.getFluidContained(stack);
            if (fluid == null || fluid.getFluid() == null) {
                if (!player.capabilities.isCreativeMode) {
                    if (stack.getItem() == Items.BUCKET) {
                        fluidContainerUse(new FillBucketEvent(player, stack, e.getWorld(), new RayTraceResult(e.getHitVec(), e.getFace())));
                    }
                    e.setResult(Event.Result.ALLOW);
                }
            }

        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void fluidContainerUse(FillBucketEvent evt) {
        if (!hardcoreFluidContainer)
            return;

        if (evt.getEmptyBucket().isEmpty()) {
            evt.setResult(Event.Result.DENY);
            return;
        }

        FluidStack fluid = FluidUtil.getFluidContained(evt.getEmptyBucket());

        RayTraceResult target = evt.getTarget();
        EntityPlayer player = evt.getEntityPlayer();
        boolean dimValid = evt.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD;

        if (dimValid) {
            if (target != null && target.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = target.getBlockPos().offset(target.sideHit);
                IBlockState state = evt.getWorld().getBlockState(pos);

                if (fluid == null || fluid.getFluid() == null) {
                    if (isWater(state) && state.getBlock().getMetaFromState(state) > 0) {
                        if (!player.capabilities.isCreativeMode) {
                            if (evt.getEmptyBucket().getItem() == Items.BUCKET) {
                                evt.setFilledBucket(new ItemStack(Items.WATER_BUCKET));
                            } else if (isFluidContainer(evt.getEmptyBucket()) && hardcoreFluidContainer) {
                                ItemStack fill = evt.getEmptyBucket().copy();
                                fill.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).fill(new FluidStack(FluidRegistry.WATER, 1000), true);
                                evt.setFilledBucket(fill);
                            }
                            evt.setResult(Event.Result.ALLOW);
                        }
                    }
                } else if (fluid != null && fluid.getFluid() != null && fluid.getFluid() == FluidRegistry.WATER) {
                    if (state.getBlock().isAir(state, evt.getWorld(), pos) || state.getBlock().isReplaceable(evt.getWorld(), pos)) {
                        if (!player.capabilities.isCreativeMode) {
                            if ((evt.getEmptyBucket().getItem() == Items.WATER_BUCKET) || (isNonVanillaBucket(evt.getEmptyBucket()) && hardcoreFluidContainer))
                                bucketEmpty(evt, pos, evt.getEmptyBucket());
                        }
                    }
                }
            }
        }
    }

    private void bucketEmpty(FillBucketEvent evt, BlockPos pos, ItemStack equip) {
        if (isWater(evt.getWorld().getBlockState(pos))) {
            IBlockState state = evt.getWorld().getBlockState(pos);
            if (state.getBlock().getMetaFromState(state) > 0) {
                emptyBucket(evt, pos, equip);
            }
        } else {
            emptyBucket(evt, pos, equip);
        }
    }

    private void emptyBucket(FillBucketEvent evt, BlockPos pos, ItemStack equip) {
        Item item = equip.getItem();
        evt.getWorld().setBlockState(pos, Blocks.FLOWING_WATER.getStateFromMeta(2));
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos p2 = pos.offset(facing);
            if (!isWater(evt.getWorld().getBlockState(p2)) && (evt.getWorld().getBlockState(p2).getBlock().isAir(evt.getWorld().getBlockState(p2), evt.getWorld(), p2) || evt.getWorld().getBlockState(p2).getBlock().isReplaceable(evt.getWorld(), p2)))
                evt.getWorld().setBlockState(p2, Blocks.FLOWING_WATER.getStateFromMeta(5));
        }
        evt.getWorld().playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        evt.setFilledBucket(item.getContainerItem(equip).copy());
        evt.setResult(Event.Result.ALLOW);
    }

    private boolean isWater(IBlockState state) {
        return state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER;
    }


    @SubscribeEvent
    public void fluidContainerUse(PlayerInteractEvent.RightClickBlock evt) {
        if (!hardcoreFluidContainer)
            return;

        ItemStack toCheck = evt.getEntityPlayer().getHeldItem(evt.getHand());
        if (toCheck.isEmpty() || toCheck.getItem() == Items.WATER_BUCKET || !isNonVanillaBucket(toCheck))
            return;

        FluidStack fluid = FluidUtil.getFluidContained(toCheck);

        if (fluid == null || fluid.getFluid() == null || fluid.getFluid() != FluidRegistry.WATER)
            return;

        if (!evt.getWorld().isRemote) {
            evt.setUseBlock(Event.Result.DENY);
            IBlockState state = evt.getWorld().getBlockState(evt.getPos());
            Block block = state.getBlock();

            boolean replaceable = block.isReplaceable(evt.getWorld(), evt.getPos());
            BlockPos pos = replaceable ? evt.getPos() : evt.getPos().offset(evt.getFace());
            state = evt.getWorld().getBlockState(pos);
            if (evt.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD) {
                if (!block.onBlockActivated(evt.getWorld(), evt.getPos(), state, evt.getEntityPlayer(), evt.getHand(), evt.getFace(), 0.5F, 0.5F, 0.5F)) {
                    if (state.getBlock().isAir(state, evt.getWorld(), pos) || state.getBlock().isReplaceable(evt.getWorld(), pos)) {
                        Item item = toCheck.getItem();
                        if (item.getItemUseAction(toCheck) == EnumAction.NONE) {
                            if (isWater(state)) {
                                if (state.getBlock().getMetaFromState(state) > 0) {
                                    placeContainerFluid(evt, pos, toCheck);
                                }
                            } else {
                                placeContainerFluid(evt, pos, toCheck);
                            }
                        }
                    }
                }
            }
        }
    }

    private void placeContainerFluid(PlayerInteractEvent.RightClickBlock evt, BlockPos pos, ItemStack equip) {
        if (!evt.getEntityPlayer().capabilities.isCreativeMode) {
            Item item = equip.getItem();
            evt.getWorld().setBlockState(pos, Blocks.FLOWING_WATER.getStateFromMeta(2));
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                BlockPos p2 = pos.offset(facing);
                if (!isWater(evt.getWorld().getBlockState(p2)) && (evt.getWorld().getBlockState(p2).getBlock().isAir(evt.getWorld().getBlockState(p2), evt.getWorld(), p2) || evt.getWorld().getBlockState(p2).getBlock().isReplaceable(evt.getWorld(), p2)))
                    evt.getWorld().setBlockState(p2, Blocks.FLOWING_WATER.getStateFromMeta(5));
            }
            if (equip.getCount() == 1) {
                EnumHand hand = evt.getHand();
                evt.getEntityPlayer().setItemStackToSlot(hand == EnumHand.OFF_HAND ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND, item.hasContainerItem(equip) ? item.getContainerItem(equip).copy() : null);
            } else if (equip.getCount() > 1) {
                equip.shrink(1);
                if (item.hasContainerItem(equip))
                    evt.getEntityPlayer().inventory.addItemStackToInventory(item.getContainerItem(equip).copy());
                evt.setUseItem(Event.Result.DENY);
            }
            evt.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void checkPlayerInventory(TickEvent.PlayerTickEvent e) {
        World world = e.player.getEntityWorld();
        if (hardcoreLavaBuckets) {
            if (world.getTotalWorldTime() % 10 == 0) {
                if (!e.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && !e.player.capabilities.isCreativeMode) {
                    IItemHandler inv = e.player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                    BlockPos pos = e.player.getPosition();
                    if (inv != null) {
                        for (int i = 0; i < inv.getSlots(); i++) {
                            ItemStack stack = inv.getStackInSlot(i);
                            if (world.rand.nextInt(50) == 0) {
                                if (!stack.isEmpty() && stack.isItemEqual(new ItemStack(Items.LAVA_BUCKET))) {
                                    IFluidHandler bucket = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                                    if (bucket != null) {
                                        bucket.drain(1000, true);
                                        world.playSound(e.player, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.PLAYERS, 1, 1);
                                        placeLavaBucket(world, pos.offset(e.player.getHorizontalFacing()), 0);
                                    } else {
                                        inv.extractItem(i, 1, false);
                                        inv.insertItem(i, new ItemStack(Items.BUCKET), false);
                                        world.playSound(e.player, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.PLAYERS, 1, 1);
                                        placeLavaBucket(world, pos.offset(e.player.getHorizontalFacing()), 0);
                                    }
                                }
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
        if (hardcoreLavaBuckets) {
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

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
