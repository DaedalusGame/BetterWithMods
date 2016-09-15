package betterwithmods.integration.jei.category;

import betterwithmods.integration.jei.wrapper.BulkRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class MillRecipeCategory  extends BWMRecipeCategory
{
    private static final int inputSlots = 2;
    private static final int outputSlots = 0;

    private static final ResourceLocation guiTexture = new ResourceLocation("betterwithmods", "textures/gui/jei/mill.png");
    @Nonnull
    private final ICraftingGridHelper craftingGrid;
    @Nonnull
    private final IDrawableAnimated gear;

    public MillRecipeCategory(IGuiHelper helper)
    {
        super(helper.createDrawable(guiTexture, 5, 6, 158, 36), "inv.mill.name");
        craftingGrid = helper.createCraftingGridHelper(inputSlots, outputSlots);
        IDrawableStatic flameDrawable = helper.createDrawable(guiTexture, 176, 0, 14, 14);
        this.gear = helper.createAnimatedDrawable(flameDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return "bwm.mill";
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft)
    {
        gear.draw(minecraft, 80, 19);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull IRecipeWrapper wrapper)
    {
        IGuiItemStackGroup stacks = layout.getItemStacks();

        stacks.init(outputSlots, false, 118, 18);
        stacks.init(outputSlots + 1, false, 118 + 18, 18);
        BulkRecipeWrapper recipe = (BulkRecipeWrapper)wrapper;
        Object[] input = recipe.getRecipe().getInput().toArray();
        for(int i = 0; i < 3; i++)
        {
            int index = inputSlots + i;
            stacks.init(index, true, 2 + i * 18, 18);
            if(input.length > i && input[i] != null)
            {
                if(input[i] instanceof ItemStack)
                    stacks.set(index, ((ItemStack) input[i]).copy());
                else if(input[i] instanceof List)
                    stacks.set(index, (List<ItemStack>) input[i]);
            }
        }

        //craftingGrid.setOutput(stacks, recipe.getOutputs());
        stacks.set(outputSlots, recipe.getRecipe().getOutput());
        if(recipe.getRecipe().getSecondary() != null && recipe.getRecipe().getSecondary().getItem() != null)
            stacks.set(outputSlots + 1, recipe.getRecipe().getSecondary());
        //craftingGrid.setInput(stacks, recipe.getRecipe().getInput());
    }
}
