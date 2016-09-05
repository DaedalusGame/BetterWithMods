package betterwithmods.util;

import com.google.common.collect.Lists;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class InvUtils {
    public static ArrayList dustNames = Lists.newArrayList();
    public static ArrayList ingotNames = Lists.newArrayList();
    public static ArrayList cropNames = Lists.newArrayList();

    public static void initOreDictGathering() {
        dustNames = getOreNames("dust");
        ingotNames = getOreNames("ingot");
        cropNames = getOreNames("crop");
    }

    private static boolean canStacksMerge(ItemStack source, ItemStack dest, int sizeLimit) {
        return dest != null && dest.getItem() == source.getItem() && dest.isStackable() && dest.stackSize < dest.getMaxStackSize() && dest.stackSize < sizeLimit && (!dest.getHasSubtypes() || dest.getItemDamage() == source.getItemDamage()) && ItemStack.areItemStackTagsEqual(dest, source);
    }

    private static ArrayList getOreNames(String prefix) {
        ArrayList list = new ArrayList();
        String[] var2 = OreDictionary.getOreNames();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String name = var2[var4];
            if (name.startsWith(prefix) && OreDictionary.getOres(name).size() > 0) {
                list.addAll(OreDictionary.getOres(name));
            }
        }

        return list;
    }

    public static boolean listContains(ItemStack check, List<ItemStack> list) {
        if (list != null) {
            if (list.isEmpty()) return false;
            for (ItemStack item : list) {
                if (ItemStack.areItemsEqual(check, item))
                    return true;
            }
        }
        return false;
    }

    public static void ejectInventoryContents(World world, BlockPos pos, IItemHandler inv) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                float fX = world.rand.nextFloat() * 0.7F + 0.15F;
                float fY = world.rand.nextFloat() * 0.7F + 0.15F;
                float fZ = world.rand.nextFloat() * 0.7F + 0.15F;

                while (stack.stackSize > 0) {
                    int j = world.rand.nextInt(21) + 10;
                    if (j > stack.stackSize) {
                        j = stack.stackSize;
                    }

                    stack.stackSize -= j;
                    EntityItem item = new EntityItem(world, (double) ((float) pos.getX() + fX), (double) ((float) pos.getY() + fY), (double) ((float) pos.getZ() + fZ), new ItemStack(stack.getItem(), j, stack.getItemDamage()));
                    float f1 = 0.05F;
                    item.motionX = (double) ((float) world.rand.nextGaussian() * f1);
                    item.motionY = (double) ((float) world.rand.nextGaussian() * f1 + 0.2F);
                    item.motionZ = (double) ((float) world.rand.nextGaussian() * f1);
                    copyTags(item.getEntityItem(), stack);
                    world.spawnEntityInWorld(item);
                }
            }
        }

    }

    public static void clearInventory(ItemStackHandler inv) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                inv.setStackInSlot(i, (ItemStack) null);
            }
        }

    }

    public static void copyTags(ItemStack destStack, ItemStack sourceStack) {
        if (sourceStack.hasTagCompound()) {
            destStack.setTagCompound((NBTTagCompound) sourceStack.getTagCompound().copy());
        }

    }

    public static ItemStack decrStackSize(ItemStackHandler inv, int slot, int amount) {
        if (inv.getStackInSlot(slot) != null) {
            ItemStack splitStack;
            if (inv.getStackInSlot(slot).stackSize <= amount) {
                splitStack = inv.getStackInSlot(slot);
                inv.setStackInSlot(slot, (ItemStack) null);
                return splitStack;
            } else {
                splitStack = inv.getStackInSlot(slot).splitStack(amount);
                if (inv.getStackInSlot(slot).stackSize < 1) {
                    inv.setStackInSlot(slot, (ItemStack) null);
                }
                return splitStack;
            }
        } else {
            return null;
        }
    }

    public static boolean addSingleItemToInv(IItemHandler inv, Item item, int meta) {
        ItemStack stack = new ItemStack(item, 1, meta);
        return attemptToInsertStack(inv,stack,0,inv.getSlots());
    }

    private static boolean canStacksMerge(ItemStack source, ItemStack dest) {
        return dest != null && dest.getItem() == source.getItem() && dest.isStackable() && dest.stackSize < dest.getMaxStackSize() && (!dest.getHasSubtypes() || dest.getItemDamage() == source.getItemDamage()) && ItemStack.areItemStackTagsEqual(dest, source);
    }

    private static int findSlotToMergeStackInRange(IItemHandler inv, ItemStack stack, int minSlot, int maxSlot) {

        for (int i = minSlot; i <= maxSlot; ++i) {
            ItemStack tempStack = inv.getStackInSlot(i);
            if (canStacksMerge(stack, tempStack)) {
                return i;
            }
        }

        return -1;
    }

    private static boolean attemptToInsertStack(IItemHandler inv, ItemStack stack, int minSlot, int maxSlot) {
        for(int slot = minSlot; slot<=maxSlot;slot++) {
            if(inv.insertItem(slot,stack,false) == null)
                return true;
        }
        return false;
    }

    public static int getFirstOccupiedStackInRange(IItemHandler inv, int minSlot, int maxSlot) {
        for (int slot = minSlot; slot <= maxSlot; ++slot) {
            if (inv.getStackInSlot(slot) != null) {
                return slot;
            }
        }

        return -1;
    }

    public static int getFirstEmptyStackInRange(IItemHandler inv, int minSlot, int maxSlot) {
        for (int slot = minSlot; slot <= maxSlot; ++slot) {
            if (inv.getStackInSlot(slot) == null) {
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
            if (inv.getStackInSlot(i) != null) {
                ++count;
            }
        }

        return count;
    }

    public static int countItemsInInventory(IItemHandler inv, Item item) {
        return countItemsInInventory(inv, item, 32767);
    }

    public static int countItemsInInventory(IItemHandler inv, Item item, int meta) {
        int itemCount = 0;
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() == item) {
                    if ((meta == 32767) || (stack.getItemDamage() == meta)) {
                        itemCount += inv.getStackInSlot(i).stackSize;
                    }
                }
            }
        }
        return itemCount;
    }

    public static int countOresInInv(IItemHandler inv, List list) {
        int ret = 0;
        if (list != null && !list.isEmpty() && list.size() > 0) {
            for (int i = 0; i < inv.getSlots(); ++i) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null) {
                    for (int o = 0; o < list.size(); ++o) {
                        Item oreItem = ((ItemStack) list.get(o)).getItem();
                        int oreMeta = ((ItemStack) list.get(o)).getItemDamage();
                        if (OreDictionary.itemMatches(new ItemStack(stack.getItem(), 1, stack.getItemDamage()), new ItemStack(oreItem, 1, oreMeta), false)) {
                            ret += stack.stackSize;
                            break;
                        }
                    }
                }
            }
        }

        return ret;
    }

    public static int countOresInInventory(IItemHandler inv, ArrayList ores) {
        int ret = 0;
        if (ores != null && !ores.isEmpty() && ores.size() > 0) {
            for (int i = 0; i < ores.size(); ++i) {
                Item item = ((ItemStack) ores.get(i)).getItem();
                int meta = ((ItemStack) ores.get(i)).getItemDamage();
                ret += countItemsInInventory(inv, item, meta);
            }
        }

        return ret;
    }

    public static boolean consumeItemsInInventory(ItemStackHandler inv, Item item, int meta, int stackSize) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null && stack.getItem() == item && (meta == 32767 || stack.getItemDamage() == meta)) {
                if (stack.stackSize >= stackSize) {
                    decrStackSize(inv, i, stackSize);
                    return false;
                }

                stackSize -= stack.stackSize;
                inv.setStackInSlot(i, (ItemStack) null);
            }
        }

        return false;
    }

    public static boolean consumeOresInInventory(ItemStackHandler inv, List list, int stackSize) {
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); ++i) {
                ItemStack tempStack = (ItemStack) list.get(i);
                Item item = tempStack.getItem();
                int meta = tempStack.getItemDamage();

                for (int j = 0; j < inv.getSlots(); ++j) {
                    ItemStack stack = inv.getStackInSlot(j);
                    if (stack != null && stack.getItem() == item && (stack.getItemDamage() == meta || meta == 32767)) {
                        if (stack.stackSize >= stackSize) {
                            decrStackSize(inv, j, stackSize);
                            return false;
                        }

                        stackSize -= stack.stackSize;
                        inv.setStackInSlot(j, (ItemStack) null);
                    }
                }
            }
        }

        return false;
    }

    public static int getFirstOccupiedStackNotOfItem(IItemHandler inv, Item item) {
        return getFirstOccupiedStackNotOfItem(inv, item, 32767);
    }

    public static int getFirstOccupiedStackNotOfItem(IItemHandler inv, Item item, int meta) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            if (inv.getStackInSlot(i) != null) {
                int tempMeta = inv.getStackInSlot(i).getItemDamage();
                if (inv.getStackInSlot(i).getItem() != item && (meta == 32767 || tempMeta != meta)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static int getFirstOccupiedStackOfItem(IItemHandler inv, Item item) {
        return getFirstOccupiedStackOfItem(inv, item, 32767);
    }

    public static int getFirstOccupiedStackOfItem(IItemHandler inv, Item item, int meta) {
        for (int i = 0; i < inv.getSlots(); ++i) {
            if (inv.getStackInSlot(i) != null) {
                int tempMeta = inv.getStackInSlot(i).getItemDamage();
                if (inv.getStackInSlot(i).getItem() == item && (meta == 32767 || tempMeta == meta)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static void ejectStackWithOffset(World world, BlockPos pos, ArrayList<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            if (stack != null)
                ejectStackWithOffset(world, pos, stack.copy());
        }
    }

    public static void ejectStackWithOffset(World world, BlockPos pos, ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            if (stack != null)
                ejectStackWithOffset(world, pos, stack.copy());
        }
    }

    public static void ejectStackWithOffset(World world, BlockPos pos, ItemStack stack) {
        if (stack == null)
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
        world.spawnEntityInWorld(item);
    }

    public static void ejectStack(World world, double x, double y, double z, ItemStack stack) {
        ejectStack(world, x, y, z, stack, 10);
    }

    public static boolean addItemStackToInv(IItemHandler inventory, ItemStack stack) {
        return attemptToInsertStack(inventory,stack,0,inventory.getSlots());
    }
}