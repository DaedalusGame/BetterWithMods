package betterwithmods.common.blocks.tile;

import betterwithmods.common.registry.bulk.CraftingManagerCrucible;
import betterwithmods.common.registry.bulk.CraftingManagerCrucibleStoked;

public class TileEntityCrucible extends TileEntityCookingPot {
    public TileEntityCrucible() {
        super(CraftingManagerCrucible.getInstance(), CraftingManagerCrucibleStoked.getInstance());
    }

    @Override
    public boolean validateStoked() {
        if (containsExplosives())
            return false;
        if (CraftingManagerCrucibleStoked.getInstance().getCraftingResult(inventory) != null)
            return true;
        return false;
    }

    @Override
    public boolean validateUnstoked() {
        if (CraftingManagerCrucible.getInstance().getCraftingResult(inventory) != null)
            return true;
        return false;
    }

    @Override
    public String getName() {
        return "inv.crucible.name";
    }

}
