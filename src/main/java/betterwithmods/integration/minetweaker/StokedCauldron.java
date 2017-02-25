package betterwithmods.integration.minetweaker;


import betterwithmods.craft.bulk.BulkRecipe;
import betterwithmods.craft.bulk.CraftingManagerCauldronStoked;
import com.blamejared.mtlib.helpers.InputHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
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
        BulkRecipe r = new BulkRecipe("cauldronStoked", InputHelper.toStack(output), InputHelper.toStack(secondaryOutput),InputHelper.toObjects(inputs));
        MineTweakerAPI.apply(new BulkAdd("cauldronStoked", CraftingManagerCauldronStoked.getInstance(),r));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        MineTweakerAPI.apply(new BulkRemove("cauldronStoked", CraftingManagerCauldronStoked.getInstance(),InputHelper.toStack(output)));
    }


}
