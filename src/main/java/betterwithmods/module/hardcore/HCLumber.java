package betterwithmods.module.hardcore;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.ChoppingRecipe;
import betterwithmods.common.registry.blockmeta.managers.SawManager;
import betterwithmods.module.Feature;
import betterwithmods.util.RecipeUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by tyler on 4/20/17.
 */
public class HCLumber extends Feature {
    private int plankAmount, barkAmount, sawDustAmount;

    public static boolean hasAxe(BlockEvent.HarvestDropsEvent event) {
        if (!event.getWorld().isRemote && !event.isSilkTouching()) {
            EntityPlayer player = event.getHarvester();
            if (player != null) {
                ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                return stack.getItem().getHarvestLevel(player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND), "axe", player, event.getState()) >= 0 || stack.getItem().getToolClasses(player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND)).contains("axe");
            }
        }
        return false;
    }

    public static int getFortune(BlockEvent.HarvestDropsEvent event) {
        EntityPlayer player = event.getHarvester();
        return !player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty() ? event.getFortuneLevel() : 0;
    }

    public static boolean isLog(IBlockState state) {
        List<ItemStack> logs = OreDictionary.getOres("logWood");
        return logs.stream().filter(stack -> stack.isItemEqual(new ItemStack(state.getBlock(), 1, OreDictionary.WILDCARD_VALUE))).findAny().isPresent();
    }

    public static boolean isSameBlock(ItemStack stack, Block block) {
        return stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock().equals(block);
    }

    @Override
    public void setupConfig() {
        plankAmount = loadPropInt("Plank Amount", "Amount of Planks dropped when Punching Wood", 2);
        barkAmount = loadPropInt("Bark Amount", "Amount of Bark dropped when Punching Wood", 1);
        sawDustAmount = loadPropInt("Sawdust Amount", "Amount of Sawdust dropped when Punching Wood", 3);

    }

    @Override
    public String getFeatureDescription() {
        return "Makes Punching Wood return a single plank and secondary drops instead of a log, to get a log an axe must be used.";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        for (int i = 0; i < 4; i++)
            RecipeUtils.addRecipe(new ChoppingRecipe(new ItemStack(Blocks.PLANKS, plankAmount, i), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST, sawDustAmount), new ItemStack(BWMBlocks.DEBARKED_OLD, barkAmount, i)));
        for (int i = 0; i < 2; i++)
            RecipeUtils.addRecipe(new ChoppingRecipe(new ItemStack(Blocks.PLANKS, plankAmount, 4 + i), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST, sawDustAmount), new ItemStack(BWMBlocks.DEBARKED_NEW, barkAmount, i)));
    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
        for (int i = 0; i < 4; i++)
            RecipeUtils.addShapelessOreRecipe(new ItemStack(Blocks.PLANKS, 3, i), new ItemStack(BWMBlocks.DEBARKED_OLD, 1, i));
        for (int i = 0; i < 2; i++)
            RecipeUtils.addShapelessOreRecipe(new ItemStack(Blocks.PLANKS, 3, 4 + i), new ItemStack(BWMBlocks.DEBARKED_NEW, 1, i));
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
        int fortune = getFortune(evt);
        boolean fort = fortune > 0;
        boolean isLog = isLog(state);
        if (!hasAxe(evt) && isLog && SawManager.INSTANCE.contains(block, harvestMeta)) {
            evt.getDrops().stream().filter(stack -> isSameBlock(stack, block)).forEach(log -> {
                List<ItemStack> outputs = SawManager.INSTANCE.getProducts(block, harvestMeta);
                List<ItemStack> newOutputs = Lists.newArrayList();
                if (outputs.size() == 3) {
                    ItemStack planks = outputs.get(0).copy();
                    planks.setCount(plankAmount + (fort ? world.rand.nextInt(2) : 0));
                    int barkStack = fort ? outputs.get(1).getCount() + world.rand.nextInt(fortune) : barkAmount;

                    ItemStack bark = new ItemStack(outputs.get(1).getItem(), barkStack, outputs.get(1).getItemDamage());
                    int sawdustStack = fort ? 1 + world.rand.nextInt(fortune) : sawDustAmount;
                    ItemStack sawdust = new ItemStack(BWMItems.MATERIAL, sawdustStack, 22);
                    newOutputs.add(planks);
                    newOutputs.add(bark);
                    newOutputs.add(sawdust);
                }
                evt.getDrops().remove(log);
                evt.getDrops().addAll(newOutputs);
            });

        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
