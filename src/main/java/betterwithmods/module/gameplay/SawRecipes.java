package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.blockmeta.managers.SawManager;
import betterwithmods.common.registry.blockmeta.recipe.SawRecipe;
import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.Random;

/**
 * Created by tyler on 5/16/17.
 */
public class SawRecipes extends Feature {
    public SawRecipes() {
        canDisable = false;
    }

    public static void addSawRecipe(Block block, int meta, ItemStack output) {
        addSawRecipe(block, meta,new ItemStack[]{output});
    }

    public static void addSawRecipe(Block block, int meta, ItemStack... outputs) {
        SawManager.INSTANCE.addRecipe(block, meta, outputs);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            addSawRecipe(BWMBlocks.WOOD_CORNER, type.getMetadata(), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR, 2));
            addSawRecipe(BWMBlocks.WOOD_MOULDING, type.getMetadata(), new ItemStack(BWMBlocks.WOOD_CORNER, 2, type.getMetadata()));
            addSawRecipe(BWMBlocks.WOOD_SIDING, type.getMetadata(), new ItemStack(BWMBlocks.WOOD_MOULDING, 2, type.getMetadata()));
        }
        addSawRecipe(Blocks.VINE, 0, new ItemStack(Blocks.VINE));
        addSawRecipe(Blocks.PUMPKIN, 0, new ItemStack(Blocks.PUMPKIN));
        SawManager.INSTANCE.addRecipe(new SawRecipe(Blocks.MELON_BLOCK, 0, null) {
            @Override
            public NonNullList<ItemStack> getOutputs() {
                Random random = new Random();
                return InvUtils.asList(new ItemStack(Items.MELON, 3 + random.nextInt(5)));
            }
        });
    }
}
