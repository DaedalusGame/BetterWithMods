package betterwithmods.blocks;

import betterwithmods.config.BWConfig;
import betterwithmods.util.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BehaviorDefaultDispenseBlock extends BehaviorDefaultDispenseItem
{
    @Override
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
    {
        EnumFacing facing = source.func_189992_e().getValue(BlockBDispenser.FACING);
        IPosition pos = BlockBDispenser.getDispensePosition(source);
        BlockPos check = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        ItemStack stack1 = stack.splitStack(1);
        FakePlayer fake = FakePlayerFactory.getMinecraft((WorldServer)source.getWorld());
        DirUtils.setEntityOrientationFacing(fake, facing.getOpposite());
        if(BWConfig.debug)
            Logger.getLogger("betterwithmods").log(Level.SEVERE, "Better With Mods FakePlayer ID: " + fake.getUniqueID());
        if(stack.getItem() instanceof ItemBlock && (source.getWorld().isAirBlock(check) || source.getWorld().getBlockState(check).getBlock().isReplaceable(source.getWorld(), check)))
        {
            Block block = ((ItemBlock)stack.getItem()).getBlock();
            boolean blockAcross = !source.getWorld().isAirBlock(check.offset(facing));
            IBlockState state = block.onBlockPlaced(source.getWorld(), check, facing, getX(facing, blockAcross), getY(facing, blockAcross), getZ(facing, blockAcross), stack.getItemDamage(), fake);
            if(block.canPlaceBlockAt(source.getWorld(), check)) {
                if (((ItemBlock) stack.getItem()).placeBlockAt(stack1, fake, source.getWorld(), check, facing, getX(facing, blockAcross), getY(facing, blockAcross), getZ(facing, blockAcross), state)) {
                    source.getWorld().playSound(null, check, state.getBlock().getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 0.7F, 1.0F);
                    return stack;
                }
            }
            else {
                stack.stackSize += 1;
                return stack;
            }

        }
        else if(stack.getItem() instanceof ItemBlockSpecial)
        {
            if(stack.getItem().onItemUse(stack1, fake, source.getWorld(), check, EnumHand.MAIN_HAND, facing, 0.1F, 0.0F, 0.1F) == EnumActionResult.SUCCESS) {
                return stack;
            }
            else {
                stack.stackSize += 1;
                return stack;
            }
        }
        else if(stack.getItem() instanceof ItemSeeds)
        {
            if(stack.getItem().onItemUse(stack1, fake, source.getWorld(), check.down(), EnumHand.MAIN_HAND, EnumFacing.UP, 0.1F, 0.0F, 0.1F) == EnumActionResult.SUCCESS) {
                return stack;
            }
            else {
                stack.stackSize += 1;
                return stack;
            }
        }
        else {
            stack.stackSize += 1;
            return stack;
        }
        return stack;
    }

    private float getX(EnumFacing facing, boolean blockAcross)
    {
        return facing == EnumFacing.NORTH && blockAcross ? 0.9F : 0.1F;
    }

    private float getY(EnumFacing facing, boolean blockAcross)
    {
        return facing == EnumFacing.UP && blockAcross ? 0.9F : 0.1F;
    }

    private float getZ(EnumFacing facing, boolean blockAcross)
    {
        return facing == EnumFacing.WEST && blockAcross ? 0.9F : 0.1F;
    }
}
