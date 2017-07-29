package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GrassPath extends Feature {

    @Override
    public String getFeatureDescription() {
        return "Allows turning more than just grass into path. Turns off when dirt2path is installed";
    }

    @Override
    public String[] getIncompatibleMods() {
        return new String[]{"dirt2path"};
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    protected boolean isBlockDirt(IBlockState iBlockStateIn) {
        if (iBlockStateIn.getBlock() == Blocks.DIRT) return true;
        return false;
    }

    protected void setPathOrDirt(World world, IBlockState blockState, BlockPos blockPos, SoundEvent soundEvent, EntityPlayer player, ItemStack itemStack, EnumHand hand) {
        world.playSound(player, blockPos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
        player.swingArm(hand);
        if (!world.isRemote) {
            world.setBlockState(blockPos, blockState, 11);
            itemStack.damageItem(1, player);
        }
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockRightclick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getResult() != Event.Result.DEFAULT || event.isCanceled())
            return;

        EntityPlayer player = event.getEntityPlayer();
        World world = event.getWorld();
        BlockPos blockPos = event.getPos();
        ItemStack stack = player.getHeldItem(event.getHand());

        if (stack.isEmpty())
            return;

        if (!stack.canHarvestBlock(Blocks.SNOW.getDefaultState()))
            return;

        IBlockState iBlockState = world.getBlockState(blockPos);

        if (world.getBlockState(blockPos.up()).getMaterial() == Material.AIR) {
            if (isBlockDirt(iBlockState)) {
                IBlockState pathState = Blocks.GRASS_PATH.getDefaultState();
                setPathOrDirt(world, pathState, blockPos, SoundEvents.ITEM_SHOVEL_FLATTEN, player, stack, event.getHand());
            }
        }
    }

}
