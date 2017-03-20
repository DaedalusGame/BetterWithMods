package betterwithmods.common.blocks.tile;

import betterwithmods.common.items.ItemMaterial;
import betterwithmods.util.InvUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Created by Christian on 24.09.2016.
 */
public class TileEntityVase extends TileBasicInventory {
    public TileEntityVase() {
    }

    public boolean onActivated(EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem) {
        ItemStack insertedStack = heldItem.copy();
        insertedStack.setCount(1);
        boolean flag = tryInsert(inventory, insertedStack);

        if (flag) {
            if (!playerIn.isCreative()) {
                heldItem.shrink(1);
                playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, heldItem.getCount() == 0 ? ItemStack.EMPTY : heldItem);
            }
            this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                    SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
                    ((getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }

        return flag;
    }

    public void onBreak() {
        ItemStack vaseitem = inventory.getStackInSlot(0);
        if (vaseitem != ItemStack.EMPTY && vaseitem.isItemEqual(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL))) {
            float intensity = 1.5f; // TODO: fiddle with this.
            getWorld().createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), intensity, true);
        } else if (vaseitem != ItemStack.EMPTY) {
            InvUtils.ejectStackWithOffset(getWorld(), pos, vaseitem);
        }
    }

    public boolean tryInsert(IItemHandler inv, ItemStack stack) {
        if (stack.getCount() > 1 || inv.getStackInSlot(0) != ItemStack.EMPTY)
            return false;
        else {
            if (!world.isRemote)
                inv.insertItem(0, stack, false);
            return true;
        }
    }

    @Override
    public ItemStackHandler createItemStackHandler() {
        return new ItemStackHandler( 1);
    }
}
