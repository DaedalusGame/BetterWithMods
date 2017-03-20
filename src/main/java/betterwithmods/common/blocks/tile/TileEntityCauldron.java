package betterwithmods.common.blocks.tile;

import betterwithmods.common.BWMItems;
import betterwithmods.common.registry.bulk.CraftingManagerCauldron;
import betterwithmods.common.registry.bulk.CraftingManagerCauldronStoked;
import betterwithmods.util.InvUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class TileEntityCauldron extends TileEntityCookingPot {
    public TileEntityCauldron() {
        super(CraftingManagerCauldron.getInstance(), CraftingManagerCauldronStoked.getInstance());
    }

    @Override
    public boolean validateUnstoked() {
        if(InvUtils.getFirstOccupiedStackOfItem(inventory, BWMItems.MATERIAL, 5) > -1 && hasNonFoulFood()) {
            this.containsValidIngredients = true;
            return false;
        }
        return true;
    }

    @Override
    public boolean validateStoked() {
        if (containsExplosives()) {
            this.containsValidIngredients = true;
            return false;
        }
        return true;
    }

    @Override
    protected boolean attemptToCookNormal() {
        int dung = InvUtils.getFirstOccupiedStackOfItem(inventory, BWMItems.MATERIAL, 5);
        if (dung > -1 && this.hasNonFoulFood()) {
            return spoilFood();
        } else
            return super.attemptToCookNormal();
    }

    private boolean hasNonFoulFood() {
        for (int i = 0; i < 27; i++) {
            if (this.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
                Item item = this.inventory.getStackInSlot(i).getItem();
                if (item != null) {
                    if (item instanceof ItemFood) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean spoilFood() {
        boolean foodSpoiled = false;
        for (int i = 0; i < 27; i++) {
            if (this.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
                Item item = this.inventory.getStackInSlot(i).getItem();
                if (item != null) {
                    if (item != BWMItems.FERTILIZER && item instanceof ItemFood) {
                        int sizeOfStack = this.inventory.getStackInSlot(i).getCount();
                        ItemStack spoiled = new ItemStack(BWMItems.FERTILIZER, sizeOfStack);
                        this.inventory.setStackInSlot(i, spoiled);
                        foodSpoiled = true;
                    }
                }
            }
        }
        return foodSpoiled;
    }


    @Override
    public String getName() {
        return "inv.cauldron.name";
    }
}
