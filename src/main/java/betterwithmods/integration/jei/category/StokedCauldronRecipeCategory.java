package betterwithmods.integration.jei.category;

import betterwithmods.BWMod;
import betterwithmods.integration.jei.wrapper.StokedCauldronRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class StokedCauldronRecipeCategory extends BWMRecipeCategory<StokedCauldronRecipeWrapper> {
    private static final int inputSlots = 2;
    private static final int outputSlots = 0;

    private static final ResourceLocation guiTexture = new ResourceLocation(BWMod.MODID, "textures/gui/jei/cooking.png");
    @Nonnull
    private final ICraftingGridHelper craftingGrid;
    @Nonnull
    private final IDrawableAnimated flame;

    public StokedCauldronRecipeCategory(IGuiHelper helper) {
        super(helper.createDrawable(guiTexture, 5, 6, 158, 60), "inv.cauldron.stoked.name");
        craftingGrid = helper.createCraftingGridHelper(inputSlots, outputSlots);
        IDrawableStatic flameDrawable = helper.createDrawable(guiTexture, 176, 16, 14, 14);
        this.flame = helper.createAnimatedDrawable(flameDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Nonnull
    @Override
    public String getUid() {
        return "bwm.cauldron.stoked";
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft) {
        flame.draw(minecraft, 80, 19);
    }

    @Deprecated
    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull StokedCauldronRecipeWrapper wrapper) {
        IGuiItemStackGroup stacks = layout.getItemStacks();

        stacks.init(outputSlots, false, 118, 18);
        stacks.init(outputSlots + 1, false, 118 + 18, 18);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int index = inputSlots + i + (j * 3);
                stacks.init(index, true, 2 + i * 18, j * 18);
            }
        }

        stacks.set(outputSlots, wrapper.getRecipe().getOutput());
        if (wrapper.getRecipe().getSecondary() != null && wrapper.getRecipe().getSecondary().getItem() != null)
            stacks.set(outputSlots + 1, wrapper.getRecipe().getSecondary());
        List<List<ItemStack>> inputList = wrapper.getInputs(); //TODO adapted for JEI 3.11.2. May not be correct.
        craftingGrid.setInputStacks(stacks, inputList);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull StokedCauldronRecipeWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();

        stacks.init(outputSlots, false, 118, 18);
        stacks.init(outputSlots + 1, false, 118 + 18, 18);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int index = inputSlots + i + (j * 3);
                stacks.init(index, true, 2 + i * 18, j * 18);
            }
        }

        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class);

        stacks.set(outputSlots, outputs.get(0));
        if (outputs.size() > 1)
            stacks.set(outputSlots + 1, outputs.get(1));

        craftingGrid.setInputStacks(stacks, inputs);
    }
}
