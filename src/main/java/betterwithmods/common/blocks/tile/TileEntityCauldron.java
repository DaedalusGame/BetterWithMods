package betterwithmods.common.blocks.tile;

import betterwithmods.common.BWMItems;
import betterwithmods.common.registry.bulk.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.CraftingManagerCauldron;
import betterwithmods.common.registry.bulk.CraftingManagerCauldronStoked;
import betterwithmods.util.InvUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntityCauldron extends TileEntityCookingPot {
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("CauldronCookTime"))
            this.cookCounter = tag.getInteger("CauldronCookTime");
        if (tag.hasKey("RenderCooldown"))
            this.stokedCooldownCounter = tag.getInteger("RenderCooldown");
        if (tag.hasKey("ContainsValidIngredients"))
            this.containsValidIngredients = tag.getBoolean("ContainsValidIngredients");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        t.setInteger("CauldronCookTime", this.cookCounter);
        t.setInteger("RenderCooldown", this.stokedCooldownCounter);
        t.setBoolean("ContainsValidIngredients", this.containsValidIngredients);
        return t;
    }

    @Override
    public void validateContents() {
        this.containsValidIngredients = false;

        if (this.fireIntensity > 0 && this.fireIntensity < 5) {
            if (InvUtils.getFirstOccupiedStackOfItem(inventory, BWMItems.MATERIAL, 5) > -1 && hasNonFoulFood()) {
                this.containsValidIngredients = true;
            } else if (CraftingManagerCauldron.getInstance().getCraftingResult(inventory) != null)
                this.containsValidIngredients = true;
        } else if (this.fireIntensity > 5) {
            if (containsExplosives())
                this.containsValidIngredients = true;
            else if (CraftingManagerCauldronStoked.getInstance().getCraftingResult(inventory) != null)
                this.containsValidIngredients = true;
        }
    }

    @Override
    protected CraftingManagerBulk getCraftingManager(boolean stoked) {
        if (stoked)
            return CraftingManagerCauldronStoked.getInstance();
        else
            return CraftingManagerCauldron.getInstance();
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

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public void readFromTag(NBTTagCompound tag) {

    }

    @Override
    public NBTTagCompound writeToTag(NBTTagCompound tag) {
        return null;
    }
}
