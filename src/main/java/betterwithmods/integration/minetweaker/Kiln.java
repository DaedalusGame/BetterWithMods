package betterwithmods.integration.minetweaker;

import betterwithmods.craft.BlockMetaRecipe;
import betterwithmods.craft.KilnInteraction;
import com.blamejared.mtlib.helpers.InputHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 12/31/16
 */
@ZenClass(Kiln.clazz)
public class Kiln {

    public static final String clazz = "mods.betterwithmods.Kiln";

    @ZenMethod
    public static void add(IItemStack[] output, @NotNull IItemStack input) {
        ItemStack blockMeta = InputHelper.toStack(input);
        if(!InputHelper.isABlock(blockMeta))
            MineTweakerAPI.getLogger().logError(input.getDisplayName() + " is not a Block");
        Block block = ((ItemBlock)blockMeta.getItem()).getBlock();

        ItemStack[] outputs = InputHelper.toStacks(output);
        if (output == null) {
            MineTweakerAPI.getLogger().logError("Could not add " + clazz + " recipe for " + input.getDisplayName() + ", outputs were null");
        }
        BlockMetaRecipe r = new BlockMetaRecipe("kiln", block, blockMeta.getMetadata(), Arrays.asList(outputs));
        MineTweakerAPI.apply(new BMAdd("kiln", KilnInteraction.INSTANCE,r));
    }
    
    @ZenMethod
    public static void remove(IItemStack output) {
        MineTweakerAPI.apply(new BMRemove("kiln", KilnInteraction.INSTANCE,InputHelper.toStack(output)));
    }


}
