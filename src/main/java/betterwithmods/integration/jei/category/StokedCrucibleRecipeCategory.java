package betterwithmods.integration.jei.category;

import betterwithmods.BWMod;
import betterwithmods.integration.jei.wrapper.BulkRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class StokedCrucibleRecipeCategory extends BWMRecipeCategory
{
    private static final int inputSlots = 2;
    private static final int outputSlots = 0;

    private static final ResourceLocation guiTexture = new ResourceLocation(BWMod.MODID, "textures/gui/jei/cooking.png");
    @Nonnull
    private final ICraftingGridHelper craftingGrid;
    @Nonnull
    private final IDrawableAnimated flame;

    public StokedCrucibleRecipeCategory(IGuiHelper helper)
    {
        super(helper.createDrawable(guiTexture, 5, 6, 158, 60), "inv.crucible.stoked.name");
        craftingGrid = helper.createCraftingGridHelper(inputSlots, outputSlots);
        IDrawableStatic flameDrawable = helper.createDrawable(guiTexture, 176, 16, 14, 14);
        this.flame = helper.createAnimatedDrawable(flameDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return "bwm.crucible.stoked";
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft)
    {
        flame.draw(minecraft, 80, 19);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull IRecipeWrapper wrapper)
    {
        IGuiItemStackGroup stacks = layout.getItemStacks();

        stacks.init(outputSlots, false, 118, 18);
        stacks.init(outputSlots + 1, false, 118 + 18, 18);

        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                int index = inputSlots + i + (j * 3);
                stacks.init(index, true, 2 + i * 18, j * 18);
            }
        }

        BulkRecipeWrapper recipe = (BulkRecipeWrapper)wrapper;
        //craftingGrid.setOutput(stacks, recipe.getOutputs());
        stacks.set(outputSlots, recipe.getRecipe().getOutput());
        if(recipe.getRecipe().getSecondary() != null && recipe.getRecipe().getSecondary().getItem() != null)
            stacks.set(outputSlots + 1, recipe.getRecipe().getSecondary());
        craftingGrid.setInput(stacks, recipe.getRecipe().getInput());
    }
}
