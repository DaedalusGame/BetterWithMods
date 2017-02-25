package betterwithmods.common.blocks;

import betterwithmods.BWMod;
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
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class BehaviorDefaultDispenseBlock extends BehaviorDefaultDispenseItem {
    @Override
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        EnumFacing facing = source.getBlockState().getValue(BlockBDispenser.FACING);
        IPosition pos = BlockBDispenser.getDispensePosition(source);
        BlockPos check = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        ItemStack stack1 = stack.splitStack(1);
        World world = source.getWorld();
        FakePlayer fake = FakePlayerFactory.getMinecraft((WorldServer) world);
        DirUtils.setEntityOrientationFacing(fake, facing.getOpposite());
        if (BWConfig.debug)
            BWMod.logger.debug("Better With Mods FakePlayer ID: " + fake.getUniqueID());
        if (stack.getItem() instanceof ItemBlock && (world.isAirBlock(check) || world.getBlockState(check).getBlock().isReplaceable(world, check))) {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            boolean blockAcross = !world.isAirBlock(check.offset(facing));
            IBlockState state = block.getStateForPlacement(world, check, facing, getX(facing, blockAcross), getY(facing, blockAcross), getZ(facing, blockAcross), stack.getItemDamage(), fake, fake.getActiveHand());
            if (block.canPlaceBlockAt(world, check)) {
                if (((ItemBlock) stack.getItem()).placeBlockAt(stack1, fake, world, check, facing, getX(facing, blockAcross), getY(facing, blockAcross), getZ(facing, blockAcross), state)) {
                    world.playSound(null, check, state.getBlock().getSoundType(state, world, check, fake).getPlaceSound(), SoundCategory.BLOCKS, 0.7F, 1.0F);
                    return stack;
                }
            } else {
                stack.grow(1);
                return stack;
            }

        } else if (stack.getItem() instanceof ItemBlockSpecial) {
            if (stack.getItem().onItemUse(fake, world, check, EnumHand.MAIN_HAND, facing, 0.1F, 0.0F, 0.1F) == EnumActionResult.SUCCESS) {
                return stack;
            } else {
                stack.grow(1);
                return stack;
            }
        } else if (stack.getItem() instanceof ItemSeeds) {
            if (stack.getItem().onItemUse(fake, world, check.down(), EnumHand.MAIN_HAND, EnumFacing.UP, 0.1F, 0.0F, 0.1F) == EnumActionResult.SUCCESS) {
                return stack;
            } else {
                stack.grow(1);
                return stack;
            }
        } else {
            stack.grow(1);
            return stack;
        }
        return stack;
    }

    private float getX(EnumFacing facing, boolean blockAcross) {
        return facing == EnumFacing.NORTH && blockAcross ? 0.9F : 0.1F;
    }

    private float getY(EnumFacing facing, boolean blockAcross) {
        return facing == EnumFacing.UP && blockAcross ? 0.9F : 0.1F;
    }

    private float getZ(EnumFacing facing, boolean blockAcross) {
        return facing == EnumFacing.WEST && blockAcross ? 0.9F : 0.1F;
    }
}
