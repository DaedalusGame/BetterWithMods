package betterwithmods.integration.jei.wrapper;

import betterwithmods.integration.jei.category.SteelAnvilRecipeCategory;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import mezz.jei.config.Constants;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created by blueyu2 on 11/25/16.
 */
public class SteelAnvilShapelessRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper {
    private static final double shapelessIconScale = 0.5;
    private final IJeiHelpers jeiHelpers;
    private final ShapelessOreRecipe recipe;
    private final IDrawable shapelessIcon;
    private final HoverChecker shapelessIconHoverChecker;

    public SteelAnvilShapelessRecipeWrapper(IJeiHelpers jeiHelpers, ShapelessOreRecipe recipe) {
        this.jeiHelpers = jeiHelpers;
        this.recipe = recipe;

        for (Object input : this.recipe.getInput()) {
            if (input instanceof ItemStack) {
                ItemStack itemStack = (ItemStack) input;
                if (itemStack.getCount() != 1) {
                    itemStack.setCount(1);
                }
            }
        }

        ResourceLocation shapelessIconLocation = new ResourceLocation(Constants.RESOURCE_DOMAIN, Constants.TEXTURE_RECIPE_BACKGROUND_PATH);
        shapelessIcon = jeiHelpers.getGuiHelper().createDrawable(shapelessIconLocation, 196, 0, 19, 15);

        int iconBottom = (int) (shapelessIcon.getHeight() * shapelessIconScale);
        //Having to replace CraftingRecipeCategory.width with SteelAnvilRecipeCategory.WIDTH is the only reason why I can't just use ShapelessOreRecipeWrapper :(
        int iconLeft = SteelAnvilRecipeCategory.WIDTH - (int) (shapelessIcon.getWidth() * shapelessIconScale);
        int iconRight = iconLeft + (int) (shapelessIcon.getWidth() * shapelessIconScale);
        shapelessIconHoverChecker = new HoverChecker(0, iconBottom, iconLeft, iconRight, 0);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);

        if (hasMultipleIngredients()) {
            int shapelessIconX = recipeWidth - (int) (shapelessIcon.getWidth() * shapelessIconScale);

            GlStateManager.pushMatrix();
            GlStateManager.scale(shapelessIconScale, shapelessIconScale, 1.0);
            GlStateManager.color(1f, 1f, 1f, 1f);
            shapelessIcon.draw(minecraft, (int) (shapelessIconX / shapelessIconScale), 0);
            GlStateManager.popMatrix();
        }
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (hasMultipleIngredients() && shapelessIconHoverChecker.checkHover(mouseX, mouseY)) {
            return Collections.singletonList(Translator.translateToLocal("jei.tooltip.shapeless.recipe"));
        }

        return super.getTooltipStrings(mouseX, mouseY);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        IStackHelper stackHelper = jeiHelpers.getStackHelper();

        List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(recipe.getInput());
        ingredients.setInputLists(ItemStack.class, inputs);

        ItemStack recipeOutput = recipe.getRecipeOutput();
        if (recipeOutput != ItemStack.EMPTY) {
            ingredients.setOutput(ItemStack.class, recipeOutput);
        }
    }

    private boolean hasMultipleIngredients() {
        return recipe.getInput().size() > 1;
    }
}
