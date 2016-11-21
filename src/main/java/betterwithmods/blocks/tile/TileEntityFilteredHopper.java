package betterwithmods.blocks.tile;

import betterwithmods.BWMBlocks;
import betterwithmods.api.block.ISoulSensitive;
import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.client.model.filters.ModelWithResource;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.craft.HopperFilters;
import betterwithmods.craft.HopperInteractions;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class TileEntityFilteredHopper extends TileEntityVisibleInventory implements IMechSubtype {
    private final ItemStack filterStack;
    public short filterType;
    public boolean outputBlocked;
    public byte power;
    private int ejectCounter;
    private int containedXP;
    private int xpDropDelay;
    public int soulsRetained;
    private String filter;

    public TileEntityFilteredHopper() {

        this.ejectCounter = 0;
        this.containedXP = 0;
        this.xpDropDelay = 10;
        this.outputBlocked = false;
        this.filterType = 0;
        this.soulsRetained = 0;
        this.occupiedSlots = 0;
        this.filterStack = null;
        this.filter = "";
    }

    public static ItemStack attemptToInsert(IItemHandler inv, ItemStack stack) {
        ItemStack leftover = null;
        for (int slot = 0; slot < inv.getSlots() - 1; slot++) {
            leftover = inv.insertItem(slot, stack, false);
            if (leftover == null)
                break;
        }
        return leftover;
    }

    public static boolean putDropInInventoryAllSlots(IItemHandler inv, EntityItem entityItem) {
        boolean putAll = false;
        if (entityItem == null) {
            return false;
        } else {
            ItemStack itemstack = entityItem.getEntityItem().copy();
            ItemStack leftovers = attemptToInsert(inv, itemstack);
            if (leftovers != null && leftovers.stackSize != 0) {
                entityItem.setEntityItemStack(leftovers);
            } else {
                putAll = true;
                entityItem.setDead();
            }
            return putAll;
        }
    }

    private static List<EntityXPOrb> getCollidingXPOrbs(World world, BlockPos pos) {
        return world.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(pos.getX() - 0.5D, pos.getY() - 0.5D, pos.getZ() - 0.5D, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D));
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("EjectCounter"))
            this.ejectCounter = tag.getInteger("EjectCounter");
        if (tag.hasKey("XPCount"))
            this.containedXP = tag.getInteger("XPCount");
        if (tag.hasKey("FilterType"))
            this.filterType = tag.getShort("FilterType");
        if (tag.hasKey("Souls"))
            this.soulsRetained = tag.getInteger("Souls");
        if (tag.hasKey("IsPowered"))
            this.power = tag.getBoolean("IsPowered") ? (byte) 1 : 0;
        if (tag.hasKey("FilterType"))
            this.filter = tag.getString("FilterType");

        validateInventory();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        t.setInteger("EjectCounter", this.ejectCounter);
        t.setInteger("XPCount", this.containedXP);
        t.setShort("FilterType", this.filterType);
        t.setInteger("Souls", this.soulsRetained);
        t.setBoolean("IsPowered", power > 1);
        t.setString("FilterType", filter);
        return t;
    }

    @Override
    public void update() {
        if (this.worldObj.isRemote)
            return;
        if (!(this.worldObj.getBlockState(this.pos).getBlock() instanceof BlockMechMachines))
            return;

        boolean isOn = false;
        if (worldObj.getBlockState(pos).getBlock() instanceof BlockMechMachines)
            isOn = ((BlockMechMachines) BWMBlocks.SINGLE_MACHINES).isMechanicalOn(this.worldObj, pos);
        entityCollision();
        if (isOn) {
            attemptToEjectXPFromInv();

            if (!this.outputBlocked) {
                this.ejectCounter += 1;
                if (this.ejectCounter > 2) {
                    attemptToEjectStackFromInv();
                    this.ejectCounter = 0;
                }
            } else
                this.ejectCounter = 0;
        } else {
            this.ejectCounter = 0;
            this.xpDropDelay = 0;
        }
        if (this.soulsRetained > 0)
            processSouls();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.worldObj != null) {
            this.outputBlocked = false;
            validateInventory();

            setFilter(getFilterType());
        }
    }

    private void setFilter(short filter) {
        this.filterType = filter;
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return player.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) <= 64.0D;
    }

    private boolean validateInventory() {
        boolean stateChanged = false;

        short currentFilter = getFilterType();

        if (currentFilter != this.filterType) {
            this.filterType = currentFilter;
            stateChanged = true;
            if (hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
                ItemStack stack = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP).getStackInSlot(18);
                if (stack != null) {
                    String check = stack.getItem().toString() + stack.getMetadata();
                    if (!filter.equals(check)) {
                        filter = check;
                        markDirty();
                    }
                } else {
                    filter = "";
                    markDirty();
                }
            }
        }

        byte slotsOccupied = (byte) InvUtils.getOccupiedStacks(inventory, 0, 17);
        if (slotsOccupied != this.occupiedSlots) {
            this.occupiedSlots = slotsOccupied;
            stateChanged = true;
        }
        if (worldObj != null && stateChanged) {
            IBlockState state = worldObj.getBlockState(pos);
            worldObj.notifyBlockUpdate(pos, state, state, 3);
        }

        return stateChanged;
    }

    private short getFilterType() {
        ItemStack filter = inventory.getStackInSlot(18);
        if (filter != null && filter.stackSize > 0)
            return (short) HopperFilters.getFilterType(filter);
        return 0;
    }

    private boolean canFilterProcessItem(ItemStack stack) {
        if (this.filterType > 0) {
            if(HopperFilters.getAllowedItems(filterType) != null)
                return HopperFilters.getAllowedItems(filterType).test(stack);
        }
        return true;
    }

    private void entityCollision() {
        boolean flag = false;
        if (!isFull()) {
            flag = captureDroppedItems();
        }
        if (!isXPFull()) {
            if (captureXP())
                worldObj.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.45F);
            flag = captureXP() || flag;
        }
        if (flag) {
            worldObj.scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(worldObj), 5);//this.getWorld().markBlockForUpdate(this.getPos());
            this.markDirty();
        }
    }

    public List<EntityItem> getCaptureItems(World worldIn, BlockPos pos) {
        return worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1D, pos.getY() + 1.5D, pos.getZ() + 1D), EntitySelectors.IS_ALIVE);
    }

    private boolean captureDroppedItems() {
        List<EntityItem> items = this.getCaptureItems(worldObj, getPos());
        if (items.size() > 0) {
            boolean flag = false;
            for (EntityItem item : items) {
                ItemStack stack = item.getEntityItem();
                if(HopperInteractions.attemptToCraft(filterType(),worldObj,getPos(),item))
                    this.worldObj.playSound((EntityPlayer)null, pos.getX(), pos.getY(),pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                if (this.canFilterProcessItem(stack)) {
                    flag = putDropInInventoryAllSlots(inventory, item) || flag;
                    this.worldObj.playSound((EntityPlayer)null, pos.getX(), pos.getY(),pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }

            }
            if (flag) {
                this.worldObj.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                if (this.validateInventory()) {
                    worldObj.scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(worldObj), 5);//worldObj.markBlockForUpdate(pos);
                }
                return true;
            }
        }
        return false;
    }

    private boolean captureXP() {
        List<EntityXPOrb> xpOrbs = getCollidingXPOrbs(worldObj, getPos());
        if (xpOrbs.size() > 0 && filterType() == 6) {
            boolean flag = false;
            for (EntityXPOrb orb : xpOrbs) {
                flag = this.attemptToSwallowXPOrb(this.getWorld(), pos, orb) || flag;
            }
            if (flag)
                return true;
        }
        return false;
    }

    private boolean isXPFull() {
        return this.containedXP > 99;
    }

    private boolean isFull() {
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            ItemStack itemstack = this.inventory.getStackInSlot(i);
            if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    private void attemptToEjectStackFromInv() {
        int stackIndex = InvUtils.getFirstOccupiedStackInRange(inventory, 0, 17);

        if (stackIndex > -1 && stackIndex < 18) {
            ItemStack invStack = inventory.getStackInSlot(stackIndex);
            int ejectStackSize = 8;
            if (8 > invStack.stackSize)
                ejectStackSize = invStack.stackSize;

            ItemStack ejectStack = new ItemStack(invStack.getItem(), ejectStackSize, invStack.getItemDamage());

            InvUtils.copyTags(ejectStack, invStack);

            BlockPos down = pos.down();

            boolean ejectIntoWorld = false;

            if (this.worldObj.isAirBlock(down))
                ejectIntoWorld = true;
            else if (this.worldObj.getBlockState(down).getBlock().isReplaceable(this.worldObj, down))
                ejectIntoWorld = true;
            else {
                Block block = this.worldObj.getBlockState(down).getBlock();

                if (block == null || (!block.isBlockSolid(this.worldObj, down, EnumFacing.UP) && (this.worldObj.getTileEntity(down) == null || !(this.worldObj.getTileEntity(down).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)))))
                    ejectIntoWorld = true;
                else {
                    TileEntity tile = this.worldObj.getTileEntity(down);
                    if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
                        IItemHandler below = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
                        ItemStack leftover;
                        for (int slot = 0; slot < below.getSlots(); slot++) {
                            leftover = below.insertItem(slot, ejectStack, false);
                            if (leftover == null) {
                                inventory.extractItem(stackIndex, ejectStackSize, false);
                                break;
                            }
                        }


                    } else if (tile != null) {
                        if (InvUtils.addItemStackToInv(inventory, ejectStack))
                            inventory.extractItem(stackIndex, ejectStackSize, false);
                    } else
                        this.outputBlocked = true;
                }
            }
            if (ejectIntoWorld) {
                List<EntityMinecart> carts = this.worldObj.getEntitiesWithinAABB(EntityMinecart.class, new AxisAlignedBB(pos.getX() + 0.4F, pos.getY() - 0.5F, pos.getZ() + 0.4F, pos.getX() + 0.6F, pos.getY(), pos.getZ() + 0.6F));
                if (carts != null && carts.size() > 0) {
                    for (EntityMinecart cart : carts) {
                        if (cart.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                            IItemHandler items = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                            int itemsStored;
                            if (InvUtils.addItemStackToInv(items, ejectStack))
                                itemsStored = ejectStackSize;
                            else
                                itemsStored = ejectStackSize - ejectStack.stackSize;
                            if (itemsStored > 0) {
                                inventory.extractItem(stackIndex, itemsStored, false);
                                this.worldObj.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            }
                            ejectIntoWorld = false;
                            break;

                        }
                    }
                }
            }

            if (ejectIntoWorld) {
                ejectStack(ejectStack);
                inventory.extractItem(stackIndex, ejectStackSize, false);
                if (validateInventory())
                    worldObj.scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(worldObj), 5);//this.worldObj.markBlockForUpdate(pos);
            }
        }
    }

    private void ejectStack(ItemStack stack) {
        float xOff = this.worldObj.rand.nextFloat() * 0.1F + 0.45F;
        float yOff = -0.35F;
        float zOff = this.worldObj.rand.nextFloat() * 0.1F + 0.45F;

        EntityItem item = new EntityItem(this.worldObj, pos.getX() + xOff, pos.getY() + yOff, pos.getZ() + zOff, stack);

        item.motionX = 0.0D;
        item.motionY = -0.009999999776482582D;
        item.motionZ = 0.0D;
        item.setDefaultPickupDelay();
        this.worldObj.spawnEntityInWorld(item);
    }

    private void attemptToEjectXPFromInv() {
        boolean shouldResetEjectCount = true;
        if (this.containedXP > 19) {
            BlockPos down = pos.down();

            boolean canEjectIntoWorld = false;
            if (this.worldObj.isAirBlock(down))
                canEjectIntoWorld = true;
            else {
                Block block = this.worldObj.getBlockState(down).getBlock();
                int meta = block.damageDropped(this.worldObj.getBlockState(down));
                if (block instanceof BlockMechMachines && (meta == 4 || meta == 12))
                    shouldResetEjectCount = attemptToEjectXPIntoHopper(down);
                else if (block.isReplaceable(this.worldObj, down))
                    canEjectIntoWorld = true;
                else if (!worldObj.getBlockState(down).getMaterial().isSolid())
                    canEjectIntoWorld = true;
            }

            if (canEjectIntoWorld) {
                if (this.xpDropDelay < 1) {
                    ejectXPOrb(20);
                    this.containedXP -= 20;
                } else
                    shouldResetEjectCount = false;
            }
        }
        if (shouldResetEjectCount)
            resetXPEjectCount();
        else
            this.xpDropDelay -= 1;
    }

    private boolean attemptToEjectXPIntoHopper(BlockPos pos) {
        TileEntityFilteredHopper tile = (TileEntityFilteredHopper) this.worldObj.getTileEntity(pos);
        if (tile != null) {
            int filterType = tile.filterType;

            if (filterType == 6) {
                int spaceRemaining = 100 - tile.containedXP;

                if (spaceRemaining > 0) {
                    if (this.xpDropDelay < 1) {
                        int xpEjected = 20;
                        if (spaceRemaining < xpEjected)
                            xpEjected = spaceRemaining;

                        tile.containedXP += xpEjected;
                        this.containedXP -= xpEjected;
                    } else
                        return false;
                }
            }
        }
        return true;
    }

    private void resetXPEjectCount() {
        this.xpDropDelay = 10 + this.worldObj.rand.nextInt(3);
    }

    private void ejectXPOrb(int value) {
        double xOff = this.worldObj.rand.nextDouble() * 0.1D + 0.45D;
        double yOff = -0.2D;
        double zOff = this.worldObj.rand.nextDouble() * 0.1D + 0.45D;
        EntityXPOrb orb = new EntityXPOrb(this.worldObj, this.pos.getX() + xOff, this.pos.getY() + yOff, this.pos.getZ() + zOff, value);

        orb.motionX = 0.0D;
        orb.motionY = 0.0D;
        orb.motionZ = 0.0D;

        this.worldObj.spawnEntityInWorld(orb);
    }

    private boolean attemptToSwallowXPOrb(World world, BlockPos pos, EntityXPOrb entity) {
        int remainingSpace = 100 - this.containedXP;

        if (remainingSpace > 0) {
            if (entity.xpValue > 0) {
                if (entity.xpValue <= remainingSpace) {
                    this.containedXP += entity.xpValue;
                    entity.setDead();
                    return true;
                }

                entity.xpValue -= remainingSpace;
                this.containedXP = 100;
            }
        }
        return false;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = this.getUpdateTag();
        return new SPacketUpdateTileEntity(this.pos, this.getBlockMetadata(), tag);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager mgr, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.getNbtCompound();
        this.readFromNBT(tag);
        IBlockState state = worldObj.getBlockState(this.pos);
        this.worldObj.notifyBlockUpdate(this.pos, state, state, 3);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    private void processSouls() {
        boolean isOn = ((BlockMechMachines) BWMBlocks.SINGLE_MACHINES).isMechanicalOn(this.worldObj, this.pos);
        BlockPos down = pos.down();
        if (this.filterType == 6) {
            Block blockBelow = this.worldObj.getBlockState(down).getBlock();
            if (soulsRetained > 0 && blockBelow instanceof ISoulSensitive && ((ISoulSensitive) blockBelow).isSoulSensitive(worldObj, down)) {
                int soulsConsumed = ((ISoulSensitive) blockBelow).processSouls(this.worldObj, down, this.soulsRetained);
                if (((ISoulSensitive) blockBelow).consumeSouls(this.worldObj, down, soulsConsumed))
                    this.soulsRetained -= soulsConsumed;
            } else if (isOn)
                this.soulsRetained = 0;
            else if (soulsRetained > 7) {
                if (spawnGhast())
                    this.worldObj.playSound(null, this.pos, SoundEvents.ENTITY_GHAST_SCREAM, SoundCategory.BLOCKS, 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.8F);
                if (worldObj.getBlockState(pos).getBlock() == BWMBlocks.SINGLE_MACHINES)
                    ((BlockMechMachines) worldObj.getBlockState(pos).getBlock()).breakHopper(worldObj, pos);
            }
        } else
            this.soulsRetained = 0;
    }

    private boolean spawnGhast() {
        EntityGhast ghast = new EntityGhast(this.worldObj);

        for (int i = 0; i < 200; i++) {
            double xPos = pos.getX() + (this.worldObj.rand.nextDouble() - this.worldObj.rand.nextDouble()) * 10.0D;
            double yPos = pos.getY() + this.worldObj.rand.nextInt(21) - 10;
            double zPos = pos.getZ() + (this.worldObj.rand.nextDouble() - this.worldObj.rand.nextDouble()) * 10.0D;

            ghast.setLocationAndAngles(xPos, yPos, zPos, this.worldObj.rand.nextFloat() * 360.0F, 0.0F);

            if (ghast.getCanSpawnHere()) {
                this.worldObj.spawnEntityInWorld(ghast);
                return true;
            }
        }
        return false;
    }

    public void increaseSoulCount(int numSouls) {
        this.soulsRetained += numSouls;
    }

    @Override
    public SimpleItemStackHandler createItemStackHandler() {
        return new SimpleItemStackHandler(this, true, 19);
    }

    @Override
    public String getName() {
        return "inv.filteredhopper.name";
    }

    @Override
    public int getSubtype() {
        return this.filterType;
    }

    @Override
    public void setSubtype(int type) {
        this.filterType = (short) Math.min(type, 7);
    }

    public int filterType() {
        return this.filterType;
    }

    @Override
    public int getMaxVisibleSlots() {
        return 18;
    }

    public ItemStack getFilterStack() {
        return filterStack;
    }

    public ModelWithResource getModel() {
        return RenderUtils.getModelFromStack(filter);
    }
}
