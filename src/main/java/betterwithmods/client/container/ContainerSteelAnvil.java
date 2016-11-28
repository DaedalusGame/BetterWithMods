package betterwithmods.client.container;

import betterwithmods.BWMBlocks;
import betterwithmods.blocks.tile.TileEntitySteelAnvil;
import betterwithmods.client.container.inventory.InventorySteelAnvilCrafting;
import betterwithmods.client.container.inventory.SlotSteelAnvilCrafting;
import betterwithmods.craft.steelanvil.CraftingManagerSteelAnvil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by blueyu2 on 11/22/16.
 */
public class ContainerSteelAnvil extends Container {
    private final int INV_FIRST = 17;
    private final int INV_LAST = 44;
    private final int HOT_LAST = 53;
    protected World world;
    protected BlockPos pos;
    private InventoryCrafting matrix;
    private IInventory result;

    public ContainerSteelAnvil(InventoryPlayer playerInventory, World worldIn, BlockPos pos, TileEntitySteelAnvil anvilIn) {
        this.world = worldIn;
        this.pos = pos;
        matrix = new InventorySteelAnvilCrafting(this, anvilIn);
        result = new InventoryCraftResult();
        this.addSlotToContainer(new SlotSteelAnvilCrafting(playerInventory.player, this.matrix, this.result, 0, 124, 44));

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                this.addSlotToContainer(new Slot(this.matrix, j + i * 4, 12 + j * 18, 17 + i * 18));
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 160));
        }

        this.onCraftMatrixChanged(this.matrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory matrix) {
        this.result.setInventorySlotContents(0, CraftingManagerSteelAnvil.INSTANCE.findMatchingRecipe(this.matrix, this.world));
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.world.getBlockState(pos).getBlock() == BWMBlocks.STEEL_ANVIL && playerIn.getDistanceSq(pos) <= 64.0D;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 16) {
                if (!this.mergeItemStack(itemstack1, INV_FIRST, HOT_LAST, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= INV_FIRST && index < INV_LAST) {
                if (!this.mergeItemStack(itemstack1, INV_LAST, HOT_LAST, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= INV_LAST && index < HOT_LAST) {
                if (!this.mergeItemStack(itemstack1, INV_FIRST, INV_LAST, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, INV_FIRST, HOT_LAST, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
}
