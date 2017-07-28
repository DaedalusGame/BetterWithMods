package betterwithmods.module.hardcore;

import betterwithmods.common.BWMRecipes;
import betterwithmods.module.Feature;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Set;

/**
 * Created by tyler on 5/25/17.
 */
public class HCSaw extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Makes the Saw required to get complex wooden blocks, including Fences, Doors, Etc.";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        Set<ItemStack> blocks = Sets.newHashSet(
                new ItemStack(Blocks.OAK_FENCE),
                new ItemStack(Blocks.ACACIA_FENCE),
                new ItemStack(Blocks.BIRCH_FENCE),
                new ItemStack(Blocks.SPRUCE_FENCE),
                new ItemStack(Blocks.JUNGLE_FENCE),
                new ItemStack(Blocks.OAK_FENCE),
                new ItemStack(Blocks.DARK_OAK_FENCE),
                new ItemStack(Blocks.OAK_FENCE_GATE),
                new ItemStack(Blocks.BIRCH_FENCE_GATE),
                new ItemStack(Blocks.SPRUCE_FENCE_GATE),
                new ItemStack(Blocks.DARK_OAK_FENCE_GATE),
                new ItemStack(Blocks.JUNGLE_FENCE_GATE),
                new ItemStack(Blocks.OAK_FENCE_GATE),
                new ItemStack(Blocks.ACACIA_FENCE_GATE),
                new ItemStack(Blocks.TRAPDOOR),
                new ItemStack(Items.OAK_DOOR),
                new ItemStack(Items.ACACIA_DOOR),
                new ItemStack(Items.BIRCH_DOOR),
                new ItemStack(Items.SPRUCE_DOOR),
                new ItemStack(Items.DARK_OAK_DOOR),
                new ItemStack(Items.JUNGLE_DOOR)

        );
        blocks.forEach(BWMRecipes::removeRecipe);
    }

}
