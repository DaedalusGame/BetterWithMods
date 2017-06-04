package betterwithmods.module.compat.minetweaker;


import betterwithmods.common.registry.bulk.manager.CrucibleManager;
import betterwithmods.common.registry.bulk.recipes.CrucibleRecipe;
import betterwithmods.module.compat.jei.category.CrucibleRecipeCategory;
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
@ZenClass(Crucible.clazz)
public class Crucible {

    public static final String clazz = "mods.betterwithmods.Crucible";

    @ZenMethod
    public static void add(IItemStack output, @Optional IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        CrucibleRecipe r = new CrucibleRecipe(InputHelper.toStack(output), InputHelper.toStack(secondaryOutput), InputHelper.toObjects(inputs));
        MineTweakerAPI.apply(new BulkAdd(CrucibleRecipeCategory.UID, CrucibleManager.getInstance(), r));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        MineTweakerAPI.apply(new BulkRemove(CrucibleRecipeCategory.UID, CrucibleManager.getInstance(), InputHelper.toStack(output), ItemStack.EMPTY));
    }

    @ZenMethod
    public static void remove(IItemStack output, IIngredient[] inputs) {
        MineTweakerAPI.apply(new BulkRemove(CrucibleRecipeCategory.UID, CrucibleManager.getInstance(), InputHelper.toStack(output), ItemStack.EMPTY, InputHelper.toObjects(inputs)));
    }
}
