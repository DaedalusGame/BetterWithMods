package betterwithmods.integration.jei.category;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;

public abstract class BWMRecipeCategory<T extends IRecipeWrapper> extends BlankRecipeCategory<T> {
    @Nonnull
    private final IDrawable background;

    @Nonnull
    private final String localizedName;

    public BWMRecipeCategory(@Nonnull IDrawable background, @Nonnull String unlocalizedName) {
        this.background = background;
        this.localizedName = I18n.format(unlocalizedName);
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
}
