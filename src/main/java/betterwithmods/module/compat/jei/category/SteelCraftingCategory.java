package betterwithmods.module.compat.jei.category;

import betterwithmods.BWMod;
import betterwithmods.common.registry.steelanvil.SteelShapelessRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class SteelCraftingCategory extends BlankRecipeCategory<IRecipeWrapper> {

    public static final String UID = "bwm.steel_anvil";
    public static final String TITLE = "inv.steel_anvil.name";

    public static final int WIDTH = 162;
    public static final int HEIGHT = 199;
    public static int craftOutputSlot = 0;

    private final IDrawable background;
    //private final String localizedName;
    private final ICraftingGridHelper craftingGridHelper;
    private final int craftInputSlot;

    public SteelCraftingCategory(IGuiHelper guiHelper)
    {
        ResourceLocation location = new ResourceLocation(BWMod.MODID, "textures/gui/jei/steel_anvil.png");
        background = guiHelper.createDrawable(location, 0, 0, WIDTH, HEIGHT);
        craftInputSlot = 1;
        craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot, craftOutputSlot);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return I18n.format(TITLE);
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(craftOutputSlot, false, 71, 176);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int index = 1 + j + (i * 4);
                guiItemStacks.init(index, true, j * 18, i * 18);
            }
        }

        recipeLayout.setRecipeTransferButton(145, 185);

        if(recipeWrapper instanceof SteelShapelessRecipe)
            recipeLayout.setShapeless();

        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);

        if (recipeWrapper instanceof IShapedCraftingRecipeWrapper) {
            IShapedCraftingRecipeWrapper wrapper = (IShapedCraftingRecipeWrapper) recipeWrapper;
            craftingGridHelper.setInputs(guiItemStacks, inputs, wrapper.getWidth(), wrapper.getHeight());
        } else {
            craftingGridHelper.setInputs(guiItemStacks, inputs);
            recipeLayout.setShapeless();
        }
        guiItemStacks.set(craftOutputSlot, outputs.get(0));
    }


}