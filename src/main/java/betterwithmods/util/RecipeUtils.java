package betterwithmods.util;

import betterwithmods.BWCrafting;
import betterwithmods.craft.SawInteraction;
import betterwithmods.craft.bulk.*;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;
import java.util.Map;

public class RecipeUtils
{
    public static void gatherCookableFood()
    {
        Map<ItemStack, ItemStack> furnace = FurnaceRecipes.instance().getSmeltingList();

        for(ItemStack input : furnace.keySet())
        {
            if(input != null)
            {
                if(input.getItem() instanceof ItemFood && input.getItem() != Items.BREAD)
                {
                    ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
                    if(output != null) {
                        BWCrafting.addCauldronRecipe(output.copy(), new ItemStack[]{input.copy()});
                    }
                }
            }
        }
    }

    public static void sawRecipeInit()
    {
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        for(int i = 0; i < recipes.size(); i++)
        {
            IRecipe recipe = recipes.get(i);

            ItemStack output = recipe.getRecipeOutput();
            if(output != null && hasOreName(output, "plankWood"))
            {
                List inputs = null;
                if(recipe instanceof ShapedRecipes)
                {
                    ShapedRecipes r = (ShapedRecipes)recipe;
                    inputs = Lists.newArrayList((Object[])r.recipeItems);
                }
                else if(recipe instanceof ShapelessRecipes)
                {
                    ShapelessRecipes r = (ShapelessRecipes)recipe;
                    inputs = Lists.newArrayList(r.recipeItems);
                }
                else if(recipe instanceof ShapedOreRecipe)
                {
                    ShapedOreRecipe r = (ShapedOreRecipe)recipe;
                    inputs = Lists.newArrayList(r.getInput());
                }
                else if(recipe instanceof ShapelessOreRecipe)
                {
                    ShapelessOreRecipe r = (ShapelessOreRecipe)recipe;
                    inputs = Lists.newArrayList(r.getInput());
                }

                if(inputs != null)
                {
                    ItemStack logInput = null;
                    for(Object input : inputs)
                    {
                        if(input instanceof ItemStack)
                        {
                            ItemStack stack = (ItemStack)input;
                            if(!hasOreName(stack, "logWood") || logInput != null)
                            {
                                logInput = null;
                                break;
                            }
                            logInput = stack.copy();
                        }
                        else if(input instanceof String)
                        {
                            logInput = null;
                            break;
                        }
                    }
                    if(logInput != null && logInput.getItem() instanceof ItemBlock)
                    {
                        ItemStack planks = recipe.getRecipeOutput().copy();
                        planks.stackSize += 2;
                        Block block = ((ItemBlock)logInput.getItem()).getBlock();
                        if(!SawInteraction.contains(block, logInput.getItemDamage()))
                            SawInteraction.addBlock(block, logInput.getItemDamage(), planks);
                    }
                }
            }
        }
    }

    public static void refreshRecipes()
    {
        CraftingManagerCauldron.getInstance().refreshRecipes();
        CraftingManagerCauldronStoked.getInstance().refreshRecipes();
        CraftingManagerCrucible.getInstance().refreshRecipes();
        CraftingManagerCrucibleStoked.getInstance().refreshRecipes();
        CraftingManagerMill.getInstance().refreshRecipes();
    }

    private static boolean hasOreName(ItemStack stack, String oreName)
    {
        if(stack.getItem() == null)
            return false;
        int ID = OreDictionary.getOreID(oreName);
        for(int i : OreDictionary.getOreIDs(stack))
            if(i == ID) return true;
        return false;
    }
}
