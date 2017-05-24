package betterwithmods.common.registry.steelanvil;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class SteelShapedRecipe implements IRecipe {
    private static final int WIDTH = 4, HEIGHT = 4;
    /**
     * How many horizontal slots this recipe is wide.
     */
    public final int width;
    /**
     * How many vertical slots this recipe uses.
     */
    public final int height;

    /**
     * Is a array of ItemStack that composes the recipe.
     */
    public final ItemStack[] recipeItems;
    /**
     * Is the ItemStack that you get when craft the recipe.
     */
    private ItemStack recipeOutput;

    public SteelShapedRecipe(int width, int height, ItemStack[] ingredients, ItemStack result) {
        this.width = width;
        this.height = height;
        this.recipeItems = ingredients;
        this.recipeOutput = result;
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
        for (int i = 0; i <= WIDTH - this.width; ++i) {
            for (int j = 0; j <= HEIGHT - this.height; ++j) {
                if (this.checkMatch(matrix, i, j, true)) {
                    return true;
                }

                if (this.checkMatch(matrix, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the region of a crafting inventory is match for the recipe.
     */
    private boolean checkMatch(InventoryCrafting matrix, int x, int y, boolean mirrored) {
        for (int k = 0; k < WIDTH; ++k) {
            for (int l = 0; l < HEIGHT; ++l) {
                int i1 = k - x;
                int j1 = l - y;
                ItemStack itemstack = ItemStack.EMPTY;

                if (i1 >= 0 && j1 >= 0 && i1 < this.width && j1 < this.height) {
                    if (mirrored) {
                        itemstack = this.recipeItems[this.width - i1 - 1 + j1 * this.width];
                    } else {
                        itemstack = this.recipeItems[i1 + j1 * this.width];
                    }
                }
                ItemStack itemstack1 = matrix.getStackInRowAndColumn(k, l);

                if (!itemstack1.isEmpty() || !itemstack.isEmpty()) {
                    if (itemstack1.isEmpty() && !itemstack.isEmpty() || !itemstack1.isEmpty() && itemstack.isEmpty()) {
                        return false;
                    }

                    if (itemstack.getItem() != itemstack1.getItem()) {
                        return false;
                    }

                    if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack1.getItemDamage()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
        return this.getRecipeOutput().copy();
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize() {
        return this.width * this.height;
    }

    public ItemStack[] getInput() {
        return recipeItems;
    }
}