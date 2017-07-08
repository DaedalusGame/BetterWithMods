package betterwithmods.module.tweaks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMRecipes;
import betterwithmods.module.Feature;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

import static betterwithmods.module.gameplay.KilnRecipes.addKilnRecipe;

/**
 * Created by tyler on 5/16/17.
 */
public class KilnCharcoal extends Feature {

    private static final ItemStack CHARCOAL = new ItemStack(Items.COAL, 1, 1);
    boolean disableFurnaceCharcoal;

    @Override
    public void setupConfig() {
        disableFurnaceCharcoal = loadPropBool("Disable Furnace Charcoal", "Remove recipes to make Charcoal in a Furnace", true);
    }

    @Override
    public String getFeatureDescription() {
        return "Add Charcoal smelting to the Kiln";
    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
    }

    @Override
    public void init(FMLInitializationEvent event) {
        List<ItemStack> logs = Lists.newArrayList();
        logs.addAll(OreDictionary.getOres("logWood"));

        for (ItemStack stack : logs) {
            if (stack.getItem() instanceof ItemBlock) {
                Item item = stack.getItem();
                Block block = ((ItemBlock) item).getBlock();
                int meta = stack.getItemDamage();
                if (disableFurnaceCharcoal)
                    BWMRecipes.removeFurnaceRecipe(stack);
                addKilnRecipe(block, meta, CHARCOAL);
            }
        }
    }


}
