package betterwithmods.common.blocks.tile;

import betterwithmods.common.blocks.BlockStake;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.util.InvUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by tyler on 5/17/17.
 */
public class TileStake extends TileBasicInventory {
    private boolean[] connected = new boolean[6];

    public boolean onActivated(EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem) {
        ItemStack insertedStack = heldItem.copy();
        insertedStack.setCount(1);
        if(InvUtils.insert(inventory,insertedStack,false)) {
            if(!playerIn.isCreative())
                heldItem.shrink(1);
            this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                    SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
                    ((getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
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

    public void connectSide(World world, EnumFacing facing) {
        inventory.setStackInSlot(0,new ItemStack(Blocks.STONE));
//        BlockPos stakePos = findStake(world,facing);
//        System.out.println(Arrays.toString(connected) + ","+ inventory);
//        if(pos != null && !connected[facing.getIndex()]) {
//            connected[facing.getIndex()] = true;
//            inventory.setStackInSlot(facing.getIndex(), new ItemStack(BWMItems.SAND_PILE,WorldUtils.getDistance(getPos(),stakePos)));
//        }
        System.out.println(inventory);
    }

    public BlockPos findStake(World world, EnumFacing facing) {
        BlockPos pos = getPos();
        boolean isStake = false;
        int i = 0;
        while(!isStake && i < 64) {
            pos = pos.offset(facing);
            if(world.getBlockState(pos).getBlock() instanceof BlockStake)
                isStake = true;
            i++;
        }
        System.out.println(world.getBlockState(pos) + ","+ i);
        if(isStake)
            return pos;
        return null;
    }
}
