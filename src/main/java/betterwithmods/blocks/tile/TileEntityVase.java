package betterwithmods.blocks.tile;

import betterwithmods.items.ItemMaterial;
import betterwithmods.util.InvUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * Created by Christian on 24.09.2016.
 */
public class TileEntityVase extends TileBasicInventory {
    public TileEntityVase() {
    }

    public boolean onActivated(EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem) {
        ItemStack insertstack = heldItem.copy();
        insertstack.func_190920_e(1);
        boolean flag = tryInsert(inventory, insertstack);

        if (flag) {
            if (!playerIn.isCreative()) {
                heldItem.func_190918_g(1);
                playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, heldItem.func_190916_E() == 0 ? ItemStack.field_190927_a : heldItem);
            }
            this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                    SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
                    ((getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }

        return flag;
    }

    public void onBreak() {
        ItemStack vaseitem = inventory.getStackInSlot(0);
        if (vaseitem != ItemStack.field_190927_a && vaseitem.isItemEqual(ItemMaterial.getMaterial("blasting_oil"))) {
            float intensity = 1.5f; // TODO: fiddle with this.
            getWorld().createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), intensity, true);
        } else {
            InvUtils.ejectInventoryContents(getWorld(), pos, getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
        }
    }

    public boolean tryInsert(IItemHandler inv, ItemStack stack) {
        if (stack.func_190916_E() > 1 || inv.getStackInSlot(0) != ItemStack.field_190927_a)
            return false;
        inv.insertItem(0, stack, false);
        return true;
    }

    @Override
    public SimpleItemStackHandler createItemStackHandler() {
        return new SimpleItemStackHandler(this, true, 1);
    }
}
