package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.registry.OreStack;
import betterwithmods.util.InvUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

//TODO Probably this should implement some recipe interface, at the very least.
public class BulkRecipe {
    @Nonnull
    protected ItemStack output = ItemStack.EMPTY;
    @Nonnull
    protected ItemStack secondary = ItemStack.EMPTY;
    @Nonnull
    protected ArrayList<Object> inputs = new ArrayList<>();//Either ItemStack or OreStack
    @Nonnull
    protected ArrayList<List<ItemStack>> jeiInputs = new ArrayList<>();

    protected BulkRecipe(ItemStack output, Object... inputs) {
        this(output, ItemStack.EMPTY, inputs);
    }

    public BulkRecipe(@Nonnull ItemStack output, @Nonnull ItemStack secondaryOutput, Object... inputs) {
        this.output = output.copy();
        this.secondary = !secondaryOutput.isEmpty() ? secondaryOutput.copy() : ItemStack.EMPTY;
        int place = -1;
        ArrayList<Object> inputList = new ArrayList<>();
        for (Object in : inputs) {
            if (in instanceof Integer || in == null)
                continue;
            place++;
            if (in instanceof ItemStack) {
                inputList.add(((ItemStack) in).copy());
            } else if (in instanceof Item) {
                inputList.add(new ItemStack((Item) in, 1, OreDictionary.WILDCARD_VALUE));
            } else if (in instanceof Block) {
                inputList.add(new ItemStack((Block) in, 1, OreDictionary.WILDCARD_VALUE));
            } else if (in instanceof String) {
                int count = place + 1 < inputs.length && inputs[place + 1] instanceof Integer ? (int) inputs[place + 1] : 1;
                OreStack stack = new OreStack((String) in, count);
                if (!stack.isEmpty())
                    inputList.add(stack);
            } else if (in instanceof OreStack) {
                inputList.add(((OreStack) in).copy());
            } else {
                StringBuilder ret = new StringBuilder("Invalid " + this.getClass().getSimpleName() + " recipe: ");
                for (Object tmp : inputList)
                    ret.append(tmp).append(", ");
                ret.append("Output: ").append(output);
                if (!secondaryOutput.isEmpty())
                    ret.append(", Secondary: ").append(secondaryOutput);
                throw new RuntimeException(ret.toString());
            }
        }
        condenseInputs(inputList);
    }

    private void condenseInputs(ArrayList<Object> list) {
        for (Object obj : list) {
            int contain = BWOreDictionary.listContains(obj, inputs);
            if (contain > -1) {
                if (obj instanceof ItemStack) {
                    ((ItemStack) inputs.get(contain)).grow(((ItemStack) obj).getCount());
                } else if (obj instanceof OreStack) {
                    ((OreStack) inputs.get(contain)).addToStack(((OreStack) obj).getStackSize());
                }
            } else {
                inputs.add(obj);
            }
        }
        for (Object obj : inputs) {
            List<ItemStack> in = new ArrayList<>();
            if (obj instanceof ItemStack)
                in.add((ItemStack) obj);
            else if (obj instanceof OreStack) {
                in.addAll(((OreStack) obj).getItems());
            }
            jeiInputs.add(in);
        }
    }


    public List<ItemStack> getOutputs() {
        return Lists.newArrayList(this.output.copy(), this.secondary.copy());
    }

    @Nonnull
    public ItemStack getOutput() {
        return this.output;
    }

    @Nonnull
    public ItemStack getSecondary() {
        return this.secondary;
    }

    @Nonnull
    public ArrayList<List<ItemStack>> getInputs() {
        return this.jeiInputs;
    }

    public ArrayList<Object> getRecipeInput() {
        return this.inputs;
    }

    public boolean matches(ItemStack output, ItemStack secondary) {
        return this.output.isItemEqual(output) && (secondary.isEmpty() && this.secondary.isEmpty()) || this.secondary.isItemEqual(secondary);
    }

    public boolean matches(ItemStackHandler inv) {
        ArrayList<Object> required = new ArrayList<>(inputs);

        if (required.size() > 0) {
            for (Object obj : required) {
                if (obj instanceof ItemStack) {
                    ItemStack stack = (ItemStack) obj;
                    if (!stack.isEmpty()) {
                        if (InvUtils.countItemStacksInInventory(inv, stack) < stack.getCount())
                            return false;
                    }
                } else if (obj instanceof OreStack) {
                    OreStack stack = (OreStack) obj;
                    if (InvUtils.countOresInInventory(inv, stack.getItems()) < stack.getStackSize())
                        return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean consumeInvIngredients(ItemStackHandler inv) {
        boolean success = true;
        ArrayList<Object> required = inputs;

        if (required != null && required.size() > 0) {
            for (Object obj : required) {
                if (obj instanceof ItemStack) {
                    ItemStack stack = (ItemStack) obj;

                    if (!stack.isEmpty()) {
                        if (!InvUtils.consumeItemsInInventory(inv, stack, stack.getCount(), false))
                            success = false;
                    }
                } else if (obj instanceof OreStack) {
                    OreStack stack = (OreStack) obj;

                    if (stack != null) {
                        if (!InvUtils.consumeOresInInventory(inv, stack.getItems(), stack.getStackSize()))
                            success = false;
                    }
                }
            }
        }
        return success;
    }

    public boolean isEmpty() {
        return this.inputs.isEmpty() || this.output.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s: %s -> %s,%s", getClass().getSimpleName(), this.inputs, this.output, this.secondary);
    }
}
