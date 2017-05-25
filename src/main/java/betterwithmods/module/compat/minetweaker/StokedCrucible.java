package betterwithmods.module.compat.minetweaker;


import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
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
@ZenClass(StokedCrucible.clazz)
public class StokedCrucible {

    public static final String clazz = "mods.betterwithmods.StokedCrucible";

    @ZenMethod
    public static void add(IItemStack output, @Optional IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        BulkRecipe r = new BulkRecipe(InputHelper.toStack(output), InputHelper.toStack(secondaryOutput), InputHelper.toObjects(inputs));
        MineTweakerAPI.apply(new BulkAdd("crucibleStoked", StokedCrucibleManager.getInstance(), r));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        MineTweakerAPI.apply(new BulkRemove("crucibleStoked", StokedCrucibleManager.getInstance(), InputHelper.toStack(output), ItemStack.EMPTY));
    }

    @ZenMethod
    public static void remove(IItemStack output, @NotNull IIngredient[] inputs) {
        MineTweakerAPI.apply(new BulkRemove("crucibleStoked", StokedCrucibleManager.getInstance(), InputHelper.toStack(output), ItemStack.EMPTY, InputHelper.toObjects(inputs)));
    }
}
