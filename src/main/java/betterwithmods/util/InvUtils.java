package betterwithmods.util;

import betterwithmods.craft.OreStack;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InvUtils {
    public static List<ItemStack> dustNames;
    public static List<ItemStack> oreNames;
    public static List<ItemStack> ingotNames;
    public static List<ItemStack> cropNames;

    public static void postInitOreDictGathering() {
        dustNames = getOreNames("dust");
        oreNames = getOreNames("ore");
        ingotNames = getOreNames("ingot");
        cropNames = getOreNames("crop");
    }

    public static ItemStack getMatchingSuffixStack(ItemStack stack, String startingPrefix, String resultingPrefix) {
        List<ItemStack> list = getMatchingSuffix(stack, startingPrefix, resultingPrefix);
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    public static List<ItemStack> getMatchingSuffix(ItemStack stack, String startingPrefix, String resultingPrefix) {
        return IntStream.of(OreDictionary.getOreIDs(stack)).mapToObj(OreDictionary::getOreName).filter(ore -> ore.startsWith(startingPrefix)).map(ore -> OreDictionary.getOres(resultingPrefix + ore.substring(startingPrefix.length()))).flatMap(List::stream).collect(Collectors.toList());
    }

    public static ArrayList<ItemStack> getOreNames(String prefix) {
        ArrayList<ItemStack> list = new ArrayList<>();
        String[] var2 = OreDictionary.getOreNames();
        for (String name : var2) {
            if (name.startsWith(prefix) && OreDictionary.getOres(name).size() > 0) {
                list.addAll(OreDictionary.getOres(name));
            }
        }
        return list;
    }

    public static int listContains(Object obj, ArrayList<Object> list) {
        if (list != null && list.size() > 0 && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (obj instanceof ItemStack && list.get(i) instanceof ItemStack) {
                    ItemStack stack = (ItemStack) obj;
                    ItemStack toCheck = (ItemStack) list.get(i);
                    if (ItemStack.areItemsEqual(stack, toCheck)) {
                        if (toCheck.hasTagCompound()) {
                            if (ItemStack.areItemStackTagsEqual(stack, toCheck))
                                return i;
                        } else if (stack.hasTagCompound()) {
                            return -1;
                        } else
                            return i;
                    }
                } else if (obj instanceof OreStack && list.get(i) instanceof OreStack) {
                    OreStack stack = (OreStack) obj;
                    OreStack toCheck = (OreStack) list.get(i);
                    if (stack.getOreName().equals(toCheck.getOreName()))
                        return i;
                }
            }
        }
        return -1;
    }

    public static boolean isOre(ItemStack stack, String ore) {
        return InvUtils.listContains(stack, OreDictionary.getOres(ore));
    }

    public static boolean listContains(ItemStack check, List<ItemStack> list) {
        if (list != null) {
            if (list.isEmpty()) return false;
            for (ItemStack item : list) {
                if (ItemStack.areItemsEqual(check, item) || (check.getItem() == item.getItem() && item.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                    return !item.hasTagCompound() || ItemStack.areItemStackTagsEqual(check, item);
                }
            }
        }
        return false;
    }

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

    public static boolean addSingleItemToInv(IItemHandler inv, Item item, int meta) {
        ItemStack stack = new ItemStack(item, 1, meta);
        return attemptToInsertStack(inv, stack, 0, inv.getSlots());
    }

    private static boolean attemptToInsertStack(IItemHandler inv, ItemStack stack, int minSlot, int maxSlot) {
        for (int slot = minSlot; slot < maxSlot; slot++) {
            if (inv.insertItem(slot, stack, false) == ItemStack.EMPTY)
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

    public static boolean consumeItemsInInventory(IItemHandlerModifiable inv, ItemStack toCheck, int sizeOfStack) {
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
                            inv.setStackInSlot(i, ItemStack.EMPTY);
                        }
                    } else {
                        if (stack.getCount() >= sizeOfStack) {
                            decrStackSize(inv, i, sizeOfStack);
                            return true;
                        }
                        sizeOfStack -= stack.getCount();
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
            if (inv.getStackInSlot(i) != ItemStack.EMPTY) {
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
        world.spawnEntity(item);
    }

    public static void ejectStack(World world, double x, double y, double z, ItemStack stack) {
        ejectStack(world, x, y, z, stack, 10);
    }

    public static boolean addItemStackToInv(IItemHandler inventory, ItemStack stack) {
        return attemptToInsertStack(inventory, stack, 0, inventory.getSlots());
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