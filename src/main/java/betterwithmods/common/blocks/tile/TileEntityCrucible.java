package betterwithmods.common.blocks.tile;

import betterwithmods.common.registry.bulk.CraftingManagerCrucible;
import betterwithmods.common.registry.bulk.CraftingManagerCrucibleStoked;

public class TileEntityCrucible extends TileEntityCookingPot {
    public TileEntityCrucible() {
        super(CraftingManagerCrucible.getInstance(),CraftingManagerCrucibleStoked.getInstance());
    }

    @Override
    public boolean validateStoked() {
        if (containsExplosives()) {
            this.containsValidIngredients = false;
            return false;
        }
        return true;
    }

    @Override
    public boolean validateUnstoked() {
        return true;
    }
    @Override
    public String getName() {
        return "inv.crucible.name";
    }

}
