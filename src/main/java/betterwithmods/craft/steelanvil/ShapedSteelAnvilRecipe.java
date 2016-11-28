package betterwithmods.craft.steelanvil;

import betterwithmods.integration.minetweaker.utils.InputHelper;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Code based off of Avarita https://minecraft.curseforge.com/projects/avaritia
 */
public class ShapedSteelAnvilRecipe implements IRecipe {
    private static final int MAX_MATRIX_WIDTH = 4;
    private static final int MAX_MATRIX_HEIGHT = 4;
    public int width;
    public int height;
    private ItemStack output = null;
    private Object[] input = null;
    private boolean mirrored = true;

    public ShapedSteelAnvilRecipe(Block result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    public ShapedSteelAnvilRecipe(Item result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    public ShapedSteelAnvilRecipe(ItemStack result, Object... recipe) {
        output = result.copy();

        String shape = "";
        int idx = 0;

        if (recipe[idx] instanceof String[]) {
            String[] parts = ((String[]) recipe[idx++]);

            for (String s : parts) {
                width = s.length();
                shape += s;
            }

            height = parts.length;
        } else {
            while (recipe[idx] instanceof String) {
                String s = (String) recipe[idx++];
                shape += s;
                width = s.length();
                height++;
            }
        }

        if (width * height != shape.length()) {
            String ret = "Invalid shaped ore recipe: ";
            for (Object tmp : recipe) {
                ret += tmp + ", ";
            }
            ret += output;
            throw new RuntimeException(ret);
        }

        HashMap<Character, Object> itemMap = new HashMap<>();

        for (; idx < recipe.length; idx += 2) {
            Character chr = (Character) recipe[idx];
            Object in = recipe[idx + 1];

            if (in instanceof ItemStack) {
                itemMap.put(chr, ((ItemStack) in).copy());
            } else if (in instanceof Item) {
                itemMap.put(chr, new ItemStack((Item) in));
            } else if (in instanceof Block) {
                itemMap.put(chr, new ItemStack((Block) in, 1, OreDictionary.WILDCARD_VALUE));
            } else if (in instanceof String) {
                itemMap.put(chr, in);
            } else {
                String ret = "Invalid shaped ore recipe: ";
                for (Object tmp : recipe) {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }

        input = new Object[width * height];
        int x = 0;
        for (char chr : shape.toCharArray()) {
            input[x++] = itemMap.get(chr);
        }
    }

    public ShapedSteelAnvilRecipe(IItemStack result, IIngredient[][] recipe) {
        output = InputHelper.toStack(result);

        height = recipe.length;
        for (IIngredient[] row : recipe) {
            if (width < row.length)
                width = row.length;
        }

        input = new Object[width * height];

        int count = 0;
        for (IIngredient[] row : recipe) {
            for (IIngredient ingredient : row) {
                if (ingredient instanceof IItemStack) {
                    input[count++] = InputHelper.toStack((IItemStack) ingredient);
                } else if (ingredient instanceof IOreDictEntry) {
                    input[count++] = ((IOreDictEntry) ingredient).getName();
                }
            }
        }
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        for (int x = 0; x <= MAX_MATRIX_WIDTH - width; x++) {
            for (int y = 0; y <= MAX_MATRIX_HEIGHT - height; ++y) {
                if (checkMatch(inv, x, y, false)) {
                    return true;
                }

                if (mirrored && checkMatch(inv, x, y, true)) {
                    return true;
                }
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
        for (int x = 0; x < MAX_MATRIX_WIDTH; x++) {
            for (int y = 0; y < MAX_MATRIX_HEIGHT; y++) {
                int subX = x - startX;
                int subY = y - startY;
                Object target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
                    if (mirror) {
                        target = input[width - subX - 1 + subY * width];
                    } else {
                        target = input[subX + subY * width];
                    }
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                if (target instanceof ItemStack) {
                    if (!OreDictionary.itemMatches((ItemStack) target, slot, false)) {
                        return false;
                    }
                } else if (target instanceof String) {
                    boolean matched = false;
                    Iterator<ItemStack> itr = OreDictionary.getOres((String) target).iterator();
                    while (itr.hasNext() && !matched) {
                        ItemStack testy = itr.next();
                        matched = OreDictionary.itemMatches(testy, slot, false);
                    }

                    if (!matched) {
                        return false;
                    }
                } else if (target == null && slot != null) {
                    return false;
                }
            }
        }

        return true;
    }

    public ShapedSteelAnvilRecipe setMirrored(boolean mirror) {
        mirrored = mirror;
        return this;
    }

    @Nullable
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return output.copy();
    }

    @Override
    public int getRecipeSize() {
        return input.length;
    }

    @Nullable
    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    public Object[] getInput() {
        return this.input;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
