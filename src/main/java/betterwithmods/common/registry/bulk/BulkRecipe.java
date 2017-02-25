package betterwithmods.common.registry.bulk;

import betterwithmods.common.registry.OreStack;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

//TODO Probably this should implement some recipe interface, at the very least.
public class BulkRecipe {
    protected ItemStack output = ItemStack.EMPTY;
    protected ItemStack secondary = ItemStack.EMPTY;
    protected ArrayList<Object> input = new ArrayList<>();//Either ItemStack or OreStack
    protected ArrayList<List<ItemStack>> jeiInput = new ArrayList<>();
    private String type;

    public BulkRecipe(String type, ItemStack output, Object... input) {
        this(type, output, null, input);
    }

    public BulkRecipe(String type, ItemStack output, ItemStack secondaryOutput, Object... input) {
        this.type = type;
        this.output = output.copy();
        if (secondaryOutput != null && secondaryOutput != ItemStack.EMPTY)
            this.secondary = secondaryOutput.copy();
        int place = -1;
        ArrayList<Object> inputs = new ArrayList<>();
        for (Object in : input) {
            if (in instanceof Integer || in == null)
                continue;
            place++;
            if (in instanceof ItemStack) {
                inputs.add(((ItemStack) in).copy());
            } else if (in instanceof Item) {
                inputs.add(new ItemStack((Item) in, 1, OreDictionary.WILDCARD_VALUE));
            } else if (in instanceof Block) {
                inputs.add(new ItemStack((Block) in, 1, OreDictionary.WILDCARD_VALUE));
            } else if (in instanceof String) {
                if (place + 1 < input.length) {
                    if (input[place + 1] instanceof Integer) {
                        inputs.add(new OreStack((String) in, (Integer) input[place + 1]));
                    } else {
                        inputs.add(new OreStack((String) in, 1));
                    }
                } else {
                    inputs.add(new OreStack((String) in, 1));
                }
            } else if (in instanceof OreStack) {
                inputs.add(((OreStack) in).copy());
            } else {
                String ret = "Invalid " + type + " recipe: ";
                for (Object tmp : input)
                    ret += tmp + ", ";
                ret += "Output: " + output;
                if (secondaryOutput != null)
                    ret += ", Secondary: " + secondaryOutput;
                throw new RuntimeException(ret);
            }
        }
        condenseInputs(inputs);
    }

    private void condenseInputs(ArrayList<Object> list) {
        ArrayList<Object> inputs = new ArrayList<>();
        ArrayList<List<ItemStack>> jeiInputs = new ArrayList<>();
        for (Object obj : list) {
            int contain = InvUtils.listContains(obj, inputs);
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
            else if (obj instanceof OreStack)
                in.addAll(getOreList((OreStack) obj));
            jeiInputs.add(in);
        }
        input = inputs;
        jeiInput = jeiInputs;
    }

    private List<ItemStack> getOreList(OreStack stack) {
        int sizeOfStack = stack.getStackSize();
        List<ItemStack> list = new ArrayList<>();
        if (stack.getOres() != null && !stack.getOres().isEmpty()) {
            for (ItemStack s : stack.getOres()) {
                list.add(new ItemStack(s.getItem(), sizeOfStack, s.getItemDamage()));
            }
        }
        return list;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public ItemStack getSecondary() {
        return this.secondary;
    }

    public ArrayList<List<ItemStack>> getInput() {
        return this.jeiInput;
    }

    public ArrayList<Object> getRecipeInput() {
        return this.input;
    }

    public boolean matches(ItemStackHandler inv) {
        ArrayList<Object> required = new ArrayList<>(input);

        if (required.size() > 0) {
            for (Object obj : required) {
                if (obj instanceof ItemStack) {
                    ItemStack stack = (ItemStack) obj;
                    if (stack != ItemStack.EMPTY) {
                        if (InvUtils.countItemStacksInInventory(inv, stack) < stack.getCount())
                            return false;
                    }
                } else if (obj instanceof OreStack) {
                    OreStack stack = (OreStack) obj;
                    if (stack != null) {
                        if (InvUtils.countOresInInventory(inv, stack.getOres()) < stack.getStackSize())
                            return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean matches(BulkRecipe recipe) {
        if (this.getOutput() != ItemStack.EMPTY && recipe.getOutput() != ItemStack.EMPTY) {
            boolean match = this.stacksMatch(this.getOutput(), recipe.getOutput());
            if (match && (this.getSecondary() != ItemStack.EMPTY || recipe.getSecondary() != ItemStack.EMPTY)) {
                match = this.getSecondary() != ItemStack.EMPTY && recipe.getSecondary() != ItemStack.EMPTY;
                if (match)
                    match = this.stacksMatch(this.getSecondary(), recipe.getSecondary());
            }
            return match;
        }
        return false;
    }

    public boolean consumeInvIngredients(ItemStackHandler inv) {
        boolean success = true;
        ArrayList<Object> required = input;

        if (required != null && required.size() > 0) {
            for (Object obj : required) {
                if (obj instanceof ItemStack) {
                    ItemStack stack = (ItemStack) obj;

                    if (stack != ItemStack.EMPTY) {
                        if (!InvUtils.consumeItemsInInventory(inv, stack, stack.getCount()))
                            success = false;
                    }
                } else if (obj instanceof OreStack) {
                    OreStack stack = (OreStack) obj;

                    if (stack != null) {
                        if (!InvUtils.consumeOresInInventory(inv, stack.getOres(), stack.getStackSize()))
                            success = false;
                    }
                }
            }
        }
        return success;
    }

    private boolean stacksMatch(ItemStack first, ItemStack second) {
        return first.getItem() == second.getItem() && first.getItemDamage() == second.getItemDamage() && first.getCount() == second.getCount();
    }

    public String getType() {
        return type;
    }
}
