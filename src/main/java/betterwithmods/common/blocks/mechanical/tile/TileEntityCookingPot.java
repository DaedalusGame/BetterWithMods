package betterwithmods.common.blocks.mechanical.tile;

import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.blocks.mechanical.BlockCookingPot;
import betterwithmods.common.blocks.tile.TileEntityVisibleInventory;
import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.common.registry.heat.BWMHeatSource;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;


public abstract class TileEntityCookingPot extends TileEntityVisibleInventory implements IMechanicalPower {
    public int cookCounter;
    public int stokedCooldownCounter;
    public int scaledCookCounter;
    public boolean containsValidIngredients;
    public int fireIntensity;
    public EnumFacing facing;
    protected CraftingManagerBulk unstoked, stoked;
    private boolean forceValidation;

    public TileEntityCookingPot(CraftingManagerBulk unstoked, CraftingManagerBulk stoked) {
        this.unstoked = unstoked;
        this.stoked = stoked;
        this.cookCounter = 0;
        this.containsValidIngredients = false;
        this.forceValidation = true;
        this.scaledCookCounter = 0;
        this.fireIntensity = -1;
        this.occupiedSlots = 0;
        this.facing = EnumFacing.UP;
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityMechanicalPower.MECHANICAL_POWER || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityMechanicalPower.MECHANICAL_POWER) {
            return CapabilityMechanicalPower.MECHANICAL_POWER.cast(this);
        }
        return super.getCapability(capability, facing);
    }

    private boolean isInputtingPower(EnumFacing facing) {
        return this.getMechanicalInput(facing) > 0;
    }

    private EnumFacing getPoweredSide() {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (isInputtingPower(facing))
                return facing;
        }
        return null;
    }

    private boolean isPowered() {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (isInputtingPower(facing))
                return true;
        }
        return false;
    }

    protected boolean isStoked() {
        return fireIntensity > 4;
    }

    @Override
    public int getInventorySize() {
        return 27;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.fireIntensity = tag.hasKey("fireIntensity") ? tag.getInteger("fireIntensity") : -1;
        this.facing = tag.hasKey("facing") ? EnumFacing.getFront(tag.getInteger("facing")) : EnumFacing.UP;
        if (tag.hasKey("CookTime"))
            this.cookCounter = tag.getInteger("CookTime");
        if (tag.hasKey("ContainsValidIngredients"))
            this.containsValidIngredients = tag.getBoolean("ContainsValidIngredients");
        if (tag.hasKey("StokedCooldown"))
            this.stokedCooldownCounter = tag.getInteger("StokedCooldown");
        validateInventory();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        t.setInteger("fireIntensity", this.fireIntensity);
        t.setInteger("facing", facing.getIndex());
        t.setInteger("CookTime", this.cookCounter);
        t.setInteger("StokedCooldown", this.stokedCooldownCounter);
        t.setBoolean("ContainsValidIngredients", this.containsValidIngredients);
        return t;
    }


    @Override
    public void update() {
        if (this.getWorld().isRemote)
            return;
        if (this.getWorld().getBlockState(this.pos).getBlock() instanceof BlockCookingPot) {
            IBlockState state = this.getWorld().getBlockState(this.pos);
            if (this.fireIntensity != getFireIntensity()) {
                validateFireIntensity();
                this.forceValidation = true;
            }
            if (!isPowered()) {
                this.facing = EnumFacing.UP;
                entityCollision();
                if (this.fireIntensity > 0) {
                    if (this.forceValidation) {
                        validateContents();
                        this.forceValidation = false;
                    }
                    if (this.fireIntensity > 4) {
                        if (this.stokedCooldownCounter < 1)
                            this.cookCounter = 0;
                        this.stokedCooldownCounter = 20;
                        performStokedFireUpdate(getCurrentFireIntensity());
                    } else if (this.stokedCooldownCounter > 0) {
                        this.stokedCooldownCounter -= 1;

                        if (this.stokedCooldownCounter < 1) {
                            this.cookCounter = 0;
                        }
                    } else if (this.stokedCooldownCounter == 0 && this.fireIntensity > 0 && this.fireIntensity < 5)
                        performNormalFireUpdate(getCurrentFireIntensity());
                } else
                    this.cookCounter = 0;
            } else {
                this.cookCounter = 0;
                this.facing = getPoweredSide();
                ejectInventory(DirUtils.rotateFacingAroundY(this.facing, false));
            }

            if (facing != state.getValue(DirUtils.TILTING)) {
                world.setBlockState(pos, state.withProperty(DirUtils.TILTING, facing));
            }


        }
        validateInventory();
        this.scaledCookCounter = this.cookCounter * 1000 / 4350;
    }

    private void entityCollision() {
        if (captureDroppedItems()) {
            getWorld().scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(getWorld()), 5);
            this.markDirty();
        }
    }

    public List<EntityItem> getCaptureItems(World worldIn, BlockPos pos) {
        return worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1D, pos.getY() + 1.5D, pos.getZ() + 1D), EntitySelectors.IS_ALIVE);
    }

    private boolean captureDroppedItems() {
        boolean insert = false;
        if (!InvUtils.isFull(inventory)) {
            List<EntityItem> items = this.getCaptureItems(getWorld(), getPos());
            for (EntityItem item : items)
                insert &= InvUtils.insertFromWorld(inventory, item, 0, 18, false);
        }
        if (insert) {
            this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            if (this.validateInventory()) {
                getWorld().scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(getWorld()), 5);
            }
            return true;
        }
        return false;
    }

    public void ejectInventory(EnumFacing facing) {
        int index = InvUtils.getFirstOccupiedStackNotOfItem(inventory, Items.BRICK);
        if (index >= 0 && index < inventory.getSlots()) {

            ItemStack stack = inventory.getStackInSlot(index);
            int ejectStackSize = 8;
            if (8 > stack.getCount()) {
                ejectStackSize = stack.getCount();
            }
            BlockPos target = pos.offset(facing);
            ItemStack eject = new ItemStack(stack.getItem(), ejectStackSize, stack.getItemDamage());
            InvUtils.copyTags(eject, stack);
            IBlockState targetState = getWorld().getBlockState(target);
            boolean ejectIntoWorld = getWorld().isAirBlock(target) || targetState.getBlock().isReplaceable(getWorld(), target) || !targetState.getMaterial().isSolid() || targetState.getBoundingBox(getWorld(), target).maxY < 0.5d;
            if (ejectIntoWorld) {
                this.getWorld().playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.2F, ((getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                ejectStack(getWorld(), target, facing, eject);
                inventory.extractItem(index, ejectStackSize, false);
            }
        }
    }

    public void ejectStack(World world, BlockPos pos, EnumFacing facing, ItemStack stack) {
        Vec3i vec = new BlockPos(0, 0, 0).offset(facing);
        EntityItem item = new EntityItem(world, pos.getX() + 0.5F - (vec.getX() / 4d), pos.getY() + 0.25D, pos.getZ() + 0.5D - (vec.getZ() / 4d), stack);
        float velocity = 0.05F;
        item.motionX = (double) (vec.getX() * velocity);
        item.motionY = vec.getY() * velocity * 0.1;
        item.motionZ = (double) (vec.getZ() * velocity);
        item.setDefaultPickupDelay();
        world.spawnEntity(item);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.forceValidation = true;
        if (this.getWorld() != null) {
            validateInventory();
        }
    }


    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.getWorld().getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public abstract boolean validateStoked();

    public abstract boolean validateUnstoked();

    public void validateContents() {
        this.containsValidIngredients = false;
        if (!isStoked()) {
            containsValidIngredients = validateUnstoked();
        } else {
            containsValidIngredients = validateStoked();
        }
    }

    protected CraftingManagerBulk getCraftingManager(boolean stoked) {
        return stoked ? this.stoked : unstoked;
    }

    public int getCurrentFireIntensity() {
        int fireFactor = 0;

        if (this.fireIntensity > 0) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    int yPos = -1;
                    BlockPos target = pos.add(x, yPos, z);
                    Block block = this.getWorld().getBlockState(target).getBlock();
                    int meta = this.getWorld().getBlockState(target).getBlock().damageDropped(this.getWorld().getBlockState(target));
                    if (BWMHeatRegistry.get(block, meta) != null)
                        fireFactor += BWMHeatRegistry.get(block, meta).value;
                }
            }
            if (fireFactor < 5)
                fireFactor = 5;
        }

        return fireFactor;
    }

    private void performNormalFireUpdate(int fireIntensity) {
        if (this.containsValidIngredients) {
            this.cookCounter += fireIntensity;

            if (this.cookCounter >= 4350) {
                attemptToCookNormal();
                this.cookCounter = 0;
            }
        } else
            this.cookCounter = 0;
    }

    private void performStokedFireUpdate(int fireIntensity) {
        if (this.containsValidIngredients) {
            this.cookCounter += fireIntensity;

            if (this.cookCounter >= 4350) {
                attemptToCookStoked();
                this.cookCounter = 0;
            }
        } else
            this.cookCounter = 0;
    }


    private boolean containsItem(Item item) {
        return containsItem(item, OreDictionary.WILDCARD_VALUE);
    }

    private boolean containsItem(Item item, int meta) {
        return InvUtils.getFirstOccupiedStackOfItem(inventory, item) > -1;
    }

    protected boolean attemptToCookNormal() {
        return attemptToCookWithManager(getCraftingManager(false));
    }

    protected boolean attemptToCookStoked() {
        return attemptToCookWithManager(getCraftingManager(true));
    }

    private boolean attemptToCookWithManager(CraftingManagerBulk man) {
        if (man != null) {
            if (man.getCraftingResult(inventory) != null) {
                NonNullList<ItemStack> output = man.craftItem(world, this, inventory);
                if (!output.isEmpty()) {
                    for (ItemStack out : output) {
                        if (!out.isEmpty()) {
                            ItemStack stack = out.copy();
                            if (!InvUtils.insert(inventory, stack, false).isEmpty())
                                InvUtils.ejectStackWithOffset(this.getWorld(), this.pos.up(), stack);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public int getCookingProgressScaled(int scale) {
        return this.scaledCookCounter * scale / 1000;
    }

    public boolean isCooking() {
        return this.scaledCookCounter > 0;
    }

    private boolean validateInventory() {
        boolean stateChanged = false;
        byte currentSlots = (byte) InvUtils.getOccupiedStacks(inventory);
        if (currentSlots != this.occupiedSlots) {
            this.occupiedSlots = currentSlots;
            stateChanged = true;
        }
        if (getWorld() != null && stateChanged) {
            IBlockState state = getWorld().getBlockState(pos);
            getWorld().notifyBlockUpdate(pos, state, state, 3);
        }

        return stateChanged;
    }

    public int getFireIntensity() {
        BlockPos down = pos.down();
        Block block = this.getWorld().getBlockState(down).getBlock();
        int meta = block.damageDropped(this.getWorld().getBlockState(down));
        BWMHeatSource source = BWMHeatRegistry.get(block, meta);
        if (source != null)
            return source.value;
        return -1;
    }

    private void validateFireIntensity() {
        BlockPos down = pos.down();
        Block block = this.getWorld().getBlockState(down).getBlock();
        int meta = block.damageDropped(this.getWorld().getBlockState(down));
        BWMHeatSource source = BWMHeatRegistry.get(block, meta);
        if (source != null)
            fireIntensity = source.value;
        else
            fireIntensity = -1;
    }

    @Override
    public int getMaxVisibleSlots() {
        return 27;
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        return MechanicalUtil.getPowerOutput(world, pos.offset(facing), facing.getOpposite());
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 1;
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }
}
