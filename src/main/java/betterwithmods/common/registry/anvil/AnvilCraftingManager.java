package betterwithmods.common.registry.anvil;

import betterwithmods.BWMod;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class AnvilCraftingManager {

    public static IForgeRegistry<IRecipe> VANILLA_CRAFTING = GameRegistry.findRegistry(IRecipe.class);

    /**
     * Retrieves an ItemStack that has multiple recipes for it.
     */
    public static ItemStack findMatchingResult(InventoryCrafting inventory, World world) {
        for (IRecipe irecipe : VANILLA_CRAFTING) {
            if (irecipe.matches(inventory, world)) {
                return irecipe.getCraftingResult(inventory);
            }
        }
        return ItemStack.EMPTY;
    }


    public static NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventory, World craftMatrix) {
        for (IRecipe irecipe : VANILLA_CRAFTING) {
            if (irecipe.matches(inventory, craftMatrix)) {
                return irecipe.getRemainingItems(inventory);
            }
        }

        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inventory.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            nonnulllist.set(i, inventory.getStackInSlot(i));
        }

        return nonnulllist;
    }
}