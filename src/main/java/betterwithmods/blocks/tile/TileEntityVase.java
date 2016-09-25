package betterwithmods.blocks.tile;

import betterwithmods.BWMBlocks;
import betterwithmods.BWSounds;
import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.blocks.BlockVase;
import betterwithmods.craft.bulk.CraftingManagerMill;
import betterwithmods.items.ItemMaterial;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 24.09.2016.
 */
public class TileEntityVase extends TileBasicInventory {
    public TileEntityVase() {
    }

    public boolean onActivated(EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem) {
        ItemStack insertstack = heldItem.copy();
        insertstack.stackSize = 1;
        boolean flag = tryInsert(inventory,insertstack);

        if(flag) {
            if(!playerIn.isCreative()) {
                heldItem.stackSize -= 1;
                playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, heldItem.stackSize == 0 ? null : heldItem);
            }
            this.worldObj.playSound((EntityPlayer) null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }

        return flag;
    }

    public void onBreak()
    {
        ItemStack vaseitem = inventory.getStackInSlot(0);
        if(vaseitem != null && vaseitem.isItemEqual(ItemMaterial.getMaterial("blasting_oil")))
        {
            float intensity = 1.5f; //TODO: fiddle with this.
            worldObj.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), intensity, true);
        }
    }

    public boolean tryInsert(IItemHandler inv, ItemStack stack)
    {
        if(stack.stackSize > 1 || inv.getStackInSlot(0) != null) return false;
        inv.insertItem(0, stack, false);
        return true;
    }

    @Override
    public SimpleItemStackHandler createItemStackHandler() {
        return new SimpleItemStackHandler(this, true, 1);
    }
}
