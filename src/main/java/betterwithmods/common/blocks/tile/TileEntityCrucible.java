package betterwithmods.common.blocks.tile;

import betterwithmods.common.registry.bulk.manager.CrucibleManager;
import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;

public class TileEntityCrucible extends TileEntityCookingPot {
    public TileEntityCrucible() {
        super(CrucibleManager.getInstance(), StokedCrucibleManager.getInstance());
    }

    @Override
    public boolean validateStoked() {
        if (containsExplosives())
            return false;
        if (StokedCrucibleManager.getInstance().getCraftingResult(inventory) != null)
            return true;
        return false;
    }

    @Override
    public boolean validateUnstoked() {
        if (CrucibleManager.getInstance().getCraftingResult(inventory) != null)
            return true;
        return false;
    }

    @Override
    public String getName() {
        return "inv.crucible.name";
    }

}
