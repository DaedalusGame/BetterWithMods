package betterwithmods.module.compat.minetweaker;

import betterwithmods.common.registry.blockmeta.managers.TurntableManager;
import betterwithmods.common.registry.blockmeta.recipe.TurntableRecipe;
import betterwithmods.module.compat.jei.category.TurntableRecipeCategory;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.ArrayUtils;
import com.google.common.collect.Lists;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

import static betterwithmods.module.compat.minetweaker.Turntable.clazz;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/2/17
 */
@ZenClass(clazz)
public class Turntable {
    public static final String clazz = "mods.betterwithmods.Turntable";
    @ZenMethod
    public static void addRecipe(IItemStack inputBlock, IItemStack outputBlock, IItemStack[] additionalOutput) {
        if(!InputHelper.isABlock(inputBlock))
            LogHelper.logError("Input must be a block", new IllegalArgumentException());
        MineTweakerAPI.apply(new Add(InputHelper.toStack(inputBlock),InputHelper.toStack(outputBlock), ArrayUtils.toArrayList(InputHelper.toStacks(additionalOutput))));
    }

    public static class Add extends BMAdd {
        public Add(ItemStack input, ItemStack output, List<ItemStack> scraps) {
            super(TurntableRecipeCategory.UID, TurntableManager.INSTANCE, Lists.newArrayList(new TurntableRecipe(input, output, scraps)));
        }
    }

}
