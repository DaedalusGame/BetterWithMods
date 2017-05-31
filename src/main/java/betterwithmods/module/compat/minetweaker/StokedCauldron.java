package betterwithmods.module.compat.minetweaker;


import betterwithmods.common.registry.bulk.manager.StokedCauldronManager;
import betterwithmods.common.registry.bulk.recipes.StokedCauldronRecipe;
import betterwithmods.module.compat.jei.category.StokedCauldronRecipeCategory;
import com.blamejared.mtlib.helpers.InputHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 12/31/16
 */
@ZenClass(StokedCauldron.clazz)
public class StokedCauldron {

    public static final String clazz = "mods.betterwithmods.StokedCauldron";

    @ZenMethod
    public static void add(IItemStack output, @Optional IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        StokedCauldronRecipe r = new StokedCauldronRecipe(InputHelper.toStack(output), InputHelper.toStack(secondaryOutput),InputHelper.toObjects(inputs));
        MineTweakerAPI.apply(new BulkAdd(StokedCauldronRecipeCategory.UID, StokedCauldronManager.getInstance(),r));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        MineTweakerAPI.apply(new BulkRemove(StokedCauldronRecipeCategory.UID, StokedCauldronManager.getInstance(),InputHelper.toStack(output), ItemStack.EMPTY));
    }

    @ZenMethod
    public static void remove(IItemStack output, IIngredient[] inputs) {
        MineTweakerAPI.apply(new BulkRemove(StokedCauldronRecipeCategory.UID, StokedCauldronManager.getInstance(),InputHelper.toStack(output), ItemStack.EMPTY, InputHelper.toObjects(inputs)));
    }
}
