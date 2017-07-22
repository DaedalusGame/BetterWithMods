package betterwithmods.common.blocks.mechanical.tile;

import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.api.block.ISoulSensitive;
import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.client.model.filters.ModelWithResource;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.blocks.mechanical.BlockMechMachines;
import betterwithmods.common.blocks.tile.IMechSubtype;
import betterwithmods.common.blocks.tile.SimpleStackHandler;
import betterwithmods.common.blocks.tile.TileEntityVisibleInventory;
import betterwithmods.common.registry.HopperFilters;
import betterwithmods.common.registry.HopperInteractions;
import betterwithmods.util.InvUtils;
import betterwithmods.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class TileEntityFilteredHopper extends TileEntityVisibleInventory implements IMechSubtype, IMechanicalPower {

    private final int STACK_SIZE = 8;
    public int filterType;
    public byte power;
    public int soulsRetained;
    private int ejectCounter, ejectXPCounter;
    private int experienceCount, maxExperienceCount = 1000;

    public TileEntityFilteredHopper() {

        this.ejectCounter = 0;
        this.experienceCount = 0;
        this.ejectXPCounter = 10;
        this.filterType = 0;
        this.soulsRetained = 0;
        this.occupiedSlots = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("EjectCounter"))
            this.ejectCounter = tag.getInteger("EjectCounter");
        if (tag.hasKey("XPCount"))
            this.experienceCount = tag.getInteger("XPCount");
        if (tag.hasKey("FilterType"))
            this.filterType = tag.getShort("FilterType");
        if (tag.hasKey("Souls"))
            this.soulsRetained = tag.getInteger("Souls");
        if (tag.hasKey("IsPowered"))
            this.power = tag.getBoolean("IsPowered") ? (byte) 1 : 0;
        validateInventory();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        t.setInteger("EjectCounter", this.ejectCounter);
        t.setInteger("XPCount", this.experienceCount);
        t.setShort("FilterType", (short) this.filterType);
        t.setInteger("Souls", this.soulsRetained);
        t.setBoolean("IsPowered", power > 1);
        return t;
    }

    private List<EntityItem> getCollidingItems(World world, BlockPos pos) {
        return world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY() +1, pos.getZ(), pos.getX() + 1D, pos.getY() + 1.0001D, pos.getZ() + 1D), EntitySelectors.IS_ALIVE);
    }

    private List<EntityXPOrb> getCollidingXPOrbs(World world, BlockPos pos) {
        return world.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1D, pos.getY() + 1.5D, pos.getZ() + 1D));
    }

    public boolean isPowered() {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof IMechanicalBlock && ((IMechanicalBlock) state.getBlock()).isMechanicalOn(world, pos);
    }

    public boolean isXPFull() {
        return experienceCount >= maxExperienceCount;
    }

    private void insert() {
        if (!InvUtils.isFull(inventory)) {
            EntityItem item = getCollidingItems(world, pos).stream().findFirst().orElse(null);
            if (item != null) {
                if (HopperInteractions.attemptToCraft(filterType, getWorld(), getPos(), item)) {
                    this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }
                if (canFilterProcessItem(item.getItem())) {
                    if (InvUtils.insertFromWorld(inventory, item, 0, 18, false))
                        this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }
            }
        }

        if (!isXPFull() && filterType == 6) {
            List<EntityXPOrb> orbs = getCollidingXPOrbs(world, pos);
            for (EntityXPOrb orb : orbs) {
                int remaining = maxExperienceCount - experienceCount;
                int value = orb.getXpValue();
                if (remaining > 0) {
                    if (value <= remaining) {
                        this.experienceCount += value;
                        orb.setDead();
                        continue;
                    }
                    orb.xpValue -= remaining;
                    this.experienceCount = maxExperienceCount;
                }
            }
        }
    }

    private void extract() {
        Optional<IItemHandler> inv = InvUtils.getItemHandler(world, pos.down(), EnumFacing.UP);
        if (ejectCounter > 2) {
            int slot = InvUtils.getFirstOccupiedStackInRange(inventory, 0, 17);
            if (slot != -1) {
                ItemStack stack = inventory.getStackInSlot(slot);
                if (inv.isPresent()) {
                    if (InvUtils.canInsert(inv.get(), stack, STACK_SIZE)) {
                        ItemStack insert = InvUtils.insert(inv.get(), stack, STACK_SIZE, false);
                        InvUtils.consumeItemsInInventory(inventory, stack, STACK_SIZE - insert.getCount(), false);
                    }
                } else {
                    InvUtils.consumeItemsInInventory(inventory, stack, STACK_SIZE, false);
                    InvUtils.spawnStack(world, pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5, STACK_SIZE, stack);
                }
            }
            ejectCounter = 0;
        } else {
            ejectCounter++;
        }
        if (ejectXPCounter > 2) {
            if (world.getBlockState(pos.down()).getMaterial().isReplaceable()) {
                if (experienceCount > 19) {
                    experienceCount -= 20;
                    spawnEntityXPOrb(20);
                }
            }
            ejectXPCounter = 0;
        } else {
            ejectXPCounter++;
        }
    }

    @Override
    public void update() {
        boolean isPowered = isPowered();
        if ((isPowered ? 1 : 0) != power)
            this.power = (byte) (isPowered() ? 1 : 0);

        if (!this.world.isRemote && world.getBlockState(pos).getBlock() instanceof BlockMechMachines && world.getBlockState(pos).getValue(BlockMechMachines.TYPE) == BlockMechMachines.EnumType.HOPPER) {
            insert();
            if (isPowered()) {
                extract();
            }
        }
        if (this.soulsRetained > 0) {
            processSouls();
        }
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return player.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) <= 64.0D;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.getWorld() != null) {
            validateInventory();
        }
    }

    private boolean validateInventory() {
        boolean stateChanged = false;
        int currentFilter = getFilterType();
        if (currentFilter != this.filterType) {
            this.filterType = currentFilter;
            stateChanged = true;
        }
        byte slotsOccupied = (byte) InvUtils.getOccupiedStacks(inventory, 0, 17);
        if (slotsOccupied != this.occupiedSlots) {
            this.occupiedSlots = slotsOccupied;
            stateChanged = true;
        }
        if (getWorld() != null && stateChanged) {
            IBlockState state = getWorld().getBlockState(pos);
            getWorld().notifyBlockUpdate(pos, state, state, 3);
        }

        return stateChanged;
    }

    private int getFilterType() {
        ItemStack filter = getFilterStack();
        if (filter.isEmpty()) {
            return -1;
        } else {
            return (short) HopperFilters.getFilterType(filter);
        }
    }

    private boolean canFilterProcessItem(ItemStack stack) {
        validateInventory();
        if (this.filterType == -1)
            return true;
        else if (this.filterType == 0)
            return this.getFilterStack().isItemEqual(stack);
        if (this.filterType > 0) {
            if (!this.isPowered())
                return false;
            if (HopperFilters.getAllowedItems(filterType) != null)
                return HopperFilters.getAllowedItems(filterType).test(stack);
        }
        return true;
    }

    private void spawnEntityXPOrb(int value) {
        double xOff = this.getWorld().rand.nextDouble() * 0.1D + 0.45D;
        double yOff = -0.5D;
        double zOff = this.getWorld().rand.nextDouble() * 0.1D + 0.45D;
        EntityXPOrb orb = new EntityXPOrb(this.getWorld(), this.pos.getX() + xOff, this.pos.getY() + yOff, this.pos.getZ() + zOff, value);
        orb.motionX = 0.0D;
        orb.motionY = 0.0D;
        orb.motionZ = 0.0D;
        this.getWorld().spawnEntity(orb);
    }

    private void processSouls() {
        BlockPos down = pos.down();
        if (this.filterType == 6) {
            Block blockBelow = this.getWorld().getBlockState(down).getBlock();
            if (soulsRetained > 0 && blockBelow instanceof ISoulSensitive && ((ISoulSensitive) blockBelow).isSoulSensitive(getWorld(), down)) {
                int soulsConsumed = ((ISoulSensitive) blockBelow).processSouls(this.getWorld(), down, this.soulsRetained);
                if (((ISoulSensitive) blockBelow).consumeSouls(this.getWorld(), down, soulsConsumed))
                    this.soulsRetained -= soulsConsumed;
            } else if (soulsRetained > 7 && !isPowered()) {
                if (WorldUtils.spawnGhast(world, pos))
                    this.getWorld().playSound(null, this.pos, SoundEvents.ENTITY_GHAST_SCREAM, SoundCategory.BLOCKS, 1.0F, getWorld().rand.nextFloat() * 0.1F + 0.8F);
                //TODO
//                if (getWorld().getBlockState(pos).getBlock() == BWMBlocks.SINGLE_MACHINES)
//                    ((BlockMechMachines) getWorld().getBlockState(pos).getBlock()).breakHopper(getWorld(), pos);
            }
        } else {
            this.soulsRetained = 0;
        }
    }

    public void increaseSoulCount(int numSouls) {
        this.soulsRetained += numSouls;
    }

    @Override
    public int getInventorySize() {
        return 19;
    }

    @Override
    public SimpleStackHandler createItemStackHandler() {
        return new HopperHandler(getInventorySize(), this);
    }

    @Override
    public String getName() {
        return "inv.filtered_hopper.name";
    }

    @Override
    public int getSubtype() {
        return this.filterType;
    }

    @Override
    public void setSubtype(int type) {
        this.filterType = (short) Math.min(type, 7);
    }

    @Override
    public int getMaxVisibleSlots() {
        return 18;
    }

    public ModelWithResource getModel() {
        return RenderUtils.getModelFromStack(getFilterStack());
    }

    public ItemStack getFilterStack() {
        return inventory.getStackInSlot(18);
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 4;
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 4;
    }

    private class HopperHandler extends SimpleStackHandler {
        TileEntityFilteredHopper hopper;

        public HopperHandler(int size, TileEntityFilteredHopper hopper) {
            super(size, hopper);
            this.hopper = hopper;
        }

        @Override
        public void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            world.markBlockRangeForRenderUpdate(pos, pos);
            getWorld().notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
        }

        @Override
        public int getSlotLimit(int slot) {
            return slot == 18 ? 1 : super.getSlotLimit(slot);
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        return capability == CapabilityMechanicalPower.MECHANICAL_POWER
                || super.hasCapability(capability, facing);
    }

    @Nonnull
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityMechanicalPower.MECHANICAL_POWER)
            return CapabilityMechanicalPower.MECHANICAL_POWER.cast(this);
        return super.getCapability(capability, facing);
    }

}
