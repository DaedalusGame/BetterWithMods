package betterwithmods;

import betterwithmods.items.ItemMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class BWFuelHandler implements IFuelHandler {

    @Override
    public int getBurnTime(ItemStack fuel) {
        Item item = fuel.getItem();
        int meta = fuel.getItemDamage();
        if (item == BWMItems.MATERIAL && meta == 1)
            return 3200;
        else if (item == BWMItems.MATERIAL && meta == 22)
            return 25;
        else if (item == BWMItems.MATERIAL && meta == 23)
            return 25;
        else if (item == BWMItems.BARK)
            return 25;
        else if (item == Item.getItemFromBlock(BWMBlocks.WOOD_SIDING))
            return 150;
        else if (item == Item.getItemFromBlock(BWMBlocks.WOOD_MOULDING))
            return 75;
        else if (item == Item.getItemFromBlock(BWMBlocks.WOOD_CORNER))
            return 38;
        return 0;
    }

}
