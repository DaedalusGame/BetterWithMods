package betterwithmods.common.registry;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by primetoxinz on 6/27/17.
 */
public class ToolDamageRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private Predicate<ItemStack> isTool;
    private ItemStack input = ItemStack.EMPTY, result = ItemStack.EMPTY;

    public ToolDamageRecipe(ItemStack result, ItemStack input, Predicate<ItemStack> isTool) {
        this.isTool = isTool;
        this.result = result;
        this.input = input;
    }

    public boolean isMatch(IInventory inv, World world) {
        boolean hasTool = false, hasInput = false;
        for (int x = 0; x < inv.getSizeInventory(); x++) {
            boolean inRecipe = false;
            ItemStack slot = inv.getStackInSlot(x);

            if (!slot.isEmpty()) {
                if (isTool.test(slot)) {
                    if (!hasTool) {
                        hasTool = true;
                        inRecipe = true;
                    } else
                        return false;
                } else if (OreDictionary.itemMatches(slot, input, true)) {
                    if (!hasInput) {
                        hasInput = true;
                        inRecipe = true;
                    } else
                        return false;
                }
                if (!inRecipe)
                    return false;
            }
        }
        return hasTool && hasInput;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return isMatch(inv, worldIn);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return result;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && isTool.test(stack)) {
                ItemStack copy = stack.copy();
                if (!copy.attemptDamageItem(1, new Random(), null)) {
                    stacks.set(i, copy.copy());
                }
            }
        }
        return stacks;
    }
}
