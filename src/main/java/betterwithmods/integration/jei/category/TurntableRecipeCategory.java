package betterwithmods.integration.jei.category;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMod;
import betterwithmods.integration.jei.wrapper.TurntableRecipeWrapper;
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
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/11/16
 */
public class TurntableRecipeCategory extends BlankRecipeCategory<TurntableRecipeWrapper> {
    public static final int width = 53;
    public static final int height = 50;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public TurntableRecipeCategory(IGuiHelper helper) {
        ResourceLocation location = new ResourceLocation(BWMod.MODID, "textures/gui/jei/turntable.png");
        background = helper.createDrawable(location, 0, 0, width, height);
        localizedName = Translator.translateToLocal("inv.turntable.name");
    }

    @Nonnull
    @Override
    public String getUid() {
        return "bwm.turntable";
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
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull TurntableRecipeWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();
        int x = 5, y = 9;
        guiItemStacks.init(0, true, x, y);
        guiItemStacks.init(1, false, x + 27, y);
        guiItemStacks.init(2, false, x + 27, y + 18);
        guiItemStacks.init(3, false, x, y + 18);
        guiItemStacks.set(ingredients);
        guiItemStacks.set(3, new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 5));

    }
}

