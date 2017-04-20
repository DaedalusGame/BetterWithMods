package betterwithmods.module.hardcore;

import betterwithmods.common.BWMItems;
import betterwithmods.common.items.tools.ItemKnife;
import betterwithmods.common.registry.SawInteraction;
import betterwithmods.module.Feature;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by tyler on 4/20/17.
 */
public class HCLumber extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Makes Punching Wood return a single plank and secondary drops instead of a log, to get a log an axe must be used";
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @SubscribeEvent
    public void harvestLog(BlockEvent.HarvestDropsEvent evt) {
        EntityPlayer player = evt.getHarvester();

        if (player == null || player.isEntityInvulnerable(DamageSource.CACTUS)) //Checking invulnerability because checking for instances of FakePlayer doesn't work.
            return;
        IBlockState state = evt.getState();
        Block block = state.getBlock();
        int harvestMeta = state.getBlock().damageDropped(state);
        World world = evt.getWorld();
        if (!world.isRemote && !evt.isSilkTouching()) {
            boolean harvest = false;
            if (player != null && player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != ItemStack.EMPTY) {
                Item item = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem();

                if (item != null) {
                    if ((item.getHarvestLevel(player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND), "axe", player, state) >= 0 || item.getToolClasses(player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND)).contains("axe")) && !(item instanceof ItemKnife)) {
                        harvest = true;
                    }
                }
            }

            if (!harvest) {
                int fortune = player != null && player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != ItemStack.EMPTY && player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() instanceof ItemKnife ? evt.getFortuneLevel() : 0;
                boolean fort = fortune > 0;
                List<ItemStack> logs = OreDictionary.getOres("logWood");
                boolean isLog = logs.stream().filter(stack -> stack.isItemEqual(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE))).findAny().isPresent();
                if (SawInteraction.INSTANCE.contains(block, harvestMeta) && isLog && !evt.isSilkTouching()) {
                    for (ItemStack logStack : evt.getDrops()) {
                        if (logStack.getItem() instanceof ItemBlock) {
                            ItemBlock iBlock = (ItemBlock) logStack.getItem();
                            if (iBlock.getBlock() == block) {
                                List<ItemStack> outputs = SawInteraction.INSTANCE.getProducts(block, harvestMeta);
                                List<ItemStack> newOutputs = Lists.newArrayList();
                                if (outputs.size() == 3) {
                                    ItemStack planks = outputs.get(0).copy();
                                    planks.setCount((planks.getCount() / 2) + (fort ? world.rand.nextInt(2) : 0));
                                    int barkStack = fort ? outputs.get(1).getCount() + world.rand.nextInt(fortune) : outputs.get(1).getCount();

                                    ItemStack bark = new ItemStack(outputs.get(1).getItem(), barkStack, outputs.get(1).getItemDamage());
                                    int sawdustStack = fort ? 1 + world.rand.nextInt(fortune) : 1;
                                    ItemStack sawdust = new ItemStack(BWMItems.MATERIAL, sawdustStack, 22);
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

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
