package betterwithmods.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InvUtils {

    public static <T> NonNullList<T> asList(T... array) {
        NonNullList<T> nonNullList = NonNullList.create();
        if (array != null)
            nonNullList.addAll(Arrays.stream(array).filter(e -> e != null).collect(Collectors.toList()));
        return nonNullList;
    }

    public static <T> NonNullList<T> asList(List<T> list) {
        NonNullList<T> nonNullList = NonNullList.create();
        if (list != null)
            nonNullList.addAll(list.stream().filter(e -> e != null).collect(Collectors.toList()));
        return nonNullList;
    }

    public static boolean usePlayerItemStrict(EntityPlayer player, EnumFacing inv, ItemStack stack, int amount) {
        IItemHandlerModifiable inventory = (IItemHandlerModifiable) player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inv);
        if (inventory != null) {
            return consumeItemsInInventoryStrict(inventory, stack, amount, false);
        }
        return false;
    }

    public static boolean usePlayerItem(EntityPlayer player, EnumFacing inv, ItemStack stack, int amount) {
        IItemHandlerModifiable inventory = (IItemHandlerModifiable) player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inv);
        if (inventory != null) {
            return consumeItemsInInventory(inventory, stack, amount, false);
        }
        return false;
    }


    public static Optional<IItemHandler> getItemHandler(World world, BlockPos pos, EnumFacing facing) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null) {
                return Optional.ofNullable(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing));
            } else {
                List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), entity -> entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing));
                Optional<Entity> entity = entities.stream().findFirst();
                if (entity.isPresent()) {
                    return Optional.ofNullable(entity.get().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing));
                }
            }
        }
        return Optional.ofNullable(null);
    }

    public static void ejectInventoryContents(World world, BlockPos pos, IItemHandler inv) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            ejectStackWithOffset(world, pos, inv.getStackInSlot(i));
        }
    }

    public static void clearInventory(IItemHandlerModifiable inv) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                inv.setStackInSlot(i, ItemStack.EMPTY);
            }
        }

    }

    public static void copyTags(ItemStack destStack, ItemStack sourceStack) {
        if (sourceStack.hasTagCompound()) {
            destStack.setTagCompound(sourceStack.getTagCompound().copy());
        }

    }

    public static ItemStack decrStackSize(IItemHandlerModifiable inv, int slot, int amount) {
        if (!inv.getStackInSlot(slot).isEmpty()) {
            ItemStack splitStack;
            if (inv.getStackInSlot(slot).getCount() <= amount) {
                splitStack = inv.getStackInSlot(slot);
                inv.setStackInSlot(slot, ItemStack.EMPTY);
                return splitStack;
            } else {
                splitStack = inv.getStackInSlot(slot).splitStack(amount);
                if (inv.getStackInSlot(slot).getCount() < 1) {
                    inv.setStackInSlot(slot, ItemStack.EMPTY);
                }
                return splitStack;
            }
        } else {
            return null;
        }
    }

    private static boolean canInsertStack(IItemHandler inv, ItemStack stack, int minSlot, int maxSlot) {
        return insert(inv, stack, minSlot, maxSlot, true);
    }

    public static boolean insertSingle(IItemHandler inv, ItemStack stack, boolean simulate) {
        return insert(inv, stack, 1, simulate);
    }

    public static boolean insert(IItemHandler inv, ItemStack stack, int count, boolean simulate) {
        ItemStack copy = stack.copy();
        if (copy.getCount() > count) {
            copy.setCount(count);
        }
        return insert(inv, copy, simulate);
    }

    public static void insert(IItemHandler inv, NonNullList<ItemStack> stacks, boolean simulate) {
        stacks.forEach(stack -> insert(inv, stack, 0, inv.getSlots(), simulate));
    }

    public static boolean insert(IItemHandler inv, ItemStack stack, boolean simulate) {
        return insert(inv, stack, 0, inv.getSlots(), simulate);
    }

    public static boolean insert(IItemHandler inv, ItemStack stack, int minSlot, int maxSlot, boolean simulate) {
        return attemptInsert(inv, stack, minSlot, maxSlot, simulate).isEmpty();
    }

    private static ItemStack attemptInsert(IItemHandler inv, ItemStack stack, int minSlot, int maxSlot, boolean simulate) {
        if (isFull(inv))
            return stack;
        ItemStack leftover = ItemStack.EMPTY;
        for (int slot = minSlot; slot < maxSlot; slot++) {
            leftover = inv.insertItem(slot, stack, simulate);
            if (leftover.isEmpty())
                break;
        }
        return leftover;
    }

    public static boolean insertFromWorld(IItemHandler inv, EntityItem entity, int minSlot, int maxSlot, boolean simulate) {
        ItemStack stack = entity.getEntityItem().copy();
        ItemStack leftover = attemptInsert(inv, stack, minSlot, maxSlot, simulate);
        if (leftover.isEmpty()) {
            entity.setDead();
            return true;
        } else {
            entity.setEntityItemStack(leftover);
            return false;
        }
    }

    public static boolean isFull(IItemHandler inv) {
        boolean full = true;
        for (int slot = 0; slot < inv.getSlots(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if (stack.getCount() != inv.getSlotLimit(slot)) {
                full = false;
                break;
            }
        }
        return full;
    }


    public static int getFirstOccupiedStackInRange(IItemHandler inv, int minSlot, int maxSlot) {
        for (int slot = minSlot; slot <= maxSlot; ++slot) {
            if (!inv.getStackInSlot(slot).isEmpty()) {
                return slot;
            }
        }
        return -1;
    }

    public static int getFirstEmptyStackInRange(IItemHandler inv, int minSlot, int maxSlot) {
        for (int slot = minSlot; slot <= maxSlot; ++slot) {
            if (inv.getStackInSlot(slot).isEmpty()) {
                return slot;
            }
        }

        return -1;
    }

    public static int getOccupiedStacks(IItemHandler inv) {
        return getOccupiedStacks(inv, 0, inv.getSlots() - 1);
    }

    public static int getOccupiedStacks(IItemHandler inv, int min, int max) {
        int count = 0;

        for (int i = min; i <= max; ++i) {
            if (inv.getStackInSlot(i).isEmpty()) {
                ++count;
            }
        }

        return count;
    }

    public static int countItemStacksInInventory(IItemHandler inv, ItemStack toCheck) {
        int itemCount = 0;
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (ItemStack.areItemsEqual(toCheck, stack) || (toCheck.getItem() == stack.getItem() && toCheck.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                    if (toCheck.hasTagCompound()) {
                        if (ItemStack.areItemStackTagsEqual(toCheck, stack))
                            itemCount += stack.getCount();
                    } else
                        itemCount += stack.getCount();
                }
            }
        }
        return itemCount;
    }

    public static int countItemsInInventory(IItemHandler inv, Item item) {
        return countItemsInInventory(inv, item, OreDictionary.WILDCARD_VALUE);
    }

    public static int countItemsInInventory(IItemHandler inv, Item item, int meta) {
        int itemCount = 0;
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == item) {
                    if ((meta == OreDictionary.WILDCARD_VALUE) || (stack.getItemDamage() == meta)) {
                        itemCount += inv.getStackInSlot(i).getCount();
                    }
                }
            }
        }
        return itemCount;
    }

    public static int countOresInInventory(IItemHandler inv, List<ItemStack> list) {
        int ret = 0;
        if (list != null && !list.isEmpty() && list.size() > 0) {
            for (ItemStack oreStack : list) {
                ret += countItemStacksInInventory(inv, oreStack);
            }
        }
        return ret;
    }

    public static boolean consumeItemsInInventoryStrict(IItemHandlerModifiable inv, ItemStack toCheck, int sizeOfStack, boolean simulate) {
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (toCheck.isItemEqual(stack) || (toCheck.getItem() == stack.getItem() && toCheck.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                    if (toCheck.hasTagCompound()) {
                        if (ItemStack.areItemStackTagsEqual(toCheck, stack)) {
                            if (stack.getCount() >= sizeOfStack) {
                                decrStackSize(inv, i, sizeOfStack);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    } else {
                        if (stack.getCount() >= sizeOfStack) {
                            if (!simulate)
                                decrStackSize(inv, i, sizeOfStack);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean consumeItemsInInventory(IItemHandler inv, ItemStack toCheck, int sizeOfStack, boolean simulate) {
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack inSlot = inv.getStackInSlot(i);
            if (toCheck.isItemEqual(inSlot) || (toCheck.getItem() == inSlot.getItem() && toCheck.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
//                if (toCheck.hasTagCompound() && ItemStack.areItemStackTagsEqual(toCheck, inSlot)) {
                return inv.extractItem(i, sizeOfStack, simulate).getCount() >= sizeOfStack;
//                } else {
//                    return inv.extractItem(i, sizeOfStack, simulate).getCount() >= sizeOfStack;
//                }
            }
        }
//        for (int i = 0; i < inv.getSlots(); i++) {
//            ItemStack stack = inv.getStackInSlot(i);
//            if (!stack.isEmpty()) {
//                if (ItemStack.areItemsEqual(toCheck, stack) || (toCheck.getItem() == stack.getItem() && toCheck.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
//                    if (toCheck.hasTagCompound()) {
//                        if (ItemStack.areItemStackTagsEqual(toCheck, stack)) {
//                            if (stack.getCount() >= sizeOfStack) {
//                                decrStackSize(inv, i, sizeOfStack);
//                                return true;
//                            }
//                            sizeOfStack -= stack.getCount();
//                            if (!simulate)
//                                inv.setStackInSlot(i, ItemStack.EMPTY);
//                        }
//                    } else {
//                        if (stack.getCount() >= sizeOfStack) {
//                            if (!simulate)
//                                decrStackSize(inv, i, sizeOfStack);
//                            return true;
//                        }
//                        sizeOfStack -= stack.getCount();
//                        if (!simulate)
//                            inv.setStackInSlot(i, ItemStack.EMPTY);
//                    }
//                }
//            }
//        }
        return false;
    }

    public static boolean consumeItemsInInventory(IItemHandlerModifiable inv, Item item, int meta, int stackSize) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == item && (meta == OreDictionary.WILDCARD_VALUE || stack.getItemDamage() == meta)) {
                if (stack.getCount() >= stackSize) {
                    decrStackSize(inv, i, stackSize);
                    return false;
                }

                stackSize -= stack.getCount();
                inv.setStackInSlot(i, ItemStack.EMPTY);
            }
        }

        return false;
    }

    public static boolean consumeOresInInventory(IItemHandlerModifiable inv, List<?> list, int stackSize) {
        if (list.size() > 0) {
            for (Object aList : list) {
                ItemStack tempStack = (ItemStack) aList;
                Item item = tempStack.getItem();
                int meta = tempStack.getItemDamage();

                for (int j = 0; j < inv.getSlots(); ++j) {
                    ItemStack stack = inv.getStackInSlot(j);
                    if (!stack.isEmpty() && stack.getItem() == item && (stack.getItemDamage() == meta || meta == OreDictionary.WILDCARD_VALUE)) {
                        if (tempStack.hasTagCompound()) {
                            if (ItemStack.areItemStackTagsEqual(tempStack, stack)) {
                                if (stack.getCount() >= stackSize) {
                                    decrStackSize(inv, j, stackSize);
                                    return false;
                                }

                                stackSize -= stack.getCount();
                                inv.setStackInSlot(j, ItemStack.EMPTY);
                            }
                        } else {
                            if (stack.getCount() >= stackSize) {
                                decrStackSize(inv, j, stackSize);
                                return false;
                            }

                            stackSize -= stack.getCount();
                            inv.setStackInSlot(j, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }

        return false;
    }

    public static int getFirstOccupiedStackNotOfItem(IItemHandler inv, Item item) {
        return getFirstOccupiedStackNotOfItem(inv, item, OreDictionary.WILDCARD_VALUE);
    }

    public static int getFirstOccupiedStackNotOfItem(IItemHandler inv, Item item, int meta) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            if (!inv.getStackInSlot(i).isEmpty()) {
                int tempMeta = inv.getStackInSlot(i).getItemDamage();
                if (inv.getStackInSlot(i).getItem() != item && (meta == OreDictionary.WILDCARD_VALUE || tempMeta != meta)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static int getFirstOccupiedStackOfItem(IItemHandler inv, Item item) {
        return getFirstOccupiedStackOfItem(inv, item, OreDictionary.WILDCARD_VALUE);
    }

    public static int getFirstOccupiedStackOfItem(IItemHandler inv, Item item, int meta) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            if (!inv.getStackInSlot(i).isEmpty()) {
                int tempMeta = inv.getStackInSlot(i).getItemDamage();
                if (inv.getStackInSlot(i).getItem() == item && (meta == OreDictionary.WILDCARD_VALUE || tempMeta == meta)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static boolean spawnStack(World world, double x, double y, double z, ItemStack stack, int pickupDelay) {
        EntityItem item = new EntityItem(world, x, y, z, stack);
        item.motionX = 0;
        item.motionY = 0;
        item.motionZ = 0;
        item.setPickupDelay(pickupDelay);
        return world.spawnEntity(item);
    }

    public static void spawnStack(World world, double x, double y, double z, int count, ItemStack stack) {
        ItemStack copy = stack.copy();
        if (copy.getCount() > count)
            copy.setCount(count);
        spawnStack(world, x, y, z, copy, 10);
    }

    public static void ejectStackWithOffset(World world, BlockPos pos, List<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty())
                ejectStackWithOffset(world, pos, stack.copy());
        }
    }

    public static void ejectStackWithOffset(World world, BlockPos pos, ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty())
                ejectStackWithOffset(world, pos, stack.copy());
        }
    }

    public static void ejectStackWithOffset(World world, BlockPos pos, ItemStack stack) {
        if (stack.isEmpty())
            return;
        float xOff = world.rand.nextFloat() * 0.7F + 0.15F;
        float yOff = world.rand.nextFloat() * 0.2F + 0.1F;
        float zOff = world.rand.nextFloat() * 0.7F + 0.15F;
        ejectStack(world, (double) ((float) pos.getX() + xOff), (double) ((float) pos.getY() + yOff), (double) ((float) pos.getZ() + zOff), stack, 10);
    }

    public static void ejectStack(World world, double x, double y, double z, ItemStack stack, int pickupDelay) {
        EntityItem item = new EntityItem(world, x, y, z, stack);
        float velocity = 0.05F;
        item.motionX = (double) ((float) world.rand.nextGaussian() * velocity);
        item.motionY = (double) ((float) world.rand.nextGaussian() * velocity + 0.2F);
        item.motionZ = (double) ((float) world.rand.nextGaussian() * velocity);
        item.setPickupDelay(pickupDelay);
        world.spawnEntity(item);
    }

    public static void ejectStack(World world, double x, double y, double z, ItemStack stack) {
        ejectStack(world, x, y, z, stack, 10);
    }


    public static void ejectBrokenItems(World world, BlockPos pos, ResourceLocation lootLocation) {
        if (!world.isRemote) {
            LootContext.Builder build = new LootContext.Builder((WorldServer) world);
            List<ItemStack> stacks = world.getLootTableManager().getLootTableFromLocation(lootLocation).generateLootForPools(world.rand, build.build());
            if (!stacks.isEmpty()) {
                ejectStackWithOffset(world, pos, stacks);
            }
        }
    }

    public static void writeToStack(IItemHandler inv, ItemStack stack) {
        NonNullList<ItemStack> list = NonNullList.withSize(inv.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getSlots(); i++) {
            if (!inv.getStackInSlot(i).isEmpty()) {
                list.set(i, inv.getStackInSlot(i).copy());
            }
        }
        NBTTagCompound tag = ItemStackHelper.saveAllItems(new NBTTagCompound(), list);

        if (!tag.hasNoTags())
            stack.setTagCompound(tag);
    }

    public static void readFromStack(IItemHandler inv, ItemStack stack) {
        if (!stack.isEmpty() && stack.hasTagCompound()) {
            NonNullList<ItemStack> list = NonNullList.withSize(inv.getSlots(), ItemStack.EMPTY);
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null) {
                ItemStackHelper.loadAllItems(tag, list);
                for (int i = 0; i < inv.getSlots(); i++) {
                    inv.insertItem(i, list.get(i), false);
                }
            }
        }
    }

    public static int calculateComparatorLevel(@Nonnull IItemHandler inventory) {
        int i = 0;
        float f = 0.0F;
        for (int j = 0; j < inventory.getSlots(); ++j) {
            ItemStack itemstack = inventory.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
                f += (float) itemstack.getCount() / (float) itemstack.getMaxStackSize();
                ++i;
            }
        }
        f = f / (float) inventory.getSlots();
        return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
    }

    public static ItemStack setCount(ItemStack input, int count) {
        ItemStack stack = input.copy();
        stack.setCount(count);
        return stack;
    }


}