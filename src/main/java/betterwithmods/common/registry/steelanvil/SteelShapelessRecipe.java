package betterwithmods.common.registry.steelanvil;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SteelShapelessRecipe implements IRecipe {

    private static final int WIDTH = 4, HEIGHT = 4;
    /**
     * Is the ItemStack that you get when craft the recipe.
     */
    private final ItemStack recipeOutput;
    /**
     * Is a List of ItemStack that composes the recipe.
     */
    public final ArrayList<Object> recipeItems = new ArrayList<Object>();

    public SteelShapelessRecipe(ItemStack result, List ingredients) {
        this.recipeOutput = result;
        for (Object in : ingredients) {
            if (in instanceof ItemStack)
            {
                recipeItems.add(((ItemStack)in).copy());
            }
            else if (in instanceof Item)
            {
                recipeItems.add(new ItemStack((Item)in));
            }
            else if (in instanceof Block)
            {
                recipeItems.add(new ItemStack((Block)in));
            }
            else if (in instanceof String)
            {
                recipeItems.add(OreDictionary.getOres((String)in));
            }
            else if (in instanceof List)
            {
                recipeItems.add(in);
            }
            else
            {
                String ret = "Invalid shapeless ore recipe: ";
                for (Object tmp :  ingredients)
                {
                    ret += tmp + ", ";
                }
                ret += recipeOutput;
                throw new RuntimeException(ret);
            }
        }
    }

    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.create();
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting matrix, World world) {
        ArrayList arraylist = new ArrayList(this.recipeItems);

        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT; ++j) {
                ItemStack itemstack = matrix.getStackInRowAndColumn(j, i);

                if (!itemstack.isEmpty()) {
                    boolean flag = false;
                    Iterator iterator = arraylist.iterator();

                    while (iterator.hasNext()) {
                        Object next = iterator.next();

                        if(next instanceof ItemStack) {
                            ItemStack itemstack1 = (ItemStack)next;
                            if (itemstack.getItem() == itemstack1.getItem() && (itemstack1.getItemDamage() == 32767 || itemstack.getItemDamage() == itemstack1.getItemDamage())) {
                                flag = true;
                                arraylist.remove(itemstack1);
                                break;
                            }
                        }
                        else if(next instanceof List)
                        {
                            Iterator<ItemStack> itr = ((List<ItemStack>)next).iterator();
                            while (itr.hasNext() && !flag)
                            {
                                flag = OreDictionary.itemMatches(itr.next(), itemstack, false);
                            }
                            if(flag) {
                                arraylist.remove(next);
                                break;
                            }
                        }
                    }

                    if (!flag) {
                        return false;
                    }
                }
            }
        }

        return arraylist.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting matrix) {
        return this.recipeOutput.copy();
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize() {
        return this.recipeItems.size();
    }
}