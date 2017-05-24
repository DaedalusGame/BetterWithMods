package betterwithmods.module.compat.minetweaker;


import betterwithmods.common.registry.blockmeta.managers.SawManager;
import betterwithmods.common.registry.blockmeta.recipe.BlockMetaRecipe;
import com.blamejared.mtlib.helpers.InputHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 12/31/16
 */
@ZenClass(Saw.clazz)
public class Saw {

    public static final String clazz = "mods.betterwithmods.Saw";

    @ZenMethod
    public static void add(IItemStack[] output, @NotNull IIngredient input) {
        List<ItemStack> stacks = input.getItems().stream().map(InputHelper::toStack).collect(Collectors.toList());
        List<BlockMetaRecipe> recipes = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (!InputHelper.isABlock(stack)) {
                MineTweakerAPI.getLogger().logWarning(stack.getDisplayName() + " is not a Block, will not be registered");
                return;
            }
            Block block = ((ItemBlock) stack.getItem()).getBlock();

            ItemStack[] outputs = InputHelper.toStacks(output);
            if (output == null) {
                MineTweakerAPI.getLogger().logError("Could not add " + clazz + " recipe for " + stack.getDisplayName() + ", outputs were null");
            }
            BlockMetaRecipe r = new BlockMetaRecipe(block, stack.getMetadata(), Arrays.asList(outputs));
            recipes.add(r);
        }
        MineTweakerAPI.apply(new BMAdd("saw", SawManager.INSTANCE, recipes));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        MineTweakerAPI.apply(new BMRemove("saw", SawManager.INSTANCE, InputHelper.toStack(output)));
    }


}
