package betterwithmods.common.blocks.tile;

import betterwithmods.common.BWMItems;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import betterwithmods.common.registry.bulk.manager.StokedCauldronManager;
import betterwithmods.util.InvUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class TileEntityCauldron extends TileEntityCookingPot {
    public TileEntityCauldron() {
        super(CauldronManager.getInstance(), StokedCauldronManager.getInstance());
    }

    @Override
    public boolean validateUnstoked() {
        return CauldronManager.getInstance().getCraftingResult(inventory) != null;
    }

    @Override
    public boolean validateStoked() {
        return StokedCauldronManager.getInstance().getCraftingResult(inventory) != null;
    }

    @Override
    protected boolean attemptToCookNormal() {
        return super.attemptToCookNormal();
    }


    @Override
    public String getName() {
        return "inv.cauldron.name";
    }

}
