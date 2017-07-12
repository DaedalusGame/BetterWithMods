package betterwithmods.common.registry;

import com.google.gson.JsonObject;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

/**
 * Created by blueyu2 on 12/12/16.
 */
public class CuttingRecipe extends ToolDamageRecipe {
    public CuttingRecipe(Ingredient input, ItemStack result) {
        super(new ResourceLocation("cutting"), result, input, stack -> stack.getItem() instanceof ItemShears);
    }

    @Override
    public ItemStack getExampleStack() {
        return new ItemStack(Items.SHEARS);
    }

    public static class Factory implements IRecipeFactory {
        @Override
        public IRecipe parse(JsonContext context, JsonObject json) {
            String group = JsonUtils.getString(json, "group", "");
            JsonObject o = JsonUtils.getJsonObject(json, "cut");
            Item item = JsonUtils.getItem(o,"item");
            int meta = JsonUtils.hasField(o, "data") ? JsonUtils.getInt(o,"data") : 0;
            Ingredient cut = Ingredient.fromStacks(new ItemStack(item, 1, meta));
            ItemStack itemstack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
            return new CuttingRecipe(cut, itemstack);
        }
    }
}
