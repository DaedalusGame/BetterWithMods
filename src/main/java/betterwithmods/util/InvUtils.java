package betterwithmods.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.List;

public class InvUtils {

    public static void ejectInventoryContents(World world, BlockPos pos, IItemHandler inv) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                float fX = world.rand.nextFloat() * 0.7F + 0.15F;
                float fY = world.rand.nextFloat() * 0.7F + 0.15F;
                float fZ = world.rand.nextFloat() * 0.7F + 0.15F;

                while (stack.getCount() > 0) {
                    int j = world.rand.nextInt(21) + 10;
                    if (j > stack.getCount()) {
                        j = stack.getCount();
                    }

                    stack.shrink(j);
                    EntityItem item = new EntityItem(world, (double) ((float) pos.getX() + fX), (double) ((float) pos.getY() + fY), (double) ((float) pos.getZ() + fZ), new ItemStack(stack.getItem(), j, stack.getItemDamage()));
                    float f1 = 0.05F;
                    item.motionX = (double) ((float) world.rand.nextGaussian() * f1);
                    item.motionY = (double) ((float) world.rand.nextGaussian() * f1 + 0.2F);
                    item.motionZ = (double) ((float) world.rand.nextGaussian() * f1);
                    copyTags(item.getEntityItem(), stack);
                    world.spawnEntity(item);
                }
            }
        }

    }

    public static void clearInventory(IItemHandlerModifiable inv) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
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
        if (inv.getStackInSlot(slot) != ItemStack.EMPTY) {
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
        return insertingStacks(inv, stack, minSlot, maxSlot, true);
    }

    public static boolean addSingleItemToInv(IItemHandler inv, Item item, int meta, boolean simulate) {
        ItemStack stack = new ItemStack(item, 1, meta);
        return attemptToInsertStack(inv, stack, 0, inv.getSlots(), simulate);
    }

    private static boolean attemptToInsertStack(IItemHandler inv, ItemStack stack, int minSlot, int maxSlot, boolean simulate) {
        return insertingStacks(inv, stack, minSlot, maxSlot, simulate);
    }

    private static boolean insertingStacks(IItemHandler inv, ItemStack stack, int minSlot, int maxSlot, boolean simulate) {
        for (int slot = minSlot; slot < maxSlot; slot++) {
            if (inv.insertItem(slot, stack, simulate) == ItemStack.EMPTY)
                return true;
        }
        return false;
    }

    public static int getFirstOccupiedStackInRange(IItemHandler inv, int minSlot, int maxSlot) {
        for (int slot = minSlot; slot <= maxSlot; ++slot) {
            if (inv.getStackInSlot(slot) != ItemStack.EMPTY) {
                return slot;
            }
        }
        return -1;
    }

    public static int getFirstEmptyStackInRange(IItemHandler inv, int minSlot, int maxSlot) {
        for (int slot = minSlot; slot <= maxSlot; ++slot) {
            if (inv.getStackInSlot(slot) == ItemStack.EMPTY) {
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
            if (inv.getStackInSlot(i) != ItemStack.EMPTY) {
                ++count;
            }
        }

        return count;
    }

    public static int countItemStacksInInventory(IItemHandler inv, ItemStack toCheck) {
        int itemCount = 0;
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
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
            if (stack != ItemStack.EMPTY) {
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

    public static boolean consumeItemsInInventory(IItemHandlerModifiable inv, ItemStack toCheck, int sizeOfStack, boolean simulate) {
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (ItemStack.areItemsEqual(toCheck, stack) || (toCheck.getItem() == stack.getItem() && toCheck.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                    if (toCheck.hasTagCompound()) {
                        if (ItemStack.areItemStackTagsEqual(toCheck, stack)) {
                            if (stack.getCount() >= sizeOfStack) {
                                decrStackSize(inv, i, sizeOfStack);
                                return true;
                            }
                            sizeOfStack -= stack.getCount();
                            if(!simulate)
                                inv.setStackInSlot(i, ItemStack.EMPTY);
                        }
                    } else {
                        if (stack.getCount() >= sizeOfStack) {
                            if(!simulate)
                                decrStackSize(inv, i, sizeOfStack);
                            return true;
                        }
                        sizeOfStack -= stack.getCount();
                        if(!simulate)
                            inv.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            }
        }
        return false;
    }

    public static boolean consumeItemsInInventory(IItemHandlerModifiable inv, Item item, int meta, int stackSize) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() == item && (meta == OreDictionary.WILDCARD_VALUE || stack.getItemDamage() == meta)) {
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
                    if (stack != ItemStack.EMPTY && stack.getItem() == item && (stack.getItemDamage() == meta || meta == OreDictionary.WILDCARD_VALUE)) {
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
            if (inv.getStackInSlot(i) != ItemStack.EMPTY) {
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

    public static boolean addItemStackToInv(IItemHandler inventory, ItemStack stack, boolean simulate) {
        return attemptToInsertStack(inventory, stack, 0, inventory.getSlots(),simulate);
    }

    public static boolean checkItemStackInsert(IItemHandler inv, ItemStack stack) {
        return canInsertStack(inv, stack, 0, inv.getSlots());
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
            if (itemstack != ItemStack.EMPTY) {
                f += (float) itemstack.getCount() / (float) itemstack.getMaxStackSize();
                ++i;
            }
        }
        f = f / (float) inventory.getSlots();
        return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
    }
}