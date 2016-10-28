package betterwithmods.integration.jei.category;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMod;
import betterwithmods.integration.jei.wrapper.SawWrapper;
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

/**
 * Created by tyler on 9/5/16.
 */
public class SawRecipeCategory extends BlankRecipeCategory<SawWrapper> {
    public static final int width = 82;
    public static final int height = 50;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public SawRecipeCategory(IGuiHelper helper) {
        ResourceLocation location = new ResourceLocation(BWMod.MODID, "textures/gui/jei/saw.png");
        background = helper.createDrawable(location, 0, 0, width, height);
        localizedName = Translator.translateToLocal("inv.saw.name");
    }

    @Nonnull
    @Override
    public String getUid() {
        return "bwm.saw";
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
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull SawWrapper wrapper) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();
        guiItemStacks.init(0, false, 8, 9);
        guiItemStacks.init(1, true, 57, 9);
        guiItemStacks.init(3, false, 32, 27);

        guiItemStacks.set(3, new ItemStack(BWMBlocks.SAW));
        guiItemStacks.setFromRecipe(0, wrapper.getInputs());
        guiItemStacks.setFromRecipe(1, wrapper.getOutputs());
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull SawWrapper wrapper, IIngredients ingredients) {
        setRecipe(layout, wrapper);
        //TODO Ingredients
    }
}
