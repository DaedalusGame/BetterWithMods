package betterwithmods.common.registry;

import betterwithmods.client.container.anvil.ContainerSteelAnvil;
import betterwithmods.util.InvUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by primetoxinz on 6/27/17.
 */
public class ToolDamageRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    private ResourceLocation group;
    protected Predicate<ItemStack> isTool;
    protected ItemStack result;
    protected Ingredient input;

    public ToolDamageRecipe(ResourceLocation group, ItemStack result, Ingredient input, Predicate<ItemStack> isTool) {
        this.group = group;
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
                } else if (OreDictionary.containsMatch(true, InvUtils.asNonnullList(input.getMatchingStacks()), slot)) {
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

    public ItemStack getExampleStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return isMatch(inv, worldIn);

    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {

        return result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result.copy();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        playSound(inv);
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

    public String getGroup() {
        if (group != null)
            return group.toString();
        return "";
    }

    public void playSound(InventoryCrafting inv) {
        Container container = ReflectionHelper.getPrivateValue(InventoryCrafting.class, inv, "eventHandler", "field_70465_c");
        EntityPlayer player = null;
        if (container instanceof ContainerWorkbench)
            player = ReflectionHelper.getPrivateValue(ContainerWorkbench.class, (ContainerWorkbench) container, "player", "field_192390_i");
        if (container instanceof ContainerPlayer)
            player = ReflectionHelper.getPrivateValue(ContainerPlayer.class, (ContainerPlayer) container, "player", "field_82862_h");
        if (container instanceof ContainerSteelAnvil)
            player = ((ContainerSteelAnvil) container).player;


        if (player != null) {
            player.world.playSound(null, player.getPosition(), getSound(), SoundCategory.BLOCKS, getSoundValues()[0], getSoundValues()[1]);
        }
    }

    public SoundEvent getSound() {
        return null;
    }

    public float[] getSoundValues() {
        return new float[2];
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(input);
        ingredients.add(new IngredientTool(isTool, getExampleStack()));
        return ingredients;
    }
}
