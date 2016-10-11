package betterwithmods.craft.bulk;

import betterwithmods.craft.OreStack;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

//TODO Probably this should implement some recipe interface, at the very least.
public class BulkRecipe
{
    protected ItemStack output = null;
    protected ItemStack secondary = null;
    protected ArrayList<Object> input = new ArrayList<Object>();
    protected ArrayList<Object> jeiInput = new ArrayList<Object>();

    public BulkRecipe(String type, ItemStack output, Object... input)
    {
        this(type, output, null, input);
    }

    public BulkRecipe(String type, ItemStack output, ItemStack secondaryOutput, Object... input)
    {
        this.output = output.copy();
        if(secondaryOutput != null)
            this.secondary = secondaryOutput.copy();
        int place = -1;
        for(Object in : input)
        {
            place++;
            if(in instanceof ItemStack) {
                this.input.add(((ItemStack) in).copy());
                this.jeiInput.add(((ItemStack) in).copy());
            }
            else if(in instanceof Item) {
                this.input.add(new ItemStack((Item) in, 1, OreDictionary.WILDCARD_VALUE));
                this.jeiInput.add(new ItemStack((Item) in, 1, OreDictionary.WILDCARD_VALUE));
            }
            else if(in instanceof Block) {
                this.input.add(new ItemStack((Block) in, 1, OreDictionary.WILDCARD_VALUE));
                this.jeiInput.add(new ItemStack((Block)in, 1, OreDictionary.WILDCARD_VALUE));
            }
            else if(in instanceof String)
            {
                if(place + 1 < input.length)
                {
                    if(input[place + 1] instanceof Integer)
                    {
                        this.input.add(new OreStack((String)in, (Integer)input[place + 1]));
                        this.jeiInput.add(getOreList(new OreStack((String)in, (Integer)input[place + 1])));
                    }
                    else {
                        this.input.add(new OreStack((String) in, 1));
                        this.jeiInput.add(getOreList(new OreStack((String)in, 1)));
                    }
                }
                else {
                    this.input.add(new OreStack((String) in, 1));
                    this.jeiInput.add(getOreList(new OreStack((String)in, 1)));
                }
            }
            else if(in instanceof Integer || in == null)
                continue;
            else if(in instanceof OreStack) {
                this.input.add(((OreStack) in).copy());
                this.jeiInput.add(getOreList(((OreStack)in).copy()));
            }
            else
            {
                String ret = "Invalid " + type + " recipe: ";
                for(Object tmp : input)
                    ret += tmp + ", ";
                ret += "Output: " + output;
                if(secondaryOutput != null)
                    ret += ", Secondary: " + secondaryOutput;
                throw new RuntimeException(ret);
            }
        }
    }

    private List<ItemStack> getOreList(OreStack stack)
    {
        int stackSize = stack.getStackSize();
        List<ItemStack> list = new ArrayList<ItemStack>();
        if(stack.getOres() != null && !stack.getOres().isEmpty()) {
            for (ItemStack s : stack.getOres()) {
                list.add(new ItemStack(s.getItem(), stackSize, s.getItemDamage()));
            }
        }
        return list;
    }

    public ItemStack getOutput()
    {
        return this.output;
    }

    public ItemStack getSecondary()
    {
        return this.secondary;
    }

    public ArrayList<Object> getInput()
    {
       return this.jeiInput;
    }

    public ArrayList<Object> getRecipeInput() {
        return this.input;
    }

    public boolean matches(ItemStackHandler inv)
    {
        ArrayList<Object> required = new ArrayList<Object>(input);

        if(required != null && required.size() > 0)
        {
            for(Object obj : required)
            {
                if(obj instanceof ItemStack)
                {
                    ItemStack stack = (ItemStack)obj;
                    if(stack != null)
                    {
                        if(InvUtils.countItemStacksInInventory(inv, stack) < stack.stackSize)
                            return false;
                    }
                }
                else if(obj instanceof OreStack)
                {
                    OreStack stack = (OreStack)obj;
                    if(stack != null)
                    {
                        if(InvUtils.countOresInInventory(inv, stack.getOres()) < stack.getStackSize())
                            return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean matches(BulkRecipe recipe)
    {
        if(this.getOutput() != null && recipe.getOutput() != null)
        {
            boolean match = this.stacksMatch(this.getOutput(), recipe.getOutput());
            if(match && (this.getSecondary() != null || recipe.getSecondary() != null))
            {
                match = this.getSecondary() != null && recipe.getSecondary() != null;
                if(match)
                    match = this.stacksMatch(this.getSecondary(), recipe.getSecondary());
            }
            return match;
        }
        return false;
    }

    public boolean consumeInvIngredients(ItemStackHandler inv)
    {
        boolean success = true;
        ArrayList<Object> required = input;

        if(required != null && required.size() > 0)
        {
            for(Object obj : required)
            {
                if(obj instanceof ItemStack)
                {
                    ItemStack stack = (ItemStack)obj;

                    if(stack != null)
                    {
                        if(!InvUtils.consumeItemsInInventory(inv, stack, stack.stackSize))
                            success = false;
                    }
                }
                else if(obj instanceof OreStack)
                {
                    OreStack stack = (OreStack)obj;

                    if(stack != null)
                    {
                        if(!InvUtils.consumeOresInInventory(inv, stack.getOres(), stack.getStackSize()))
                            success = false;
                    }
                }
            }
        }
        return success;
    }

    private boolean stacksMatch(ItemStack first, ItemStack second)
    {
        return first.getItem() == second.getItem() && first.getItemDamage() == second.getItemDamage() && first.stackSize == second.stackSize;
    }
}
