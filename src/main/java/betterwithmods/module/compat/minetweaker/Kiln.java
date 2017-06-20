package betterwithmods.module.compat.minetweaker;

import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.common.registry.blockmeta.managers.KilnManager;
import betterwithmods.common.registry.blockmeta.recipe.BlockMetaRecipe;
import betterwithmods.common.registry.blockmeta.recipe.KilnRecipe;
import betterwithmods.module.compat.jei.category.KilnRecipeCategory;
import betterwithmods.util.RecipeUtils;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseUndoable;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
@ZenClass(Kiln.clazz)
public class Kiln {

    public static final String clazz = "mods.betterwithmods.Kiln";

    @ZenMethod
    public static void add(IItemStack[] output, @NotNull IIngredient input) {

       List<ItemStack> stacks = input.getItems().stream().map(InputHelper::toStack).collect(Collectors.toList());
        MineTweakerAPI.logInfo(String.valueOf(stacks));
       List<BlockMetaRecipe> recipes = new ArrayList<>();
        for(ItemStack stack: stacks) {
            if (!InputHelper.isABlock(stack)) {
                MineTweakerAPI.getLogger().logWarning(stack.getDisplayName() + " is not a Block, will not be registered");
                return;
            }
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            MineTweakerAPI.logInfo(block.getUnlocalizedName());
            ItemStack[] outputs = InputHelper.toStacks(output);
            if (output == null) {
                MineTweakerAPI.getLogger().logError("Could not add " + clazz + " recipe for " + stack.getDisplayName() + ", outputs were null");
            }
            KilnRecipe r = new KilnRecipe(block, stack.getMetadata(), Arrays.asList(outputs));
            recipes.add(r);
        }
        MineTweakerAPI.apply(new BMAdd(KilnRecipeCategory.UID, KilnManager.INSTANCE, recipes));
    }
    
    @ZenMethod
    public static void remove(IItemStack output) {
        MineTweakerAPI.apply(new BMRemove(KilnRecipeCategory.UID, KilnManager.INSTANCE,InputHelper.toStack(output)));
    }


    @ZenMethod
    public static void registerBlock(IItemStack block) {
        MineTweakerAPI.apply(new KilnBlock(RecipeUtils.getStateFromStack(InputHelper.toStack(block))));
    }


    public static class KilnBlock extends BaseUndoable {
        private IBlockState state;

        protected KilnBlock(IBlockState state) {
            super("kiln_block");
            this.state = state;
        }

        @Override
        public void apply() {
            KilnStructureManager.registerKilnBlock(state);
        }

        @Override
        public void undo() {
            KilnStructureManager.removeKilnBlock(state);
        }
    }
}
