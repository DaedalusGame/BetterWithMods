package betterwithmods.integration.jei;

import betterwithmods.craft.OreStack;
import betterwithmods.craft.bulk.BulkRecipe;
import betterwithmods.craft.bulk.CraftingManagerCauldron;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BulkRecipeWrapper extends BWMRecipeWrapper
{
    public BulkRecipeWrapper(@Nonnull BulkRecipe recipe)
    {
        super(recipe);
    }

    @Nonnull
    @Override
    public List getInputs()
    {
        List<Object> inputs = new ArrayList();
        for(Object obj : getRecipe().getInput())
        {
            if(obj instanceof ItemStack)
            {
                ItemStack stack = (ItemStack)obj;
                if(stack != null && stack.getItem() != null)
                    inputs.add(stack.copy());
            }
            else if(obj instanceof OreStack)
            {
                OreStack stack = (OreStack)obj;
                if(stack.getOres() != null && !stack.getOres().isEmpty())
                {
                    List<ItemStack> oreStacks = new ArrayList();
                    for(ItemStack ore : stack.getOres())
                    {
                        oreStacks.add(new ItemStack(ore.getItem(), stack.getStackSize(), ore.getItemDamage()));
                    }
                    inputs.add(oreStacks);
                }
            }
        }
        return inputs;
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs()
    {
        List<ItemStack> outputs = new ArrayList();
        outputs.add(getRecipe().getOutput().copy());
        if(getRecipe().getSecondary() != null)
            outputs.add(getRecipe().getSecondary().copy());
        return outputs;
    }
}
