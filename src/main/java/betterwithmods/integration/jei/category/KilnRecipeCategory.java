package betterwithmods.integration.jei.category;


import betterwithmods.BWMod;
import betterwithmods.integration.jei.wrapper.KilnWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;


public class KilnRecipeCategory extends BlankRecipeCategory<KilnWrapper> {

    public static final int width = 145;
    public static final int height = 80;

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public KilnRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(BWMod.MODID, "textures/gui/jei/kiln.png");
        background = guiHelper.createDrawable(location, 0, 0, width, height);
        localizedName = Translator.translateToLocal("inv.kiln.name");
    }

    @Nonnull
    @Override
    public String getUid() {
        return "bwm.kiln";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }


    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull KilnWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();
        guiItemStacks.init(0, true, 20, 31);
        guiItemStacks.init(1, false, 94, 31);
        guiItemStacks.set(0, ingredients.getInputs(ItemStack.class).get(0));
        guiItemStacks.set(1, ingredients.getOutputs(ItemStack.class));
    }
}
