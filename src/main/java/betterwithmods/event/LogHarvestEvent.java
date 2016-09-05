package betterwithmods.event;

import betterwithmods.BWRegistry;
import betterwithmods.api.block.IDebarkable;
import betterwithmods.config.BWConfig;
import betterwithmods.craft.SawInteraction;
import betterwithmods.items.ItemKnife;
import betterwithmods.util.InvUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

import static betterwithmods.BWRegistry.bark;

public class LogHarvestEvent {
    @SubscribeEvent
    public void debarkLog(PlayerInteractEvent.RightClickBlock evt) {
        if (!evt.getWorld().isRemote) {
            Block block = evt.getWorld().getBlockState(evt.getPos()).getBlock();
            ItemStack playerStack = evt.getEntityPlayer().getHeldItem(evt.getHand());
            BlockPos playerPos = evt.getPos().offset(evt.getFace());
            if (playerStack != null && playerStack.getItem().getHarvestLevel(playerStack, "axe") >= 0) {
                if (block == Blocks.LOG) {
                    IBlockState state = evt.getWorld().getBlockState(evt.getPos());
                    IBlockState dbl = BWRegistry.debarkedOld.getDefaultState().withProperty(BlockLog.LOG_AXIS, state.getValue(BlockLog.LOG_AXIS)).withProperty(BlockOldLog.VARIANT, state.getValue(BlockOldLog.VARIANT));
                    InvUtils.ejectStackWithOffset(evt.getWorld(), playerPos, ((IDebarkable) dbl.getBlock()).getBark(dbl));
                    evt.getWorld().setBlockState(evt.getPos(), dbl);
                    evt.getWorld().playSound(null, evt.getPos(), SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.BLOCKS, 0.5F, 2.5F);
                    playerStack.damageItem(1, evt.getEntityPlayer());
                } else if (block == Blocks.LOG2) {
                    IBlockState state = evt.getWorld().getBlockState(evt.getPos());
                    IBlockState dbl = BWRegistry.debarkedOld.getDefaultState().withProperty(BlockLog.LOG_AXIS, state.getValue(BlockLog.LOG_AXIS)).withProperty(BlockNewLog.VARIANT, state.getValue(BlockNewLog.VARIANT));
                    InvUtils.ejectStackWithOffset(evt.getWorld(), playerPos, ((IDebarkable) dbl.getBlock()).getBark(dbl));
                    evt.getWorld().setBlockState(evt.getPos(), dbl);
                    evt.getWorld().playSound(null, evt.getPos(), dbl.getBlock().getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                    playerStack.damageItem(1, evt.getEntityPlayer());
                } else {
                    IBlockState state = evt.getWorld().getBlockState(evt.getPos());
                    if (SawInteraction.contains(block, block.getMetaFromState(state))) {
                        InvUtils.ejectStackWithOffset(evt.getWorld(), playerPos, new ItemStack(bark, 1, 0));
                        IBlockState dbl = BWRegistry.debarkedOld.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
                        evt.getWorld().setBlockState(evt.getPos(), dbl);
                        evt.getWorld().playSound(null, evt.getPos(), SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.BLOCKS, 0.5F, 2.5F);
                        playerStack.damageItem(1, evt.getEntityPlayer());
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public void harvestLog(BlockEvent.HarvestDropsEvent evt) {
        if (!BWConfig.hardcoreLumber || evt.getHarvester() == null)
            return;
        else if (evt.getHarvester() != null) { //Checking invulnerability because checking for instances of FakePlayer doesn't work.
            if (evt.getHarvester().isEntityInvulnerable(DamageSource.cactus))
                return;
        }
        Block block = evt.getState().getBlock();
        int harvestMeta = evt.getState().getBlock().damageDropped(evt.getState());
        if (!evt.getWorld().isRemote && (!evt.isSilkTouching() || evt.getHarvester() != null)) {
            boolean harvest = false;
            if (evt.getHarvester() != null && evt.getHarvester().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != null) {
                Item item = evt.getHarvester().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem();

                if (item != null) {
                    if ((item.getHarvestLevel(evt.getHarvester().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND), "axe") >= 0) && !(item instanceof ItemKnife)) {
                        harvest = true;
                    }
                }
            }

            if (!harvest) {
                int fortune = evt.getHarvester() != null && evt.getHarvester().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != null && evt.getHarvester().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() instanceof ItemKnife ? evt.getFortuneLevel() : 0;
                boolean fort = fortune > 0;
                List<ItemStack> logs = OreDictionary.getOres("logWood");
                boolean isLog = logs.stream().filter(stack -> stack.isItemEqual(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE))).findAny().isPresent();
                if (SawInteraction.contains(block, harvestMeta) && isLog && !evt.isSilkTouching()) {
                    for (ItemStack logStack : evt.getDrops()) {
                        if (logStack.getItem() instanceof ItemBlock) {
                            ItemBlock iBlock = (ItemBlock) logStack.getItem();
                            if (iBlock.getBlock() == block) {
                                List<ItemStack> outputs = SawInteraction.getProducts(block, harvestMeta);
                                List<ItemStack> newOutputs = Lists.newArrayList();
                                if (outputs.size() == 3) {
                                    ItemStack planks = outputs.get(0).copy();
                                    int plankStack = (planks.stackSize / 2) + (fort ? evt.getWorld().rand.nextInt(2) : 0);
                                    planks.stackSize = plankStack;
                                    int barkStack = fort ? 1 + evt.getWorld().rand.nextInt(fortune) : 1;
                                    ItemStack bark = new ItemStack(BWRegistry.bark, barkStack, 0);
                                    barkStack = fort ? outputs.get(1).stackSize + evt.getWorld().rand.nextInt(fortune) : outputs.get(1).stackSize;

                                    bark = new ItemStack(outputs.get(1).getItem(), barkStack, outputs.get(1).getItemDamage());
                                    int sawdustStack = fort ? 1 + evt.getWorld().rand.nextInt(fortune) : 1;
                                    ItemStack sawdust = new ItemStack(BWRegistry.material, sawdustStack, 22);
                                    newOutputs.add(planks);
                                    newOutputs.add(bark);
                                    newOutputs.add(sawdust);
                                }
                                evt.getDrops().remove(logStack);
                                evt.getDrops().addAll(newOutputs);
                                break;
                            }
                        }
                    }
                }
            }
        }

    }


    public static IRecipe findMatchingRecipe(InventoryCrafting inv, World world) {
        for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); i++) {
            IRecipe recipe = CraftingManager.getInstance().getRecipeList().get(i);

            if (recipe.matches(inv, world)) {
                return recipe;
            }
        }
        return null;
    }
}
