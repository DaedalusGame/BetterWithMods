package betterwithmods.common.blocks.tile;

import betterwithmods.common.registry.bulk.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.CraftingManagerCrucible;
import betterwithmods.common.registry.bulk.CraftingManagerCrucibleStoked;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntityCrucible extends TileEntityCookingPot {
    public TileEntityCrucible() {
        super();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.cookCounter = tag.getInteger("CrucibleCookTime");
        if (tag.hasKey("StokedCooldown"))
            this.stokedCooldownCounter = tag.getInteger("StokedCooldown");
        if (tag.hasKey("ContainsValidIngredients"))
            this.containsValidIngredients = tag.getBoolean("ContainsValidIngredients");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        t.setInteger("CrucibleCookTime", this.cookCounter);
        t.setInteger("StokedCooldown", this.stokedCooldownCounter);
        t.setBoolean("ContainsValidIngredients", this.containsValidIngredients);
        return t;
    }

    @Override
    public void validateContents() {
        this.containsValidIngredients = false;
        if (this.fireIntensity > 0 && this.fireIntensity < 5) {
            if (CraftingManagerCrucible.getInstance().getCraftingResult(inventory) != null)
                this.containsValidIngredients = true;
        } else {
            if (containsExplosives())
                this.containsValidIngredients = false;//true;
            else if (CraftingManagerCrucibleStoked.getInstance().getCraftingResult(inventory) != null)
                this.containsValidIngredients = true;
        }
    }

    @Override
    protected CraftingManagerBulk getCraftingManager(boolean stoked) {
        if (stoked)
            return CraftingManagerCrucibleStoked.getInstance();
        else
            return CraftingManagerCrucible.getInstance();
    }


    @Override
    public String getName() {
        return "inv.crucible.name";
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
