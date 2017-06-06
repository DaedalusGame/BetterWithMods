package betterwithmods.module.gameplay;

import betterwithmods.api.block.IDebarkable;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.registry.blockmeta.managers.SawManager;
import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import static betterwithmods.common.BWMItems.BARK;

/**
 * Created by primetoxinz on 6/6/17.
 */
public class Debarking extends Feature {
    public static boolean requiresSneaking;
    @Override
    public void setupConfig() {
        requiresSneaking = loadPropBool("Requires Sneaking", "Making Debarking Wood Require the Player to be Sneaking", true);
    }

    @Override
    public String getFeatureDescription() {
        return "Allows Right-Clicking Logs with an Axe to remove the Bark";
    }

    @SubscribeEvent
    public void debarkLog(PlayerInteractEvent.RightClickBlock evt) {
        World world = evt.getWorld();
        if (!world.isRemote) {
            BlockPos pos = evt.getPos();
            EntityPlayer player = evt.getEntityPlayer();
            Block block = world.getBlockState(pos).getBlock();
            ItemStack playerStack = player.getHeldItem(evt.getHand());
            if (playerStack.isEmpty())
                return;
            BlockPos playerPos = pos.offset(evt.getFace());
            if (!playerStack.isEmpty() && (playerStack.getItem().getHarvestLevel(playerStack, "axe", player, world.getBlockState(pos)) >= 0) || playerStack.getItem().getToolClasses(playerStack).contains("axe")) {
                if (block == Blocks.LOG) {
                    IBlockState state = world.getBlockState(pos);
                    IBlockState dbl = BWMBlocks.DEBARKED_OLD.getDefaultState().withProperty(BlockLog.LOG_AXIS, state.getValue(BlockLog.LOG_AXIS)).withProperty(BlockOldLog.VARIANT, state.getValue(BlockOldLog.VARIANT));
                    InvUtils.ejectStackWithOffset(world, playerPos, ((IDebarkable) dbl.getBlock()).getBark(dbl));
                    world.setBlockState(pos, dbl);
                    world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.BLOCKS, 0.5F, 2.5F);
                    playerStack.damageItem(1, player);
                } else if (block == Blocks.LOG2) {
                    IBlockState state = world.getBlockState(pos);
                    IBlockState dbl = BWMBlocks.DEBARKED_NEW.getDefaultState().withProperty(BlockLog.LOG_AXIS, state.getValue(BlockLog.LOG_AXIS)).withProperty(BlockNewLog.VARIANT, state.getValue(BlockNewLog.VARIANT));
                    InvUtils.ejectStackWithOffset(world, playerPos, ((IDebarkable) dbl.getBlock()).getBark(dbl));
                    world.setBlockState(pos, dbl);
                    world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.BLOCKS, 0.5F, 2.5F);
                    playerStack.damageItem(1, player);
                } else {
                    IBlockState state = world.getBlockState(pos);
                    if (SawManager.INSTANCE.contains(block, block.getMetaFromState(state)) && BWOreDictionary.listContains(new ItemStack(block, 1, block.damageDropped(state)), OreDictionary.getOres("logWood"))) {
                        ItemStack bark = new ItemStack(BARK, 1, 0);
                        IBlockState dbl = BWMBlocks.DEBARKED_OLD.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
                        if (state.getBlock() instanceof IDebarkable) {
                            IDebarkable debarkable = (IDebarkable) state.getBlock();
                            bark = debarkable.getBark(state);
                            dbl = debarkable.getStrippedState(state);
                        }
                        InvUtils.ejectStackWithOffset(world, playerPos, bark);
                        world.setBlockState(pos, dbl);
                        world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.BLOCKS, 0.5F, 2.5F);
                        playerStack.damageItem(1, player);
                    }

                }
            }
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
