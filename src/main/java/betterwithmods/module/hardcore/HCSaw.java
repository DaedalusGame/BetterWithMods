package betterwithmods.module.hardcore;

import betterwithmods.module.Feature;
import betterwithmods.util.RecipeUtils;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

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
    public void postInit(FMLPostInitializationEvent event) {
        Set<Block> blocks = Sets.newHashSet(
                Blocks.OAK_FENCE,
                Blocks.ACACIA_FENCE,
                Blocks.BIRCH_FENCE,
                Blocks.SPRUCE_FENCE,
                Blocks.DARK_OAK_FENCE,
                Blocks.JUNGLE_FENCE,
                Blocks.OAK_FENCE,
                Blocks.OAK_FENCE_GATE,
                Blocks.ACACIA_FENCE_GATE,
                Blocks.BIRCH_FENCE_GATE,
                Blocks.SPRUCE_FENCE_GATE,
                Blocks.DARK_OAK_FENCE_GATE,
                Blocks.JUNGLE_FENCE_GATE,
                Blocks.OAK_FENCE_GATE,
                Blocks.TRAPDOOR

        );
        Set<Item> items = Sets.newHashSet(
                Items.OAK_DOOR,
                Items.ACACIA_DOOR,
                Items.BIRCH_DOOR,
                Items.SPRUCE_DOOR,
                Items.DARK_OAK_DOOR,
                Items.JUNGLE_DOOR);
        blocks.forEach(RecipeUtils::removeRecipes);
        items.forEach(RecipeUtils::removeRecipes);
    }
}
