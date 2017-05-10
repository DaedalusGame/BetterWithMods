package betterwithmods.common.registry;

import betterwithmods.common.BWOreDictionary;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

public class DyeWithTagRecipe extends ShapelessOreRecipe {
    private final ItemStack taggedItem;
    private final String dye;

    public DyeWithTagRecipe(ItemStack output, ItemStack taggedItem, String dye) {
        super(output, taggedItem, dye);
        this.taggedItem = taggedItem;
        this.dye = dye;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return isMatch(inv);
    }

    private ItemStack getTagStack(IInventory inv) {
        for (int x = 0; x < inv.getSizeInventory(); x++) {
            ItemStack slot = inv.getStackInSlot(x);
            if (OreDictionary.itemMatches(taggedItem, slot, false))
                return slot.copy();
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack output = this.output.copy();
        ItemStack tagStack = getTagStack(inv);
        if (tagStack.hasTagCompound()) {
            NBTTagCompound tag = tagStack.getTagCompound();
            if (tag != null)
                output.setTagCompound(tag.copy());
            return output;
        }
        return output;
    }

    private boolean isMatch(IInventory inv) {
        boolean hasItem = false;
        boolean hasDye = false;
        for (int x = 0; x < inv.getSizeInventory(); x++) {
            boolean inRecipe = false;
            ItemStack slot = inv.getStackInSlot(x);

            if (slot != ItemStack.EMPTY) {
                if (OreDictionary.itemMatches(taggedItem, slot, false)) {
                    if(!hasItem) {
                        hasItem = true;
                        inRecipe = true;
                    }
                    else
                        return false;
                }
                else if (BWOreDictionary.listContains(slot, OreDictionary.getOres(dye))) {
                    if(!hasDye) {
                        hasDye = true;
                        inRecipe = true;
                    }
                    else
                        return false;
                }
                if(!inRecipe)
                    return false;
            }
        }
        return hasItem && hasDye;
    }
}
