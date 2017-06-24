package betterwithmods.module.hardcore;

import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.registry.ChoppingRecipe;
import betterwithmods.module.Feature;
import betterwithmods.util.RecipeUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
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

    @Override
    public void setupConfig() {
        plankAmount = loadPropInt("Plank Amount", "Amount of Planks dropped when Punching Wood", 2);
        barkAmount = loadPropInt("Bark Amount", "Amount of Bark dropped when Punching Wood", 1);
        sawDustAmount = loadPropInt("Sawdust Amount", "Amount of Sawdust dropped when Punching Wood", 2);

    }

    @Override
    public String getFeatureDescription() {
        return "Makes Punching Wood return a single plank and secondary drops instead of a log, to get a log an axe must be used.";
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if (!Loader.isModLoaded("primal")) {
            for (BWOreDictionary.Wood wood : BWOreDictionary.woods) {
                RecipeUtils.removeRecipes(wood.plank);
                RecipeUtils.addRecipe(new ChoppingRecipe(wood.getPlank(plankAmount), wood.getBark(barkAmount), wood.getSawdust(sawDustAmount), wood.log));
            }
        }
    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @SubscribeEvent
    public void harvestLog(BlockEvent.HarvestDropsEvent evt) {
        if (hasAxe(evt) || Loader.isModLoaded("primal"))
            return;
        ItemStack stack = RecipeUtils.getStackFromState(evt.getState());
        BWOreDictionary.Wood wood = BWOreDictionary.woods.stream().filter(w -> w.getLog(1).isItemEqual(stack)).findAny().orElse(null);
        if (wood != null) {
            evt.getDrops().clear();
            evt.getDrops().addAll(Lists.newArrayList(wood.getPlank(1), wood.getSawdust(2), wood.getBark(1)));
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
