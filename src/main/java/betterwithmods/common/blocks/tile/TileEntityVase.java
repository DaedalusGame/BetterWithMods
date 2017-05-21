package betterwithmods.common.blocks.tile;

import betterwithmods.common.items.ItemMaterial;
import betterwithmods.util.InvUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import javax.annotation.Nullable;

/**
 * Created by Christian on 24.09.2016.
 */
public class TileEntityVase extends TileBasicInventory {
    public TileEntityVase() {
    }

    public boolean onActivated(EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem) {
        if(InvUtils.insertSingle(inventory,heldItem,false)) {


            return true;
        }
        return false;
    }

    @Override
    public void onBreak() {
        ItemStack vaseitem = inventory.getStackInSlot(0);
        if (vaseitem.isItemEqual(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL))) {
            float intensity = 1.5f;
            getWorld().createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), intensity, true);
        } else  {
            super.onBreak();
        }
    }


    @Override
    public int getInventorySize() {
        return 1;
    }

}
