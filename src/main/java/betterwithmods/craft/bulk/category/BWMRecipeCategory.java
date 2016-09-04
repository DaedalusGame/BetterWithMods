package betterwithmods.craft.bulk.category;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;

public abstract class BWMRecipeCategory extends BlankRecipeCategory
{
    @Nonnull
    private final IDrawable background;

    @Nonnull
    private final String localizedName;

    public BWMRecipeCategory(@Nonnull IDrawable background, String unlocalizedName)
    {
        this.background = background;
        this.localizedName = I18n.translateToLocal(unlocalizedName);
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return background;
    }
}
