package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.util.InvUtils;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by primetoxinz on 6/24/17.
 */
public class CauldronFoodRecipe extends CauldronRecipe {

    public CauldronFoodRecipe(ItemStack output, Object[] inputs) {
        super(output, inputs);
        setPriority(1);
    }

    @Override
    public NonNullList<ItemStack> onCraft(World world, TileEntity tile, ItemStackHandler inv) {
        if (shouldFoul(inv)) {
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack item = inv.getStackInSlot(i);
                if (item.getItem() instanceof ItemFood) {
                    ItemStack fertilizer = new ItemStack(BWMItems.FERTILIZER, item.getCount());
                    inv.setStackInSlot(i, fertilizer);
                }
            }
            return NonNullList.create();
        } else {
            return super.onCraft(world, tile, inv);
        }

    }

    public boolean shouldFoul(ItemStackHandler inv) {
        return InvUtils.getFirstOccupiedStackOfItem(inv, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG)) > -1;
    }
}